package com.melody.approx.bio;

import java.util.HashMap;
import java.util.Map;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.util.Log;
import com.melody.approx.util.RandomGenerator;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public abstract class Problem implements FitnessInterface, LocalSearchInterface {

	public enum LocalSearchType {
		Mean, Mode, Random, None;
	}

	protected PitchContour contour;
	protected double targetFitness = 0.0d;
	protected double[][] timeMatrix;
	protected double[] solutionVector;
	protected LocalSearchType localSearchType;

	public Problem(PitchContour contour) throws ProblemException {
		this.contour = contour;
		if (this.contour == null) {
			Log.error("Pitch contour should not be null");
			throw new ProblemException("Pitch contour should not be null");
		} else if (this.contour.getContour().isEmpty()) {
			Log.error("Pitch contour should not be empty");
			throw new ProblemException("Pitch contour should not be empty");
		}

		localSearchType = LocalSearchType.None;
		Log.debug("Local search set to: " + localSearchType.toString());

	}

	public LocalSearchType getLocalSearchType() {
		return localSearchType;
	}

	public void setLocalSearchType(LocalSearchType localSearchType) {
		this.localSearchType = localSearchType;
	}

	public double getTargetFitness() {
		return targetFitness;
	}

	protected double calculateFitness(Individual individual) {
		double fitness = 0.0d;

		for (int i = 0; i < solutionVector.length; i++) {
			double note = 0.0d;
			for (int j = 0; j < individual.getNumberOfGenes(); j++) {
				note += individual.getChromosome().getGene(j) * timeMatrix[i][j];
			}
			fitness += Math.pow(Math.floor(note) - solutionVector[i], 2.0d);
		}

		return fitness;
	}

	public abstract double getFitness(Individual individual) throws ProblemException;

	protected abstract void loadTimeMatrix() throws ProblemException;

	protected double meanSearch(Individual local, int sk) {
		// iterate over time matrix
		double accum = 0.0d;
		for (int i = 0; i < solutionVector.length; i++) {
			double partNote = 0.0d;
			for (int j = 0; j < local.getNumberOfGenes(); j++) {
				if (j != sk) {
					partNote += local.getChromosome().getGene(j) * timeMatrix[i][j];
				}
			}
			accum += (solutionVector[i] - partNote) / timeMatrix[i][sk];
		}

		return accum / (double) solutionVector.length;
	}

	protected double modeSearch(Individual local, int sk) {
		// iterate over time matrix
		Map<Double, Integer> values = new HashMap<Double, Integer>();
		for (int i = 0; i < solutionVector.length; i++) {
			double partNote = 0.0d;
			for (int j = 0; j < local.getNumberOfGenes(); j++) {
				if (j != sk) {
					partNote += local.getChromosome().getGene(j) * timeMatrix[i][j];
				}
			}
			double value = (solutionVector[i] - partNote) / timeMatrix[i][sk];
			if (values.containsKey(value)) {
				values.put(value, values.get(value) + 1);
			} else {
				values.put(value, 1);
			}
		}

		int max = 0;
		double prop = 0.0d;

		for (Map.Entry<Double, Integer> entry : values.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				prop = entry.getKey();
			}
		}
		/*
		 * values = Utils.sortByComparator(values); for (Map.Entry<Double,
		 * Integer> entry : values.entrySet()) { return entry.getKey(); }
		 */
		return prop;

	}

	@Override
	public Individual localSearch(Individual individual) throws ChromosomeException, ProblemException {
		if (localSearchType.equals(LocalSearchType.None)) {
			return individual;
		}

		// for each gene perform the local search
		Individual temp = individual.copy();
		// double stdDev = 1.0d;
		for (int sk = 0; sk < individual.getNumberOfGenes(); sk++) {
			Individual local = temp.copy();
			double propGene;

			switch (localSearchType) {
			case Random:
				propGene = RandomGenerator.nextGaussian(1.0d, local.getChromosome().getGene(sk));
				break;
			case Mean:
				propGene = meanSearch(local, sk);
				break;
			case Mode:
				propGene = modeSearch(local, sk);
				break;
			default:
				propGene = local.getChromosome().getGene(sk);
				break;
			}
			
			local.getChromosome().setGene(sk, propGene);
			local.setFitness(getFitness(local));

			if (local.getFitness() < temp.getFitness()) {
				// if the change improves the fitness, preserve
				temp = local;
				Log.debug("Individual updated by local search, new fitness=" + local.getFitness());
			} // otherwise rollback the last change (do nothing)

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
