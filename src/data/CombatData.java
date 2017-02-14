package data;

import java.util.ArrayList;
import java.util.List;

public class CombatData {

	// Fields
	private List<CombatItem> combatList;

	// Constructors
	public CombatData() {
		this.combatList = new ArrayList<CombatItem>();
	}

	// Public Methods
	public void addItem(CombatItem item) {
		combatList.add(item);
	}

	public void fillMissingData(BossData bossData, List<AgentItem> NPCAgentList) {

		if (bossData.getName().equals("Xera")) {
			for (CombatItem c : combatList) {
				if (c.get_src_cid() == 16286) {
					c.set_src_cid(16246);
				} else if (c.get_dst_cid() == 16286) {
					c.set_dst_cid(16246);
				}
			}
		}

		// if (bossData.getName().equals("Xera")) {
		// int boss_id = bossData.getCID();
		// List<Integer> other_ids = new ArrayList<Integer>();
		// for (AgentItem NPC : NPCAgentList) {
		// if (NPC.getName().equals("Xera")) {
		// other_ids.add(NPC.getCID());
		// }
		// }
		// for (CombatItem c : combatList) {
		// if (other_ids.contains(c.get_src_cid())) {
		// c.set_src_agent(c.get_src_agent());
		// c.set_src_cid(boss_id);
		// } else if (other_ids.contains(c.get_dst_cid())) {
		// c.set_dst_agent(c.get_dst_agent());
		// c.set_dst_cid(boss_id);
		// }
		// }
		// }
	}

	// Getters
	public List<CombatItem> getCombatList() {
		return combatList;
	}

}
