package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.AgentItem;
import data.BossData;
import data.CombatItem;
import data.SkillData;
import enums.Boon;
import enums.StateChange;

public class Player {

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

	public Map<String, List<BoonLog>> getBoonMap(BossData bossData, SkillData skillData, List<CombatItem> combatList) {
		if (boonMap.isEmpty()) {
			setBoonMap(bossData, skillData, combatList);
		}
		return boonMap;
	}

	// Private Methods
	private void setOutDamageLogs(BossData bossData, List<CombatItem> combatList) {

		int timeStart = bossData.getFightStart();

		for (CombatItem c : combatList) {
			if (agent.getCID() == c.get_src_cid() || agent.getCID() == c.get_src_master_cid()) {
				StateChange state = c.is_statechange();
				int time = c.get_time() - timeStart;
				if (bossData.getCID() == c.get_dst_cid() && c.iff()) {
					if (state.equals(StateChange.NORMAL)) {
						if (c.is_buff() && c.get_buff_dmg() != 0) {
							outDamageLogs
									.add(new DamageLog(time, c.get_buff_dmg(), c.get_skill_id(), true, c.get_result(),
											c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation()));
						} else if (!c.is_buff() && c.get_value() != 0) {
							outDamageLogs
									.add(new DamageLog(time, c.get_value(), c.get_skill_id(), false, c.get_result(),
											c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation()));
						}
					}
				} else if (agent.getCID() == c.get_src_cid() && state.equals(StateChange.CHANGE_DOWN)) {
					outDamageLogs.add(new DamageLog(time, c.get_value(), c.get_skill_id(), false, c.get_result(),
							c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation()));
				} else if (agent.getCID() == c.get_src_cid() && state.equals(StateChange.CHANGE_DEAD)) {
					outDamageLogs.add(new DamageLog(time, c.get_value(), c.get_skill_id(), false, c.get_result(),
							c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation()));
				}
			}
		}
	}

	public void setBoonMap(BossData bossData, SkillData skillData, List<CombatItem> combatList) {

		List<String> boonList = Boon.getList();
		for (String boon : boonList) {
			boonMap.put(boon, new ArrayList<BoonLog>());
		}

		int timeStart = bossData.getFightStart();

		for (CombatItem c : combatList) {
			if (agent.getCID() == c.get_dst_cid()) {
				String skill_name = skillData.getName(c.get_skill_id());
				if (c.is_buff() && (c.get_value() > 0)) {
					if (boonList.contains(skill_name)) {
						int time = c.get_time() - timeStart;
						boonMap.get(skill_name).add(new BoonLog(time, c.get_value()));
					}
				}
			}
		}
	}

}
