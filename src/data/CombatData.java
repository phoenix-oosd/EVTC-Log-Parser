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

	public void fillMissingData(String name) {
		if (name.equals("Xera")) {
			for (CombatItem c : combatList) {
				if (c.get_src_cid() == 16286) {
					c.set_src_cid(16246);
				} else if (c.get_dst_cid() == 16286) {
					c.set_dst_cid(16246);
				}
			}
		}
	}

	// Getters
	public List<CombatItem> getCombatList() {
		return combatList;
	}

}
