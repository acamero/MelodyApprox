package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.CrossoverInterface.CrossoverException;
import com.melody.approx.bio.Individual;
import com.melody.approx.bio.SinglePointCrossover;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class SinglePointCrossoverTest {

	private SinglePointCrossover spx;
	private Individual indA, indB;
	private int length=10;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log.setLogLevel(LogLevel.INFO);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		spx = new SinglePointCrossover();
		length=10;
		indA = new Individual(length);
		indA.getChromosome().setGene(0, 1.0d);
		indA.getChromosome().setGene(length-1, 1.0d);
		indB = new Individual(length);
		indB.getChromosome().setGene(0, 2.0d);
		indB.getChromosome().setGene(length-1, 2.0d);
		Log.info("IndA: "+indA.toString());
		Log.info("IndB: "+indB.toString());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = CrossoverException.class)
	public void nullA() throws CrossoverException {
		spx.crossover(0.0d, null, indB);
	}
	
	@Test(expected = CrossoverException.class)
	public void nullB() throws CrossoverException {
		spx.crossover(0.0d, indA, null);
	}
	
	@Test(expected = CrossoverException.class)
	public void diffGenes() throws CrossoverException, ChromosomeException {
		Individual tmp = new Individual(length+1);
		spx.crossover(0.0d, indA, tmp);
	}
	
	@Test
	public void zeroP() throws CrossoverException {
		Individual tmp = spx.crossover(0.0d, indA, indB);
		Log.info("ZeroP "+ tmp.toString());
		assertTrue( indA.toString().equals(tmp.toString()) || indB.toString().equals(tmp.toString()));
	}

	@Test
	public void oneP() throws CrossoverException {
		Individual tmp = spx.crossover(1.0d, indA, indB);
		Log.info("OneP "+ tmp.toString());
		assertTrue( !indA.toString().equals(tmp.toString()) && !indB.toString().equals(tmp.toString()));
	}
}
