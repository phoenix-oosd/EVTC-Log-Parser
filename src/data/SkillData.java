package data;

import java.util.ArrayList;
import java.util.List;

public class SkillData {

	// Fields
	private List<SkillItem> skillData;

	// Constructors
	public SkillData() {
		this.skillData = new ArrayList<SkillItem>();
	}

	// Public Methods
	public void addItem(SkillItem item) {
		skillData.add(item);
	}

	public String getName(int ID) {
		for (SkillItem s : skillData) {
			if (s.getID() == ID) {
				return s.getName();
			}
		}
		return "id: " + String.valueOf(ID);
	}

	// Getters
	public List<SkillItem> getSkillData() {
		return skillData;
	}

}
