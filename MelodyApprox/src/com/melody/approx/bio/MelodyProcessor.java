package com.melody.approx.bio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map.Entry;

import com.melody.approx.bio.Algorithm.AlgorithmException;
import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.CrossoverInterface.CrossoverException;
import com.melody.approx.bio.IndividualInitInterface.IndividualInitInterfaceException;
import com.melody.approx.bio.MutationInterface.MutationException;
import com.melody.approx.bio.Population.PopulationException;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.util.Log;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class MelodyProcessor {

	public enum AlgorithmType {
		LEGENDRE3(4), LEGENDRE5(6), POLYTRI(8);

		private int numberOfGenes;

		private AlgorithmType(int numberOfGenes) {
			this.numberOfGenes = numberOfGenes;
		}

		public int getNumberOfGenes() {
			return numberOfGenes;
		}
	}

	private Melody melody;
	private double crossoverProb;
	private double mutationProb;
	private AlgorithmType algorithmType;
	private int maxEvaluations;
	private int popSize;
	private int offspringSize;

	public MelodyProcessor(AlgorithmType algorithmType, Melody melody) throws MelodyProcessorException {
		if (melody == null || melody.getPhrases().isEmpty()) {
			Log.error("Melody is null or empty");
			throw new MelodyProcessorException("Melody is null or empty");
		}

		if (algorithmType == null) {
			Log.error("Algorithm type is null");
			throw new MelodyProcessorException("Algorithm type is null");
		}

		this.melody = melody;
		this.algorithmType = algorithmType;

		// Default values
		this.setCrossoverProb(0.8d);
		this.setMutationProb(0.005d);
		this.setMaxEvaluations(10000);
		this.setPopSize(50);
		this.setOffspringSize(1);

	}

	public void processMelody(BufferedWriter partialWriter, BufferedWriter finalWriter, String prepend)
			throws ProblemException, AlgorithmException, PopulationException, ChromosomeException,
			IndividualInitInterfaceException, CrossoverException, MutationException, IOException,
			MelodyProcessorException {
		if (partialWriter == null || finalWriter == null) {
			Log.error("Buffered writers should not be null");
			throw new MelodyProcessorException("Buffered writers should not be null");
		}
		Algorithm algorithm;
		Individual individual;
		double fitness = 0.0d;
		long startTime = System.currentTimeMillis();
		// apply the algorithm to each one of the contours
		for (Entry<Double, PitchContour> e : melody.getPhrases().entrySet()) {
			algorithm = prepareAlgorithm(e.getValue());
			long initTime = System.currentTimeMillis();
			Log.info("Init time(" + e.getKey() + "): " + initTime);
			individual = algorithm.startAlgorithm();
			long endTime = System.currentTimeMillis();
			Log.info("End time(" + e.getKey() + "): " + endTime);
			Log.info("Offset"+e.getKey()+ "\t"+ algorithm.statsToString());
			fitness += individual.getFitness();

			partialWriter.write(prepend);
			partialWriter.write(algorithmType.toString()+";");
			partialWriter.write(e.getKey() + ";");
			partialWriter.write(initTime + ";");
			partialWriter.write(endTime + ";");
			partialWriter.write(individual.getFitness() + ";");
			partialWriter.write(algorithm.getEvaluations() + ";");
			partialWriter.write(e.getValue().getContour().size() + ";");
			partialWriter.write(individual.getChromosome().toString());
			partialWriter.write("\n");
		}
		long finishTime = System.currentTimeMillis();

		finalWriter.write(prepend);
		finalWriter.write(algorithmType.toString()+";");;
		finalWriter.write(startTime + ";");
		finalWriter.write(finishTime + ";");
		finalWriter.write(fitness+"");
		finalWriter.write("\n");

		Log.info("Start time=" + startTime + "\tFinish time=" + finishTime + "\tTotal fitness=" + fitness);
	}

	private Algorithm prepareAlgorithm(PitchContour contour) throws ProblemException, AlgorithmException,
			PopulationException, ChromosomeException, IndividualInitInterfaceException {
		Algorithm algorithm;
		double mean = calculateMean(contour);
		double stdDev = calculateStdDev(contour, mean);

		CrossoverInterface crossoverInterface = new SinglePointCrossover();
		MutationInterface mutationInterface = null;
		FitnessInterface fitnessCalc = null;
		Population population = null;
		IndividualInitInterface individualInit = null;

		switch (this.algorithmType) {
		case LEGENDRE3:
		case LEGENDRE5:
			mutationInterface = new LegendreMutation(stdDev);
			fitnessCalc = new ProblemLegendre(contour);
			individualInit = new LegendreInit(mean, stdDev);
			break;
		case POLYTRI:
			mutationInterface = new PolyTriMutation(stdDev);
			int cosSin = (algorithmType.getNumberOfGenes() - 2) / 2;
			fitnessCalc = new ProblemPolyTri(contour, cosSin, cosSin, 32.0d);
			individualInit = new PolyTriInit(mean, stdDev);
			break;
		}

		population = new Population(this.popSize, this.algorithmType.getNumberOfGenes(), individualInit);

		algorithm = new Algorithm(fitnessCalc, population, this.offspringSize, this.maxEvaluations, crossoverInterface,
				mutationInterface, this.crossoverProb, this.mutationProb);

		return algorithm;
	}

	private double calculateMean(PitchContour contour) {
		if (contour.getContour().isEmpty()) {
			return 0.0d;
		}

		double accum = 0.0d;
		for (Double value : contour.getContour().values()) {
			accum += value;
		}

		return accum / (double) contour.getContour().size();
	}

	private double calculateStdDev(PitchContour contour, double mean) {
		if (contour.getContour().isEmpty()) {
			return 0.0d;
		}

		double accum = 0.0d;
		for (Double value : contour.getContour().values()) {
			accum += Math.pow(value - mean, 2.0d);
		}

		return Math.sqrt(accum / (double) contour.getContour().size());
	}

	public double getCrossoverProb() {
		return crossoverProb;
	}

	public void setCrossoverProb(double crossoverProb) {
		if (crossoverProb < 0.0d || crossoverProb > 1.0d) {
			Log.warning("Crossover probability is out of range " + crossoverProb);
		}
		this.crossoverProb = crossoverProb;
	}

	public double getMutationProb() {
		return mutationProb;
	}

	public void setMutationProb(double mutationProb) {
		if (mutationProb < 0.0d || mutationProb > 1.0d) {
			Log.warning("Mutation probability is out of range " + mutationProb);
		}
		this.mutationProb = mutationProb;
	}

	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public void setMaxEvaluations(int maxEvaluations) {
		if (maxEvaluations < 1) {
			Log.warning("Max number of evaluations is out of range" + popSize);
		}
		this.maxEvaluations = maxEvaluations;
	}

	public int getPopSize() {
		return popSize;
	}

	public void setPopSize(int popSize) {
		if (popSize < 1) {
			Log.warning("Population size is out of range" + popSize);
		}
		this.popSize = popSize;
	}

	public int getOffspringSize() {
		return offspringSize;
	}

	public void setOffspringSize(int offspringSize) {
		if (offspringSize < 1) {
			Log.warning("Offspring size is out of range" + offspringSize);
		}
		this.offspringSize = offspringSize;
	}

	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}

	public Melody getMelody() {
		return melody;
	}

	public class MelodyProcessorException extends Exception {
		private static final long serialVersionUID = -348644576773649014L;

		public MelodyProcessorException(String message) {
			super(message);
		}

		public MelodyProcessorException(Throwable cause) {
			super(cause);
		}

		public MelodyProcessorException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
