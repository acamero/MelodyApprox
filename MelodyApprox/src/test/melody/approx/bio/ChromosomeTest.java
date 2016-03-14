package test.melody.approx.bio;

import static org.junit.Assert.*;

import org.junit.Test;

import com.melody.approx.bio.Chromosome;
import com.melody.approx.bio.Chromosome.ChromosomeException;

public class ChromosomeTest {

	private Chromosome chromosome;
	
	@Test
	public void withoutInit() throws ChromosomeException {
		int size = 10;
		chromosome = new Chromosome(size);
		assertEquals(size,chromosome.getNumberOfGenes());
	}
	
	@Test(expected = ChromosomeException.class)
	public void negativeGenes() throws ChromosomeException {
		int size = -110;
		chromosome = new Chromosome(size);
	}
	
	@Test
	public void create() throws ChromosomeException {
		chromosome = new Chromosome(2);
		for(int i= 0;i<chromosome.getNumberOfGenes();i++) {
			assertEquals(0.0d, chromosome.getGene(i),0.0d);
		}
	}

}
