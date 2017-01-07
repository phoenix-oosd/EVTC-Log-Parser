import java.util.ArrayList;
import java.util.List;

public class playerData {
	
	private int agent = 0;
	private int CID = 0;
	private String name = null;
	private String prof = null;
	private int toughness = 0;
	private int healing = 0;
	private int condition = 0;
	private List<damageLog> damage_logs = new ArrayList<damageLog>();;
	
	// Constructor
	public playerData(int agent, int CID, String name, String prof, int toughness, int healing, int condition) {
		this.agent = agent;
		this.CID = CID;
		this.name = name;
		this.prof = prof;
		this.toughness = toughness;
		this.healing = healing;
		this.condition = condition;
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


	public String getProf() {
		return prof;
	}


	public int getToughness() {
		return toughness;
	}


	public int getHealing() {
		return healing;
	}


	public int getCondition() {
		return condition;
	}
	
	public List<damageLog> get_damage_logs() {
		return damage_logs;
	}
	
	// Setters
	public void setCID(int CID) {
		this.CID = CID;
	}

}
