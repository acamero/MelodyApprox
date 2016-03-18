package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.util.Log;
import com.melody.approx.util.RandomGenerator;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class SinglePointCrossover implements CrossoverInterface {

	@Override
	public Individual crossover(double crossoverProb, Individual a, Individual b) throws CrossoverException {
		if (a == null || b == null || a.getNumberOfGenes() != b.getNumberOfGenes()) {
			Log.error("Individuals should not be null nor of different size");
			throw new CrossoverException("Individuals should not be null nor of different size");
		}

		Individual individual = null;
		try {
			individual = new Individual(a.getNumberOfGenes());
			int pos = (int) (RandomGenerator.nextDouble() * (double) a.getNumberOfGenes() - 1.0d + 0.5d);

			if (pos > a.getNumberOfGenes() - 1)
				pos = a.getNumberOfGenes() - 1;
			
			if (RandomGenerator.nextDouble() <= crossoverProb) {
				// Copy the first portion from A
				for (int i = 0; i < pos; i++) {
					individual.getChromosome().setGene(i, a.getChromosome().getGene(i));
				}
				// And the remainder from B
				for (int i = pos; i < individual.getNumberOfGenes(); i++) {
					individual.getChromosome().setGene(i, b.getChromosome().getGene(i));
				}
			} else {
				// If no crossover then randomly return one parent
				Individual tmp;
				if (RandomGenerator.nextDouble() > 0.5d) {
					tmp = a;
				} else {
					tmp = b;
				}

				for (int i = 0; i < individual.getNumberOfGenes(); i++) {
					individual.getChromosome().setGene(i, tmp.getChromosome().getGene(i));
				}
			}
			
		} catch (ChromosomeException e) {
			Log.error("The number of genes is not valid, this exception is weird,"
					+ " because the number is taken from an individual");
			throw new CrossoverException("The number of genes is not valid, this exception is weird,"
					+ " because the number is taken from an individual");
		}

		return individual;
	}

}
