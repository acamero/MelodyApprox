package com.melody.approx.dsp;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.melody.approx.pitch.FrequencyContour;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.PitchContourException;

public class SilenceChopper extends MelodiaReader {

	@Override
	public Melody getMelody(String filePath)
			throws MelodiaReaderException, PitchContourException, MelodyException {
		FrequencyContour fc = readMelodia(filePath, false);
		return chop(fc);
	}

	private Melody chop(FrequencyContour contour) throws PitchContourException, MelodyException {
		Melody melody = new Melody();
		FrequencyContour temp = new FrequencyContour();
		TreeMap<Double, Double> ordered = new TreeMap<Double, Double>(contour.getContour());
		double offset = -1.0d;

		for (Entry<Double, Double> s : ordered.entrySet()) {
			if (s.getValue() != PitchContour.getSilence()) {
				if(offset==-1.0d) {
					offset = s.getKey();
				}
				temp.appendFrequency(s.getKey() - offset, s.getValue());
			} else if (!temp.getContour().isEmpty()) {
				melody.addPhrase(offset, temp);
				temp = new FrequencyContour();
				offset = -1.0d;
			}
			
		}

		return melody;
	}

}
