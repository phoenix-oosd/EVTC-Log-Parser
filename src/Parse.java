import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Parse {
	
	private FileInputStream file = null;
	
	// Constructor
	public Parse(File f) {
		try {
			this.file = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Private Methods
	
	private String get_boss_name(short cid) {
		
	    if (cid == 15438) {
	    	return "Vale Guardian";
	    }
	    else if (cid == 15429) {
	    	return "Gorseval";
	    }
	    else if (cid == 15375) {
	    	return "Sabetha";
	    }
	    else if (cid == 16123) {
	    	return "Slothasor";
	    }
	    else if (cid == 16088) {
	    	return "Berg";
	    }
	    else if (cid == 16137) {
	    	return "Zane";
	    }
	    else if (cid == 16125) {
	    	return "Narella";
	    }
	    else if (cid == 16115) {
	    	return "Matthias";
	    }
	    else if (cid == 16235) {
	    	return "Keep Construct";
	    }
	    else if (cid == 16246) {
	    	return "Xera";
	    }
	    else {
	    	return "UNKNOWN";
	    }
	}
	
	private int get_boss_HP(short cid) {
	
	    if (cid == 15438) {
	    	return 22400000;
	    }
	    else if (cid == 15429) {
	    	return 21600000;
	    }
	    else if (cid == 15375) {
	    	return 34000000;
	    }
	    else if (cid == 16123) {
	    	return 19000000;
	    }
	    else if (cid == 16088) {
	    	return 6900000;
	    }
	    else if (cid == 16137) {
	    	return 5900000;
	    }
	    else if (cid == 16125) {
	    	return 4900000;
	    }
	    else if (cid == 16115) {
	    	return 25900000;
	    }
	    else if (cid == 16235) {
	    	return 55053600;
	    }
	    else if (cid == 16246) {
	    	return 22611300;
	    }
	    else {
	    	return 0;
	    }
		
	}	
	
	private String get_String(byte[] bytes) {
		return new String(bytes);	
	}
	
	private short get_int16(byte[] bytes) {
		ByteBuffer wrapped = ByteBuffer.wrap(bytes);
		wrapped.order(ByteOrder.LITTLE_ENDIAN);
		return wrapped.getShort();
	}
	
	// Public Methods
	
	public bossData get_boss_data() throws IOException {
		
		// 4 bytes: EVTC
		file.skip(4);
		
		// 8 bytes: date
		byte[] date_buffer = new byte[8];
		file.read(date_buffer);
		
		// 1 byte: skip
		file.skip(1);
		
		// 2 bytes: Boss CID
		byte[] cid_buffer = new byte[2];
		file.read(cid_buffer);
		short cid = get_int16(cid_buffer);
		
		// 1 byte: skip
		file.skip(1);

		return new bossData(0, cid, get_boss_name(cid), get_boss_HP(cid), 0, get_String(date_buffer));
	}
	
	
//	private playerData get_player_data() throws IOException {
//		
//		
//	}
//	
//	private skillData get_skill_data() throws IOException {
//		
//		
//	}
//	
//	private combatData get_combat_data() throws IOException {
//		
//		
//	}

}