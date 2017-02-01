package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public final class Utility {

	public static String get_String(byte[] bytes) {
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

	public static boolean get_bool(int i) {
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
}