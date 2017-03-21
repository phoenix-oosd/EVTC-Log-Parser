package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.AgentItem;
import data.BossData;
import data.CombatItem;
import data.SkillData;
import enums.Boon;
import enums.IFF;
import enums.StateChange;
import statistics.Statistics;

public class Player
{
	// Fields
	private int instid;
	private String account;
	private String character;
	private String group;
	private String prof;
	private int toughness;
	private int healing;
	private int condition;
	private List<DamageLog> damage_logs = new ArrayList<DamageLog>();
	private Map<String, List<BoonLog>> boon_map = new HashMap<>();

	// Constructors
	public Player(AgentItem agent)
	{
		this.instid = agent.getInstid();
		String[] name = agent.getName().split(Character.toString('\0'));
		this.character = name[0];
		this.account = name[1];
		this.group = name[2];
		if (Statistics.hiding_players)
		{
			this.character = "P:" + String.format("%04d", instid);
			this.account = ":A." + String.format("%04d", instid);
		}
		this.prof = agent.getProf();
		this.toughness = agent.getToughness();
		this.healing = agent.getHealing();
		this.condition = agent.getCondition();
	}

	// Getters
	public int getInstid()
	{
		return instid;
	}

	public String getAccount()
	{
		return account;
	}

	public String getCharacter()
	{
		return character;
	}

	public String getGroup()
	{
		return group;
	}

	public String getProf()
	{
		return prof;
	}

	public int getToughness()
	{
		return toughness;
	}

	public int getHealing()
	{
		return healing;
	}

	public int getCondition()
	{
		return condition;
	}

	public List<DamageLog> getDamageLogs(BossData bossData, List<CombatItem> combatList)
	{
		if (damage_logs.isEmpty())
		{
			setDamageLogs(bossData, combatList);
		}
		return damage_logs;
	}

	public Map<String, List<BoonLog>> getBoonMap(BossData bossData, SkillData skillData, List<CombatItem> combatList)
	{
		if (boon_map.isEmpty())
		{
			setBoonMap(bossData, skillData, combatList);
		}
		return boon_map;
	}

	// Private Methods
	private void setDamageLogs(BossData bossData, List<CombatItem> combatList)
	{
		int time_start = bossData.getFirstAware();
		for (CombatItem c : combatList)
		{
			if (instid == c.getSrcInstid() || instid == c.getSrcMasterInstid())
			{
				StateChange state = c.isStateChange();
				int time = c.getTime() - time_start;
				if (bossData.getInstid() == c.getDstInstid() && c.getIFF().equals(IFF.FOE))
				{
					if (state.equals(StateChange.NORMAL))
					{
						if (c.isBuff() == 1 && c.getBuffDmg() != 0)
						{
							damage_logs.add(new DamageLog(time, c.getBuffDmg(), c.getSkillID(), c.isBuff(),
									c.getResult(), c.isNinety(), c.isMoving(), c.isFlanking()));
						}
						else if (c.isBuff() == 0 && c.getValue() != 0)
						{
							damage_logs.add(new DamageLog(time, c.getValue(), c.getSkillID(), c.isBuff(),
									c.getResult(), c.isNinety(), c.isMoving(), c.isFlanking()));
						}
					}
				}
			}
		}
	}

	public void setBoonMap(BossData bossData, SkillData skillData, List<CombatItem> combatList)
	{

		// Initialize Boon Map with every Boon
		for (Boon boon : Boon.values())
		{
			boon_map.put(boon.getName(), new ArrayList<BoonLog>());
		}

		// Fill in Boon Map
		int time_start = bossData.getFirstAware();
		int fight_duration = bossData.getLastAware() - time_start;
		for (CombatItem c : combatList)
		{
			if (instid == c.getDstInstid())
			{
				String skill_name = skillData.getName(c.getSkillID());
				if (c.isBuff() == 1 && c.getValue() > 0)
				{
					if (boon_map.containsKey(skill_name))
					{
						int time = c.getTime() - time_start;
						if (time < fight_duration)
						{
							boon_map.get(skill_name).add(new BoonLog(time, c.getValue()));
						}
						else
						{
							break;
						}
					}
				}
			}
		}

	}

}
