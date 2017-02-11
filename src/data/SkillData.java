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

	// Getters
	public List<SkillItem> getSkillData() {
		return skillData;
	}

}
