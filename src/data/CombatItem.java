package data;

import enums.Activation;
import enums.BuffRemove;
import enums.IFF;
import enums.Result;
import enums.StateChange;

public class CombatItem
{

	// Fields
	private int time;
	private long src_agent;
	private long dst_agent;
	private int value;
	private int buff_dmg;
	private int overstack_value;
	private int skill_id;
	private int src_instid;
	private int dst_instid;
	private int src_master_instid;
	private IFF iff;
	private int is_buff;
	private Result result;
	private Activation is_activation;
	private BuffRemove is_buffremove;
	private int is_ninety;
	private int is_fifty;
	private int is_moving;
	private StateChange is_statechange;
	private int is_flanking;

	// Constructor
	public CombatItem(int time, long src_agent, long dst_agent, int value, int buff_dmg, int overstack_value,
			int skill_id, int src_instid, int dst_instid, int src_master_instid, IFF iff, int buff, Result result,
			Activation is_activation, BuffRemove is_buffremove, int is_ninety, int is_fifty, int is_moving,
			StateChange is_statechange, int is_flanking)
	{
		this.time = time;
		this.src_agent = src_agent;
		this.dst_agent = dst_agent;
		this.value = value;
		this.buff_dmg = buff_dmg;
		this.overstack_value = overstack_value;
		this.skill_id = skill_id;
		this.src_instid = src_instid;
		this.dst_instid = dst_instid;
		this.src_master_instid = src_master_instid;
		this.iff = iff;
		this.is_buff = buff;
		this.result = result;
		this.is_activation = is_activation;
		this.is_buffremove = is_buffremove;
		this.is_ninety = is_ninety;
		this.is_fifty = is_fifty;
		this.is_moving = is_moving;
		this.is_statechange = is_statechange;
		this.is_flanking = is_flanking;
	}

	// Public Methods
	public String[] toStringArray()
	{
		String[] array = new String[20];
		array[0] = String.valueOf(time);
		array[1] = Long.toHexString(src_agent);
		array[2] = Long.toHexString(dst_agent);
		array[3] = String.valueOf(value);
		array[4] = String.valueOf(buff_dmg);
		array[5] = String.valueOf(overstack_value);
		array[6] = String.valueOf(skill_id);
		array[7] = String.valueOf(src_instid);
		array[8] = String.valueOf(dst_instid);
		array[9] = String.valueOf(src_master_instid);
		array[10] = String.valueOf(iff);
		array[11] = String.valueOf(is_buff);
		array[12] = String.valueOf(result);
		array[13] = String.valueOf(is_activation);
		array[14] = String.valueOf(is_buffremove);
		array[15] = String.valueOf(is_ninety);
		array[16] = String.valueOf(is_fifty);
		array[17] = String.valueOf(is_moving);
		array[18] = String.valueOf(is_statechange);
		array[19] = String.valueOf(is_flanking);
		return array;
	}

	// Getters
	public int getTime()
	{
		return time;
	}

	public long getSrcAgent()
	{
		return src_agent;
	}

	public long getDstAgent()
	{
		return dst_agent;
	}

	public int getValue()
	{
		return value;
	}

	public int getBuffDmg()
	{
		return buff_dmg;
	}

	public int getOverstackValue()
	{
		return overstack_value;
	}

	public int getSkillID()
	{
		return skill_id;
	}

	public int getSrcInstid()
	{
		return src_instid;
	}

	public int getDstInstid()
	{
		return dst_instid;
	}

	public int getSrcMasterInstid()
	{
		return src_master_instid;
	}

	public IFF getIFF()
	{
		return iff;
	}

	public int isBuff()
	{
		return is_buff;
	}

	public Result getResult()
	{
		return result;
	}

	public Activation isActivation()
	{
		return is_activation;
	}

	public BuffRemove isBuffremove()
	{
		return is_buffremove;
	}

	public int isNinety()
	{
		return is_ninety;
	}

	public int isFifty()
	{
		return is_fifty;
	}

	public int isMoving()
	{
		return is_moving;
	}

	public int isFlanking()
	{
		return is_flanking;
	}

	public StateChange isStateChange()
	{
		return is_statechange;
	}

	// Setters
	public void setSrcAgent(long src_agent)
	{
		this.src_agent = src_agent;
	}

	public void setDstAgent(long dst_agent)
	{
		this.dst_agent = dst_agent;
	}

	public void setSrcInstid(int src_instid)
	{
		this.src_instid = src_instid;
	}

	public void setDstInstid(int dst_instid)
	{
		this.dst_instid = dst_instid;
	}

}