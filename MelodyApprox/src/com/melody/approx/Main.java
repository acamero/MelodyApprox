package com.melody.approx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.melody.approx.dsp.MelodiaReader;
import com.melody.approx.dsp.MelodiaReader.MelodiaReaderException;
import com.melody.approx.dsp.SilenceChopper;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.pitch.PitchContour.PitchContourException;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class Main {

	private static final String PROGRAM_NAME = "melody";

	private static enum ParseMelodia {
		DEFAULT, SILENCE_CHOPPER
	}

	private static Melody parseMelodia(ParseMelodia funcOpt, String filePath, boolean isMidi)
			throws MelodiaReaderException, PitchContourException, MelodyException {
		MelodiaReader reader = null;
		switch (funcOpt) {
		case DEFAULT: // proceed with method SILENCE_CHOPPER
			reader = new SilenceChopper();
			break;
		case SILENCE_CHOPPER:
			reader = new SilenceChopper();
			break;
		}

		Melody melody = reader.getMelody(filePath);
		if (isMidi) {
			melody = Melody.transform(melody, ContourType.MIDI);
		}
		return melody;
	}

	public static void serialize(Object obj, String filePath) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
			Log.info("Serialized data is saved in " + filePath);
		} catch (IOException e) {
			exceptionToLog(e);
		}
	}
	
	public static String getFileName(String originalFilePath, String newExtension) {
		String filePath;
		if (originalFilePath.contains("/")) {
			filePath = originalFilePath.substring(originalFilePath.lastIndexOf("/") + 1);
		} else {
			filePath = originalFilePath;
		}

		if (filePath.contains(".")) {
			filePath = filePath.substring(0, filePath.lastIndexOf(".")+1) + newExtension;
		} else {
			filePath += "."+newExtension;
		}
		return filePath;
	}

	public static Object deserialize(String filePath) throws IOException, ClassNotFoundException {
		Object obj = null;

		FileInputStream fileIn = new FileInputStream(filePath);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		obj = in.readObject();
		in.close();
		fileIn.close();

		return obj;
	}

	private static void exceptionToLog(Exception e) {
		Log.error(e.getMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		Log.error(sw.toString());
	}

	private static Options setOptions() {
		// create the Options
		Options options = new Options();
		options.addOption(Option.builder().longOpt("log-level").hasArg()
				.desc("set the logging level (" + Arrays.toString(Log.LogLevel.values()) + ")").build());
		options.addOption(Option.builder().longOpt("file-name").hasArg().desc("set the file to be process").build());
		options.addOption("h", "help", false, "print help information");
		options.addOption(Option.builder().longOpt("parse-melodia").hasArg().desc(
				"parse 'melodia' plugin file using specified method (" + Arrays.toString(ParseMelodia.values()) + ")")
				.build());
		options.addOption(Option.builder().longOpt("pitch-midi")
				.desc("process the melody using pitch encoded in MIDI format").build());
		options.addOption(
				Option.builder().longOpt("out-console").desc("output results are printed to the console").build());
		options.addOption(Option.builder().longOpt("out-dir").hasArg().desc("output directory").build());
		
		return options;
	}

	public static void main(String[] args) {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();
		Options options = setOptions();
		String filePath = null;
		ParseMelodia parseMelodia = null;
		String outDirectory = "";
		boolean isMidi = false;
		boolean isConsole = false;

		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(PROGRAM_NAME, options, true);
				// stop processing and print help
				return;
			}

			if (line.hasOption("log-level")) {
				Log.setLogLevel(LogLevel.getLogLevel(line.getOptionValue("log-level")));
				Log.info("Set log level to " + line.getOptionValue("log-level"));
			}

			if (line.hasOption("pitch-midi")) {
				isMidi = true;
				Log.info("Set MIDI as the preferred encode for processing");
			}

			if (line.hasOption("out-console")) {
				isConsole = true;
				Log.info("Set output to console");
			}
			
			if (line.hasOption("out-dir")) {
				outDirectory = line.getOptionValue("out-dir");
				if(!outDirectory.endsWith("/")) {
					outDirectory = outDirectory + "/";
				}
				Log.info("Set output directory to " + outDirectory);
			}

			if (line.hasOption("parse-melodia")) {
				String tmp = line.getOptionValue("parse-melodia");
				for (ParseMelodia f : ParseMelodia.values()) {
					if (f.toString().compareToIgnoreCase(tmp) == 0) {
						parseMelodia = f;
						Log.info("Parse melodia method selection: " + parseMelodia.toString());
					}
				}
				if (parseMelodia == null) {
					Log.warning("Invalid function name, proceed with default method");
					parseMelodia = ParseMelodia.DEFAULT;
				}
			}

			if (line.hasOption("file-name")) {
				filePath = line.getOptionValue("file-name");
				if (filePath == null) {
					Log.error("Invalid file name");
					// stop processing
					return;
				}
			} else {
				Log.error("An input file must be provided");
				// stop processing
				return;
			}

		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
		}

		Melody melody = null;
		if (parseMelodia != null) {
			try {
				melody = parseMelodia(parseMelodia, filePath, isMidi);
			} catch (MelodiaReaderException | PitchContourException | MelodyException e) {
				exceptionToLog(e);
				// quit program
				return;
			}

			if (isConsole) {
				System.out.println(melody.toString());
			} else {
				File file = new File(outDirectory);
				if(!file.exists()) {
					if(file.mkdirs()) {
						Log.info("Directory "+outDirectory+" succesfully created");
					} else {
						Log.error("Unable to create directory "+outDirectory);
						return;
					}
				}
				String serFilePath = outDirectory  + getFileName(filePath, "ser");
				serialize(melody, serFilePath);
			}
		}

	}

}
