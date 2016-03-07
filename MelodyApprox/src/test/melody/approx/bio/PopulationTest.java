package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.Test;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Population;
import com.melody.approx.bio.Population.PopulationException;

public class PopulationTest {

	private Population pop;
	
	@Test(expected = PopulationException.class)
	public void negPopSize() throws PopulationException, ChromosomeException {
		pop = new Population(-1,1,null);
	}
	
	@Test(expected = ChromosomeException.class)
	public void nullInit() throws PopulationException, ChromosomeException {
		pop = new Population(1,1,null);
	}
	
	@Test(expected = ChromosomeException.class)
	public void negGenes() throws PopulationException, ChromosomeException {
		pop = new Population(1,-1,null);
	}

	@Test
	public void create() throws PopulationException, ChromosomeException {
		int popSize = 10;
		pop = new Population(popSize,1,null);
		assertEquals(popSize,pop.getPopulationSize());
	}
}
