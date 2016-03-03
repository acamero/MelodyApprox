package test.melody.approx.dsp;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.dsp.MelodiaReader.MelodiaReaderException;
import com.melody.approx.dsp.SilenceChopper;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.PitchContourException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class SilenceChopperTest {
	private static final String correct = "correct.tmp";
	private static final String disordered = "disordered.tmp";
	private static Melody melody = new Melody();
	private SilenceChopper mReader;	

	@BeforeClass
	public static void oneTimeSetUp() throws IOException, PitchContourException, MelodyException {
		SilenceChopper melodia = new SilenceChopper();

		File testFile = new File(correct);
		if (!testFile.exists()) {
			testFile.createNewFile();
		}

		FileWriter fw = new FileWriter(testFile);
		fw.write("0.1" + melodia.getSeparator() + "440.0\n");
		fw.write("0.2" + melodia.getSeparator() + "440.0\n");
		fw.write("0.3" + melodia.getSeparator() + "220.0\n");
		fw.write("0.4" + melodia.getSeparator() + "220.0\n");
		fw.write("0.5" + melodia.getSeparator() + "-220.0\n");
		fw.write("0.6" + melodia.getSeparator() + "-110.0\n");
		fw.write("0.7" + melodia.getSeparator() + "110.0\n");
		fw.write("0.8" + melodia.getSeparator() + "-110.0\n");
		fw.write("0.9" + melodia.getSeparator() + "880.0\n");
		fw.write("1.0" + melodia.getSeparator() + "880.0\n");
		fw.close();
		
		testFile = new File(disordered);
		if (!testFile.exists()) {
			testFile.createNewFile();
		}

		fw = new FileWriter(testFile);
		fw.write("0.4" + melodia.getSeparator() + "220.0\n");
		fw.write("0.1" + melodia.getSeparator() + "440.0\n");		
		fw.write("0.3" + melodia.getSeparator() + "220.0\n");
		fw.write("0.2" + melodia.getSeparator() + "440.0\n");
		fw.write("0.6" + melodia.getSeparator() + "-110.0\n");
		fw.write("0.7" + melodia.getSeparator() + "110.0\n");
		fw.write("0.5" + melodia.getSeparator() + "-220.0\n");
		fw.write("0.9" + melodia.getSeparator() + "880.0\n");
		fw.write("0.8" + melodia.getSeparator() + "-110.0\n");
		fw.write("1.0" + melodia.getSeparator() + "880.0\n");
		fw.close();
		
		PitchContour fc = new PitchContour();
		fc.appendFrequency(0.0d, 440.0d);
		fc.appendFrequency(0.1d, 440.0d);
		fc.appendFrequency(0.2d, 220.0d);
		fc.appendFrequency(0.3d, 220.0d);
		melody.addPhrase(0.1d, fc);
		
		fc = new PitchContour();
		fc.appendFrequency(0.0d, 110.0d);		
		melody.addPhrase(0.7d, fc);
		
		fc = new PitchContour();
		fc.appendFrequency(0.0d, 880.0d);
		fc.appendFrequency(1.0d, 880.0d);
		melody.addPhrase(0.9d, fc);
	}

	@AfterClass
	public static void oneTimeTearDown() {
		File testFile = new File(correct);
		testFile.delete();
		testFile = new File(disordered);
		testFile.delete();
	}

	@Before
	public void setUp() {
		mReader = new SilenceChopper();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void correctFileOffset() throws MelodiaReaderException, PitchContourException, MelodyException {
		Melody mTest = mReader.getMelody(correct);	
		assertEquals(mTest.getPhrases().keySet(), melody.getPhrases().keySet());
	}
	
	@Test
	public void correctFilePhrases() throws MelodiaReaderException, PitchContourException, MelodyException {
		Melody mTest = mReader.getMelody(correct);	
		for(Entry<Double,PitchContour> s : melody.getPhrases().entrySet()) {
			PitchContour pc = mTest.getPhrases().get(s.getKey());
			double sumOffset = 0.0d, sumOffsetTest = 0.0d;
			double sumFreq = 0.0d, sumFreqTest = 0.0d;
			for(Entry<Double,Double> e : pc.getContour().entrySet()) {
				sumFreqTest += e.getValue();
				sumOffsetTest += e.getKey();
			}
			
			for(Entry<Double,Double> e : s.getValue().getContour().entrySet()) {
				sumFreq += e.getValue();
				sumOffset += e.getKey();
			}
			assertEquals(sumFreq, sumFreqTest, 0.1d);
			assertEquals(sumOffset, sumOffsetTest, 1.0d);
		}
	}
	
	@Test
	public void disorderedFilePhrases() throws MelodiaReaderException, PitchContourException, MelodyException {
		Melody mTest = mReader.getMelody(disordered);	
		for(Entry<Double,PitchContour> s : melody.getPhrases().entrySet()) {
			PitchContour pc = mTest.getPhrases().get(s.getKey());
			double sumOffset = 0.0d, sumOffsetTest = 0.0d;
			double sumFreq = 0.0d, sumFreqTest = 0.0d;
			for(Entry<Double,Double> e : pc.getContour().entrySet()) {
				sumFreqTest += e.getValue();
				sumOffsetTest += e.getKey();
			}
			
			for(Entry<Double,Double> e : s.getValue().getContour().entrySet()) {
				sumFreq += e.getValue();
				sumOffset += e.getKey();
			}
			assertEquals(sumFreq, sumFreqTest, 0.1d);
			assertEquals(sumOffset, sumOffsetTest, 1.0d);
		}
	}
	
	@Test
	public void disorderedFileOffset() throws MelodiaReaderException, PitchContourException, MelodyException {
		Melody mTest = mReader.getMelody(disordered);		
		assertEquals(mTest.getPhrases().keySet(), melody.getPhrases().keySet());
	}

}
