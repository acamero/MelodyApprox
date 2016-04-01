package test.melody.approx.bio;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Individual;
import com.melody.approx.bio.IndividualInitInterface;
import com.melody.approx.bio.IndividualInitInterface.IndividualInitInterfaceException;
import com.melody.approx.bio.LegendreInit;
import com.melody.approx.bio.Population;
import com.melody.approx.bio.Population.PopulationException;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.bio.ProblemLegendre;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.PitchContourException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class PopulationTest {

	private Population pop;
	private int popSize = 5;
	private IndividualInitInterface init;
	private int numberOfGenes = 1;

	@BeforeClass
	public static void oneTimeSetUp() {
		Log.setLogLevel(LogLevel.INFO);
	}

	@Before
	public void setUp() throws PopulationException, ChromosomeException, IndividualInitInterfaceException,
			ProblemException, PitchContourException {
		PitchContour pc = new PitchContour();
		pc.appendFrequency(0.0d, 440.0d);
		// ProblemLegendre legendre = new ProblemLegendre(pc);
		LegendreInit legInit = new LegendreInit(440.0d, 1.0d);
		init = legInit;
		pop = new Population(popSize, numberOfGenes, init);
		ProblemLegendre fit = new ProblemLegendre(pc);
		for (Individual ind : pop.getPopulation()) {
			ind.setFitness(fit.getFitness(ind));
			Log.info("Individual added " + ind.toString());
		}
	}

	@After
	public void tearDown() {
	}

	@Test(expected = PopulationException.class)
	public void negPopSize()
			throws PopulationException, ChromosomeException, IndividualInitInterfaceException, ProblemException {
		pop = new Population(-1, numberOfGenes, init);
	}

	@Test(expected = PopulationException.class)
	public void nullInit()
			throws PopulationException, ChromosomeException, IndividualInitInterfaceException, ProblemException {
		pop = new Population(popSize, numberOfGenes, null);
	}

	@Test(expected = PopulationException.class)
	public void negGenes()
			throws PopulationException, ChromosomeException, IndividualInitInterfaceException, ProblemException {
		pop = new Population(popSize, -1, init);
	}

	@Test
	public void create() throws PopulationException, ChromosomeException {
		assertEquals(popSize, pop.getPopulationSize());
	}

	@Test
	public void replaceWorst() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		Individual tmp = new Individual(numberOfGenes);
		tmp.setFitness(1.0d);
		offsprings.add(tmp);
		double expectedFitness = 0.0d;
		for (Individual ind : pop.getPopulation()) {
			expectedFitness += ind.getFitness();
		}
		expectedFitness -= pop.getPopulation().get(pop.getWorstPosition()).getFitness();
		expectedFitness += tmp.getFitness();
		pop.replaceIndividuals(offsprings);

		double newFitness = 0.0d;
		for (Individual ind : pop.getPopulation()) {
			newFitness += ind.getFitness();
		}
		assertEquals(expectedFitness, newFitness, 0.000001d);
	}

	@Test
	public void replaceN() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		int n =popSize - 3;
		for (int i = 0; i < n; i++) {
			Individual tmp = new Individual(numberOfGenes);
			tmp.setFitness(1.0d);
			offsprings.add(tmp);
		}
		
		List<Double> fits = new ArrayList<Double>();
		for (Individual ind : pop.getPopulation()) {
			fits.add(ind.getFitness());
		}
		Collections.sort(fits);		
		double expectedFitness = 0.0d;
		for(int i=0;i<popSize-n;i++) {
			expectedFitness += fits.get(i);
		}
		
		for (Individual ind : offsprings) {
			expectedFitness += ind.getFitness();
		}
		
		Log.info("Replace N=" + offsprings.size() + " , Pop=" + popSize);
		pop.replaceIndividuals(offsprings);
		
		double newFitness = 0.0d;
		for (Individual ind : pop.getPopulation()) {
			newFitness += ind.getFitness();
		}

		assertEquals(expectedFitness, newFitness, 0.000001d);
	}

	@Test
	public void replaceElite() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize - 1; i++) {
			Individual tmp = new Individual(numberOfGenes);
			tmp.setFitness(1.0d);
			offsprings.add(tmp);
		}

		double expectedFitness = pop.getPopulation().get(pop.getBestPosition()).getFitness();
		for (Individual ind : offsprings) {
			expectedFitness += ind.getFitness();
		}

		Log.info("Replace elite (N=" + offsprings.size() + ")");
		pop.replaceIndividuals(offsprings);

		double newFitness = 0.0d;
		for (Individual ind : pop.getPopulation()) {
			newFitness += ind.getFitness();
		}

		assertEquals(expectedFitness, newFitness, 0.000001d);
	}

	@Test
	public void replaceEqual() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize; i++) {
			Individual tmp = new Individual(numberOfGenes);
			tmp.setFitness(1.0d);
			offsprings.add(tmp);
		}

		double expectedFitness = 0.0d;
		for (Individual ind : offsprings) {
			expectedFitness += ind.getFitness();
		}

		Log.info("Replace elite (N=" + offsprings.size() + ")");
		pop.replaceIndividuals(offsprings);

		double newFitness = 0.0d;
		for (Individual ind : pop.getPopulation()) {
			newFitness += ind.getFitness();
		}

		assertEquals(expectedFitness, newFitness, 0.000001d);
	}

	@Test(expected = PopulationException.class)
	public void emptyOffsprings() throws PopulationException {
		List<Individual> offsprings = new ArrayList<Individual>();
		pop.replaceIndividuals(offsprings);
	}

	@Test(expected = PopulationException.class)
	public void nullOffsprings() throws PopulationException {
		pop.replaceIndividuals(null);
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void replaceWorstAdd() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		offsprings.add(new Individual(numberOfGenes));
		pop.replaceIndividuals(offsprings);
		pop.getPopulation().add(new Individual(1));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void replaceNAdd() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize - 2; i++) {
			offsprings.add(new Individual(numberOfGenes));
		}
		Log.info("Replace N=" + offsprings.size() + " , Pop=" + popSize);
		pop.replaceIndividuals(offsprings);
		pop.getPopulation().add(new Individual(1));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void replaceEliteAdd() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize - 1; i++) {
			offsprings.add(new Individual(numberOfGenes));
		}
		Log.info("Replace elite (N=" + offsprings.size() + ")");
		pop.replaceIndividuals(offsprings);
		pop.getPopulation().add(new Individual(1));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void replaceEqualAdd() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize; i++) {
			offsprings.add(new Individual(numberOfGenes));
		}
		Log.info("Replace elite (N=" + offsprings.size() + ")");
		pop.replaceIndividuals(offsprings);
		pop.getPopulation().add(new Individual(1));
	}
	
	@Test
	public void replaceBigger() throws ChromosomeException, PopulationException {
		Log.setLogLevel(LogLevel.DEBUG);
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize+2; i++) {
			offsprings.add(new Individual(numberOfGenes));
			offsprings.get(i).setFitness(2.0d*(double)i);
			Log.debug("Individual fitness set ="+offsprings.get(i).getFitness());
		}
		for(int i=0; i< popSize;i++) {
			pop.getPopulation().get(i).setFitness(1.0d +(2.0d*(double)i));
			Log.debug("Individual fitness set ="+pop.getPopulation().get(i).getFitness());
		}
		
		pop.replaceIndividuals(offsprings);
		for(int i=0; i< popSize;i++) {
			assertEquals(i,(int)pop.getPopulation().get(i).getFitness());
		}
	}

}
