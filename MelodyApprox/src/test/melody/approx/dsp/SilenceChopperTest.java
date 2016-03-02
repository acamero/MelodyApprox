package test.melody.approx.dsp;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
		fw.write("0.8" + melodia.getSeparator() + "-110.0\n");
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
	}

	@AfterClass
	public static void oneTimeTearDown() {
		File testFile = new File(correct);
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
	public void correctFile() throws MelodiaReaderException, PitchContourException, MelodyException {
		Melody mTest = mReader.getMelody(correct);
		
		assertEquals(mTest.getPhrases().keySet(), melody.getPhrases().keySet());
	}

}
