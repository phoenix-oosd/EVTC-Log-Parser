import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
	
	private bossData b_data = null;
	private List<playerData> p_data = null;
	private List<skillData> s_data = null;
	private List<combatData> c_data = null;
	
	// Constructor
	public Statistics(bossData b_data, List<playerData> p_data, List<skillData> s_data, List<combatData> c_data) {
		this.b_data = b_data;
		this.p_data = p_data;
		this.s_data = s_data;
		this.c_data = c_data;
	}
	
	// Public Methods
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

	public void get_final_dps() {
		
//		TableList tl = new TableList(3, "Character Name", "Profession", "Final DPS").sortBy(0).withUnicode(false);
		
		double fight_duration = (double) b_data.getFightDuration() / 1000;

		for (playerData p : p_data) {
			
			List<damageLog> damage_logs = p.get_damage_logs();
			double total_damage = 0;
			for (damageLog log : damage_logs) {
				total_damage = total_damage + log.getDamage();
			}


//			tl.addRow(p.getName(), p.getProf(), (String.format("%.2f", total_damage / fight_duration)));
			
		}
		
		get_fight_intervals();
		
//		tl.print();
		
	}
	
	// Private Methods
	private List<Point> get_fight_intervals() {
		
		List<Point> fight_intervals = new ArrayList<Point>();
		
		int i_count;
		int t_invuln;

		if (b_data.getName() == "Vale Guardian") {
			i_count = 2;
			t_invuln = 20000;
		}
		else if (b_data.getName() == "Gorseval") {
			i_count = 2;
			t_invuln = 30000;
		}
		else if (b_data.getName() == "Sabetha") {
			i_count = 3;
			t_invuln = 25000;
		}
		else if (b_data.getName() == "Sabetha") {
			i_count = 1;
			t_invuln = 60000;
		}
		else {
			fight_intervals.add(new Point(0, b_data.getFightDuration()));
			return fight_intervals;
		}
		
		// Get the interval when the boss is invulnerable
		List<List<Point>> i_intervals = new ArrayList<List<Point>>();
		
		for (playerData p : p_data) {
			List<damageLog> damage_logs = p.get_damage_logs();
			int t_curr = 0;
			int t_prev = 0;
			List<Point> player_intervals = new ArrayList<Point>();
			for (damageLog log : damage_logs) {
				if (!log.is_condi()) {
					t_curr = log.getTime();
					if ((t_curr - t_prev) > t_invuln) {
						player_intervals.add(new Point(t_prev, t_curr));
					}
					t_prev = t_curr;
				}
			}
			if (player_intervals.size() == i_count) {
				i_intervals.add(player_intervals);
			}
			
		}
		
		// Derive the fight intervals
		List<Point> real_fight_intervals = new ArrayList<Point>();
		for (int i = 0; i < i_count; i++) {
			fight_intervals.add(new Point(0, b_data.getFightDuration()));
			real_fight_intervals.add(new Point(0, b_data.getFightDuration()));
		}
		real_fight_intervals.add(new Point(0, b_data.getFightDuration()));
		for (List<Point> player_intervals : i_intervals) {			
			for (int i = 0; i < i_count; i++) {		
				Point new_point = player_intervals.get(i);
				Point old_point = fight_intervals.get(i);	
				int t_begin = new_point.x;
				int t_end = new_point.y;	
				if (t_begin > old_point.x) {
					old_point.x = t_begin;
				}
				if (t_end < old_point.y) {
					old_point.y = t_end;
				}
			}
		}
		// Shift points to the right
		for (int i = 0; i < real_fight_intervals.size(); i++) {
			// Start
			if (i == 0) {
				real_fight_intervals.get(i).y = fight_intervals.get(i).x;
			}
			// End
			else if ((i + 1) == real_fight_intervals.size()) {
				real_fight_intervals.get(i).x = fight_intervals.get(i - 1).y;
			}
			// Middle
			else {
				real_fight_intervals.get(i).x = fight_intervals.get(i - 1).y;
				real_fight_intervals.get(i).y = fight_intervals.get(i).x;		
			}
		}
		
		return real_fight_intervals;
		
	}
	
}
