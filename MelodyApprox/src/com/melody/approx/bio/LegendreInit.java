package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.util.Log;
import com.melody.approx.util.RandomGenerator;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class LegendreInit implements IndividualInitInterface {

	private double mean;
	private double stdDev;

	
	public LegendreInit(double mean, double stdDev) {
		this.mean = mean;
		this.stdDev = stdDev;
	}

	@Override
	public Individual nextIndividual(int numberOfGenes)
			throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		Individual individual = new Individual(numberOfGenes);

		// Legendre polynomial of order 0
		individual.getChromosome().setGene(0, RandomGenerator.nextGaussian(stdDev, mean));
		// Polynomials of order 1 and greater
		for (int i = 1; i < numberOfGenes; i++) {
			individual.getChromosome().setGene(i, RandomGenerator.nextGaussian());
		}

		Log.debug("New individual: "+individual.toString());
		return individual;
	}

}
