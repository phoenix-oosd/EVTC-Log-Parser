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
import enums.CustomSkill;
import enums.IFF;
import enums.StateChange;
import statistics.Statistics;

public class Player {

	// Fields
	// private long agent;
	private int instid;
	private String account;
	private String character;
	private String sub_group = "N/A";
	private String prof;
	private int toughness;
	private int healing;
	private int condition;
	private List<DamageLog> outDamageLogs = new ArrayList<DamageLog>();
	private Map<String, List<BoonLog>> boonMap = new HashMap<>();

	// Constructors
	public Player(AgentItem agent) {
		// this.agent = agent.get_agent();
		this.instid = agent.get_instid();
		String[] name = agent.get_name().split(Character.toString('\0'));
		if (name.length >= 2) {
			this.character = name[0];
			this.account = name[1];
			if (name.length == 3) {
				this.sub_group = name[2];
			}
		} else {
			this.character = agent.get_name();
			this.account = "Account.XXXX";
		}
		if (Statistics.willHidePlayers) {
			this.character = " P:" + String.format("%04d", instid);
			this.account = " :A." + String.format("%04d", instid);
		}
		this.prof = agent.get_prof();
		this.toughness = agent.get_toughness();
		this.healing = agent.get_healing();
		this.condition = agent.get_condition();
	}

	// Getters
	public String get_account() {
		return account;
	}

	public String get_character() {
		return character;
	}

	public String get_sub_group() {
		return sub_group;
	}

	public String get_prof() {
		return prof;
	}

	public int get_toughness() {
		return toughness;
	}

	public int get_healing() {
		return healing;
	}

	public int get_condition() {
		return condition;
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

		int timeStart = bossData.get_first_aware();

		for (CombatItem c : combatList) {
			if (instid == c.get_src_instid() || instid == c.get_src_master_instid()) {
				StateChange state = c.is_statechange();
				int time = c.get_time() - timeStart;
				if (bossData.get_instid() == c.get_dst_instid() && c.get_iff().equals(IFF.FOE)) {
					if (state.equals(StateChange.NORMAL)) {
						if (c.is_buff() && c.get_buff_dmg() != 0) {
							outDamageLogs.add(new DamageLog(time, c.get_buff_dmg(), c.get_skill_id(), true,
									c.get_result(), c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation(),
									c.isFlanking()));
						} else if (!c.is_buff() && c.get_value() != 0) {
							outDamageLogs.add(new DamageLog(time, c.get_value(), c.get_skill_id(), false,
									c.get_result(), c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation(),
									c.isFlanking()));
						}
					}
				} else if (instid == c.get_src_instid()) {
					if (state.equals(StateChange.CHANGE_DEAD)) {
						outDamageLogs.add(new DamageLog(time, c.get_value(), c.get_skill_id(), false, c.get_result(),
								c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation(), c.isFlanking()));
					} else if (state.equals(StateChange.CHANGE_DOWN)) {
						outDamageLogs.add(new DamageLog(time, c.get_value(), c.get_skill_id(), false, c.get_result(),
								c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation(), c.isFlanking()));
					} else if (CustomSkill.getEnum(c.get_skill_id()) != null) {
						outDamageLogs.add(new DamageLog(time, c.get_value(), c.get_skill_id(), false, c.get_result(),
								c.is_ninety(), c.is_moving(), c.is_statechange(), c.is_activation(), c.isFlanking()));
					}

				}

			}
		}
	}

	public void setBoonMap(BossData bossData, SkillData skillData, List<CombatItem> combatList) {

		List<String> boonList = Boon.getList();
		for (String boon : boonList) {
			boonMap.put(boon, new ArrayList<BoonLog>());
		}

		int time_start = bossData.get_first_aware();
		int fight_duration = bossData.get_last_aware() - time_start;

		for (CombatItem c : combatList) {
			if (instid == c.get_dst_instid()) {
				String skill_name = skillData.getName(c.get_skill_id());
				if (c.is_buff() && (c.get_value() > 0)) {
					if (boonList.contains(skill_name)) {
						int time = c.get_time() - time_start;
						if (time < fight_duration) {
							boonMap.get(skill_name).add(new BoonLog(time, c.get_value()));
						} else {
							break;
						}
					}
				}
			}
		}
	}

}
