package data;

import java.util.ArrayList;
import java.util.List;

public class SkillData {

	// Fields
	private List<SkillItem> skillList;

	// Constructors
	public SkillData() {
		this.skillList = new ArrayList<SkillItem>();
	}

	// Public Methods
	public void addItem(SkillItem item) {
		skillList.add(item);
	}

	public String getName(int ID) {
		for (SkillItem s : skillList) {
			if (s.getID() == ID) {
				return s.getName();
			}
		}
		if (ID == 65001) {
			return "Dodge";
		}
		return "id: " + String.valueOf(ID);
	}

	// Getters
	public List<SkillItem> getSkillList() {
		return skillList;
	}

}
