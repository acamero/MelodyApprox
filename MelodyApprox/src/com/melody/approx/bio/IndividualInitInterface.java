package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Problem.ProblemException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public interface IndividualInitInterface {

	// initialize an individual without a fitness value
	public Individual nextIndividual(int numberOfGenes) throws ChromosomeException, IndividualInitInterfaceException, ProblemException;

	public class IndividualInitInterfaceException extends Exception {
		private static final long serialVersionUID = 536348314796786425L;

		public IndividualInitInterfaceException(String message) {
			super(message);
		}

		public IndividualInitInterfaceException(Throwable cause) {
			super(cause);
		}

		public IndividualInitInterfaceException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
