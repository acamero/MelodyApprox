package com.melody.approx.pitch;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.pitch.PitchContour.PitchContourException;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class Melody implements Serializable {
	private static final long serialVersionUID = 8417284023258188547L;
	private Map<Double, PitchContour> phrases = new HashMap<Double, PitchContour>();
	private ContourType contourType = null;

	public void addPhrase(double offset, PitchContour contour) throws MelodyException {
		if (offset >= 0.0d && contour != null) {
			// set initial contour type of the melody
			if (contourType == null) {
				contourType = contour.getContourType();
			}

			if (contourType.equals(contour.getContourType())) {
				phrases.put(offset, contour);
			} else {
				throw new MelodyException("Could not add phrase because it has different contour type");
			}
		} else {
			throw new MelodyException("Could not add phrase, check offset or pitch contour");
		}
	}

	public Map<Double, PitchContour> getPhrases() {
		return phrases;
	}

	public ContourType getContourType() {
		return contourType;
	}
	
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("Contour type: ");
		sw.write(contourType.toString());
		sw.write("\n");
		for(Entry<Double, PitchContour> e : phrases.entrySet()) {
			sw.write("Offset: ");
			sw.write(e.getKey().toString());
			sw.write("\n");
			sw.write(e.getValue().toString());			
		}
		return sw.toString();
	}

	public static Melody transform(Melody in, ContourType outType) throws PitchContourException, MelodyException {
		if (in.getPhrases().isEmpty() || in.getContourType().equals(outType)) {
			return in;
		}
		Melody output = new Melody();
		for (Entry<Double, PitchContour> s : in.getPhrases().entrySet()) {
			output.addPhrase(s.getKey(), PitchContour.transform(s.getValue(), outType));
		}
		return output;
	}

	public class MelodyException extends Exception {
		private static final long serialVersionUID = 5650938595069795678L;

		public MelodyException(String message) {
			super(message);
		}

		public MelodyException(Throwable cause) {
			super(cause);
		}

		public MelodyException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
