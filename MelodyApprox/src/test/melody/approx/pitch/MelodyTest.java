package test.melody.approx.pitch;

import static org.junit.Assert.*;

import org.junit.Test;

import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.pitch.PitchContour.PitchContourException;

public class MelodyTest {

	@Test(expected = MelodyException.class)
	public void differentContourType() throws MelodyException {
		Melody melody = new Melody();
		PitchContour pc = new PitchContour(ContourType.FREQUENCY);
		melody.addPhrase(0.0d, pc);
		pc = new PitchContour(ContourType.MIDI);
		melody.addPhrase(1.0d, pc);
	}
	
	@Test
	public void transformMidiToFreq() throws MelodyException, PitchContourException {
		Melody melody = new Melody();
		PitchContour pc = new PitchContour(ContourType.MIDI);
		pc.appendMidi(0.1d, 69);
		pc.appendMidi(0.2d, 69);
		melody.addPhrase(0.0d, pc);
		Melody transMelody = Melody.transform(melody, ContourType.FREQUENCY);
		assertEquals(ContourType.FREQUENCY, transMelody.getContourType());
	}

}
