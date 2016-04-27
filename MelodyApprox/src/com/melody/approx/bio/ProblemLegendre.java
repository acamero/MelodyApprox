package com.melody.approx.bio;

import java.util.Map.Entry;

import com.melody.approx.pitch.PitchContour;
import com.melody.approx.util.Log;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class ProblemLegendre extends Problem {
	private static final int MAX_ORDER = 5;

	public ProblemLegendre(PitchContour contour) throws ProblemException {
		super(contour);
		timeMatrix = new double[contour.getContour().size()][MAX_ORDER + 1];
		solutionVector = new double[contour.getContour().size()];
		// load time matrix
		loadTimeMatrix();
	}

	@Override
	public double getFitness(Individual individual) throws ProblemException {
		if (individual == null) {
			throw new ProblemException("Null individual");
		} else if (individual.getNumberOfGenes() > (MAX_ORDER + 1)) {
			throw new ProblemException("Unsupported Legendre order");
		}


		return calculateFitness(individual);
	}

	private double legendrePoly(double t, int order) throws ProblemException {
		switch (order) {
		case 0:
			return 1.0d;
		case 1:
			return t;
		case 2:
			// return (3.0d * Math.pow(t, 2.0d) - 1.0d) * 0.5d;
			return 1.5d * Math.pow(t, 2.0d) - 0.5d;
		case 3:
			// return (5.0d * Math.pow(t, 3.0d) - 3.0d * t) * 0.5d;
			return 2.5d * Math.pow(t, 3.0d) - 1.5d * t;
		case 4:
			// return (35.0d * Math.pow(t, 4.0d) - 30.0d * Math.pow(t, 2.0d) +
			// 3.0d)
			// * 0.125d;
			return 4.375d * Math.pow(t, 4.0d) - 4.75d * Math.pow(t, 2.0d) + 0.375d;
		case 5:
			// return (63.0d * Math.pow(t, 5.0d) - 70.0d * Math.pow(t, 3.0d) +
			// 15.0d
			// * t) * 0.125d;
			return 7.875d * Math.pow(t, 5.0d) - 8.75d * Math.pow(t, 3.0d) + 1.875d * t;
		default:
			Log.error("Unimplemented Legendre polynomials order (order=" + order + ")");
			throw new ProblemException("Unimplemented Legendre polynomials order (order=" + order + ")");
		}
	}

	@Override
	protected void loadTimeMatrix() throws ProblemException {
		int i = 0;
		for (Entry<Double, Double> c : contour.getContour().entrySet()) {
			for (int j = 0; j <= MAX_ORDER; j++) {
				timeMatrix[i][j] = legendrePoly(c.getKey(), j);
			}
			solutionVector[i] = c.getValue();
			i++;
		}

	}

	

}
