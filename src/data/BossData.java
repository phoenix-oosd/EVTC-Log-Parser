package data;

import java.util.List;

import enums.StateChange;

public class BossData {

	// Fields
	private int agent;
	private int CID;
	private String name;
	private int HP;
	private int fightStart;
	private int fightEnd;
	private boolean isKill;
	private String buildVersion;

	// Constructor
	public BossData(int agent, int CID, String name, int HP, int fightStart, int fightEnd, String buildVersion) {
		this.agent = agent;
		this.CID = CID;
		this.name = name;
		this.HP = HP;
		this.fightStart = fightStart;
		this.fightEnd = fightEnd;
		this.buildVersion = buildVersion;
	}

	// Public Methods
	public void fillMissingData(List<AgentItem> NPCAgentList, List<CombatItem> combatList) {

		for (AgentItem agent : NPCAgentList) {
			this.CID = NPCAgentList.get(0).getCID();
			if (this.name.equals(agent.getName())) {
				this.CID = agent.getCID();
				break;
			}
		}

		boolean haveStartTime = false;
		for (CombatItem c : combatList) {
			if (c.get_src_cid() == CID) {
				if (c.is_statechange().equals(StateChange.ENTER_COMBAT)) {
					agent = c.get_src_agent();
				} else if (c.is_statechange().equals(StateChange.CHANGE_DEAD)) {
					fightEnd = c.get_time();
					isKill = true;
				}
			} else if (!haveStartTime) {
				if (c.is_statechange().equals(StateChange.ENTER_COMBAT)) {
					fightStart = c.get_time();
					haveStartTime = true;
				}
			}
		}

		if (fightEnd == 0) {
			fightEnd = combatList.get(combatList.size() - 1).get_time();
			isKill = false;
		}
	}

	public String[] toStringArray() {
		String[] array = new String[8];
		array[0] = String.valueOf(agent);
		array[1] = String.valueOf(CID);
		array[2] = String.valueOf(name);
		array[3] = String.valueOf(HP);
		array[4] = String.valueOf(fightStart);
		array[5] = String.valueOf(fightEnd);
		array[6] = String.valueOf(isKill);
		array[7] = String.valueOf(buildVersion);
		return array;
	}

	// Getters
	public long getAgent() {
		return agent;
	}

	public int getCID() {
		return CID;
	}

	public String getName() {
		return name;
	}

	public int getHP() {
		return HP;
	}

	public int getFightStart() {
		return fightStart;
	}

	public int getFightEnd() {
		return fightEnd;
	}

	public boolean isKill() {
		return isKill;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	// Setters
	public void setAgent(int agent) {
		this.agent = agent;
	}

	public void setFightStart(int fightStart) {
		this.fightStart = fightStart;
	}

	public void setFightEnd(int fightEnd) {
		this.fightEnd = fightEnd;
	}

}
