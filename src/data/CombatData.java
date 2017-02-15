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

	// Getters
	public List<CombatItem> getCombatList() {
		return combatList;
	}

}
