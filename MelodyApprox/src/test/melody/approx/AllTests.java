package test.melody.approx;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.melody.approx.bio.ChromosomeTest;
import test.melody.approx.bio.IndividualTest;
import test.melody.approx.bio.PopulationTest;
import test.melody.approx.dsp.MelodiaReaderTest;
import test.melody.approx.dsp.SilenceChopperTest;
import test.melody.approx.pitch.FrequencyContourTest;
import test.melody.approx.pitch.MidiContourTest;
import test.melody.approx.pitch.PitchContourTest;
import test.melody.approx.util.RandomGeneratorTest;
import test.melody.approx.util.UtilsTest;

@RunWith(Suite.class)
@SuiteClasses({ PitchContourTest.class, FrequencyContourTest.class, MidiContourTest.class, MelodiaReaderTest.class,
		SilenceChopperTest.class, RandomGeneratorTest.class, MainTest.class, UtilsTest.class,
		ChromosomeTest.class, IndividualTest.class, PopulationTest.class})
public class AllTests {

}
