import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
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
			
			// 68 bytes: name
			byte[] name_buffer = new byte[68];
			f.read(name_buffer);
			
			// add player
			p_data.add(new playerData(get_int32(agent_buffer), 0, get_String(name_buffer), get_prof(get_int32(prof_buffer), get_bool(is_elite_buffer)), get_int32(toughness_buffer), get_int32(healing_buffer), get_int32(condition_buffer)));	
			
		}
		return p_data;
	}

	
	public List<skillData> get_skill_data() throws IOException {
		
		// skillData array
		List<skillData> s_data = new ArrayList<skillData>();
		
		// 4 bytes: player count
		byte[] sc_buffer = new byte[4];
		f.read(sc_buffer);
		int skill_count = get_int32(sc_buffer);
		
		// 68 bytes: each skill
		for (int i = 0; i  < skill_count; i++) {
			
			// 4 bytes: id
			byte[] id_buffer = new byte[4];
			f.read(id_buffer);
			
			// 64 bytes: name
			byte[] name_buffer = new byte[64];
			f.read(name_buffer);
			
			// add skill
			s_data.add(new skillData(get_int32(id_buffer), get_String(name_buffer)));

		}
		
		return s_data;
		
	}
	
	public List<combatData> get_combat_data() throws IOException {
		
		// combatData array
		List<combatData> c_data = new ArrayList<combatData>();
		
		// 64 bytes: each combat
		while (f.available() >= 64) {
			
			// 8 bytes: time
			byte[] time_buffer = new byte[8];
			f.read(time_buffer);
			
			// 8 bytes: src_agent
			byte[] src_agent_buffer = new byte[8];
			f.read(src_agent_buffer);
			
			// 8 bytes: dst_agent
			byte[] dst_agent_buffer = new byte[8];
			f.read(dst_agent_buffer);
			
			// 4 bytes: value
			byte[] value_buffer = new byte[4];
			f.read(value_buffer);
			
			// 4 bytes: buff_dmg
			byte[] buff_dmg_buffer = new byte[4];
			f.read(buff_dmg_buffer);
			
			// 2 bytes: overstack_value
			byte[] overstack_value_buffer = new byte[2];
			f.read(overstack_value_buffer);
			
			// 2 bytes: skill_id
			byte[] skill_id_buffer = new byte[2];
			f.read(skill_id_buffer);
			
			// 2 bytes: src_cid
			byte[] src_cid_buffer = new byte[2];
			f.read(src_cid_buffer);
			
			// 2 bytes: dst_cid
			byte[] dst_cid_buffer = new byte[2];
			f.read(dst_cid_buffer);
			
			// 2 bytes: src_master_cid
			byte[] src_master_cid_buffer = new byte[2];
			f.read(src_master_cid_buffer);
			
			// 9 bytes: garbage
			f.skip(9);
			
			// 1 byte: iff
			byte[] iff_buffer = new byte[1];
			f.read(iff_buffer);
			
			// 1 byte: is_buff
			byte[] is_buff_buffer = new byte[1];
			f.read(is_buff_buffer);
			
			// 1 byte: is_crit
			byte[] is_crit_buffer = new byte[1];
			f.read(is_crit_buffer);
			
			// 1 byte: is_activation
			byte[] is_activation_buffer = new byte[1];
			f.read(is_activation_buffer);
			
			// 1 byte: is_buffremove
			byte[] is_buffremove_buffer = new byte[1];
			f.read(is_buffremove_buffer);
			
			// 1 byte: is_ninety
			byte[] is_ninety_buffer = new byte[1];
			f.read(is_ninety_buffer);
			
			// 1 byte: is_fifty
			byte[] is_fifty_buffer = new byte[1];
			f.read(is_fifty_buffer);
			
			// 1 byte: is_moving
			byte[] is_moving_buffer = new byte[1];
			f.read(is_moving_buffer);
			
			// 1 byte: is_statechange
			byte[] is_statechange_buffer = new byte[1];
			f.read(is_statechange_buffer);	
			
			// 4 bytes: garbage
			f.skip(4);

			// add combat
			c_data.add(new combatData(get_int32(time_buffer), get_int32(src_agent_buffer), get_int32(dst_agent_buffer),
					get_int32(value_buffer), get_int32(buff_dmg_buffer),
					get_int16(overstack_value_buffer), (get_int16(skill_id_buffer) & 0xffff),
					get_int16(src_cid_buffer), get_int16(dst_cid_buffer), get_int16(src_master_cid_buffer),
					get_bool(iff_buffer[0]), get_bool(is_buff_buffer[0]), get_bool(is_crit_buffer[0]),
					get_bool(is_activation_buffer[0]), get_bool(is_buffremove_buffer[0]), get_bool(is_ninety_buffer[0]),
					get_bool(is_fifty_buffer[0]), get_bool(is_moving_buffer[0]), get_bool(is_statechange_buffer[0])));
		}
		return c_data;
	}

	public void fill_missing_data(bossData b_data, List<playerData> p_data, List<skillData> s_data, List<combatData> c_data) {
		
		// Update boss agent
		for (combatData c : c_data) {
			if (c.get_src_cid() == b_data.getCID()){
				b_data.setAgent(c.get_src_agent());
				break;
			}
		}
		
		// Update boss fight duration
		b_data.setFightDuration(c_data.get(c_data.size() - 1).get_time() - c_data.get(0).get_time());
		
		// Update player CIDs
		for (playerData p: p_data) {
			for (combatData c: c_data) {
				if (p.getAgent() == c.get_src_agent()) {
					if (c.get_src_master_cid() == 0){
						p.setCID(c.get_src_cid());
					}
					else{
						p.setCID(c.get_src_master_cid());
					}
					break;
				}	
			}
		}
		
		// Delete players with no CID
		Iterator<playerData> iter = p_data.iterator();
		while (iter.hasNext()) {
			playerData p = iter.next();
			if (p.getCID() == 0) {
				iter.remove();
			}
		}
		
		// Update combat for Xera logs
		if (b_data.getName() == "Xera") {
			int xera_50 = 16286;
			for (combatData c : c_data) {
				if (c.get_src_cid() == xera_50) {
					c.set_src_agent(b_data.getAgent());
					c.set_src_cid(b_data.getCID());
				}
				else if (c.get_dst_cid() == xera_50) {
					c.set_dst_agent(b_data.getAgent());
					c.set_dst_cid(b_data.getCID());
				}
			}
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
	
	
	
	private boolean get_bool(byte[] bytes) {
		boolean bool = (ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt() != 0);
		return bool;
	}
	
	private boolean get_bool(int i) {
		boolean bool = (i != 0);
		return bool;
	}

}