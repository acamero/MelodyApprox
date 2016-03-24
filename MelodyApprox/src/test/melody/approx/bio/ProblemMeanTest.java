package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.ProblemMean;
import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Individual;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;

public class ProblemMeanTest {
	
	private ProblemMean problem ;
	private PitchContour contour;
	private double mean;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		contour = new PitchContour(ContourType.MIDI);
		contour.appendMidi(0.0d, 69);
		contour.appendMidi(1.0d, 70);
		contour.appendMidi(2.0d, 71);
		mean = 70.0d;
		problem = new ProblemMean(contour, mean);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected =ProblemException.class)
	public void nullIndividual() throws ProblemException {
		 problem.getFitness(null);
	}
	
	@Test
	public void fitness() throws ChromosomeException, ProblemException {
		Individual ind = new Individual(1);
		double fit = problem.getFitness(ind);
		assertEquals(2.0d, fit, 0.0d);
	}

}
