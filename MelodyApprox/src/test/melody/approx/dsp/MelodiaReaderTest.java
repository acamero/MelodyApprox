package test.melody.approx.dsp;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.dsp.MelodiaReader;
import com.melody.approx.dsp.MelodiaReader.MelodiaReaderException;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.PitchContourException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class MelodiaReaderTest {
	private static final String correct = "correct.tmp";
	private static final String incorrectSeparator = "separator.tmp";
	private static final String incorrectNumber = "number.tmp";
	private static final String incorrectOffset = "offset.tmp";
	private static PitchContour correctNotConvert = new PitchContour();
	private static PitchContour correctConvert = new PitchContour();
	private MelodiaReaderTestImpl mReader;

	@BeforeClass
	public static void oneTimeSetUp() throws IOException, PitchContourException {
		MelodiaReaderTestImpl melodia = new MelodiaReaderTestImpl();

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

		correctConvert.appendFrequency(0.1d, 440.0d);
		correctConvert.appendFrequency(0.2d, 440.0d);
		correctConvert.appendFrequency(0.3d, 220.0d);
		correctConvert.appendFrequency(0.4d, 220.0d);
		correctConvert.appendFrequency(0.5d, 220.0d);
		correctConvert.appendFrequency(0.6d, 110.0d);
		correctConvert.appendFrequency(0.7d, 110.0d);
		correctConvert.appendFrequency(0.8d, 110.0d);

		correctNotConvert.appendFrequency(0.1d, 440.0d);
		correctNotConvert.appendFrequency(0.2d, 440.0d);
		correctNotConvert.appendFrequency(0.3d, 220.0d);
		correctNotConvert.appendFrequency(0.4d, 220.0d);
		correctNotConvert.appendSilence(0.5d);
		correctNotConvert.appendSilence(0.6d);
		correctNotConvert.appendFrequency(0.7d, 110.0d);
		correctNotConvert.appendSilence(0.8d);

		testFile = new File(incorrectSeparator);
		if (!testFile.exists()) {
			testFile.createNewFile();
		}
		fw = new FileWriter(testFile);
		fw.write("0.2440.0\n");
		fw.close();

		testFile = new File(incorrectNumber);
		if (!testFile.exists()) {
			testFile.createNewFile();
		}
		fw = new FileWriter(testFile);
		fw.write("0.1" + melodia.getSeparator() + "440.a\n");
		fw.close();

		testFile = new File(incorrectOffset);
		if (!testFile.exists()) {
			testFile.createNewFile();
		}
		fw = new FileWriter(testFile);
		fw.write("-0.1" + melodia.getSeparator() + "440.0\n");
		fw.close();
	}

	@AfterClass
	public static void oneTimeTearDown() {
		File testFile = new File(correct);
		testFile.delete();
		testFile = new File(incorrectSeparator);
		testFile.delete();
		testFile = new File(incorrectNumber);
		testFile.delete();
		testFile = new File(incorrectOffset);
		testFile.delete();
	}

	@Before
	public void setUp() {
		mReader = new MelodiaReaderTestImpl();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void correctReadNotConvert() throws MelodiaReaderException, PitchContourException, IOException {
		FileReader fr = new FileReader(correct);
		BufferedReader br = new BufferedReader(fr);
		PitchContour pc = mReader.readMelodiaTest(br, false);
		for (Entry<Double, Double> s : correctNotConvert.getContour().entrySet()) {
			assertEquals(pc.getContour().get(s.getKey()), s.getValue(), 0.001d);
		}
		br.close();
	}

	@Test
	public void correctReadConvert() throws MelodiaReaderException, PitchContourException, IOException {
		FileReader fr = new FileReader(correct);
		BufferedReader br = new BufferedReader(fr);
		PitchContour pc = mReader.readMelodiaTest(br, true);
		for (Entry<Double, Double> s : correctConvert.getContour().entrySet()) {
			assertEquals(pc.getContour().get(s.getKey()), s.getValue(), 0.001d);
		}
		br.close();
	}

	@Test(expected = MelodiaReaderException.class)
	public void incorrectSeparatorNotConvert() throws MelodiaReaderException, PitchContourException, IOException {
		FileReader fr = new FileReader(incorrectSeparator);
		BufferedReader br = new BufferedReader(fr);
		mReader.readMelodiaTest(br, false);
		br.close();
	}

	@Test(expected = MelodiaReaderException.class)
	public void incorrectSeparatorConvert() throws MelodiaReaderException, PitchContourException, IOException {
		FileReader fr = new FileReader(incorrectSeparator);
		BufferedReader br = new BufferedReader(fr);
		mReader.readMelodiaTest(br, true);
		br.close();
	}

	@Test(expected = MelodiaReaderException.class)
	public void incorrectNumberNotConvert() throws MelodiaReaderException, PitchContourException, IOException {
		FileReader fr = new FileReader(incorrectNumber);
		BufferedReader br = new BufferedReader(fr);
		mReader.readMelodiaTest(br, false);
		br.close();
	}

	@Test(expected = MelodiaReaderException.class)
	public void incorrectNumberConvert() throws MelodiaReaderException, PitchContourException, IOException {
		FileReader fr = new FileReader(incorrectNumber);
		BufferedReader br = new BufferedReader(fr);
		mReader.readMelodiaTest(br, true);
		br.close();
	}

	@Test(expected = PitchContourException.class)
	public void incorrectOffsetNotConvert() throws MelodiaReaderException, PitchContourException, IOException {
		FileReader fr = new FileReader(incorrectOffset);
		BufferedReader br = new BufferedReader(fr);
		mReader.readMelodiaTest(br, false);
		br.close();
	}

	@Test(expected = PitchContourException.class)
	public void incorrectOffsetConvert() throws MelodiaReaderException, PitchContourException, IOException {
		FileReader fr = new FileReader(incorrectOffset);
		BufferedReader br = new BufferedReader(fr);
		mReader.readMelodiaTest(br, true);
		br.close();
	}

	protected static class MelodiaReaderTestImpl extends MelodiaReader {

		public PitchContour readMelodiaTest(BufferedReader bufferedReader, boolean convertNegative)
				throws MelodiaReaderException, PitchContourException {
			return readMelodia(bufferedReader, convertNegative);
		}

		@Override
		public Melody getMelody(BufferedReader bufferedReader)
				throws MelodiaReaderException, PitchContourException, MelodyException {
			return null;
		}

	}

}
