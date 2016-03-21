package test.melody.approx;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.melody.approx.bio.AlgorithmTest;
import test.melody.approx.bio.ChromosomeTest;
import test.melody.approx.bio.IndividualTest;
import test.melody.approx.bio.LegendreInitTest;
import test.melody.approx.bio.LegendreMutationTest;
import test.melody.approx.bio.MelodyProcessorTest;
import test.melody.approx.bio.PopulationTest;
import test.melody.approx.bio.ProblemLegendreTest;
import test.melody.approx.bio.ProblemPolyTriTest;
import test.melody.approx.bio.SinglePointCrossoverTest;
import test.melody.approx.dsp.MelodiaReaderTest;
import test.melody.approx.dsp.SilenceChopperTest;
import test.melody.approx.pitch.FrequencyContourTest;
import test.melody.approx.pitch.MidiContourTest;
import test.melody.approx.pitch.PitchContourTest;
import test.melody.approx.util.RandomGeneratorTest;
import test.melody.approx.util.UtilsTest;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ PitchContourTest.class, FrequencyContourTest.class, MidiContourTest.class, MelodiaReaderTest.class,
		SilenceChopperTest.class, RandomGeneratorTest.class, MainTest.class, UtilsTest.class, ChromosomeTest.class,
		IndividualTest.class, PopulationTest.class, ProblemLegendreTest.class, AlgorithmTest.class,
		LegendreInitTest.class, LegendreMutationTest.class, SinglePointCrossoverTest.class, MelodyProcessorTest.class,
		ProblemPolyTriTest.class})
public class AllTests {

}
