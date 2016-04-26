package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.util.Log;
import com.melody.approx.util.RandomGenerator;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class NarrowMutation implements MutationInterface {

	private static final int MUTATION_NARROW_CYCLE = 500;
	private double stdDev;
	private int counter;
	
	public NarrowMutation(double stdDev) {
		this.stdDev = stdDev;
		this.counter = 0;
	}

	@Override
	public void mutate(double mutationProb, Individual individual) throws MutationException {
		if (individual == null) {
			Log.error("Null individual, unable to mutate");
			throw new MutationException("Null individual, unable to mutate");
		}

		for (int i = 0; i < individual.getNumberOfGenes(); i++) {
			if (RandomGenerator.nextDouble() < mutationProb) {
				// mutate gene i
				try {
					individual.getChromosome().setGene(i,
							RandomGenerator.nextGaussian(stdDev, individual.getChromosome().getGene(i)));
				} catch (ChromosomeException e) {
					Log.error("Something weird happen, because the gene number is out of range");
					throw new MutationException("Something weird happen, because the gene number is out of range");
				}
			}
		}
		
		stdDev = 0.99d*stdDev;
		counter++;
		if(counter%MUTATION_NARROW_CYCLE ==0) {			
			Log.debug("Standard Deviation actual value= "+stdDev);
		}
		
	}

}
