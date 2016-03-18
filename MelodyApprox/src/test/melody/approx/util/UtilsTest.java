package test.melody.approx.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour.PitchContourException;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;
import com.melody.approx.util.Utils;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class UtilsTest {

	private static String serializeFile = "temp.ser";

	@BeforeClass
	public static void oneTimeSetUp() {
		Log.setLogLevel(LogLevel.INFO);
	}

	@AfterClass
	public static void oneTimeTearDown() {
		File file = new File(serializeFile);
		file.delete();
	}

	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() {
	}
	
	@Test
	public void serializeMelody() throws PitchContourException, MelodyException, ClassNotFoundException, IOException {
		Melody melody = new Melody();
		PitchContour pc = new PitchContour();
		pc.appendFrequency(0.0d, 440.0d);
		melody.addPhrase(0.0d, pc);
		Utils.serialize(melody, serializeFile);
		
		Melody desMelody = (Melody)Utils.deserialize(serializeFile);
		assertEquals(melody.getContourType(),desMelody.getContourType());
		for(Entry<Double, PitchContour> s : melody.getPhrases().entrySet()) {
			PitchContour desPc = desMelody.getPhrases().get(s.getKey());
			for(Entry<Double,Double> e : desPc.getContour().entrySet()) {
				assertEquals(s.getValue().getContour().get(e.getKey()), e.getValue(),0.0d);
			}
		}
	}

}
