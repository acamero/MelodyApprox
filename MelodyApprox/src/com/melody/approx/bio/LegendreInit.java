package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Problem.ProblemException;

public class LegendreInit implements IndividualInitInterface {

	private int numberOfGenes;
	private FitnessInterface fitness;

	public LegendreInit(int numberOfGenes, FitnessInterface fitness) {
		this.numberOfGenes = numberOfGenes;
		this.fitness = fitness;
	}

	@Override
	public Individual nextIndividual() throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		Individual individual = new Individual(numberOfGenes);
		// TODO set each gene

		if (fitness != null) {
			individual.setFitness(fitness.getFitness(individual));
		} else {
			throw new IndividualInitInterfaceException("A FitnessInterface should be provided");
		}
		return individual;
	}

}
