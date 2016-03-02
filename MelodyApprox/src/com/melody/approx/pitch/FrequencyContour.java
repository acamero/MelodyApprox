package com.melody.approx.pitch;


public class FrequencyContour extends PitchContour {
	
	public void appendFrequency(double time, double frequency) throws PitchContourException {
		if (frequency <= 0.0d) {
			throw new PitchContourException("Frequency should be greater than 0");
		} else if (time <= 0.0d) {
			throw new PitchContourException("Duration should be greater than 0");
		}
		contour.put(time, frequency);		
	}

	public void appendMidi(double time, int midi) throws PitchContourException {
		if (time <= 0.0d) {
			throw new PitchContourException("Duration should be greater than 0");
		}
		contour.put(time, midiToFrequency(midi));		
	}
}
