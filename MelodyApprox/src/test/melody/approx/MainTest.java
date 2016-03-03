package test.melody.approx;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.Main;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.PitchContourException;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class MainTest {
	
	private static String serializeFile = "temp.ser";

	@BeforeClass
	public static void oneTimeSetUp() {
	}

	@AfterClass
	public static void oneTimeTearDown() {
		File file = new File(serializeFile);
		file.delete();
	}

	@Before
	public void setUp() {
		Log.setLogLevel(LogLevel.INFO);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void changeLogLevel() {

		String[] args = new String[1];
		args[0] = "--log-level=SILENT";
		Main.main(args);
		assertEquals(Log.getLogLevel(), LogLevel.SILENT);
	}

	@Test
	public void help() {
		String[] args = new String[1];
		args[0] = "-h";
		Main.main(args);		
	}

	@Test
	public void changeFileNamePath() {
		assertEquals("name.new", Main.getFileName("/my/path/name.ext", "new"));
	}
	
	@Test
	public void changeFileNameNoPath() {
		assertEquals("name.new", Main.getFileName("name.ext", "new"));
	}
	
	@Test
	public void changeFileNamePathNoExt() {
		assertEquals("name.new", Main.getFileName("/my/path/name", "new"));
	}
	
	@Test
	public void changeFileNameNoPathNoExt() {
		assertEquals("name.new", Main.getFileName("name", "new"));
	}
	
	@Test
	public void serializeMelody() throws PitchContourException, MelodyException, ClassNotFoundException, IOException {
		Melody melody = new Melody();
		PitchContour pc = new PitchContour();
		pc.appendFrequency(0.0d, 440.0d);
		melody.addPhrase(0.0d, pc);
		Main.serialize(melody, serializeFile);
		
		Melody desMelody = (Melody)Main.deserialize(serializeFile);
		assertEquals(melody.getContourType(),desMelody.getContourType());
		for(Entry<Double, PitchContour> s : melody.getPhrases().entrySet()) {
			PitchContour desPc = desMelody.getPhrases().get(s.getKey());
			for(Entry<Double,Double> e : desPc.getContour().entrySet()) {
				assertEquals(s.getValue().getContour().get(e.getKey()), e.getValue(),0.0d);
			}
		}
	}
}