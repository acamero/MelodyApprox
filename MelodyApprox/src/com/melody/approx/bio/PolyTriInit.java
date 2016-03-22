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
public class PolyTriInit implements IndividualInitInterface {
	
	private double mean;
	private double stdDev;
	
	public PolyTriInit(double mean, double stdDev) {
		this.mean = mean;
		this.stdDev = stdDev;
	}

	@Override
	public Individual nextIndividual(int numberOfGenes)
			throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		
		if(numberOfGenes < 2) {
			throw new IndividualInitInterfaceException("Number of genes should be greater than 2");
		}
		
		Individual individual = new Individual(numberOfGenes);
		
		// alpha
		individual.getChromosome().setGene(0, RandomGenerator.nextGaussian());
		// beta
		individual.getChromosome().setGene(1, RandomGenerator.nextGaussian());
		
		for (int i = 2; i < numberOfGenes; i++) {
			individual.getChromosome().setGene(i, RandomGenerator.nextGaussian(stdDev, mean));
		}

		Log.debug("New individual: "+individual.toString());
		return individual;
	}

}
