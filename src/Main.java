import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import enums.Choice;
import statistics.Parse;
import statistics.Statistics;
import utility.Utility;

public class Main {

	// Fields
	private static boolean quitting = false;
	private static boolean displaying_version = true;

	// Main
	public static void main(String[] args) {

		// Scanner
		Scanner scan = null;
		try {
			scan = new Scanner(System.in);

			// File Association
			if (args.length == 2) {
				displaying_version = false;
				int[] choices = args[1].chars().map(x -> x - '0').toArray();

				StringBuilder output = new StringBuilder("<START>");
				for (int i : choices) {
					Choice c = Choice.getChoice(i);
					if (i > 0) {
						output.append(System.lineSeparator());
						output.append(parsing(c, new File(args[0])));
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
					System.out.printf("%n");
					System.out.println("Press Enter to exit.");
					System.out.printf("%n");
					scan.nextLine();
					return;
				}

				// Display menu
				else {
					while (!quitting) {
						System.out.println("_______________");
						System.out.printf("%n");
						System.out.println("EVTC Log Parser");
						System.out.println("_______________");
						System.out.printf("%n");
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
						System.out.println("_______________");
						System.out.printf("%n");
						System.out.println("Enter an option by number below:");

						// Read user input
						Choice choice = null;
						try {
							choice = Choice.getChoice(scan.nextInt());

						} catch (InputMismatchException e) {
							e.printStackTrace();
						}
						scan.nextLine();

						// Invalid choice
						if (choice == null) {
							System.out.println("Invalid option. Try again.\n");
						}

						// Quitting
						else if (choice.equals(Choice.QUIT)) {
							quitting = true;
						}

						// Valid choice
						else {

							// Apply option to all .evtc files in /logs/
							for (File log : logs) {
								System.out.println("\nInput file:\t" + log.getName());
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

	private static String parsing(Choice choice, File log) {

		// Parse the log
		String base = log.getName().split("\\.(?=[^\\.]+$)")[0];
		Parse parsed = null;
		Statistics stats = null;
		try {
			parsed = new Parse(log);
			if (displaying_version) {
				System.out.println("Log version:\t" + parsed.getB().getVersion());
			}
			stats = new Statistics(parsed);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Damage related options
		if (choice.getType().equals("damage")) {
			stats.get_damage_logs();
			if (choice.equals(Choice.FINAL_DPS)) {
				return stats.get_final_dps();
			}

			else if (choice.equals(Choice.PHASE_DPS)) {
				return stats.get_phase_dps();
			}

			else if (choice.equals(Choice.DMG_DIST)) {
				return stats.get_damage_distribution();
			}

			else if (choice.equals(Choice.G_TOTAL_DMG)) {
				return "Output file:\t" + stats.get_total_damage_graph(base);
			}

			else if (choice.equals(Choice.MISC_STATS)) {
				return stats.get_combat_stats();
			}
		}

		// Boon related options
		else if (choice.getType().equals("boons")) {
			stats.get_boon_logs();
			if (choice.equals(Choice.FINAL_BOONS)) {
				return stats.get_final_boons();
			} else if (choice.equals(Choice.PHASE_BOONS)) {
				return stats.get_phase_boons();
			}

		}

		// Text Dumps
		else if (choice.getType().equals("text")) {
			if (choice.equals(Choice.DUMP_EVTC)) {
				File evtc_dump = new File("./tables/" + base + "_" + parsed.getB().getName() + "_evtc-dump.txt");
				try {
					Utility.writeToFile(parsed.toString(), evtc_dump);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "Output file:\t" + evtc_dump.getName();
			} else if (choice.equals(Choice.DUMP_TABLES)) {
				stats.get_damage_logs();
				stats.get_boon_logs();
				File text_dump = new File("./tables/" + base + "_" + parsed.getB().getName() + "_all-tables.txt");
				try {
					Utility.writeToFile(stats.get_final_dps() + System.lineSeparator() + stats.get_phase_dps()
							+ System.lineSeparator() + stats.get_combat_stats() + System.lineSeparator()
							+ stats.get_final_boons() + System.lineSeparator() + stats.get_phase_boons()
							+ System.lineSeparator() + stats.get_damage_distribution(), text_dump);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "Output file:\t" + text_dump.getName();
			}
		}
		return "";
	}
}
