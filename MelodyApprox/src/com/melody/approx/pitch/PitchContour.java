package com.melody.approx.pitch;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PitchContour {
	private static final double LOG2 = 0.6931472d;
	private static final double SILENCE = 0.0d;
	private ContourType contourType = ContourType.FREQUENCY;
	protected Map<Double, Double> contour = new HashMap<Double, Double>();

	public PitchContour() {
	}

	public PitchContour(ContourType contourType) {
		this.contourType = contourType;
	}

	public void appendFrequency(double time, double frequency) throws PitchContourException {
		if (frequency <= 0.0d) {
			throw new PitchContourException("Frequency should be greater than 0");
		} else if (time < 0.0d) {
			throw new PitchContourException("Duration should be greater than 0");
		}

		switch (contourType) {
		case FREQUENCY:
			contour.put(time, frequency);
			break;
		case MIDI:
			contour.put(time, (double) frequencyToMidi(frequency));
			break;
		}

	}

	public void appendMidi(double time, int midi) throws PitchContourException {
		if (midi <= 0.0d) {
			throw new PitchContourException("MIDI should be greater than 0");
		} else if (time < 0.0d) {
			throw new PitchContourException("Duration should be greater than 0");
		}
		contour.put(time, midiToFrequency(midi));
		switch (contourType) {
		case FREQUENCY:
			contour.put(time, midiToFrequency(midi));
			break;
		case MIDI:
			contour.put(time, (double) midi);
			break;
		}
	}

	public void appendSilence(double time) throws PitchContourException {
		if (time <= 0.0d) {
			throw new PitchContourException("Duration should be greater than 0");
		}
		contour.put(time, SILENCE);
	}

	public Map<Double, Double> getContour() {
		return contour;
	}

	public ContourType getContourType() {
		return contourType;
	}

	public static double getSilence() {
		return SILENCE;
	}

	public static int frequencyToMidi(double frequency) throws PitchContourException {
		if (frequency <= 0.0d) {
			throw new PitchContourException("Frequency should be greater than 0");
		}
		return (int) Math.round(Math.max(0.0d, Math.log(frequency / 440.0d) / LOG2 * 12.0d + 69.0d));
	}

	public static double midiToFrequency(int midi) throws PitchContourException {
		if (midi <= 0) {
			throw new PitchContourException("MIDI should be greater than 0");
		}
		return 440.0d * Math.pow(2.0d, (midi - 69.0d) / 12.0d);
	}

	public static PitchContour transform(PitchContour in, ContourType outType) throws PitchContourException {
		if (in.getContourType().equals(outType)) {
			return in;
		}

		PitchContour temp = new PitchContour(outType);
		for (Entry<Double, Double> s : in.getContour().entrySet()) {
			if (s.getValue() == SILENCE) {
				temp.appendSilence(s.getKey());
			} else {
				switch (in.getContourType()) {
				case FREQUENCY:
					temp.appendFrequency(s.getKey(), s.getValue());
					break;
				case MIDI:
					temp.appendMidi(s.getKey(), (int) Math.round(s.getValue()));
					break;
				}
			}
		}

		return temp;
	}

	public enum ContourType {
		FREQUENCY, MIDI
	}

	public static class PitchContourException extends Exception {
		private static final long serialVersionUID = -5340100033686461768L;

		public PitchContourException(String message) {
			super(message);
		}

		public PitchContourException(Throwable cause) {
			super(cause);
		}

		public PitchContourException(String message, Throwable cause) {
			super(message, cause);
		}
	}

}
