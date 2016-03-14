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
			for(int i=0;i<numberOfGenes;i++) {
				genes.set(i, new Double(0.0d));
			}
		} else {
			throw new ChromosomeException("The number of genes should be greater than zero");
		}
	}
	
	public int getNumberOfGenes() {		
		return numberOfGenes;
	}
	
	public double getGene(int geneNumber) {
		return genes.get(geneNumber);
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
