package com.melody.approx.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

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
	
	public static void exceptionToLog(Exception e) {
		Log.error(e.getMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		Log.error(sw.toString());
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
}
