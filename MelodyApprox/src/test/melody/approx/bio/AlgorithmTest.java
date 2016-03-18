package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.CrossoverInterface;
import com.melody.approx.bio.CrossoverInterface.CrossoverException;
import com.melody.approx.bio.FitnessInterface;
import com.melody.approx.bio.Individual;
import com.melody.approx.bio.IndividualInitInterface;
import com.melody.approx.bio.LegendreInit;
import com.melody.approx.bio.LegendreMutation;
import com.melody.approx.bio.MutationInterface;
import com.melody.approx.bio.MutationInterface.MutationException;
import com.melody.approx.bio.Population;
import com.melody.approx.bio.Population.PopulationException;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.bio.ProblemLegendre;
import com.melody.approx.bio.SinglePointCrossover;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.bio.Algorithm;
import com.melody.approx.bio.Algorithm.AlgorithmException;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class AlgorithmTest {

	private int popSize = 10;
	private int maxEvals = 20;
	private int genes = 4;
	private FitnessInterface fitnessInt;
	private CrossoverInterface crossoverInt;
	private MutationInterface mutateInt;
	private Population population;
	private PitchContour pc;
	private IndividualInitInterface initInterface;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log.setLogLevel(LogLevel.INFO);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		pc = new PitchContour(ContourType.MIDI);
		pc.appendMidi(0.0d, 60);
		pc.appendMidi(0.5d, 61);
		pc.appendMidi(1.0d, 63);
		pc.appendMidi(1.5d, 60);
		fitnessInt = new ProblemLegendre(pc);
		crossoverInt = new SinglePointCrossover();
		mutateInt = new LegendreMutation(1.0d);
		initInterface = new LegendreInit(62.0d, 1.0d);
		population = new Population(popSize, genes, initInterface);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = AlgorithmException.class)
	public void nullFitnessInterface() throws ProblemException, AlgorithmException {
		new Algorithm(null, population, popSize - 1, maxEvals, crossoverInt, mutateInt, 1.0d, 1.0d);
	}

	@Test(expected = AlgorithmException.class)
	public void nullPopulation() throws ProblemException, AlgorithmException {
		new Algorithm(fitnessInt, null, popSize - 1, maxEvals, crossoverInt, mutateInt, 1.0d, 1.0d);
	}

	@Test(expected = AlgorithmException.class)
	public void nullCrossover() throws ProblemException, AlgorithmException {
		new Algorithm(fitnessInt, population, popSize - 1, maxEvals, null, mutateInt, 1.0d, 1.0d);
	}

	@Test(expected = AlgorithmException.class)
	public void nullMutate() throws ProblemException, AlgorithmException {
		new Algorithm(fitnessInt, population, popSize - 1, maxEvals, crossoverInt, null, 1.0d, 1.0d);
	}

	@Test(expected = AlgorithmException.class)
	public void zeroOffspring() throws ProblemException, AlgorithmException {
		new Algorithm(fitnessInt, population, 0, maxEvals, crossoverInt, mutateInt, 1.0d, 1.0d);
	}

	@Test(expected = AlgorithmException.class)
	public void greaterOffspring() throws ProblemException, AlgorithmException {
		new Algorithm(fitnessInt, population, popSize + 1, maxEvals, crossoverInt, mutateInt, 1.0d, 1.0d);
	}

	@Test
	public void steadyAlgorithm()
			throws ProblemException, AlgorithmException, PopulationException, CrossoverException, MutationException {
		Algorithm alg = new Algorithm(fitnessInt, population, 1, maxEvals, crossoverInt, mutateInt, 1.0d, 1.0d);
		Individual ind = alg.startAlgorithm();
		Log.info("Best individual "+ind.toString()+ " after "+alg.getEvaluations()+" evaluations");
		assertTrue(alg.getEvaluations()>=maxEvals);
	}
	
	@Test
	public void eliteAlgorithm()
			throws ProblemException, AlgorithmException, PopulationException, CrossoverException, MutationException {
		Algorithm alg = new Algorithm(fitnessInt, population, popSize-1, maxEvals, crossoverInt, mutateInt, 1.0d, 1.0d);
		Individual ind = alg.startAlgorithm();
		Log.info("Best individual "+ind.toString()+ " after "+alg.getEvaluations()+" evaluations");
		assertTrue(alg.getEvaluations()>=maxEvals);
	}

}
