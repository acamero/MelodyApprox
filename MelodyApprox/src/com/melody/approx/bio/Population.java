package com.melody.approx.bio;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.IndividualInitInterface.IndividualInitInterfaceException;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.util.Log;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class Population {

	private int populationSize;
	private List<Individual> population;
	private Individual[] popArray; 
	private double bestFitnessEver;
	private double bestFitness;
	private double worstFitness;
	private double avgFitness;
	private int bestPosition;
	private int worstPosition;

	public Population(int populationSize, int numberOfGenes, IndividualInitInterface init)
			throws PopulationException, ChromosomeException, IndividualInitInterfaceException, ProblemException {
		if (populationSize > 0) {
			this.populationSize = populationSize;
			// set population list as a fixed size list
			if (numberOfGenes > 0) {
				popArray = new Individual[this.populationSize];
				population = Arrays.asList(popArray);
			} else {
				Log.error("The number of genes should be greater than zero");
				throw new PopulationException("The number of genes should be greater than zero");
			}
		} else {
			Log.error("Population size should be greater than zero");
			throw new PopulationException("Population size should be greater than zero");
		}

		if (init == null) {
			Log.error("IndividualInitInterface should not be null");
			throw new PopulationException("IndividualInitInterface should not be null");
		} else {
			initPopulation(numberOfGenes, init);
		}
		
		// initialize fitness
		bestFitness = Double.MAX_VALUE;
		bestFitnessEver = Double.MAX_VALUE;
	}

	private void initPopulation(int numberOfGenes, IndividualInitInterface init)
			throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		
		for (int i = 0; i < populationSize; i++) {
			// the individuals are initialized without a fitness value
			population.set(i, init.nextIndividual());		
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
		Log.info("Stats:\tAVG="+avgFitness+"\tBestFitness="+bestFitness+"\tEver="+bestFitnessEver);
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
			Log.error("The number of offsprings should be grater than zero");
			throw new PopulationException("The number of offsprings should be grater than zero");
		} else if (offsprings.size() == 1) {
			// steady state
			Log.info("Worst individual replaced");
			population.set(worstPosition, offsprings.get(0));
		} else if (offsprings.size() == populationSize - 1) {
			// elite
			offsprings.add(population.get(bestPosition));
			Log.info("Best individual added to the offsprings population");
			offsprings.toArray(popArray);
			population = Arrays.asList(popArray);
		} else if (offsprings.size() > populationSize) {
			throw new PopulationException("The number of offsprings shoul not be greater than the population size");
		} else if (offsprings.size() == populationSize) {
			Log.info("All population replaced");
			offsprings.toArray(popArray);
			population = Arrays.asList(popArray);
		} else {
			// replace the 'n' worst individuals
			Collections.sort(population);
			int n = populationSize - offsprings.size();
			for (int i = n; i < populationSize; i++) {				
				population.set(i, offsprings.get(i - n));
			}
			Log.info("Replace "+n+" individuals");			
		}
		
		// update population statistics
		computeStats();
	}
	
	public List<Individual> getPopulation() {
		return population;
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
