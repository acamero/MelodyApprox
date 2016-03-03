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
		Log.setLogLevel(LogLevel.INFO);
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void changeLogLevel() {
		Log.setLogLevel(LogLevel.SILENT);
		String[] args = new String[1];
		args[0] = "--log-level=INFO";
		Main.main(args);
		assertEquals(Log.getLogLevel(),LogLevel.INFO);
	}
	
	@Test
	public void help() {
		String[] args = new String[1];
		args[0] = "-h";
		Main.main(args);
	}
}