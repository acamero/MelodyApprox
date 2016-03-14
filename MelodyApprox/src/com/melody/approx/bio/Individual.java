package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;

public class Individual implements Comparable<Individual> {

	private Chromosome chromosome;
	private double fitness;

	public Individual(int numberOfGenes) throws ChromosomeException {
		chromosome = new Chromosome(numberOfGenes);
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public Chromosome getChromosome() {
		return chromosome;
	}

	public int getNumberOfGenes() {
		return chromosome.getNumberOfGenes();
	}

	public String toString() {
		return "Fitness=" + fitness + ", Chromosome=(" + chromosome.toString() + ")";
	}

	@Override
	public int compareTo(Individual arg0) {
		// ascending
		double diff = this.fitness - arg0.getFitness();
		if (diff < 0.0d) {
			return -1;
		} else if (diff==0.0d){
			return 0;
		} else {
			return 1;
		}		
	}
}
