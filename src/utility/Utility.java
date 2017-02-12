package utility;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Utility {

	public static String getString(byte[] bytes) {
		String string;
		try {
			string = new String(bytes, "UTF-8");
			int i = string.indexOf(0);
			return i == -1 ? string : string.substring(0, i);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "UNKNOWN";
	}

	public static boolean getBool(int i) {
		return (i != 0);
	}

	public static void writeToFile(String s, File f) throws IOException {
		try (BufferedReader getter = new BufferedReader(new StringReader(s));
				PrintWriter writer = new PrintWriter(f, "UTF-8");) {
			getter.lines().forEach(line -> writer.println(line));
			writer.close();
		}
	}

	public static void recursiveFileSearch(String dirName, List<File> files) {
		File dir = new File(dirName);
		File[] file_array = dir.listFiles();
		for (File f : file_array) {
			if (f.isFile() && f.toString().endsWith(".evtc")) {
				files.add(f);
			} else if (f.isDirectory()) {
				recursiveFileSearch(f.getAbsolutePath(), files);
			}
		}
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public static String centerString(String text, int len) {
		String output = String.format("%" + len + "s%s%" + len + "s", "", text, "");
		float start = (output.length() / 2) - (len / 2);
		return output.substring((int) start, (int) (start + len));
	}

	public static String fillWithChar(int len, char c) {
		if (len > 0) {
			char[] array = new char[len];
			Arrays.fill(array, c);
			return new String(array);
		}
		return "";
	}

	public static List<Point> mergeIntervals(List<Point> intervals) {

		if (intervals.size() == 1) {
			return intervals;
		}

		List<Point> merged = new ArrayList<Point>();
		int x = intervals.get(0).x;
		int y = intervals.get(0).y;

		for (int i = 1; i < intervals.size(); i++) {
			Point current = intervals.get(i);
			if (current.x <= y) {
				y = Math.max(current.y, y);
			} else {
				merged.add(new Point(x, y));
				x = current.x;
				y = current.y;
			}
		}

		merged.add(new Point(x, y));

		return merged;
	}

	public static String[] concatStringArray(String[] a, String[] b) {
		int i = a.length;
		int j = b.length;
		String[] output = new String[i + j];
		System.arraycopy(a, 0, output, 0, i);
		System.arraycopy(b, 0, output, i, j);
		return output;
	}

}