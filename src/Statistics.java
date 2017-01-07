import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Statistics {
	
	private bossData b_data = null;
	private List<playerData> p_data = null;
	private List<skillData> s_data = null;
	private List<combatData> c_data = null;
	
	public Statistics(bossData b_data, List<playerData> p_data, List<skillData> s_data, List<combatData> c_data) {
		this.b_data = b_data;
		this.p_data = p_data;
		this.s_data = s_data;
		this.c_data = c_data;
	}
	
	public void get_damage_logs() {

		// Start time of the fight
	    int t_start = c_data.get(0).get_time();
	    
		// Add damage logs for each player
	    for (playerData p : p_data) {	    	
	    	// Check all combat logs
	    	for (combatData c : c_data) {
	    		// The target is the boss and the player is an enemy
	    		if ((c.get_dst_cid() == b_data.getCID()) && c.iff()) {
	    			// The player or their pets is the source
		    		if ((p.getCID() == c.get_src_cid()) || (p.getCID() == c.get_src_master_cid())) {
			    		// Physical or condition damage
		    			if ((!c.is_buff() && (c.get_value() > 0)) || (c.is_buff() && (c.get_buff_dmg() > 0))) {
		    				int time = c.get_time() - t_start;
		    				int damage;
		    				if (c.is_buff()) {
		    					damage = c.get_buff_dmg();
		    				}
		    				else {
		    					damage = c.get_value();
		    				}
		    				p.get_damage_logs().add(new damageLog(time, damage, c.is_buff(), c.is_crit(), c.is_ninety(), c.is_moving()));
			    		}
		    		}  			
	    		}		
	    	}
	    }
	}

}
