package data;

public class bossData {

	// Fields
	private long agent = 0;
	private int CID = 0;
	private String name = null;
	private int HP = 0;
	private long fight_duration = 0;
	private long date = 0;

	// Constructor
	public bossData(int agent, int CID, String name, int HP, long fight_duration, long date) {
		this.agent = agent;
		this.CID = CID;
		this.name = name;
		this.HP = HP;
		this.fight_duration = fight_duration;
		this.date = date;

	}

	// Getters
	public long getAgent() {
		return agent;
	}

	public int getCID() {
		return CID;
	}

	public String getName() {
		return name;
	}

	public int getHP() {
		return HP;
	}

	public int getFightDuration() {
		return (int) fight_duration;
	}

	public long getDate() {
		return date;
	}

	// Setters
	public void setAgent(long agent) {
		this.agent = agent;
	}

	public void setFightDuration(long fight_duration) {
		this.fight_duration = fight_duration;
	}

}
