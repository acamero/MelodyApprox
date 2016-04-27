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
		Log.debug("Proceed to load time matrix and solution vector");
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
		
		return calculateFitness(individual);
	}
	
	@Override
	protected void loadTimeMatrix() {
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
