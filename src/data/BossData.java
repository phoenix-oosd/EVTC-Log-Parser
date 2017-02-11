package data;

public class BossData {

	// // Update boss agent
	// for (CombatItem c : combatData.getCombatData()) {
	// if (c.get_src_cid() == bossData.getCID()) {
	// bossData.setAgent(c.get_src_agent());
	// break;
	// }
	// }
	//
	// // Update boss fight duration
	// bossData.setFightDuration(combatData.get(combatData.size() -
	// 1).get_time() - combatData.get(0).get_time());
	//
	// // Update player CIDs
	// for (AgentItem p : agentData) {
	// for (CombatItem c : combatData) {
	// if (p.getAgent() == c.get_src_agent()) {
	// if (c.get_src_master_cid() == 0) {
	// p.setCID(c.get_src_cid());
	// } else {
	// p.setCID(c.get_src_master_cid());
	// }
	// break;
	// }
	// }
	// }
	//
	// // Delete players with no CID
	// Iterator<AgentItem> iter = agentData.iterator();
	// while (iter.hasNext()) {
	// AgentItem p = iter.next();
	// if (p.getCID() == 0) {
	// iter.remove();
	// }
	// }
	//
	// // Update combat for Xera logs
	// if (bossData.getName().equals("Xera")) {
	// long xera_50 = 16286;
	// for (CombatItem c : combatData) {
	// if (c.get_src_cid() == xera_50) {
	// c.set_src_agent(bossData.getAgent());
	// c.set_src_cid(bossData.getCID());
	// } else if (c.get_dst_cid() == xera_50) {
	// c.set_dst_agent(bossData.getAgent());
	// c.set_dst_cid(bossData.getCID());
	// }
	// }
	// }

	// Fields
	private int agent;
	private int CID;
	private String name;
	private int HP;
	private int fightStart;
	private int fightEnd;
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
	public String[] toStringArray() {
		String[] array = new String[7];
		array[0] = String.valueOf(agent);
		array[1] = String.valueOf(CID);
		array[2] = String.valueOf(name);
		array[3] = String.valueOf(HP);
		array[4] = String.valueOf(fightStart);
		array[5] = String.valueOf(fightEnd);
		array[6] = String.valueOf(buildVersion);
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
