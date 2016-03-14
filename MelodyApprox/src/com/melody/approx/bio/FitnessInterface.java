package com.melody.approx.bio;

import com.melody.approx.bio.Problem.ProblemException;

public interface FitnessInterface {

	public double getFitness(Individual individual) throws ProblemException ;
	
}

