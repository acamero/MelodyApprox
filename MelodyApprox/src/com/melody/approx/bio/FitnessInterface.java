package com.melody.approx.bio;

import com.melody.approx.bio.Problem.ProblemException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public interface FitnessInterface {

	public double getFitness(Individual individual) throws ProblemException ;
	
}

