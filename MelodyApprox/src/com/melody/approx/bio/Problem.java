package com.melody.approx.bio;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.util.Log;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public abstract class Problem implements FitnessInterface, LocalSearchInterface {
	
	protected PitchContour contour;
	protected double targetFitness = 0.0d;
	protected double[][] timeMatrix;
	protected double[] solutionVector;

	public Problem(PitchContour contour) throws ProblemException {		
		this.contour = contour;
		if(this.contour==null) {
			Log.error("Pitch contour should not be null");
			throw new ProblemException("Pitch contour should not be null");
		} else if(this.contour.getContour().isEmpty()) {
			Log.error("Pitch contour should not be empty");
			throw new ProblemException("Pitch contour should not be empty");
		}		
		
	}
	
	public double getTargetFitness() {
		return targetFitness;
	}
	
	protected double calculateFitness(Individual individual) {
		double fitness = 0.0d;

		for(int i=0;i<solutionVector.length;i++) {
			double note = 0.0d;
			for (int j = 0; j < individual.getNumberOfGenes(); j++) {
				note += individual.getChromosome().getGene(j) * timeMatrix[i][j];
			}
			fitness += Math.pow(note - solutionVector[i], 2.0d);
		}		
		
		return fitness;
	}
	
	public abstract double getFitness(Individual individual) throws ProblemException ;
	
	protected abstract void loadTimeMatrix() throws ProblemException;
	
	@Override
	public Individual localSearch(Individual individual) throws ChromosomeException, ProblemException {
		// for each gene perform the local search
		Individual temp = individual.copy();
		for (int sk = 0; sk < individual.getNumberOfGenes(); sk++) {
			Individual local = temp.copy();
			
			// iterate over time matrix
			double accum = 0.0d;
			for (int i = 0; i < solutionVector.length; i++) {
				double partNote = 0.0d;
				for (int j = 0; j < local.getNumberOfGenes(); j++) {
					if( j!=sk) {
						partNote += local.getChromosome().getGene(j) * timeMatrix[i][j];
					}
				}
				accum += (solutionVector[i] - partNote) / timeMatrix[i][sk];
			}
			double propGene = accum / (double)solutionVector.length;
			local.getChromosome().setGene(sk, propGene);
			local.setFitness( getFitness(local) );
			
			if (local.getFitness() < temp.getFitness()) {
				// if the change improves the fitness, preserve
				temp = local;
				Log.debug("Individual updated by local search, new fitness="+local.getFitness());
			} 	// otherwise rollback the last change (do nothing)
				
		}
		return temp;
	}
	
	public class ProblemException extends Exception {
		private static final long serialVersionUID = -3722473717296968691L;
		public ProblemException(String message) {
			super(message);
		}

		public ProblemException(Throwable cause) {
			super(cause);
		}

		public ProblemException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
}
