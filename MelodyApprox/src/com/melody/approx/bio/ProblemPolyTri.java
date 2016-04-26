package com.melody.approx.bio;

import java.util.Map.Entry;

import com.melody.approx.pitch.PitchContour;
import com.melody.approx.util.Log;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class ProblemPolyTri extends Problem {
	public static final int BASE_CONSTANTS=3;
	private int sinOffset;	
	private int numberOfGenes;
	private double omega;
	private double[][] timeMatrix;
	private double[] solutionVector;
	
	public ProblemPolyTri(PitchContour contour, int numberSin, int numberCos, double omega) throws ProblemException {
		super(contour);		
		if(numberCos<0 || numberSin<0) {
			throw new ProblemException("The number of 'sin' and 'cos' should be equal or greater than zero");
		}
		
		if(omega<=0.0d) {
			throw new ProblemException("Interpolation frequency should be greater than zero");
		}
		
		this.numberOfGenes = numberCos + numberSin + BASE_CONSTANTS;
		this.omega = omega;
		this.sinOffset = BASE_CONSTANTS+numberSin;	
		Log.debug("Sin offset: "+sinOffset);
		timeMatrix = new double[contour.getContour().size()][numberOfGenes];
		solutionVector = new double[contour.getContour().size()];
		loadTimeMatrix();
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

		for(int i=0;i<solutionVector.length;i++) {
			double note = 0.0d;
			for (int j = 0; j < individual.getNumberOfGenes(); j++) {
				note += individual.getChromosome().getGene(j) * timeMatrix[i][j];
			}
			fitness += Math.pow(note - solutionVector[i], 2.0d);
		}
		
		/*
		for (Entry<Double, Double> c : contour.getContour().entrySet()) {
			// offset, pitch
			// Log.debug("Processing note (" + c.getKey() + "," + c.getValue() + ")");
			double note = 0.0d;
			
			//constant
			note += individual.getChromosome().getGene(0);
			// alpha
			note += individual.getChromosome().getGene(1) * c.getKey();
			// beta
			note += individual.getChromosome().getGene(2) * c.getKey() * c.getKey();
			
			for (int i = BASE_CONSTANTS; i < sinOffset; i++) {
				double j = (double)i - (double)BASE_CONSTANTS + 1.0d;				
				// angle in rads
				note += individual.getChromosome().getGene(i) * Math.sin( Math.toRadians(j * omega * c.getKey()));
				// angle in degs
				// note += individual.getChromosome().getGene(i) * Math.sin( j * omega * c.getKey());
			}
			
			for (int i = sinOffset; i < numberOfGenes; i++) {
				double j = (double)i - (double)sinOffset + 1.0d;
				// angle in rads
				note += individual.getChromosome().getGene(i) * Math.cos( Math.toRadians(j * omega * c.getKey()));
				// angle in degs
				// note += individual.getChromosome().getGene(i) * Math.cos( j * omega * c.getKey());
			}
			
			fitness += Math.pow(note - c.getValue(), 2.0d);
			
		}
		*/

		return fitness;
	}
	
	private void loadTimeMatrix() {
		int i=0;
		for (Entry<Double, Double> c : contour.getContour().entrySet()) {						
			//constant
			timeMatrix[i][0] = 1;
			// alpha
			timeMatrix[i][1] = c.getKey();
			// beta
			timeMatrix[i][2] = c.getKey() * c.getKey();
			
			for (int j = BASE_CONSTANTS; j < sinOffset; j++) {
				double k = (double)j - (double)BASE_CONSTANTS + 1.0d;				
				// angle in rads
				timeMatrix[i][j] = Math.sin( Math.toRadians(k * omega * c.getKey()));				
			}
			
			for (int j = sinOffset; j < numberOfGenes; j++) {
				double k = (double)j - (double)sinOffset + 1.0d;
				// angle in rads
				timeMatrix[i][j] = Math.cos( Math.toRadians(k * omega * c.getKey()));				
			}
			
			solutionVector[i] = c.getValue();
			i++;
		}
		
	}

}
