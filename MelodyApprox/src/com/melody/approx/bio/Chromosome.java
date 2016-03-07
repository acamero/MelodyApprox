package com.melody.approx.bio;

import java.util.Arrays;
import java.util.List;

public class Chromosome {
	private List<Double> genes;
	private int numberOfGenes;

	public Chromosome(int numberOfGenes) throws ChromosomeException {
		if (numberOfGenes > 0) {
			this.numberOfGenes=numberOfGenes;
			// initialize the list as a fixed list
			genes = Arrays.asList(new Double[this.numberOfGenes]);
		} else {
			throw new ChromosomeException("The number of genes should be greater than zero");
		}
	}
	
	public Chromosome(int numberOfGenes, ChromosomeInitInterface init) throws ChromosomeException {
		if (numberOfGenes > 0 && init!=null) {
			this.numberOfGenes=numberOfGenes;
			// initialize the list as a fixed list
			genes = Arrays.asList(new Double[this.numberOfGenes]);
			// TODO initialize chromosome using init
		} else if (numberOfGenes <= 0) {
			throw new ChromosomeException("The number of genes should be greater than zero");
		} else {
			throw new ChromosomeException("You should define an init function for the chromsomes");
		}
	}

	public int getNumberOfGenes() {		
		return numberOfGenes;
	}
	
	public String toString() {
		return Arrays.toString(genes.toArray());
	}
	
	public class ChromosomeException extends Exception {
		private static final long serialVersionUID = 1345015079346216448L;
		public ChromosomeException(String message) {
			super(message);
		}

		public ChromosomeException(Throwable cause) {
			super(cause);
		}

		public ChromosomeException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
}
