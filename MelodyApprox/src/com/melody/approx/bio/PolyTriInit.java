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
		
		if(numberOfGenes < ProblemPolyTri.BASE_CONSTANTS) {
			throw new IndividualInitInterfaceException("Number of genes should be greater than "+ProblemPolyTri.BASE_CONSTANTS);
		}
		
		Individual individual = new Individual(numberOfGenes);
		
		// constant
		individual.getChromosome().setGene(0, RandomGenerator.nextGaussian(stdDev, mean));
		// alpha
		individual.getChromosome().setGene(1, RandomGenerator.nextGaussian(stdDev, 0.0d));
		// beta
		individual.getChromosome().setGene(2, RandomGenerator.nextGaussian(stdDev, 0.0d));
		
		for (int i = 3; i < numberOfGenes; i++) {
			individual.getChromosome().setGene(i, RandomGenerator.nextGaussian(stdDev, 0.0d));
		}

		Log.debug("New individual: "+individual.toString());
		return individual;
	}

}
