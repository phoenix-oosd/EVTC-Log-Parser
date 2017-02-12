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

}
