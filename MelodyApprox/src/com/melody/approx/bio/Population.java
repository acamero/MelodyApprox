package com.melody.approx.bio;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.melody.approx.bio.Chromosome.ChromosomeException;

public class Population {

	private int populationSize;
	private List<Individual> population;
	private double bestFitnessEver;
	private double bestFitness;
	private double worstFitness;
	private double avgFitness;
	private int bestPosition;
	private int worstPosition;

	public Population(int populationSize, int numberOfGenes, ChromosomeInitInterface init)
			throws PopulationException, ChromosomeException {
		if (populationSize > 0) {
			this.populationSize = populationSize;
			// set population list as a fixed size list
			population = Arrays.asList(new Individual[this.populationSize]);
		} else {
			throw new PopulationException("Population size should be greater than zero");
		}

		initPopulation(numberOfGenes, init);
		bestFitnessEver = Double.MAX_VALUE;
	}
	
	private void initPopulation(int numberOfGenes, ChromosomeInitInterface init) throws ChromosomeException {
		for(int i=0;i<populationSize;i++) {
			population.set(i, new Individual(numberOfGenes, init));
		}
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public double getBestFitnessEver() {
		return bestFitnessEver;
	}

	public double getBestFitness() {
		return bestFitness;
	}

	public double getWorstFitness() {
		return worstFitness;
	}

	public double getAvgFitness() {
		return avgFitness;
	}

	public int getBestPosition() {
		return bestPosition;
	}

	public int getWorstPosition() {
		return worstPosition;
	}

	public void computeStats() {
		double total;
		total = 0.0d;
		bestFitness = population.get(0).getFitness();
		bestPosition = 0;

		for (int i = 0; i < populationSize; i++) {

			if (population.get(i).getFitness() < bestFitness) {
				bestFitness = population.get(i).getFitness();
				bestPosition = i;
			}
			
			if (population.get(i).getFitness() > worstFitness) {
				worstFitness = population.get(i).getFitness();
				worstPosition = i;
			}
			
			if (population.get(i).getFitness() < bestFitnessEver) {
				bestFitnessEver = population.get(i).getFitness();
			}
			total += population.get(i).getFitness();
		}

		avgFitness = total / (double) populationSize;
	}

	/**
	 * Replace the 'n' worst individuals of the population with the offsprings,
	 * where 'n' is the number of offsprings
	 * 
	 * @param offsprings
	 * @throws PopulationException
	 */
	public void replaceIndividuals(List<Individual> offsprings) throws PopulationException {
		if (offsprings == null || offsprings.isEmpty()) {
			// something bad happen
			throw new PopulationException("The number of offsprings should be grater than zero");
		} else if (offsprings.size() == 1) {
			// steady state
			population.set(worstPosition, offsprings.get(0));
		} else if (offsprings.size() == populationSize - 1) {
			// elite
			offsprings.add(population.get(bestPosition));
			population = Arrays.asList((Individual[]) offsprings.toArray());
		} else if (offsprings.size() > populationSize) {
			throw new PopulationException("The number of offsprings shoul not be greater than the population size");
		} else {
			// replace the 'n' worst individuals
			Collections.sort(population);
			for (int i = populationSize - offsprings.size(); i < populationSize; i++) {
				population.set(i, offsprings.get(i - offsprings.size()));
			}
		}
	}

	public class PopulationException extends Exception {
		private static final long serialVersionUID = -8630050463815306413L;

		public PopulationException(String message) {
			super(message);
		}

		public PopulationException(Throwable cause) {
			super(cause);
		}

		public PopulationException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
