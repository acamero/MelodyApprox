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
		LEGENDRE3(4, 0.0d), LEGENDRE5(6, 0.0d), 
		POLYTRI_CUSTOM(15, 16.0d), MEAN(1, 16.0d),
		POLYTRI_6616(15, 16.0d), POLYTRI_6632(15, 32.0d),POLYTRI_6664(15, 64.0d),POLYTRI_66128(15, 128.0d),POLYTRI_66256(15, 256.0d),
		POLYTRI_101016(23, 16.0d), POLYTRI_101032(23, 32.0d),POLYTRI_101064(23, 64.0d),POLYTRI_1010128(23, 128.0d),POLYTRI_1010256(23, 256.0d),
		POLYTRI_202016(43, 16.0d), POLYTRI_202032(43, 32.0d),POLYTRI_202064(43, 64.0d),POLYTRI_2020128(43, 128.0d),POLYTRI_2020256(43, 256.0d),
		POLYTRI_303016(63, 16.0d), POLYTRI_303032(63, 32.0d),POLYTRI_303064(63, 64.0d),POLYTRI_3030128(63, 128.0d),POLYTRI_3030256(63, 256.0d)
		;

		private int numberOfGenes;
		private double omega;

		private AlgorithmType(int numberOfGenes, double omega) {
			this.numberOfGenes = numberOfGenes;
			this.omega = omega;
		}

		public int getNumberOfGenes() {
			return numberOfGenes;
		}

		public double getOmega() {
			return omega;
		}

		public void setNumberOfGenes(int numberOfGenes) {
			this.numberOfGenes = numberOfGenes;
		}

		public void setOmega(double omega) {
			this.omega = omega;
		}
	}

	private Melody melody;
	private double crossoverProb;
	private double mutationProb;
	private AlgorithmType algorithmType;
	private int maxEvaluations;
	private int popSize;
	private int offspringSize;
	private boolean narrowMutation;
	private Problem problem;
	private Problem.LocalSearchType localSearchType;

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
		this.setLocalSearchType(null);
		setNarrowMutation(false);

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
		int points = 0;
		long startTime = System.currentTimeMillis();
		
		// apply the algorithm to each one of the contours
		for (Entry<Double, PitchContour> e : melody.getPhrases().entrySet()) {
			algorithm = prepareAlgorithm(e.getValue());
			long initTime = System.currentTimeMillis();
			Log.info("Init time(" + e.getKey() + "): " + initTime);
			individual = algorithm.startAlgorithm();
			long endTime = System.currentTimeMillis();
			Log.info("End time(" + e.getKey() + "): " + endTime);
			Log.info("Offset" + e.getKey() + "\t" + algorithm.statsToString());
			fitness += individual.getFitness();

			partialWriter.write(prepend);
			partialWriter.write(algorithmType.toString() + ";");
			partialWriter.write(e.getKey() + ";");
			partialWriter.write(initTime + ";");
			partialWriter.write(endTime + ";");
			partialWriter.write(individual.getFitness() + ";");
			partialWriter.write(algorithm.getEvaluations() + ";");
			partialWriter.write(e.getValue().getContour().size() + ";");
			partialWriter.write(individual.getChromosome().toString());
			partialWriter.write("\n");
			points += e.getValue().getContour().size();
		}
		long finishTime = System.currentTimeMillis();

		finalWriter.write(prepend);
		finalWriter.write(algorithmType.toString() + ";");
		;
		finalWriter.write(startTime + ";");
		finalWriter.write(finishTime + ";");
		finalWriter.write(fitness + ";");
		finalWriter.write(points + "");
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
		LocalSearchInterface localSearch = null;

		switch (this.algorithmType) {
		case LEGENDRE3:
			mutationProb = 1.0d / algorithmType.getNumberOfGenes();
		case LEGENDRE5:
			problem = new ProblemLegendre(contour);			
			individualInit = new LegendreInit(mean, stdDev);
			mutationProb = 1.0d / algorithmType.getNumberOfGenes();
			break;
		case POLYTRI_CUSTOM:
			int cosSin = (algorithmType.getNumberOfGenes() - ProblemPolyTri.BASE_CONSTANTS) / 2;
			problem = new ProblemPolyTri(contour, cosSin, cosSin, algorithmType.getOmega());
			individualInit = new PolyTriInit(mean, stdDev);
			mutationProb = 1.0d / algorithmType.getNumberOfGenes();
			break;		
			
		case MEAN:
			individualInit = new LegendreInit(mean, stdDev);
			problem = new ProblemMean(contour, mean);
			popSize = 1;
			maxEvaluations = 1;
			offspringSize = 1;
			crossoverProb = 0.0d;
			mutationProb = 0.0d;
			break;
		default: // POLYTRI_xxx
			cosSin = (algorithmType.getNumberOfGenes() - ProblemPolyTri.BASE_CONSTANTS) / 2;
			problem = new ProblemPolyTri(contour, cosSin, cosSin, algorithmType.getOmega());
			individualInit = new PolyTriInit(mean, stdDev);
			mutationProb = 1.0d / algorithmType.getNumberOfGenes();
			break;
		}
		
		if(localSearchType!=null) {
			problem.setLocalSearchType(localSearchType);
		}
		
		fitnessCalc = problem;
		localSearch = problem;

		if (isNarrowMutation()) {
			mutationInterface = new NarrowMutation(stdDev);
		} else {
			mutationInterface = new SimpleMutation(stdDev);
		}
		population = new Population(this.popSize, this.algorithmType.getNumberOfGenes(), individualInit);

		algorithm = new Algorithm(fitnessCalc, population, this.offspringSize, this.maxEvaluations, crossoverInterface,
				mutationInterface, this.crossoverProb, this.mutationProb, localSearch);

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

	public boolean isNarrowMutation() {
		return narrowMutation;
	}

	public void setNarrowMutation(boolean narrowMutation) {
		this.narrowMutation = narrowMutation;
	}

	public Problem.LocalSearchType getLocalSearchType() {
		return localSearchType;
	}

	public void setLocalSearchType(Problem.LocalSearchType localSearchType) {
		this.localSearchType = localSearchType;
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
