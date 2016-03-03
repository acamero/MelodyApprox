package com.melody.approx.pitch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class Melody implements Serializable {
	private static final long serialVersionUID = 8417284023258188547L;
	private Map<Double,PitchContour> phrases = new HashMap<Double,PitchContour>();
	
	public void addPhrase(double offset, PitchContour contour) throws MelodyException {
		if(offset>=0.0d && contour!=null) {
			phrases.put(offset, contour);
		} else {
			throw new MelodyException("Could not add phrase, check offset or pitch contour");
		}
	}
	
	public Map<Double,PitchContour> getPhrases() {
		return phrases;
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
