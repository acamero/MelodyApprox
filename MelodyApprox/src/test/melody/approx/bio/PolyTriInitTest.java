package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.Individual;
import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.IndividualInitInterface.IndividualInitInterfaceException;
import com.melody.approx.bio.PolyTriInit;
import com.melody.approx.bio.Problem.ProblemException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class PolyTriInitTest {
	
	private PolyTriInit init;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		init = new PolyTriInit(1.0d, 1.0d);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IndividualInitInterfaceException.class)
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
	
	@Test(expected = IndividualInitInterfaceException.class)
	public void zeroGene() throws ChromosomeException, IndividualInitInterfaceException, ProblemException {
		init.nextIndividual(0);
	}

}
