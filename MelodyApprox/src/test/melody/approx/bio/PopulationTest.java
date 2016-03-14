package test.melody.approx.bio;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;
import com.melody.approx.bio.ProblemLegendre;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.PitchContourException;

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
		ProblemLegendre legendre = new ProblemLegendre(pc);
		LegendreInit legInit = new LegendreInit(1, legendre);
		init = legInit;
		pop = new Population(popSize, numberOfGenes, init);
	}

	@After
	public void tearDown() {
	}

	@Test(expected = PopulationException.class)
	public void negPopSize() throws PopulationException, ChromosomeException, IndividualInitInterfaceException, ProblemException {
		pop = new Population(-1, numberOfGenes, init);
	}

	@Test(expected = PopulationException.class)
	public void nullInit() throws PopulationException, ChromosomeException, IndividualInitInterfaceException, ProblemException {
		pop = new Population(popSize, numberOfGenes, null);
	}

	@Test(expected = PopulationException.class)
	public void negGenes() throws PopulationException, ChromosomeException, IndividualInitInterfaceException, ProblemException {
		pop = new Population(popSize, -1, init);
	}

	@Test
	public void create() throws PopulationException, ChromosomeException {
		assertEquals(popSize, pop.getPopulationSize());
	}

	@Test
	public void replaceWorst() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		offsprings.add(new Individual(numberOfGenes));
		pop.replaceIndividuals(offsprings);
		fail("must implement assert");
	}

	@Test
	public void replaceN() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize - 2; i++) {
			offsprings.add(new Individual(numberOfGenes));
		}
		Log.info("Replace N=" + offsprings.size() + " , Pop=" + popSize);
		pop.replaceIndividuals(offsprings);
		fail("must implement assert");
	}

	@Test
	public void replaceElite() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize - 1; i++) {
			offsprings.add(new Individual(numberOfGenes));
		}
		Log.info("Replace elite (N=" + offsprings.size() + ")");
		pop.replaceIndividuals(offsprings);
		fail("must implement assert");
	}
	
	@Test
	public void replaceEqual() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize ; i++) {
			offsprings.add(new Individual(numberOfGenes));
		}
		Log.info("Replace elite (N=" + offsprings.size() + ")");
		pop.replaceIndividuals(offsprings);
		fail("must implement assert");
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

	@Test(expected = PopulationException.class)
	public void biggerOffsprings() throws PopulationException, ChromosomeException {
		List<Individual> offsprings = new ArrayList<Individual>();
		for (int i = 0; i < popSize + 1; i++) {
			offsprings.add(new Individual(numberOfGenes));
		}
		pop.replaceIndividuals(offsprings);
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
		for (int i = 0; i < popSize ; i++) {
			offsprings.add(new Individual(numberOfGenes));
		}
		Log.info("Replace elite (N=" + offsprings.size() + ")");
		pop.replaceIndividuals(offsprings);
		pop.getPopulation().add(new Individual(1));
	}

}
