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
import com.melody.approx.bio.ProblemPolyTri;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class ProblemPolyTriTest {
	
	private PitchContour pc ;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		pc = new PitchContour(ContourType.MIDI);
		pc.appendMidi(1.0d, 69);
		pc.appendMidi(2.0d, 69);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = ProblemException.class)
	public void negativeSin() throws ProblemException {
		new ProblemPolyTri(pc, -1, 1, 1.0d);
	}
	
	@Test(expected = ProblemException.class)
	public void negativeCos() throws ProblemException {
		new ProblemPolyTri(pc, 1, -1, 1.0d);
	}
	
	@Test(expected = ProblemException.class)
	public void negativeOmega() throws ProblemException {
		new ProblemPolyTri(pc, 1, 1, -1.0d);
	}
	
	@Test(expected = ProblemException.class)
	public void nullContour() throws ProblemException {
		new ProblemPolyTri(null, -1, 1, 1.0d);
	}
	
	@Test(expected = ProblemException.class)
	public void missmatchedGenes() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);
		Individual ind = new Individual(10);
		problem.getFitness(ind);
	}
	
	@Test(expected = ProblemException.class)
	public void nullIndividual() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);		
		problem.getFitness(null);
	}
	
	@Test
	public void constantFitness() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);
		Individual ind = new Individual(5);
		ind.getChromosome().setGene(0, 69.0d);
		ind.setFitness(problem.getFitness(ind));
		assertEquals(0.0d, ind.getFitness(),0.0d);
	}
	
	@Test
	public void linearFitness() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);
		Individual ind = new Individual(5);
		ind.getChromosome().setGene(0, 69.0d);
		ind.getChromosome().setGene(1, 1.0d);
		ind.setFitness(problem.getFitness(ind));
		assertEquals(5.0d, ind.getFitness(),0.0d);
	}
	
	@Test
	public void quadFitness() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);
		Individual ind = new Individual(5);
		ind.getChromosome().setGene(0, 69.0d);
		ind.getChromosome().setGene(2, 1.0d);
		ind.setFitness(problem.getFitness(ind));
		assertEquals(17.0d, ind.getFitness(),0.0d);
	}

}
