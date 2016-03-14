package com.melody.approx.bio;

import com.melody.approx.pitch.PitchContour;

public abstract class Problem implements FitnessInterface {
	
	protected PitchContour contour;
	protected double targetFitness = 0.0d;

	public Problem(PitchContour contour) throws ProblemException {		
		this.contour = contour;
		if(this.contour==null) {
			throw new ProblemException("Pitch contour should not be null");
		} else if(this.contour.getContour().isEmpty()) {
			throw new ProblemException("Pitch contour should not be empty");
		}
	}
	
	public double getTargetFitness() {
		return targetFitness;
	}
	
	public abstract double getFitness(Individual individual) throws ProblemException ;
	
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
