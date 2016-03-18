package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Problem.ProblemException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class LegendreInit implements IndividualInitInterface {

	private int numberOfGenes;
	

	public LegendreInit(int numberOfGenes) {
		this.numberOfGenes = numberOfGenes;
		
	}

	@Override
	public Individual nextIndividual() throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		Individual individual = new Individual(numberOfGenes);
		// TODO set each gene

		
		return individual;
	}

}
