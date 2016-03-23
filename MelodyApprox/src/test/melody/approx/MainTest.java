package test.melody.approx;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.melody.approx.Main;
import com.melody.approx.util.Log;
import com.melody.approx.util.Log.LogLevel;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
public class MainTest {
	
	
	@BeforeClass
	public static void oneTimeSetUp() {
	}

	@AfterClass
	public static void oneTimeTearDown() {		
	}

	@Before
	public void setUp() {
		Log.setLogLevel(LogLevel.INFO);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void changeLogLevel() {

		String[] args = new String[1];
		args[0] = "--log-level=SILENT";
		Main.main(args);
		assertEquals(Log.getLogLevel(), LogLevel.SILENT);
	}

	@Test
	public void help() {
		String[] args = new String[1];
		args[0] = "-h";
		Main.main(args);		
	}

	@Test
	public void changeFileNamePath() {
		assertEquals("name.new", Main.getFileName("/my/path/name.ext", "new"));
	}
	
	@Test
	public void changeFileNameNoPath() {
		assertEquals("name.new", Main.getFileName("name.ext", "new"));
	}
	
	@Test
	public void changeFileNamePathNoExt() {
		assertEquals("name.new", Main.getFileName("/my/path/name", "new"));
	}
	
	@Test
	public void changeFileNameNoPathNoExt() {
		assertEquals("name.new", Main.getFileName("name", "new"));
	}
	
	@Test 
	public void fullProcess() {
		Log.setLogLevel(LogLevel.INFO);
		String[] args = new String[6];
		args[0] = "--file-name=test/london-bridge-melodia.csv";
		args[1] = "--parse-melodia=SILENCE_CHOPPER";
		args[2] = "--out-dir=test";
		args[3] = "--algorithm=MEAN";
		args[4] = "--pitch-midi";
		args[5] = "--seed=2";
		
		Main.main(args);
		
		fail("Not implemented yet");
	}
	
}