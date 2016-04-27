package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class Individual implements Comparable<Individual> {

	private Chromosome chromosome;
	private double fitness;

	public Individual(int numberOfGenes) throws ChromosomeException {
		chromosome = new Chromosome(numberOfGenes);
		fitness = Double.MAX_VALUE;
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

	/**
	 * Returns a copy of the Individual
	 * @return
	 * @throws ChromosomeException
	 */
	public Individual copy() throws ChromosomeException {
		Individual copy = new Individual(this.getNumberOfGenes());
		for (int i = 0; i < copy.getNumberOfGenes(); i++) {
			copy.getChromosome().setGene(i, this.getChromosome().getGene(i));
		}
		copy.setFitness(this.getFitness());
		return copy;
	}

	@Override
	public int compareTo(Individual arg0) {
		// ascending
		double diff = this.fitness - arg0.getFitness();
		if (diff < 0.0d) {
			return -1;
		} else if (diff == 0.0d) {
			return 0;
		} else {
			return 1;
		}
	}
}
