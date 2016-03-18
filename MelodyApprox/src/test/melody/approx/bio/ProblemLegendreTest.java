package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Individual;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.bio.ProblemLegendre;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class ProblemLegendreTest {

	private PitchContour pc;
	private Individual individual;
	private ProblemLegendre problem;

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
		pc.appendMidi(0.0d, 69);
		pc.appendMidi(1.0d, 69);
		problem = new ProblemLegendre(pc);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void zeroFitness1Genes() throws ChromosomeException, ProblemException {
		individual = new Individual(1);
		individual.getChromosome().setGene(0, 69);

		double fitness = problem.getFitness(individual);
		assertEquals(0.0d, fitness, 0.0d);
	}

	@Test
	public void zeroFitness2Genes() throws ChromosomeException, ProblemException {
		individual = new Individual(2);
		individual.getChromosome().setGene(0, 69);
		individual.getChromosome().setGene(1, 0);

		double fitness = problem.getFitness(individual);
		assertEquals(0.0d, fitness, 0.0d);
	}

	@Test
	public void zeroFitness3Genes() throws ChromosomeException, ProblemException {
		individual = new Individual(3);
		individual.getChromosome().setGene(0, 69);
		individual.getChromosome().setGene(1, 0);
		individual.getChromosome().setGene(2, 0);

		double fitness = problem.getFitness(individual);
		assertEquals(0.0d, fitness, 0.0d);
	}

	@Test
	public void zeroFitness4Genes() throws ChromosomeException, ProblemException {
		individual = new Individual(4);
		individual.getChromosome().setGene(0, 69);
		individual.getChromosome().setGene(1, 0);
		individual.getChromosome().setGene(2, 0);
		individual.getChromosome().setGene(3, 0);

		double fitness = problem.getFitness(individual);
		assertEquals(0.0d, fitness, 0.0d);
	}

	@Test
	public void zeroFitness5Genes() throws ChromosomeException, ProblemException {
		individual = new Individual(5);
		individual.getChromosome().setGene(0, 69);
		individual.getChromosome().setGene(1, 0);
		individual.getChromosome().setGene(2, 0);
		individual.getChromosome().setGene(3, 0);
		individual.getChromosome().setGene(4, 0);

		double fitness = problem.getFitness(individual);
		assertEquals(0.0d, fitness, 0.0d);
	}

	@Test
	public void zeroFitness6Genes() throws ChromosomeException, ProblemException {
		individual = new Individual(6);
		individual.getChromosome().setGene(0, 69);
		individual.getChromosome().setGene(1, 0);
		individual.getChromosome().setGene(2, 0);
		individual.getChromosome().setGene(3, 0);
		individual.getChromosome().setGene(4, 0);
		individual.getChromosome().setGene(5, 0);

		double fitness = problem.getFitness(individual);
		assertEquals(0.0d, fitness, 0.0d);
	}

	@Test(expected = ProblemException.class)
	public void nonImplementedPoly() throws ChromosomeException, ProblemException {
		individual = new Individual(7);
		problem.getFitness(individual);
	}
}
