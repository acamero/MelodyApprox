package com.melody.approx.dsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.pitch.PitchContour.PitchContourException;

public abstract class MelodiaReader {
	private static final Logger LOGGER = Logger.getLogger(MelodiaReader.class.getName());
	private String separator = ",";

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public abstract Melody getMelody(String filePath)
			throws MelodiaReaderException, PitchContourException, MelodyException;

	protected PitchContour readMelodia(String filePath, boolean convertNegative)
			throws MelodiaReaderException, PitchContourException {
		String line;
		PitchContour contour = new PitchContour(ContourType.FREQUENCY);
		FileReader fileReader;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(filePath);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(separator);
				double time = Double.parseDouble(values[0]);
				double freq = Double.parseDouble(values[1]);
				if (convertNegative) {
					freq = Math.abs(freq);
				}

				if (freq > 0.0d) {
					contour.appendFrequency(time, freq);
					LOGGER.info("Read from 'melodia' (t=" + time + ", f=" + freq + ")");
				} else {
					contour.appendSilence(time);
					LOGGER.info("Read from 'melodia' (t=" + time + ", silence)");
				}

			}
		} catch (IOException e) {
			throw new MelodiaReaderException("Unable to process 'melodia' (" + e.getMessage() + ")", e.getCause());
		} catch (NumberFormatException e) {
			throw new MelodiaReaderException("Invalid format", e.getCause());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					throw new MelodiaReaderException("Unable to close file '" + filePath + "'");
				}
			}
		}

		return contour;
	}

	public class MelodiaReaderException extends Exception {
		private static final long serialVersionUID = -5340100033686461768L;

		public MelodiaReaderException(String message) {
			super(message);
		}

		public MelodiaReaderException(Throwable cause) {
			super(cause);
		}

		public MelodiaReaderException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
