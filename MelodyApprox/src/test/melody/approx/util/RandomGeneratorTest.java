package test.melody.approx.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.melody.approx.util.RandomGenerator;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class RandomGeneratorTest {

	@Before
	public void setUp() {
		RandomGenerator.setSeed(0);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void initialSeed() {
		assertEquals(0, RandomGenerator.getActualSeedPosition());
	}

	@Test
	public void moveOneSeed() {
		RandomGenerator.nextSeed();
		assertEquals(1, RandomGenerator.getActualSeedPosition());
	}

	@Test
	public void setOneSeed() {
		RandomGenerator.setSeed(1);
		assertEquals(1, RandomGenerator.getActualSeedPosition());
	}

	@Test
	public void setOneNegSeed() {
		RandomGenerator.setSeed(-1);
		assertEquals(1, RandomGenerator.getActualSeedPosition());
	}

	@Test
	public void setOneRoundSeed() {
		RandomGenerator.setSeed(RandomGenerator.getNumberOfSeeds() + 1);
		assertEquals(1, RandomGenerator.getActualSeedPosition());
	}

	@Test
	public void intervalDouble() {
		double d = RandomGenerator.nextDouble(true, true);
		assertTrue(d <= 1.0d);
		assertTrue(d >= 0.0d);
	}

	@Test
	public void nextDouble() {
		double d = RandomGenerator.nextDouble();
		assertTrue(Double.isFinite(d));
	}

	@Test
	public void nextInt() {
		int i = RandomGenerator.nextInt();
		assertTrue(Integer.MAX_VALUE >= i);
		assertTrue(Integer.MIN_VALUE <= i);
	}

}
