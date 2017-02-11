package data;

public class AgentItem {

	// Fields
	private long agent;
	private int CID;
	private String name;
	private String prof;
	private int toughness;
	private int healing;
	private int condition;
	// private List<damageLog> damage_logs = new ArrayList<damageLog>();
	// private Map<String, List<boonLog>> boon_logs = new HashMap<>();

	// Constructor
	public AgentItem(long agent, int CID, String name, String prof, int toughness, int healing, int condition) {
		this.agent = agent;
		this.CID = CID;
		this.name = name;
		this.prof = prof;
		this.toughness = toughness;
		this.healing = healing;
		this.condition = condition;
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[7];
		array[0] = String.valueOf(agent);
		array[1] = String.valueOf(CID);
		array[2] = String.valueOf(name);
		array[3] = String.valueOf(prof);
		array[4] = String.valueOf(toughness);
		array[5] = String.valueOf(healing);
		array[6] = String.valueOf(condition);
		return array;
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

	// public List<damageLog> get_damage_logs() {
	// return damage_logs;
	// }
	//
	// public Map<String, List<boonLog>> get_boon_logs() {
	// return boon_logs;
	// }

	// Setters
	public void setCID(int CID) {
		this.CID = CID;
	}

	// public void setBoons(String[] boonArray) {
	// for (String boon : boonArray) {
	// boon_logs.put(boon, new ArrayList<boonLog>());
	// }
	// }

}
