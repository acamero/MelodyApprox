package test.melody.approx;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.melody.approx.dsp.MelodiaReaderTest;
import test.melody.approx.dsp.SilenceChopperTest;
import test.melody.approx.pitch.FrequencyContourTest;
import test.melody.approx.pitch.MidiContourTest;
import test.melody.approx.pitch.PitchContourTest;
import test.melody.approx.util.RandomGeneratorTest;

@RunWith(Suite.class)
@SuiteClasses({ PitchContourTest.class, FrequencyContourTest.class, MidiContourTest.class, MelodiaReaderTest.class,
		SilenceChopperTest.class, RandomGeneratorTest.class, MainTest.class })
public class AllTests {

}
