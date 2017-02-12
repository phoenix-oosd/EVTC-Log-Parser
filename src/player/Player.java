package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.AgentItem;
import data.BossData;
import data.CombatItem;
import enums.StateChange;

public class Player {

	// public List<DamageLog> get_damage_logs() {
	// return damage_logs;
	// }
	//

	// public void setBoons(String[] boonArray) {
	// for (String boon : boonArray) {
	// boon_logs.put(boon, new ArrayList<boonLog>());
	// }
	// }
	// Fields
	private AgentItem agent;
	private List<DamageLog> outDamageLogs;
	private Map<String, List<BoonLog>> boonMap;

	// Constructors
	public Player(AgentItem agent) {
		this.agent = agent;
		this.outDamageLogs = new ArrayList<DamageLog>();
		this.boonMap = new HashMap<>();
	}

	// Getters
	public AgentItem getAgent() {
		return agent;
	}

	public List<DamageLog> getOutBossDamage(BossData bossData, List<CombatItem> combatList) {

		if (outDamageLogs.isEmpty()) {
			setOutDamageLogs(bossData, combatList);
		}
		return outDamageLogs;
	}

	public Map<String, List<BoonLog>> getBoonMap() {
		return boonMap;
	}

	// Private Methods
	private void setOutDamageLogs(BossData bossData, List<CombatItem> combatList) {

		int timeStart = bossData.getFightStart();

		for (CombatItem c : combatList) {
			// Player or pet is the source
			if (agent.getCID() == c.get_src_cid() || agent.getCID() == c.get_src_master_cid()) {
				StateChange state = c.is_statechange();
				// Boss is the target
				if (bossData.getCID() == c.get_dst_cid() && c.iff()) {
					// Valid logs have NORMAL state;
					if (state.equals(StateChange.NORMAL)) {
						int time = c.get_time() - timeStart;
						// Condition
						if (c.is_buff() && c.get_buff_dmg() != 0) {
							outDamageLogs
									.add(new DamageLog(time, c.get_buff_dmg(), c.get_skill_id(), false, c.get_result(),
											c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation()));
						}
						// Physical
						else if (!c.is_buff() && c.get_value() != 0) {
							outDamageLogs.add(
									new DamageLog(c.get_time(), c.get_value(), c.get_skill_id(), false, c.get_result(),
											c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation()));
						}
					}
				}
				// Add relevant states
				if (state.equals(StateChange.CHANGE_DOWN)) {
					outDamageLogs.add(new DamageLog(c.get_time(), c.get_value(), c.get_skill_id(), false,
							c.get_result(), c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation()));
				}
			}
		}
	}

	public void setBoonMap(List<CombatItem> combatData) {
	}

}
