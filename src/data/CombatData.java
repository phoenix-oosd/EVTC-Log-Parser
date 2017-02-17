package data;

import java.util.ArrayList;
import java.util.List;

public class CombatData {

	// Fields
	private List<CombatItem> combat_list;

	// Constructors
	public CombatData() {
		this.combat_list = new ArrayList<CombatItem>();
	}

	// Public Methods
	public void addItem(CombatItem item) {
		combat_list.add(item);
	}

	// Getters
	public List<CombatItem> getCombatList() {
		return combat_list;
	}

}
