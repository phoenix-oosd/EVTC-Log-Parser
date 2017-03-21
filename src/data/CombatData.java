package data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import enums.StateChange;

public class CombatData
{

	// Fields
	private List<CombatItem> combat_list;

	// Constructors
	public CombatData()
	{
		this.combat_list = new ArrayList<CombatItem>();
	}

	// Public Methods
	public void addItem(CombatItem item)
	{
		combat_list.add(item);
	}

	public List<Point> getStates(int src_instid, StateChange change)
	{
		List<Point> states = new ArrayList<Point>();
		for (CombatItem c : combat_list)
		{
			if (c.getSrcInstid() == src_instid && c.isStateChange().equals(change))
			{
				states.add(new Point(c.getTime(), (int) c.getDstAgent()));
			}
		}
		return states;
	}

	public int getSkillCount(int src_instid, int skill_id)
	{
		int count = 0;
		for (CombatItem c : combat_list)
		{
			if (c.getSrcInstid() == src_instid && c.getSkillID() == skill_id)
			{
				count++;
			}
		}
		return count;
	}

	// Getters
	public List<CombatItem> getCombatList()
	{
		return combat_list;
	}

}
