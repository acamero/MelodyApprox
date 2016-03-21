package com.melody.approx.bio;

import java.util.ArrayList;
import java.util.List;

import com.melody.approx.bio.CrossoverInterface.CrossoverException;
import com.melody.approx.bio.MutationInterface.MutationException;
import com.melody.approx.bio.Population.PopulationException;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.util.Log;
import com.melody.approx.util.RandomGenerator;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class Algorithm {
	// report the statistics every EVALUATIONS_REPORT evaluations 
	private static final int EVALUATIONS_REPORT = 500;
	
	private FitnessInterface fitnessCalc;
	private CrossoverInterface crossoverInt;
	private MutationInterface mutateInt;
	private Population population;
	private int offspringSize;
	private int maxEvaluations;
	private int evaluations;
	private double crossoverProb;
	private double mutationProb;

	public Algorithm(FitnessInterface fitnessCalc, Population population, int offspringSize, int maxEvaluations,
			CrossoverInterface crossoverInt, MutationInterface mutateInt, double crossoverProb, double mutationProb)
			throws ProblemException, AlgorithmException {
		if (fitnessCalc == null) {
			Log.error("Fitness interface should not be null");
			throw new AlgorithmException("Fitness interface should not be null");
		}

		if (population == null || population.getPopulationSize() == 0) {
			Log.error("Population should not be null");
			throw new AlgorithmException("Population should not be null");
		}

		if (crossoverInt == null) {
			Log.error("Crossover interfave should not be null");
			throw new AlgorithmException("Crossover interface should not be null");
		}

		if (mutateInt == null) {
			Log.error("Mutation interface should not be null");
			throw new AlgorithmException("Mutation interface should not be null");
		}

		if (offspringSize < 1 || offspringSize >= population.getPopulationSize()) {
			Log.error("Invalid offspring size");
			throw new AlgorithmException("Invalid offspring size");
		}

		if (crossoverProb < 0.0d || crossoverProb > 1.0d) {
			Log.warning("Crossover probability is out of probability range");
		}

		if (mutationProb < 0.0d || mutationProb > 1.0d) {
			Log.warning("Mutation probability is out of probability range");
		}

		if (maxEvaluations < population.getPopulationSize()) {
			Log.warning("The maximum number of evaluations is less than population size");
		}

		this.fitnessCalc = fitnessCalc;
		this.population = population;
		this.offspringSize = offspringSize;
		this.maxEvaluations = maxEvaluations;
		this.crossoverInt = crossoverInt;
		this.mutateInt = mutateInt;
		this.evaluations = 0;
		this.crossoverProb = crossoverProb;
		this.mutationProb = mutationProb;

		// calculate the fitness of the initial population and
		// initialize statistics of the population
		calculateFitness(population.getPopulation());
		population.computeStats();
	}

	private void calculateFitness(List<Individual> individuals) throws ProblemException {
		for (Individual ind : individuals) {
			ind.setFitness(fitnessCalc.getFitness(ind));
			evaluations++;
			// from time to time, report the stats
			if(evaluations%EVALUATIONS_REPORT==0) {
				Log.info(statsToString());
			}
		}
	}

	public String statsToString() {
		return "Evaluations="+evaluations+"\t"+population.statsToString();
	}

	public Individual startAlgorithm()
			throws PopulationException, ProblemException, CrossoverException, MutationException {
		List<Individual> offspring = new ArrayList<Individual>();
		// check stop conditions
		while (evaluations < maxEvaluations && population.getBestFitness() > 0.0d) {
			// create and evaluate offspring
			offspring.clear();
			for (int i = 0; i < offspringSize; i++) {
				// crossover of two individuals
				Individual ind = crossoverInt.crossover(crossoverProb, binaryTournament(), binaryTournament());
				// mutate individual
				mutateInt.mutate(mutationProb, ind);
				// add individual to the list of offsprings
				offspring.add(ind);				
			}
			// compute the fitness of the offspring
			calculateFitness(offspring);

			// replace population
			population.replaceIndividuals(offspring);
		}

		return population.getPopulation().get(population.getBestPosition());
	}

	public int getEvaluations() {
		return evaluations;
	}

	private Individual binaryTournament() {
		int p1, p2;

		p1 = (int) (RandomGenerator.nextDouble() * (double) population.getPopulationSize() + 0.5d);
		if (p1 > population.getPopulationSize() - 1) {
			p1 = population.getPopulationSize() - 1;
		}

		do {
			p2 = (int) (RandomGenerator.nextDouble() * (double) population.getPopulationSize() + 0.5d);
			if (p2 > population.getPopulationSize() - 1) {
				p2 = population.getPopulationSize() - 1;
			}
		} while (p1 == p2);

		if (population.getPopulation().get(p1).getFitness() < population.getPopulation().get(p2).getFitness()) {
			return population.getPopulation().get(p1);
		} else {
			return population.getPopulation().get(p2);
		}
	}

	public class AlgorithmException extends Exception {
		private static final long serialVersionUID = 2057222637367895615L;

		public AlgorithmException(String message) {
			super(message);
		}

		public AlgorithmException(Throwable cause) {
			super(cause);
		}

		public AlgorithmException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
