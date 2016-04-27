package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Problem.ProblemException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public interface LocalSearchInterface {

	public Individual localSearch(Individual individual) throws ChromosomeException, ProblemException;

}
