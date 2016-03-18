package test.melody.approx.bio;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Individual;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class IndividualTest {

	private Individual ind;
	
	@Test
	public void withoutInit() throws ChromosomeException {
		int size = 10;
		ind = new Individual(size);
		assertEquals(size,ind.getNumberOfGenes());
	}
	
	@Test
	public void compareG() throws ChromosomeException {
		int size = 10;
		ind = new Individual(size);
		ind.setFitness(1.0d);
		Individual indG = new Individual(size);
		indG.setFitness(2.0d);
		assertTrue(ind.compareTo(indG)<0);
	}
	
	@Test
	public void compareL() throws ChromosomeException {
		int size = 10;
		ind = new Individual(size);
		ind.setFitness(1.0d);
		Individual indG = new Individual(size);
		indG.setFitness(2.0d);
		assertTrue(indG.compareTo(ind)>0);
	}
	
	@Test
	public void compareE() throws ChromosomeException {
		int size = 10;
		ind = new Individual(size);
		ind.setFitness(1.0d);		
		assertTrue(ind.compareTo(ind)==0);
	}
	
	@Test
	public void sortList() throws ChromosomeException {
		List<Individual> inds = Arrays.asList(new Individual[3]);
		ind = new Individual(10);
		ind.setFitness(2.0d);
		inds.set(0, ind);
		ind = new Individual(10);
		ind.setFitness(1.0d);		
		inds.set(1,ind);
		ind = new Individual(10);
		ind.setFitness(3.0d);		
		inds.set(2,ind);
		Collections.sort(inds);
		assertEquals(1.0d,inds.get(0).getFitness(),0.0d);
		assertEquals(2.0d,inds.get(1).getFitness(),0.0d);
		assertEquals(3.0d,inds.get(2).getFitness(),0.0d);
	}
}
