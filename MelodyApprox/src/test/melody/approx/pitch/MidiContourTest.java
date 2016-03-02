package test.melody.approx.pitch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.pitch.PitchContour.PitchContourException;

public class MidiContourTest {
	PitchContour pc;
	
	@Before
	public void setUp() {
		pc = new PitchContour(ContourType.MIDI);
	}

	@After
	public void tearDown() {
		
	}
	
	@Test(expected = PitchContourException.class)
	public void negativeMidi() throws PitchContourException {
		pc.appendMidi(1.0d, -1);
	}
	
	@Test(expected = PitchContourException.class)
	public void zeroMidi() throws PitchContourException {
		pc.appendMidi(1.0d, 0);
	}
	
	@Test(expected = PitchContourException.class)
	public void zeroDurationMidi() throws PitchContourException {
		pc.appendMidi(0.0d, 69);
	}
	
	@Test(expected = PitchContourException.class)
	public void negativeFrequency() throws PitchContourException {
		pc.appendFrequency(1.0d, -1.0d);
	}
	
	@Test(expected = PitchContourException.class)
	public void zeroFrequency() throws PitchContourException {
		pc.appendFrequency(1.0d, 0.0d);
	}

}
