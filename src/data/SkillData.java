package data;

import java.util.ArrayList;
import java.util.List;

public class SkillData {

	// Fields
	private List<SkillItem> skill_list;

	// Constructors
	public SkillData() {
		this.skill_list = new ArrayList<SkillItem>();
	}

	// Public Methods
	public void addItem(SkillItem item) {
		skill_list.add(item);
	}

	public String getName(int id) {
		for (SkillItem s : skill_list) {
			if (s.get_id() == id) {
				return s.get_name();
			}
		}
		return "id: " + String.valueOf(id);
	}

	// Getters
	public List<SkillItem> get_skill_list() {
		return skill_list;
	}

}
