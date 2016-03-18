package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Individual;
import com.melody.approx.bio.IndividualInitInterface.IndividualInitInterfaceException;
import com.melody.approx.bio.LegendreInit;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class LegendreInitTest {
	
	private LegendreInit init;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log.setLogLevel(LogLevel.INFO);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {	
		init = new LegendreInit(60.0d,1.0d);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void oneGene() throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		Individual ind = init.nextIndividual(1);
		assertEquals(1, ind.getNumberOfGenes());
	}
	
	@Test
	public void twoGenes() throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		Individual ind = init.nextIndividual(2);
		assertEquals(2, ind.getNumberOfGenes());
	}
	
	@Test
	public void sixGenes() throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		Individual ind = init.nextIndividual(6);
		assertEquals(6, ind.getNumberOfGenes());
	}
	
	@Test(expected = ChromosomeException.class)
	public void zeroGene() throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		init.nextIndividual(0);
	}

}
