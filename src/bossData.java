

public class bossData {
	
	private int agent = 0;
	private int CID = 0;
	private String name = null;
	private int HP = 0;
	private int fight_duration = 0;
	private String date = null;
	
	public bossData(int agent, int CID, String name, int HP, int fight_duration, String date){
		this.agent = agent;
		this.CID = CID;
		this.name = name;
		this.HP = HP;
		this.fight_duration = fight_duration;
		this.date = date;
		
	}
	
	// Getters
	
	public int getAgent() {
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
		return fight_duration;
	}

	public String getDate() {
		return date;
	}
	
	// Setters
	
	public void setAgent(int agent) {
		this.agent = agent;
	}

	public void setFightDuration(int fight_duration) {
		this.fight_duration = fight_duration;
	}
	
}
