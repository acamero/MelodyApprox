package test.melody.approx.pitch;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.PitchContourException;

public class PitchContourTest {

	PitchContour pc;

	@Before
	public void setUp() {
		pc = new PitchContour();
	}

	@After
	public void tearDown() {

	}

	@Test(expected = PitchContourException.class)
	public void zeroDurationSilence() throws PitchContourException {
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

}
