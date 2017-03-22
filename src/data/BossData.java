package data;

public class BossData
{

	// Fields
	private long agent = 0;
	private int instid = 0;
	private int first_aware = 0;
	private int last_aware = Integer.MAX_VALUE;
	private int id;
	private String name = "UNKNOWN";
	private int health = -1;

	// Constructors
	public BossData(int id)
	{
		this.id = id;
	}

	// Public Methods
	public String[] getPhaseNames()
	{
		if (name.equals("Vale Guardian"))
		{
			return new String[] { "100% - 66%", "66% - 33%", "33% - 0%" };
		}
		else if (name.equals("Gorseval the Multifarious"))
		{
			return new String[] { "100% - 66%", "66% - 33%", "33% - 0%" };
		}
		else if (name.equals("Sabetha the Saboteur"))
		{
			return new String[] { "100% - 75%", "75% - 50%", "50% - 25%", "25% - 0%" };
		}
		else if (name.equals("Slothasor"))
		{
			return new String[] { "100% - 80%", "80% - 60%", "60% - 40%", "40% - 20%", "20% - 10%", "10% - 0%" };
		}
		else if (name.equals("Matthias Gabrel"))
		{
			return new String[] { "100% - 80%", "80% - 60%", "60% - 40%", "40% - 0%" };
		}
		else if (name.equals("Keep Construct"))
		{
			return new String[] { "100% - 66%", "66% - 33%", "33% - 0%" };
		}
		else if (name.equals("Xera"))
		{
			return new String[] { "100% - 50%", "50% - 0%" };
		}
		else if (name.equals("Samarog"))
		{
			return new String[] { "100% - 66%", "66% - 33%", "33% - 0%" };
		}
		else if (name.equals("Deimos"))
		{
			return new String[] { "100% - 75%", "75% - 50%", "50% - 25%", "25% - 10%" };
		}
		return new String[] { "100% - 0%" };
	}

	public String[] toStringArray()
	{
		String[] array = new String[7];
		array[0] = Long.toHexString(agent);
		array[1] = String.valueOf(instid);
		array[2] = String.valueOf(first_aware);
		array[3] = String.valueOf(last_aware);
		array[4] = String.valueOf(id);
		array[5] = name;
		array[6] = String.valueOf(health);
		return array;
	}

	// Getters
	public long getAgent()
	{
		return agent;
	}

	public int getInstid()
	{
		return instid;
	}

	public int getFirstAware()
	{
		return first_aware;
	}

	public int getLastAware()
	{
		return last_aware;
	}

	public int getID()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getHealth()
	{
		return health;
	}

	// Setters
	public void setAgent(long agent)
	{
		this.agent = agent;
	}

	public void setInstid(int instid)
	{
		this.instid = instid;
	}

	public void setFirstAware(int first_aware)
	{
		this.first_aware = first_aware;
	}

	public void setLastAware(int last_aware)
	{
		this.last_aware = last_aware;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

}
