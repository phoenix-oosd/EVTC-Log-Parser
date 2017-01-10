import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
	
	private static boolean quitting;
	private static final int[] damage_choices = new int [] {1, 2, 3, 4};
	private static final int[] boon_choices = new int [] {5, 6};
	private static final int[] all_choices = new int [] {1, 2, 3, 4, 5, 6, 7};
	private static final String[] boon_array = new String [] {"Might", "Quickness", "Fury", "Protection", "Alacrity",
			"Spotter", "Spirit of Frost", "Glyph of Empowerment", "Grace of the Land",
            "Empower Allies", "Banner of Strength", "Banner of Discipline"};
//	private static final String[] boon_array = new String [] {"Fury"};
	


	public static void main(String[] args) {
		
		// Scanner
		Scanner scan = null;
		try {
			// Initialize scanner
			scan = new Scanner(System.in);
			
		    // Files
			String cwd = System.getProperty("user.dir"); 
	    	new File(cwd + "/logs").mkdirs();
	    	new File(cwd + "/graphs").mkdirs();
	    	new File(cwd + "/tables").mkdirs();	
	    	File dir = new File(cwd + "/logs");
	    	File[] logs = dir.listFiles();

	    	// Loop
	    	quitting = false;
	    	while (!quitting) {
	    		// Menu display
	    		System.out.println("EVTC Log Parser\n"
	    				+ "---------------\n"
	    				+ "1. Final DPS\n"
	    				+ "2. Phase DPS\n"
	    				+ "3. Graph Total Damage (not implemented)\n"
	    				+ "4. Misc. Combat Stats\n"
	    				+ "5. Final Boons\n"
	    				+ "6. Phase Boons (not implemented)\n"
	    				+ "7. Text Dump Tables\n"
	    				+ "8. Quit\n");
	    		System.out.println("Choose an option below: ");
	    		// Choose an option
	    		int choice = scan.nextInt();
	    		scan.nextLine();
	    		// Parse ".evtc" files
	        	if (logs.length > 0 && choice != 8) {
	        		for (File log : logs) {
	        			parsing(choice, log);
	        		}
	        	}
	        	else {
	        		System.exit(0);
	    		}
	    	}
		}
		
		finally {
			if (scan != null) {
				scan.close();
			}
		}
	}
	
	private static void parsing(int choice, File log) {
		
		String base = log.getName().split("\\.(?=[^\\.]+$)")[0];
		String extn = log.getName().split("\\.(?=[^\\.]+$)")[1];
		if (extn.equals("evtc")) {
			if (is_in(choice, all_choices)) {
				
				// Parse the logs
				System.out.println("Parsing " + base + "...");
        		Parse parser = new Parse(log);
        		bossData b_data;
        		List<playerData> p_data;
        		List<skillData> s_data;
        		List<combatData> c_data;
        		Statistics stats = null;
        		List<String> boon_list;
				try {
					b_data = parser.get_boss_data();
	        		p_data = parser.get_player_data();
	        		s_data = parser.get_skill_data();
	        		c_data = parser.get_combat_data();
//					for (combatData c: c_data) {
//						System.out.println("ID:" + c.get_skill_id());
//					}
//					System.exit(0);
	        		parser.fill_missing_data(b_data, p_data, s_data, c_data);
	        		stats = new Statistics(b_data, p_data, s_data, c_data);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (choice != 7) {
					
					if (is_in(choice, damage_choices)) {
						
						stats.get_damage_logs();
						
						if (choice == 1) {
		            		stats.get_final_dps();
						}
						else if (choice == 2) {
							stats.get_phase_dps();
						}
						else if (choice == 3) {
							return;
						}
						else if (choice == 4) {
							stats.get_combat_stats();
						}
					}
					
					else if (is_in(choice, boon_choices)) {
						
						
						boon_list = Arrays.asList(boon_array);
						stats.get_boon_logs(boon_list);
						
						if (choice == 5) {
							stats.get_final_boons(boon_list);
						}
						else if (choice == 6) {
							return;
						}
					}
				}
				else {
					stats.get_damage_logs();
					stats.get_final_dps();
					stats.get_phase_dps();
					stats.get_combat_stats();
					boon_list = Arrays.asList(boon_array);
					stats.get_boon_logs(boon_list);
					stats.get_final_boons(boon_list);
				}
			}
			else {
				System.out.println("Not a valid option. Try again.");
			}
		}
	}
	
	public static boolean is_in(int i, int[] array) {
		return IntStream.of(array).anyMatch(x -> x == i);
	}
	
}
    
