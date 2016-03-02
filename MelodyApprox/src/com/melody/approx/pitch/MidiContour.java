package com.melody.approx.pitch;


public class MidiContour extends PitchContour {
	
	public void appendFrequency(double time, double frequency) throws PitchContourException {
		if (time <= 0.0d) {
			throw new PitchContourException("Duration should be greater than 0");
		}
		contour.put(time, (double)frequencyToMidi(frequency));		
	}

	public void appendMidi(double time, int midi) throws PitchContourException {
		if (time <= 0.0d) {
			throw new PitchContourException("Duration should be greater than 0");
		} else if (midi<=0){
			throw new PitchContourException("MIDI should be greater than 0");
		}
		contour.put(time, (double)midi);		
	}
}
