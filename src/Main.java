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

import data.bossData;
import data.combatData;
import data.playerData;
import data.skillData;
import parse.Parse;
import parse.Statistics;

public class Main {

	// Fields
	private static boolean quitting;
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
			if (args.length > 0) {
				String output = "<OUTPUT>\n";
				for (char c : args[1].toCharArray()) {
					output += parsing(Character.getNumericValue(c), new File(args[0]));
				}
				System.out.println(output + "</OUTPUT>");
				scan.nextLine();
				return;
			}
			File dir = new File("./logs");
			dir.mkdir();
			new File("./graphs").mkdirs();
			new File("./tables").mkdirs();
			List<File> logs = new ArrayList<File>();
			recursiveFileSearch("./logs", logs);
			if (logs.size() == 0) {
				System.out.println("/logs/ has no .evtc files.\nNothing to parse.\nPress Enter to exit.\n");
				scan.nextLine();
				return;
			} else {
				// Menu loop
				quitting = false;
				while (!quitting) {
					// Menu display
					System.out.println("EVTC Log Parser\n" + "---------------\n" + "1. Final DPS\n" + "2. Phase DPS\n"
							+ "3. Damage Distribution\n" + "4. Graph Total Damage\n" + "5. Misc. Combat Stats\n"
							+ "6. Final Boons\n" + "7. Phase Boons\n" + "8. Text Dump Tables\n" + "9. Quit\n");
					System.out.println("Choose an option below: ");

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
							System.out.println("Parsing " + log.toString() + "...");
							String output = parsing(choice, log);
							if (output.isEmpty()) {
								break;
							} else {
								System.out.println(output);
							}
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
			// System.out.println("Parsing " + base + "...");
			Parse parser = new Parse(log);
			Statistics stats = null;
			try {
				bossData b_data = parser.get_boss_data();
				List<playerData> p_data = parser.get_player_data();
				List<skillData> s_data = parser.get_skill_data();
				List<combatData> c_data = parser.get_combat_data();
				parser.fill_missing_data(b_data, p_data, s_data, c_data);
				stats = new Statistics(b_data, p_data, s_data, c_data);
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
						return stats.get_top_k_combat();
					} else if (choice == 4) {
						stats.get_total_damage_graph(base);
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
					File text_dump = new File(System.getProperty("user.dir") + "/tables/" + base + ".txt");
					writeToFile(stats.get_final_dps() + stats.get_phase_dps() + stats.get_top_k_combat()
							+ stats.get_combat_stats() + stats.get_final_boons(boon_list)
							+ stats.get_phase_boons(boon_list), text_dump);
					stats.get_total_damage_graph(base);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Not a valid option. Try again.\n");
			return "";
		}
		return "";
	}

	// Public Methods
	public static boolean is_in(int i, int[] array) {
		return IntStream.of(array).anyMatch(x -> x == i);
	}

	// Private Methods
	private static void writeToFile(String string, File file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new StringReader(string));
				PrintWriter writer = new PrintWriter(file, "UTF-8");) {
			reader.lines().forEach(line -> writer.println(line));
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
