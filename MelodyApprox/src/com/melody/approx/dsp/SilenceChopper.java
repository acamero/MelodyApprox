package com.melody.approx.dsp;

import java.util.Map.Entry;
import java.io.BufferedReader;
import java.util.TreeMap;

import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour.PitchContourException;
import com.melody.approx.util.Log;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class SilenceChopper extends MelodiaReader {
		
	@Override
	public Melody getMelody(BufferedReader bufferedReader) throws MelodiaReaderException, PitchContourException, MelodyException {
		PitchContour fc = readMelodia(bufferedReader, false);
		return chop(fc);
	}

	private Melody chop(PitchContour contour) throws PitchContourException, MelodyException {
		Melody melody = new Melody();
		PitchContour temp = new PitchContour();
		TreeMap<Double, Double> ordered = new TreeMap<Double, Double>(contour.getContour());
		double offset = -1.0d;

		for (Entry<Double, Double> s : ordered.entrySet()) {
			if (s.getValue() != PitchContour.getSilence()) {
				if (offset == -1.0d) {
					offset = s.getKey();
				}
				temp.appendFrequency(s.getKey() - offset, s.getValue());
			} else if (!temp.getContour().isEmpty()) {
				melody.addPhrase(offset, temp);
				Log.info("New phrase added in offset="+offset);
				temp = new PitchContour();
				offset = -1.0d;
			}
		}
		
		if(!temp.getContour().isEmpty()) {
			melody.addPhrase(offset, temp);
			Log.info("New phrase added in offset="+offset);
		}

		return melody;
	}

}
