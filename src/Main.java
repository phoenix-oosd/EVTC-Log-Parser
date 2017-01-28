import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import data.Parse;
import data.bossData;
import data.combatData;
import data.playerData;
import data.skillData;
import statistics.Statistics;

public class Main {

	// Fields
	private static boolean quitting = false;
	private static boolean displaying_version = true;
	private static final int[] damage_choices = new int[] { 1, 2, 3, 4, 5 };
	private static final int[] boon_choices = new int[] { 6, 7 };
	private static final int[] all_choices = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
	private static final List<String> boon_list = Arrays.asList(new String[] { "Might", "Quickness", "Fury",
			"Protection", "Alacrity", "Spotter", "Spirit of Frost", "Glyph of Empowerment", "Grace of the Land",
			"Empower Allies", "Banner of Strength", "Banner of Discipline" });

	// Main
	public static void main(String[] args) {

		// Start scanner
		Scanner scan = null;
		try {
			scan = new Scanner(System.in);
			// Files
			if (args.length == 2) {
				displaying_version = false;
				String output = "<START>";
				char[] choices = args[1].toCharArray();
				char[] possible = new char[] { '1', '2', '3', '5', '6', '7' };
				for (char c : choices) {
					if (is_in(c, possible)) {
						output += "\n" + parsing(Character.getNumericValue(c), new File(args[0]));
					}
				}
				System.out.println(output + "<END>");
				scan.nextLine();
				return;
			}
			File dir = new File("./logs");
			dir.mkdir();
			new File("./graphs").mkdirs();
			new File("./tables").mkdirs();
			List<File> logs = new ArrayList<File>();
			recursiveFileSearch("./logs", logs);
			if (logs.isEmpty()) {
				System.out.println("/logs/ has no .evtc files.\nNothing to parse.\nPress Enter to exit.\n");
				scan.nextLine();
				return;
			} else {
				// Menu loop
				quitting = false;
				while (!quitting) {
					// Menu display
					System.out.println("_______________\n\nEVTC Log Parser\n" + "_______________\n\n" + "1. Final DPS\n"
							+ "2. Phase DPS\n" + "3. Damage Distribution\n" + "4. Graph Total Damage\n"
							+ "5. Miscellaneous Combat Statistics\n" + "6. Final Boons\n" + "7. Phase Boons\n"
							+ "8. Text Dump Tables\n" + "9. Quit\n_______________\n");
					System.out.println("Enter an option below: ");

					// Choose an option
					int choice = -1;
					try {
						choice = scan.nextInt();
					} catch (InputMismatchException e) {
					}
					scan.nextLine();

					// Parse ".evtc" files
					if (choice != 9) {
						for (File log : logs) {
							System.out.println("\nInput file:\t" + log.getName());
							String output = parsing(choice, log);
							System.out.println(output);
						}
					} else {
						quitting = true;
					}
				}
			}
		}
		// Close scanner
		finally {
			if (scan != null) {
				scan.close();
			}
		}
		return;
	}

	// Handle user choice
	private static String parsing(int choice, File log) {
		if (is_in(choice, all_choices)) {
			// Parse the log
			String base = log.getName().split("\\.(?=[^\\.]+$)")[0];
			Parse parser = null;
			Statistics stats = null;
			bossData b_data = null;
			try {
				parser = new Parse(log);
				b_data = parser.get_boss_data();
				List<playerData> p_data = parser.get_player_data();
				List<skillData> s_data = parser.get_skill_data();
				List<combatData> c_data = parser.get_combat_data();
				parser.fill_missing_data(b_data, p_data, s_data, c_data);
				stats = new Statistics(b_data, p_data, s_data, c_data);
				if (displaying_version) {
					System.out.println("Log version:\t" + b_data.getVersion());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// A single choice
			if (choice != 8) {
				// Damage Related
				if (is_in(choice, damage_choices)) {
					stats.get_damage_logs();
					if (choice == 1) {
						return stats.get_final_dps();
					} else if (choice == 2) {
						return stats.get_phase_dps();
					} else if (choice == 3) {
						return stats.get_damage_distribution();
					} else if (choice == 4) {
						System.out.println("Output file:\t" + stats.get_total_damage_graph(base));
					} else if (choice == 5) {
						return stats.get_combat_stats();
					}
				}
				// Boon Related
				else if (is_in(choice, boon_choices)) {
					stats.get_boon_logs(boon_list);
					if (choice == 6) {
						return stats.get_final_boons(boon_list);
					} else if (choice == 7) {
						stats.get_damage_logs();
						return stats.get_phase_boons(boon_list);
					}
				}
			}
			// All choices
			else {
				// Write to file
				stats.get_damage_logs();
				stats.get_boon_logs(boon_list);
				try {
					File text_dump = new File("./tables/" + base + "_" + b_data.getName() + ".txt");
					writeToFile(stats.get_final_dps() + "\n" + stats.get_phase_dps() + "\n" + stats.get_combat_stats()
							+ "\n" + stats.get_final_boons(boon_list) + "\n" + stats.get_phase_boons(boon_list) + "\n"
							+ stats.get_damage_distribution(), text_dump);
					System.out.println("Output file:\t" + text_dump.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "";
			}
		} else {
			System.out.println("Invalid option. Try again.\n");
			return "";
		}
		return "";
	}

	// Public Methods
	public static boolean is_in(int i, int[] array) {
		return IntStream.of(array).anyMatch(x -> x == i);
	}

	// Private Methods
	public static boolean is_in(char c, char[] array) {
		return new String(array).contains("" + c);
	}

	private static void writeToFile(String string, File file) throws IOException {
		try (BufferedReader geter = new BufferedReader(new StringReader(string));
				PrintWriter writer = new PrintWriter(file, "UTF-8");) {
			geter.lines().forEach(line -> writer.println(line));
			writer.close();
		}
	}

	private static void recursiveFileSearch(String dirName, List<File> files) {
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
