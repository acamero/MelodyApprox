package com.melody.approx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.melody.approx.bio.Algorithm.AlgorithmException;
import com.melody.approx.bio.Chromosome.ChromosomeException;
import com.melody.approx.bio.CrossoverInterface.CrossoverException;
import com.melody.approx.bio.IndividualInitInterface.IndividualInitInterfaceException;
import com.melody.approx.bio.MelodyProcessor;
import com.melody.approx.bio.MelodyProcessor.AlgorithmType;
import com.melody.approx.bio.MelodyProcessor.MelodyProcessorException;
import com.melody.approx.bio.MutationInterface.MutationException;
import com.melody.approx.bio.Population.PopulationException;
import com.melody.approx.bio.Problem.ProblemException;
import com.melody.approx.dsp.MelodiaReader;
import com.melody.approx.dsp.MelodiaReader.MelodiaReaderException;
import com.melody.approx.dsp.SilenceChopper;
import com.melody.approx.pitch.Melody;
import com.melody.approx.pitch.Melody.MelodyException;
import com.melody.approx.pitch.PitchContour.ContourType;
import com.melody.approx.pitch.PitchContour.PitchContourException;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;
import com.melody.approx.util.RandomGenerator;
import com.melody.approx.util.Utils;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class Main {

	// name of the program (to show in help menu)
	private static final String PROGRAM_NAME = "java -jar melody.jar";
	// constant to be used as a non-assigned value
	private static final double NON_ASSIGNED_DOUBLE = -1.0d;
	private static final int NON_ASSIGNED_INT = -1;
	
	private static enum ParseMelodia {
		DEFAULT, SILENCE_CHOPPER
	}

	private static Melody parseMelodia(ParseMelodia funcOpt, String filePath, boolean isMidi)
			throws MelodiaReaderException, PitchContourException, MelodyException, IOException {
		MelodiaReader reader = null;
		switch (funcOpt) {
		case DEFAULT: // proceed with method SILENCE_CHOPPER
			reader = new SilenceChopper();
			break;
		case SILENCE_CHOPPER:
			reader = new SilenceChopper();
			break;
		}

		Melody melody = null;
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		melody = reader.getMelody(br);
		if (isMidi) {
			melody = Melody.transform(melody, ContourType.MIDI);
		}
		br.close();
		return melody;
	}

	public static String getFileName(String originalFilePath, String newExtension) {
		String filePath;
		if (originalFilePath.contains("/")) {
			filePath = originalFilePath.substring(originalFilePath.lastIndexOf("/") + 1);
		} else {
			filePath = originalFilePath;
		}

		if (filePath.contains(".")) {
			filePath = filePath.substring(0, filePath.lastIndexOf(".") + 1) + newExtension;
		} else {
			filePath += "." + newExtension;
		}
		return filePath;
	}

	private static Options setOptions() {
		// create the Options
		Options options = new Options();
		// set the logging level
		options.addOption(Option.builder().longOpt("log-level").hasArg()
				.desc("set the logging level (" + Arrays.toString(Log.LogLevel.values()) + ")").build());
		// input file
		options.addOption(Option.builder().longOpt("file-name").hasArg().desc("set the file to be process").build());
		// help menu
		options.addOption("h", "help", false, "print help information");
		// parse a MELODIA file (the file contains frequencies) using the
		// specified melody chopper
		options.addOption(Option.builder().longOpt("parse-melodia").hasArg().desc(
				"parse 'melodia' plugin file using specified method (" + Arrays.toString(ParseMelodia.values()) + ")")
				.build());
		// set MIDI as the preferred encode
		options.addOption(Option.builder().longOpt("pitch-midi")
				.desc("process the melody using pitch encoded in MIDI format").build());
		// write output to console
		options.addOption(
				Option.builder().longOpt("out-console").desc("output results are printed to the console").build());
		// write output to a file in the specified directory
		options.addOption(Option.builder().longOpt("out-dir").hasArg().desc("output directory").build());
		// process a melody using the specified algorithm
		options.addOption(Option.builder().longOpt("algorithm").hasArg()
				.desc("process a melody using bio inspired algorithm (" + Arrays.toString(AlgorithmType.values()) + ")")
				.build());
		// select the seed to be used in the pseudo-random number generation
		options.addOption(
				Option.builder().longOpt("seed").hasArg().desc("set the number of the seed to be used").build());
		// crossover probability
		options.addOption(
				Option.builder().longOpt("crossover-prob").hasArg().desc("set the crossover probability").build());
		// mutation probability
		options.addOption(
				Option.builder().longOpt("mutation-prob").hasArg().desc("set the mutation probability").build());
		// maximum number of evaluations
		options.addOption(
				Option.builder().longOpt("max-evals").hasArg().desc("set the maximum number of evaluations").build());
		// population size
		options.addOption(
				Option.builder().longOpt("pop-size").hasArg().desc("set the size of the population").build());
		// number of offsprings
		options.addOption(
				Option.builder().longOpt("offsprings").hasArg().desc("set the number of offsprings per generation").build());




		return options;
	}

	public static void main(String[] args) {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();
		Options options = setOptions();
		String filePath = null;
		ParseMelodia parseMelodia = null;
		AlgorithmType algorithm = null;
		String outDirectory = "./";
		boolean isMidi = false;
		boolean isConsole = false;
		double crossoverProb = NON_ASSIGNED_DOUBLE;
		double mutationProb = NON_ASSIGNED_DOUBLE;
		int popSize = NON_ASSIGNED_INT;
		int offsprings = NON_ASSIGNED_INT;
		int maxEvals = NON_ASSIGNED_INT;

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
				if (!outDirectory.endsWith("/")) {
					outDirectory = outDirectory + "/";
				}
				Log.info("Set output directory to " + outDirectory);
			}

			if (line.hasOption("algorithm")) {
				String tmp = line.getOptionValue("algorithm");
				for (AlgorithmType f : AlgorithmType.values()) {
					if (f.toString().compareToIgnoreCase(tmp) == 0) {
						algorithm = f;
						Log.info("Process melody algorithm selection: " + algorithm.toString());
					}
				}
				if (algorithm == null) {
					Log.error("Invalid algorithm named");
					return;
				}
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
				Log.info("File name=" + filePath);
			} else {
				Log.error("An input file must be provided");
				// stop processing
				return;
			}

			if (line.hasOption("seed")) {
				String seedLine = line.getOptionValue("seed");
				Log.info("Seed number argument parsed (seed '" + seedLine + "' selected)");
				int seed = Integer.valueOf(seedLine);
				RandomGenerator.setSeed(seed);
			}
			
			if (line.hasOption("crossover-prob")) {
				crossoverProb = Double.valueOf(line.getOptionValue("crossover-prob"));
				Log.info("Crossover probability set to "+ crossoverProb);				
			}
			
			if (line.hasOption("mutation-prob")) {
				mutationProb = Double.valueOf(line.getOptionValue("mutation-prob"));
				Log.info("Mutation probability set to "+mutationProb);				
			}
			
			if (line.hasOption("pop-size")) {
				popSize = Integer.valueOf(line.getOptionValue("pop-size"));
				Log.info("Population size set to "+popSize);				
			}
			
			if (line.hasOption("offsprings")) {
				offsprings = Integer.valueOf(line.getOptionValue("offsprings"));
				Log.info("Mutation probability set to "+offsprings);				
			}
			
			if (line.hasOption("max-evals")) {
				maxEvals = Integer.valueOf(line.getOptionValue("max-evals"));
				Log.info("Maximum number of evaluations set to "+maxEvals);				
			}
			
		} catch (ParseException e) {
			Log.error("Unexpected exception:" + e.getMessage());
			Utils.exceptionToLog(e);
			return;
		}

		Melody melody = null;
		// parse MELODIA file
		if (parseMelodia != null) {
			try {
				melody = parseMelodia(parseMelodia, filePath, isMidi);
			} catch (MelodiaReaderException | PitchContourException | MelodyException e) {
				Utils.exceptionToLog(e);
				// quit program
				return;
			} catch (IOException e) {
				Utils.exceptionToLog(e);
				// quit program
				return;
			}

			if (isConsole) {
				System.out.println(melody.toString());
			} else {
				File file = new File(outDirectory);
				if (!file.exists()) {
					if (file.mkdirs()) {
						Log.info("Directory " + outDirectory + " succesfully created");
					} else {
						Log.error("Unable to create directory " + outDirectory);
						return;
					}
				}
				String serFilePath = outDirectory + getFileName(filePath, "ser");
				Utils.serialize(melody, serFilePath);
			}
		}

		// process melody using bio-inspired algorithm
		if (algorithm != null) {
			if (melody == null) {
				// if this is the case, we have to retrieve the melody from the
				// specified file
				try {
					melody = (Melody) Utils.deserialize(filePath);
					if (isMidi && !melody.getContourType().equals(ContourType.MIDI)) {
						melody = Melody.transform(melody, ContourType.MIDI);
						Log.info("Melody transformed into MIDI encode");
					}
				} catch (ClassNotFoundException | IOException e) {
					Log.error("Unable to deserialize the melody from the given file");
					Utils.exceptionToLog(e);
					return;
				} catch (PitchContourException | MelodyException e) {
					Log.error("Unable to continue processing");
					Utils.exceptionToLog(e);
					return;
				}
			} // otherwise use the melody already parsed

			try {
				MelodyProcessor processor = new MelodyProcessor(algorithm, melody);
				// set the parameters of this run (if applies)
				if(	crossoverProb != NON_ASSIGNED_DOUBLE ){
					processor.setCrossoverProb(crossoverProb);
					Log.debug("Crossover probability assigned");
				}
				if( mutationProb != NON_ASSIGNED_DOUBLE) {
					processor.setMutationProb(mutationProb);
					Log.debug("Mutation probability assigned");
				}
				if( popSize != NON_ASSIGNED_INT){
					processor.setPopSize(popSize);
					Log.debug("Population size assigned");
				}
				if( offsprings != NON_ASSIGNED_INT){
					processor.setOffspringSize(offsprings);
					Log.debug("Offsprings size assigned");
				}
				if( maxEvals != NON_ASSIGNED_INT) {
					processor.setMaxEvaluations(maxEvals);
					Log.debug("Maximum evaluations assigned");
				}
				
				String prepend;
				if (filePath.contains("/")) {
					prepend = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()) + ";";
				} else {
					prepend = filePath + ";";
				}
				BufferedWriter partialWriter = null;
				BufferedWriter finalWriter = null;

				if (isConsole) {
					partialWriter = new BufferedWriter(new OutputStreamWriter(System.out));
					finalWriter = new BufferedWriter(new OutputStreamWriter(System.out));
				} else {
					boolean init = false;
					// partial results to one file
					String outPath = outDirectory + getFileName(filePath, "detail.csv");
					File file = new File(outPath);
					if (!file.exists()) {
						file.createNewFile();
						init = true;
					}
					// turn append mode "on"
					FileWriter fw = new FileWriter(file, true);
					partialWriter = new BufferedWriter(fw);
					if (init) {
						// write a header to the file
						partialWriter.write("file;algorithm;offset;startTime;endTime;fitness;evaluations;points;solution\n");
						partialWriter.flush();
					}

					init = false;
					// and summarized results to another file
					outPath = outDirectory + getFileName(filePath, "summary.csv");
					file = new File(outPath);
					if (!file.exists()) {
						file.createNewFile();
						init = true;
					}
					// turn append mode "on"
					fw = new FileWriter(file, true);
					finalWriter = new BufferedWriter(fw);

					if (init) {
						// write a header to the file
						finalWriter.write("file;algorithm;startTime;endTime;fitness\n");
						finalWriter.flush();
					}
				}

				processor.processMelody(partialWriter, finalWriter, prepend);

				partialWriter.close();
				finalWriter.close();

			} catch (MelodyProcessorException e) {
				Log.error("Unable to setup MelodyProcessor");
				Utils.exceptionToLog(e);
				return;
			} catch (ProblemException | AlgorithmException | PopulationException | ChromosomeException
					| IndividualInitInterfaceException | CrossoverException | MutationException e) {
				Log.error("Unable to process melody");
				Utils.exceptionToLog(e);
				return;
			} catch (IOException e) {
				Log.error("Unable to open or write to the output buffer");
				Utils.exceptionToLog(e);
				return;
			}
		}

	}

}
