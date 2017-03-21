package data;

import java.util.ArrayList;
import java.util.List;

import enums.CustomSkill;

public class SkillData
{

	// Fields
	private List<SkillItem> skill_list;

	// Constructors
	public SkillData()
	{
		this.skill_list = new ArrayList<SkillItem>();
	}

	// Public Methods
	public void addItem(SkillItem item)
	{
		skill_list.add(item);
	}

	public String getName(int ID)
	{

		// Custom
		CustomSkill custom_skill = CustomSkill.getEnum(ID);
		if (custom_skill != null)
		{
			return custom_skill.name();
		}

		// Normal
		for (SkillItem s : skill_list)
		{
			if (s.getID() == ID)
			{
				return s.getName();
			}
		}

		// Unknown
		return "uid: " + String.valueOf(ID);
	}

	// Getters
	public List<SkillItem> getSkillList()
	{
		return skill_list;
	}

}
