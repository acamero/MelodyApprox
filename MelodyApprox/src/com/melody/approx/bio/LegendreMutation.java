package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.util.Log;
import com.melody.approx.util.RandomGenerator;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class LegendreMutation implements MutationInterface {

	private double stdDev;

	public LegendreMutation(double stdDev) {
		this.stdDev = stdDev;
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
	}

}
