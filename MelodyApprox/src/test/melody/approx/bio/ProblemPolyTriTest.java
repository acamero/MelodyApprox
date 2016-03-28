package test.melody.approx.bio;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.Individual;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.bio.ProblemPolyTri;
import com.melody.approx.dsp.MelodiaReader;
import com.melody.approx.dsp.MelodiaReader.MelodiaReaderException;
import com.melody.approx.dsp.SilenceChopper;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.pitch.PitchContour.PitchContourException;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class ProblemPolyTriTest {
	
	private PitchContour pc ;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		pc = new PitchContour(ContourType.MIDI);
		pc.appendMidi(1.0d, 69);
		pc.appendMidi(2.0d, 69);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = ProblemException.class)
	public void negativeSin() throws ProblemException {
		new ProblemPolyTri(pc, -1, 1, 1.0d);
	}
	
	@Test(expected = ProblemException.class)
	public void negativeCos() throws ProblemException {
		new ProblemPolyTri(pc, 1, -1, 1.0d);
	}
	
	@Test(expected = ProblemException.class)
	public void negativeOmega() throws ProblemException {
		new ProblemPolyTri(pc, 1, 1, -1.0d);
	}
	
	@Test(expected = ProblemException.class)
	public void nullContour() throws ProblemException {
		new ProblemPolyTri(null, -1, 1, 1.0d);
	}
	
	@Test(expected = ProblemException.class)
	public void missmatchedGenes() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);
		Individual ind = new Individual(10);
		problem.getFitness(ind);
	}
	
	@Test(expected = ProblemException.class)
	public void nullIndividual() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);		
		problem.getFitness(null);
	}
	
	@Test
	public void constantFitness() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);
		Individual ind = new Individual(5);
		ind.getChromosome().setGene(0, 69.0d);
		ind.setFitness(problem.getFitness(ind));
		assertEquals(0.0d, ind.getFitness(),0.0d);
	}
	
	@Test
	public void linearFitness() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);
		Individual ind = new Individual(5);
		ind.getChromosome().setGene(0, 69.0d);
		ind.getChromosome().setGene(1, 1.0d);
		ind.setFitness(problem.getFitness(ind));
		assertEquals(5.0d, ind.getFitness(),0.0d);
	}
	
	@Test
	public void quadFitness() throws ProblemException, ChromosomeException {
		ProblemPolyTri problem = new ProblemPolyTri(pc, 1, 1, 1.0d);
		Individual ind = new Individual(5);
		ind.getChromosome().setGene(0, 69.0d);
		ind.getChromosome().setGene(2, 1.0d);
		ind.setFitness(problem.getFitness(ind));
		assertEquals(17.0d, ind.getFitness(),0.0d);
	}
	
	@Test
	public void test() throws MelodiaReaderException, PitchContourException, MelodyException, FileNotFoundException,
			ProblemException, ChromosomeException {
		Log.setLogLevel(LogLevel.SILENT);
		MelodiaReader reader = new SilenceChopper();
		FileReader fr = new FileReader("test/london-bridge-melodia.csv");
		BufferedReader br = new BufferedReader(fr);
		Melody melody = Melody.transform(reader.getMelody(br), ContourType.MIDI);
		
		Individual ind = new Individual(9);
		ind.getChromosome().setGene(0, 68.52691876745764d);
		ind.getChromosome().setGene(1, -0.2190010090338726d);
		ind.getChromosome().setGene(2, 0.39810526605256696d);
		ind.getChromosome().setGene(3, -0.5170268168714599d);
		ind.getChromosome().setGene(4, -0.03777658420894796d);
		ind.getChromosome().setGene(5, -1.2416479640115767d);
		ind.getChromosome().setGene(6, -0.6349102686936535d);
		ind.getChromosome().setGene(7, -0.5131233051419892d);
		ind.getChromosome().setGene(8, 0.3074289422453135d);
		
		Log.setLogLevel(LogLevel.DEBUG);
		ProblemPolyTri problem = new ProblemPolyTri(melody.getPhrases().get(5.590204082d), 3, 3, 1.0d);
		ind.setFitness( problem.getFitness(ind));
		Log.debug("Offset 5.590204082 \tFitness: "+ind.getFitness());
		
	}

}
