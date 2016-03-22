package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.Individual;
import com.melody.approx.bio.LegendreMutation;
import com.melody.approx.bio.MutationInterface.MutationException;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class LegendreMutationTest {

	private LegendreMutation mut;
	private Individual ind;
	private String original;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log.setLogLevel(LogLevel.INFO);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		mut = new LegendreMutation(1.0d);
		ind = new Individual(10);
		original = ind.toString();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void mutateP1() throws MutationException {
		mut.mutate(1.0d, ind);
		assertNotEquals(original, ind.toString());
	}

	@Test
	public void mutateP0() throws MutationException {
		mut.mutate(0.0d, ind);
		assertEquals(original, ind.toString());
	}

	@Test(expected = MutationException.class)
	public void nullIndividual() throws MutationException {
		mut.mutate(1.0d, null);
	}

}
