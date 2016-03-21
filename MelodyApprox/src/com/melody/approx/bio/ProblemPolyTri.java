package com.melody.approx.bio;

import java.util.Map.Entry;

import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.pitch.PitchContour;

public class ProblemPolyTri extends Problem {

	private int sinOffset;	
	private int numberOfGenes;
	private double omega;
	
	public ProblemPolyTri(PitchContour contour, int numberSin, int numberCos, double omega) throws ProblemException {
		super(contour);		
		if(numberCos<0 || numberSin<0) {
			throw new ProblemException("The number of 'sin' and 'cos' should be equal or greater than zero");
		}
		
		if(omega<=0.0d) {
			throw new ProblemException("Interpolation frequency should be greater than zero");
		}
		
		this.numberOfGenes = numberCos + numberSin + 2;
		this.omega = omega;
		this.sinOffset = 2+numberSin;		
	}

	@Override
	public double getFitness(Individual individual) throws ProblemException {
		if(individual==null) {
			throw new ProblemException("Null individual");
		}
		
		if( individual.getNumberOfGenes() != numberOfGenes ) {
			throw new ProblemException("The number of genes missmatched the problem definition");
		}
		
		double fitness = 0.0d;

		for (Entry<Double, Double> c : contour.getContour().entrySet()) {
			// offset, pitch
			// Log.debug("Processing note (" + c.getKey() + "," + c.getValue() + ")");
			double note = 0.0d;
			
			// alpha
			note += individual.getChromosome().getGene(0) * c.getKey();
			// beta
			note += individual.getChromosome().getGene(1) * c.getKey() * c.getKey();
			
			for (int i = 2; i < sinOffset; i++) {
				note += individual.getChromosome().getGene(i) * Math.sin( i * omega * c.getKey());
			}
			
			for (int i = sinOffset; i < numberOfGenes; i++) {
				note += individual.getChromosome().getGene(i) * Math.cos( i * omega * c.getKey());
			}
			
			fitness += Math.pow(note - c.getValue(), 2.0d);
		}

		return fitness;
	}

}