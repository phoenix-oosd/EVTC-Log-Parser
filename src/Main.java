import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import enums.MenuChoice;
import statistics.Parse;
import statistics.Statistics;
import utility.Utility;

public class Main {

	// Fields
	private static boolean willQuit = false;
	private static boolean willDisplayVersions = true;
	private static boolean willHidePlayers = false;
	private static Map<String, String> arguments = new HashMap<>();;

	// Main
	public static void main(String[] args) {

		// Scanner
		Scanner scan = null;
		try {
			scan = new Scanner(System.in);

			// Read arguments
			for (String arg : args) {
				if (arg.contains("=")) {
					arguments.put(arg.substring(0, arg.indexOf('=')), arg.substring(arg.indexOf('=') + 1));
				}
			}
			String is_anon = arguments.get("is_anon");
			String file_path = arguments.get("file_path");
			String options = arguments.get("options");

			// Data anonymization
			if (is_anon != null) {
				willHidePlayers = Utility.getBool(Integer.valueOf(is_anon));
			}

			// File Association
			if (file_path != null && !file_path.isEmpty() && options != null) {
				willDisplayVersions = false;
				int[] choices = options.chars().map(x -> x - '0').toArray();

				StringBuilder output = new StringBuilder("<START>");
				for (int i : choices) {
					MenuChoice c = MenuChoice.getEnum(i);
					if (c != null && c.canBeAssociated()) {
						output.append(System.lineSeparator() + parsing(c, new File(file_path)));
					}
				}
				output.append("<END>");
				System.out.println(output.toString());
				scan.nextLine();

				return;
			}

			// Menu
			else {

				// Create required directories
				new File("./logs").mkdir();
				new File("./graphs").mkdirs();
				new File("./tables").mkdirs();

				// Obtain list of .evtc files in /logs/
				List<File> logs = new ArrayList<File>();
				Utility.recursiveFileSearch("./logs", logs);

				// /logs/ must be non-empty
				if (logs.isEmpty()) {
					System.out.println("/logs/ contains no .evtc files.");
					System.out.println("Press Enter to exit.");
					scan.nextLine();
					return;
				}

				// Display menu
				else {
					while (!willQuit) {
						System.out.println("_______________" + System.lineSeparator());
						System.out.println("EVTC Log Parser");
						System.out.println("_______________" + System.lineSeparator());
						System.out.println("0. Dump EVTC");
						System.out.println("1. Final DPS");
						System.out.println("2. Phase DPS");
						System.out.println("3. Damage Distribution");
						System.out.println("4. Graph Total Damage");
						System.out.println("5. Misc. Combat Stats");
						System.out.println("6. Final Boons");
						System.out.println("7. Phase Boons");
						System.out.println("8. Dump Tables");
						System.out.println("9. Quit");
						System.out.println("_______________" + System.lineSeparator());
						System.out.println("Enter an option by number below:");

						// Read user input
						MenuChoice choice = null;
						try {
							choice = MenuChoice.getEnum(scan.nextInt());

						} catch (InputMismatchException e) {
							e.printStackTrace();
						}
						scan.nextLine();

						// Invalid choice
						if (choice == null) {
							System.out.println("Invalid option. Try again." + System.lineSeparator());
						}

						// Quitting
						else if (choice.equals(MenuChoice.QUIT)) {
							willQuit = true;
						}

						// Valid choice
						else {

							// Apply option to all .evtc files in /logs/
							for (File log : logs) {
								System.out.println(System.lineSeparator() + "Input file:\t" + log.getName());
								String output = parsing(choice, log);
								System.out.println(output);
							}
						}
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

	private static String parsing(MenuChoice choice, File log) {

		// Parse the log
		String base = log.getName().split("\\.(?=[^\\.]+$)")[0];
		Parse parsed = null;
		Statistics stats = null;
		try {
			parsed = new Parse(log, willHidePlayers);
			if (willDisplayVersions) {
				System.out.println("Log version:\t" + parsed.getBossData().getBuildVersion());
			}
			stats = new Statistics(parsed);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Damage related options
		if (choice.getType().equals("damage")) {
			// stats.get_damage_logs();
			if (choice.equals(MenuChoice.FINAL_DPS)) {
				return stats.get_final_dps();
			}
			//
			// else if (choice.equals(MenuChoice.PHASE_DPS)) {
			// return stats.get_phase_dps();
			// }
			//
			else if (choice.equals(MenuChoice.DMG_DIST)) {
				return stats.get_damage_distribution();
			} else if (choice.equals(MenuChoice.G_TOTAL_DMG)) {
				return "Output file:\t" + stats.get_total_damage_graph(base);
			}
			//
			// else if (choice.equals(MenuChoice.MISC_STATS)) {
			// return stats.get_combat_stats();
			// }
		}

		// Boon related options
		else if (choice.getType().equals("boons")) {
			// stats.get_boon_logs();
			// if (choice.equals(MenuChoice.FINAL_BOONS)) {
			// return stats.get_final_boons();
			// } else if (choice.equals(MenuChoice.PHASE_BOONS)) {
			// stats.get_damage_logs();
			// return stats.get_phase_boons();
			// }

		}

		// Text Dumps
		else if (choice.getType().equals("text")) {
			if (choice.equals(MenuChoice.DUMP_EVTC)) {
				File evtc_dump = new File("./tables/" + base + "_" + parsed.getBossData().getName() + "_evtc-dump.txt");
				try {
					Utility.writeToFile(parsed.toString(), evtc_dump);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "Output file:\t" + evtc_dump.getName();
			}
			// else if (choice.equals(MenuChoice.DUMP_TABLES)) {
			// stats.get_damage_logs();
			// stats.get_boon_logs();
			// File text_dump = new File("./tables/" + base + "_" +
			// parsed.getB().getName() + "_all-tables.txt");
			// try {
			// Utility.writeToFile(stats.get_final_dps() +
			// System.lineSeparator() + stats.get_phase_dps()
			// + System.lineSeparator() + stats.get_combat_stats() +
			// System.lineSeparator()
			// + stats.get_final_boons() + System.lineSeparator() +
			// stats.get_phase_boons()
			// + System.lineSeparator() + stats.get_damage_distribution(),
			// text_dump);
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// return "Output file:\t" + text_dump.getName();
			// }
		}
		return "";
	}
}
