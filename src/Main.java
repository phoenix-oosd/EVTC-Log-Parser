import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import enums.MenuChoice;
import statistics.Parse;
import statistics.Statistics;
import utility.Utility;

public class Main
{

	// Fields
	private static boolean will_quit = false;
	private static boolean will_display_versions = true;
	private static Map<String, String> argument_map = new HashMap<>();
	private static String old_version = Utility.boxText("ERROR   : This only supports versions 20170311 and onwards");
	private static String current_file;
	private static Parse parsed_file;
	private static Statistics statistics;

	// Main
	public static void main(String[] args)
	{
		// Scanner
		Scanner scan = null;
		try
		{
			scan = new Scanner(System.in);

			// Read arguments
			for (String arg : args)
			{
				if (arg.contains("="))
				{
					argument_map.put(arg.substring(0, arg.indexOf('=')), arg.substring(arg.indexOf('=') + 1));
				}
			}
			String is_anon = argument_map.get("is_anon");
			String file_path = argument_map.get("file_path");
			String options = argument_map.get("options");

			// Handle arguments
			if (is_anon != null)
			{
				Statistics.willHidePlayers = Utility.toBool(Integer.valueOf(is_anon));
			}

			// File Association
			if (file_path != null && !file_path.isEmpty() && options != null)
			{
				will_display_versions = false;
				int[] choices = options.chars().map(x -> x - '0').toArray();

				StringBuilder output = new StringBuilder();
				for (int i : choices)
				{
					MenuChoice c = MenuChoice.getEnum(i);
					if (c != null && c.canBeAssociated())
					{
						String result = parseFileByChoice(c, Paths.get(file_path));
						if (result.contains("ERROR"))
						{
							System.out.println(old_version);
							scan.nextLine();
							return;
						}
						else
						{
							output.append(result + System.lineSeparator());
						}
					}
				}
				System.out.println(output.toString());
				scan.nextLine();
				return;
			}

			// Menu
			else
			{
				// Create required directories
				new File("./logs").mkdir();
				new File("./graphs").mkdirs();
				new File("./tables").mkdirs();

				// Obtain list of .evtc files in /logs/
				List<Path> log_files = new ArrayList<Path>();
				File log_folder = new File("./logs");
				Utility.recursiveFileSearch(log_folder, log_files);

				// No logs to process
				if (log_files.isEmpty())
				{
					try
					{
						System.out.println(Utility.boxText("ERROR : No log files found at \""
								+ log_folder.getCanonicalPath().toString() + "\" ... press Enter to exit"));
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					scan.nextLine();
					return;
				}
				// There are logs to process
				else
				{
					while (!will_quit)
					{
						// Display menu
						System.out.println("\u250C" + Utility.fillWithChar(24, '\u2500') + "\u2510");
						System.out.println("\u2502    EVTC Log Parser     \u2502");
						System.out.println("\u251C" + Utility.fillWithChar(24, '\u2500') + "\u2524");
						System.out.println("\u2502 0. Dump EVTC           \u2502");
						System.out.println("\u2502 1. Final DPS           \u2502");
						System.out.println("\u2502 2. Phase DPS           \u2502");
						System.out.println("\u2502 3. Damage Distribution \u2502");
						System.out.println("\u2502 4. Graph Total Damage  \u2502");
						System.out.println("\u2502 5. Misc. Combat Stats  \u2502");
						System.out.println("\u2502 6. Final Boon Rates    \u2502");
						System.out.println("\u2502 7. Phase Boon Rates    \u2502");
						System.out.println("\u2502 8. Dump All Tables     \u2502");
						System.out.println("\u2502 9. Quit                \u2502");
						System.out.println("\u2514" + Utility.fillWithChar(24, '\u2500') + "\u2518");

						// Read user input
						MenuChoice choice = null;
						System.out.println(Utility.boxText("Enter an option by number"));
						System.out.print(" >> ");
						if (scan.hasNextInt())
						{
							choice = MenuChoice.getEnum(scan.nextInt());
						}
						scan.nextLine();

						// Invalid option
						if (choice == null)
						{
							System.out.println(Utility.boxText("WARNING : Invalid option"));
						}
						// Quit
						else if (choice.equals(MenuChoice.QUIT))
						{
							will_quit = true;
						}
						// Valid option
						else
						{
							// Apply option to all logs
							for (Path log : log_files)
							{
								System.out.println(Utility.boxText("INPUT   : " + log.getFileName().toString()));
								String output = parseFileByChoice(choice, log);
								System.out.println(output + System.lineSeparator() + System.lineSeparator()
										+ Utility.fillWithChar(50, '\u2500') + System.lineSeparator());
							}
						}
					}
				}
			}
		}

		// Close scanner
		finally
		{
			if (scan != null)
			{
				scan.close();
			}
		}
		return;
	}

	private static String parseFileByChoice(MenuChoice choice, Path path)
	{
		// Parse a new file
		if (current_file == null || !current_file.equals(path.getFileName().toString().split("\\.")[0]))
		{
			try
			{
				parsed_file = new Parse(path.toString());
				statistics = new Statistics(parsed_file);
				current_file = path.getFileName().toString().split("\\.")[0];
				if (will_display_versions)
				{
					System.out.println(Utility.boxText("VERSION : " + parsed_file.getLogData().getBuildVersion()));
				}
				if (Integer.valueOf(parsed_file.getLogData().getBuildVersion().replaceAll("EVTC", "")) < 20170311)
				{
					return old_version;
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// Apply option
		if (choice.equals(MenuChoice.FINAL_DPS))
		{
			return statistics.getFinalDPS();
		}
		else if (choice.equals(MenuChoice.PHASE_DPS))
		{
			return statistics.getPhaseDPS();
		}
		else if (choice.equals(MenuChoice.DMG_DIST))
		{
			return statistics.getDamageDistribution();
		}
		else if (choice.equals(MenuChoice.G_TOTAL_DMG))
		{
			return Utility.boxText("OUTPUT  : " + statistics.getTotalDamageGraph(current_file));
		}
		else if (choice.equals(MenuChoice.MISC_STATS))
		{
			return statistics.getCombatStatistics();
		}
		else if (choice.equals(MenuChoice.FINAL_BOONS))
		{
			return statistics.getFinalBoons();
		}
		else if (choice.equals(MenuChoice.PHASE_BOONS))
		{
			return statistics.getPhaseBoons();
		}
		else if (choice.equals(MenuChoice.DUMP_EVTC))
		{
			File evtc_dump = new File(
					"./tables/" + current_file + "_" + parsed_file.getBossData().getName() + "_evtc-dump.txt");
			try
			{
				Utility.writeToFile(parsed_file.toString(), evtc_dump);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			return Utility.boxText("OUTPUT  : " + evtc_dump.getName());
		}
		else if (choice.equals(MenuChoice.DUMP_TABLES))
		{
			File text_dump = new File(
					"./tables/" + current_file + "_" + parsed_file.getBossData().getName() + "_all-tables.txt");
			try
			{
				Utility.writeToFile(statistics.getFinalDPS() + System.lineSeparator() + statistics.getPhaseDPS()
						+ System.lineSeparator() + statistics.getCombatStatistics() + System.lineSeparator()
						+ statistics.getFinalBoons() + System.lineSeparator() + statistics.getPhaseBoons()
						+ System.lineSeparator() + statistics.getDamageDistribution(), text_dump);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			return Utility.boxText("OUTPUT  : " + text_dump.getName());
		}
		return "";
	}
}
