package data;

import java.util.ArrayList;
import java.util.List;

public class CombatData {

	// Fields
	private List<CombatItem> combatData;

	// Constructors
	public CombatData() {
		this.combatData = new ArrayList<CombatItem>();
	}

	// Public Methods
	public void addItem(CombatItem item) {
		combatData.add(item);
	}

	// Getters
	public List<CombatItem> getCombatData() {
		return combatData;
	}

	// // Public Methods
	// public void get_damage_logs(AgentData agentData) {
	//
	// // Start time of the fight
	// long t_start = combatData.get(0).get_time();
	//
	// // Add damage logs for each player
	// for (AgentItem a : agentData.getPlayerAgents()) {
	//
	// // Check all combat logs
	// for (CombatItem c : combatData) {
	// // The player or their pets is the source
	// if ((a.getCID() == c.get_src_cid()) || (a.getCID() ==
	// c.get_src_master_cid())) {
	// // The target is the boss and the player is an enemy
	// if ((c.get_dst_cid() == b_data.getCID()) && c.iff()) {
	// // Physical or condition damage
	// if ((!c.is_buff() && (c.get_value() > 0)) || (c.is_buff() &&
	// (c.get_buff_dmg() > 0))) {
	// int time = (int) (c.get_time() - t_start);
	// int damage;
	// if (c.is_buff()) {
	// damage = c.get_buff_dmg();
	// } else {
	// damage = c.get_value();
	// }
	// a.get_damage_logs().add(new damageLog(time, damage, c.get_skill_id(),
	// c.is_buff(),
	// c.get_result(), c.is_ninety(), c.is_moving(), c.is_statechange(), 0));
	// }
	// } else if (a.getCID() == c.get_src_cid() && c.get_value() == 0 &&
	// c.is_statechange() > 0) {
	// int time = (int) (c.get_time() - t_start);
	// a.get_damage_logs().add(new damageLog(time, 0, c.get_skill_id(),
	// c.is_buff(), c.get_result(),
	// c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation()));
	// }
	// }
	// }
	// }
	// }
	//
	// public void get_boon_logs(AgentData agentData) {
	//
	// // Start time of the fight
	// long t_start = combatData.get(0).get_time();
	// List<String> boon_list = Boon.getList();
	//
	// // Add boon logs for each player
	// for (AgentItem a : agentData) {
	// // Initialize boon list
	// a.setBoons(boon_list);
	// // Check all combat logs
	// for (CombatItem c : combatData) {
	// // The player is the target
	// if (a.getCID() == c.get_dst_cid()) {
	// // If the skill is a buff and in the boon list
	// String skill_name = get_skill_name(c.get_skill_id());
	// if ((c.is_buff() && (c.get_value() > 0)) &&
	// (boon_list.contains(skill_name))) {
	// int time = (int) (c.get_time() - t_start);
	// int src_cid;
	// if (c.get_src_cid() > 0) {
	// src_cid = c.get_src_cid();
	// } else {
	// src_cid = c.get_src_master_cid();
	// }
	// a.get_boon_logs().get(skill_name).add(new boonLog(time, c.get_value(),
	// src_cid));
	// }
	// }
	// }
	// }
	// }

}
