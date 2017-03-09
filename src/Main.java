import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private static boolean will_quit = false;
	private static boolean will_display_versions = true;
	private static Map<String, String> argument_map = new HashMap<>();
	private static String current_file;
	private static Parse parsed_file;
	private static Statistics statistics;

	// Main
	public static void main(String[] args) {

		// Scanner
		Scanner scan = null;
		try {
			scan = new Scanner(System.in);

			// Read arguments
			for (String arg : args) {
				if (arg.contains("=")) {
					argument_map.put(arg.substring(0, arg.indexOf('=')), arg.substring(arg.indexOf('=') + 1));
				}
			}
			String is_anon = argument_map.get("is_anon");
			String file_path = argument_map.get("file_path");
			String options = argument_map.get("options");

			// Handle arguments
			if (is_anon != null) {
				Statistics.willHidePlayers = Utility.toBool(Integer.valueOf(is_anon));
			}

			// File Association
			if (file_path != null && !file_path.isEmpty() && options != null) {
				will_display_versions = false;
				int[] choices = options.chars().map(x -> x - '0').toArray();

				StringBuilder output = new StringBuilder("<START>");
				for (int i : choices) {
					MenuChoice c = MenuChoice.getEnum(i);
					if (c != null && c.canBeAssociated()) {
						String result = parseFileByChoice(c, Paths.get(file_path));
						if (result.startsWith("Warning")) {
							System.out.println(
									"Warning:\tThis log is outdated. Make sure the log is created by arcdps build 20170214 onwards.");
							scan.nextLine();
						} else {
							output.append(System.lineSeparator() + result);
						}
					}
				}
				output.append(System.lineSeparator() + "<END>");
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
				List<Path> log_files = new ArrayList<Path>();
				Utility.recursiveFileSearch("./logs", log_files);

				// /logs/ must be non-empty
				if (log_files.isEmpty()) {
					System.out.println("/logs/ contains no .evtc files.");
					System.out.println("Press Enter to exit.");
					scan.nextLine();
					return;
				}

				// Display menu
				else {
					while (!will_quit) {
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
							will_quit = true;
						}

						// Valid choice
						else {
							// Apply option to all .evtc files in /logs/
							for (Path log : log_files) {
								System.out.println(
										System.lineSeparator() + "Input file:\t" + log.getFileName().toString());
								String output = parseFileByChoice(choice, log);
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

	private static String parseFileByChoice(MenuChoice choice, Path path) {

		// Parse the log
		if (current_file == null || !current_file.equals(path.getFileName().toString().split("\\.")[0])) {
			try {
				parsed_file = new Parse(path.toString());
				statistics = new Statistics(parsed_file);
				current_file = path.getFileName().toString().split("\\.")[0];
				if (will_display_versions) {
					System.out.println(
							"Log version:\t" + parsed_file.getLogData().getBuildVersion() + System.lineSeparator());
				}
				if (Integer.valueOf(parsed_file.getLogData().getBuildVersion().replaceAll("EVTC", "")) < 20170214) {
					return "Warning:\t\tThis log is outdated. Make sure the log is created by arcdps build 20170214 onwards.";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Choice
		if (choice.equals(MenuChoice.FINAL_DPS)) {
			return statistics.getFinalDPS();
		} else if (choice.equals(MenuChoice.PHASE_DPS)) {
			return statistics.getPhaseDPS();
		} else if (choice.equals(MenuChoice.DMG_DIST)) {
			return statistics.getDamageDistribution();
		} else if (choice.equals(MenuChoice.G_TOTAL_DMG)) {
			return "Output file:\t" + statistics.getTotalDamageGraph(current_file);
		} else if (choice.equals(MenuChoice.MISC_STATS)) {
			return statistics.getCombatStatistics();
		} else if (choice.equals(MenuChoice.FINAL_BOONS)) {
			return statistics.getFinalBoons();
		} else if (choice.equals(MenuChoice.PHASE_BOONS)) {
			return statistics.getPhaseBoons();
		} else if (choice.equals(MenuChoice.DUMP_EVTC)) {
			File evtc_dump = new File(
					"./tables/" + current_file + "_" + parsed_file.getBossData().getName() + "_evtc-dump.txt");
			try {
				Utility.writeToFile(parsed_file.toString(), evtc_dump);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "Output file:\t" + evtc_dump.getName();
		} else if (choice.equals(MenuChoice.DUMP_TABLES)) {
			File text_dump = new File(
					"./tables/" + current_file + "_" + parsed_file.getBossData().getName() + "_all-tables.txt");
			try {
				Utility.writeToFile(statistics.getFinalDPS() + System.lineSeparator() + statistics.getPhaseDPS()
						+ System.lineSeparator() + statistics.getCombatStatistics() + System.lineSeparator()
						+ statistics.getFinalBoons() + System.lineSeparator() + statistics.getPhaseBoons()
						+ System.lineSeparator() + statistics.getDamageDistribution(), text_dump);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "Output file:\t" + text_dump.getName();
		}
		return "";
	}
}
