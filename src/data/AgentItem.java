package data;

public class AgentItem {

	// Fields
	private int firstAppeared;
	private int agent;
	private int instid;
	private String name;
	private String prof;
	private int toughness;
	private int healing;
	private int condition;
	private boolean isSet;

	// Constructor
	public AgentItem(int agent, int instid, String name, String prof, int toughness, int healing, int condition) {
		this.agent = agent;
		this.instid = instid;
		this.name = name;
		this.prof = prof;
		this.toughness = toughness;
		this.healing = healing;
		this.condition = condition;
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[8];
		array[0] = String.valueOf(firstAppeared);
		array[1] = String.valueOf(agent);
		array[2] = String.valueOf(instid);
		array[3] = String.valueOf(name);
		array[4] = String.valueOf(prof);
		array[5] = String.valueOf(toughness);
		array[6] = String.valueOf(healing);
		array[7] = String.valueOf(condition);
		return array;
	}

	// Getters
	public long getAgent() {
		return agent;
	}

	public int get_instid() {
		return instid;
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

	public boolean isSet() {
		return isSet;
	}

	// Setters
	public void setFirstAppeared(int firstAppeared) {
		this.firstAppeared = firstAppeared;
	}

	public void setinstid(int instid) {
		this.isSet = true;
		this.instid = instid;
	}

}
