package com.melody.approx.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Andrés Camero Unzueta
 *
 */
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
		Log.info("Object deserialized from "+filePath);

		return obj;
	}
	
	public static Map<Double, Integer> sortByComparator(Map<Double, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<Double, Integer>> list = 
			new LinkedList<Map.Entry<Double, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Double, Integer>>() {
			public int compare(Map.Entry<Double, Integer> o1,
                                           Map.Entry<Double, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<Double, Integer> sortedMap = new LinkedHashMap<Double, Integer>();
		for (Iterator<Map.Entry<Double, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Double, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
