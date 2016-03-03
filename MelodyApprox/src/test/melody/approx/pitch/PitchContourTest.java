package test.melody.approx.pitch;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.pitch.PitchContour.PitchContourException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class PitchContourTest {

	PitchContour pc;

	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() {

	}

	@Test(expected = PitchContourException.class)
	public void zeroDurationSilence() throws PitchContourException {
		pc = new PitchContour();
		pc.appendSilence(0.0d);
	}

	@Test
	public void a4MidiToFreq() throws PitchContourException {
		assertEquals(440.0d, PitchContour.midiToFrequency(69), 0.01d);
	}

	@Test
	public void c2MidiToFreq() throws PitchContourException {
		assertEquals(65.406d, PitchContour.midiToFrequency(36), 0.01d);
	}

	@Test
	public void a4FreqToMidi() throws PitchContourException {
		assertEquals(69, PitchContour.frequencyToMidi(440.0d));
	}

	@Test
	public void c2FreqToMidi() throws PitchContourException {
		assertEquals(36, PitchContour.frequencyToMidi(65.406d));
	}

	@Test(expected = PitchContourException.class)
	public void negativeMidiToFreq() throws PitchContourException {
		PitchContour.midiToFrequency(-1);
	}

	@Test(expected = PitchContourException.class)
	public void zeroMidiToFreq() throws PitchContourException {
		PitchContour.midiToFrequency(0);
	}

	@Test(expected = PitchContourException.class)
	public void negativeFreqToMidi() throws PitchContourException {
		PitchContour.frequencyToMidi(-1.0d);
	}

	@Test(expected = PitchContourException.class)
	public void zeroFreqToMidi() throws PitchContourException {
		PitchContour.frequencyToMidi(0.0d);
	}
	
	@Test
	public void contourToString() throws PitchContourException {
		pc = new PitchContour(ContourType.FREQUENCY);
		pc.appendFrequency(0.0d, 440.0d);
		pc.appendFrequency(0.1d, 440.0d);
		String expected = "Contour type: "+ContourType.FREQUENCY.toString()+"\n";
		expected += "Offset: "+Double.toString(0.0d)+" Pitch: "+Double.toString(440.0d)+"\n";
		expected += "Offset: "+Double.toString(.1d)+" Pitch: "+Double.toString(440.0d)+"\n";
		assertEquals(expected,pc.toString());
	}

}
