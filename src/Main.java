import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

    	// Make directories if they don't exist
        String cwd = System.getProperty("user.dir");    	
    	new File(cwd + "/logs").mkdirs();
    	new File(cwd + "/graphs").mkdirs();
    	new File(cwd + "/tables").mkdirs();	
    	
    	// Menu loop
    	try(Scanner scanner = new Scanner(System.in)) {
    		System.out.println("EVTC Log Visualiser\n"
    				+ "-------------------\n"
    				+ "1. Final DPS\n"
    				+ "2. Phase DPS\n"
    				+ "3. Graph Total Damage (not implemented)\n"
    				+ "4. Misc. Combat Stats\n"
    				+ "5. Final Boons/Buffs\n"
    				+ "6. Phase Boons/Buffs (not implemented)\n"
    				+ "7. Text Dump Tables\n"
    				+ "8. Quit\n");
    		System.out.println("Choose an option (Enter to confirm): ");
    		int choice = scanner.nextInt();
    		while(choice != 8) {  			
    	        switch (choice) {
    	            case 1:
    	            case 2:
    	            case 3:
    	            case 4:
    	                // Damage Statistics
    	            	if(choice == 1){
    	            		
    	            	}
    	            	else if(choice == 2){
    	            		
    	            	}
    	            	else if(choice == 3){
    	            		
    	            	}
    	            	else if(choice == 4){
    	            		
    	            	}
    	                break;
    	                
    	            case 5:
    	            case 6:
    	            	// Boon Statistics
    	            	if(choice == 5){
    	            		
    	            	}
    	            	else if(choice == 6){
    	            		
    	            	}
    	            	break;
    	            case 7:
    	                // Text Dump All Statistics
    	                break;
    	            case 8:
    	                // Quit.
    	                break;
    	            default:
    	                System.out.println("Nope. Try again.\n");
    	        }	
            }
    	}
    }
    
}