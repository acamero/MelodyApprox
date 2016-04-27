package com.melody.approx.bio;

import java.util.Map.Entry;

import com.melody.approx.pitch.PitchContour;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class ProblemMean extends Problem {
	public static final int GENE_NUMBER = 1;

	private double mean;
	
	/**
	 * This class is intended to validate the "basic" approximation, i.e. using
	 * the mean of the data set. Therefore the fitness is calculated regardless
	 * the value of the individual
	 * @param contour
	 * @param mean
	 * @throws ProblemException
	 */
	public ProblemMean(PitchContour contour, double mean) throws ProblemException {
		super(contour);
		this.mean = mean;
	}

	@Override
	public double getFitness(Individual individual) throws ProblemException {
		if (individual == null) {
			throw new ProblemException("Null individual");
		}

		if (individual.getNumberOfGenes() != GENE_NUMBER) {
			throw new ProblemException("The number of genes missmatched the problem definition");
		}

		double fitness = 0.0d;

		for (Entry<Double, Double> c : contour.getContour().entrySet()) {
			// offset, pitch
			// Log.debug("Processing note (" + c.getKey() + "," + c.getValue() +
			// ")");
			fitness += Math.pow(mean - c.getValue(), 2.0d);
		}

		return fitness;
	}

	@Override
	public Individual localSearch(Individual individual) {
		return individual;
	}

	@Override
	protected void loadTimeMatrix() throws ProblemException {
		// TODO Auto-generated method stub
		
	}

}
