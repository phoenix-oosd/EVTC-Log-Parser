import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Parse {
	
	private FileInputStream f = null;
	
	// Constructor
	public Parse(File f) {
		try {
			this.f = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Private Methods
	
	private String get_boss_name(int cid) {
		
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
	
	private int get_boss_HP(int cid) {
	
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
	
	private String get_prof(int prof_id, boolean is_elite) {
		
	    if (prof_id == 1) {
	    	if (is_elite) {
	    		return "Dragonhunter";
	    	}
	    	else {
	    		return "Guardian";	    		
	    	}
	    }
	    else if (prof_id == 2) {
	    	if (is_elite) {
	    		return "Berserker";
	    	}
	    	else{
	    		return "Warrior";
	    	}
	    }
	    else if (prof_id == 3) {
	    	if (is_elite) {
	    		return "Scrapper";
	    	}
	    	else{
	    		return "Engineer";
	    	}
	    }
	    else if (prof_id == 4) {
	    	if (is_elite) {
	    		return "Druid";
	    	}
	    	else {
	    		return "Ranger";
	    	}
	    }
	    else if (prof_id == 5) {
	    	if (is_elite) {
	    		return "Daredevil";
	    	}
	    	else {
	    		return "Thief";
	    	}
	    }
	    else if (prof_id == 6) {
	    	if (is_elite) {
	    		return "Tempest";
	    	}
	    	else {
	    		return "Elementalist";
	    	}
	    }
	    else if (prof_id == 7) {
	    	if (is_elite) {
	    		return "Chronomancer";
	    	}
	    	else {
	    		return "Mesmer";
	    	}
	    }
	    else if (prof_id == 8) {
	    	if (is_elite) {
	    		return "Reaper";
	    	}
	    	else {
	    		return "Necromancer";
	    	}
	    }
	    else if (prof_id == 9) {
	    	if (is_elite) {
	    		return "Herald";
	    	}
	    	else {
	    		return "Revenant";
	    	}
	    }
	    else {
	    	return "UNKNOWN";
	    }

	}
	
	private String get_String(byte[] bytes) {
		String str = new String(bytes);
	    int i = str.indexOf(0);
	    return i == -1 ? str : str.substring(0, i);
	}
	
	private short get_int16(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	private int get_int32(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	private boolean get_bool(int i) {
		boolean bool = (i != 0);
		return bool;
	}
	
	
	// Public Methods
	
	public bossData get_boss_data() throws IOException {
		
		// 4 bytes: EVTC
		f.skip(4);
		
		// 8 bytes: date
		byte[] date_buffer = new byte[8];
		f.read(date_buffer);
		
		// 1 byte: skip
		f.skip(1);
		
		// 2 bytes: Boss CID
		byte[] cid_buffer = new byte[2];
		f.read(cid_buffer);
		short cid = get_int16(cid_buffer);
		
		// 1 byte: skip
		f.skip(1);

		return new bossData(0, cid, get_boss_name(cid), get_boss_HP(cid), 0, get_String(date_buffer));
	}
	
	
	public List<playerData> get_player_data() throws IOException {
		
		// playerData array
		List<playerData> p_data = new ArrayList<playerData>();

		// 4 bytes: player count
		byte[] pc_buffer = new byte[4];
		f.read(pc_buffer);
		int player_count = get_int32(pc_buffer);
		
		// 96 bytes: each player
		for (int i = 0; i  < player_count; i++) {
			
			// 8 bytes: agent
			byte[] agent_buffer = new byte[8];
			f.read(agent_buffer);
			
			// 4 bytes: profession
			byte[] prof_buffer = new byte[4];
			f.read(prof_buffer);
			
			// 4 bytes: is_elite
			byte[] is_elite_buffer = new byte[4];
			f.read(is_elite_buffer);
			
			// 4 bytes: toughness
			byte[] toughness_buffer = new byte[4];
			f.read(toughness_buffer);
			
			// 4 bytes: healing
			byte[] healing_buffer = new byte[4];
			f.read(healing_buffer);
			
			// 4 bytes: condition
			byte[] condition_buffer = new byte[4];
			f.read(condition_buffer);
			
			// 4 bytes: name
			byte[] name_buffer = new byte[68];
			f.read(name_buffer);
			
			// add player
			p_data.add(new playerData(get_int32(agent_buffer), 0, get_String(name_buffer), get_prof(get_int32(prof_buffer), get_bool(get_int32(is_elite_buffer))), get_int32(toughness_buffer), get_int32(healing_buffer), get_int32(condition_buffer)));	
			
		}
		return p_data;
	}

	
//	public List<skillData> get_skill_data() throws IOException {
//		
//		
//	}
//	
//	public List<combatData> get_combat_data() throws IOException {
//		
//		
//	}

}