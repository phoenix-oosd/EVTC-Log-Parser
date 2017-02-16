package data;

public class AgentItem {

	// Fields
	private long agent;
	private int instid = 0;
	// private long master_agent = 0;
	// private int master_instid = 0;
	private int first_aware = 0;
	private int last_aware = Integer.MAX_VALUE;
	private String name;
	private String prof;
	private int toughness = 0;
	private int healing = 0;
	private int condition = 0;

	// Constructors
	public AgentItem(long agent, String name, String prof) {
		this.agent = agent;
		this.name = name;
		this.prof = prof;
	}

	public AgentItem(long agent, String name, String prof, int toughness, int healing, int condition) {
		this.agent = agent;
		this.name = name;
		this.prof = prof;
		this.toughness = toughness;
		this.healing = healing;
		this.condition = condition;
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[9];
		array[0] = String.valueOf(agent);
		array[1] = String.valueOf(instid);
		// array[2] = String.valueOf(master_agent);
		// array[3] = String.valueOf(master_instid);
		array[2] = String.valueOf(first_aware);
		array[3] = String.valueOf(last_aware);
		array[4] = String.valueOf(name);
		array[5] = String.valueOf(prof);
		array[6] = String.valueOf(toughness);
		array[7] = String.valueOf(healing);
		array[8] = String.valueOf(condition);
		return array;
	}

	// Getters
	public long get_agent() {
		return agent;
	}

	public int get_instid() {
		return instid;
	}

	// public long get_master_agent() {
	// return master_agent;
	// }
	//
	// public int get_master_instid() {
	// return master_instid;
	// }

	public int get_first_aware() {
		return first_aware;
	}

	public int get_last_aware() {
		return last_aware;
	}

	public String get_name() {
		return name;
	}

	public String get_prof() {
		return prof;
	}

	public int get_toughness() {
		return toughness;
	}

	public int get_healing() {
		return healing;
	}

	public int get_condition() {
		return condition;
	}

	// Setters
	public void setInstid(int instid) {
		this.instid = instid;
	}

	// public void setMasterAgent(long agent) {
	// this.master_agent = agent;
	// }
	//
	// public void setMasterInstid(int instid) {
	// this.master_instid = instid;
	// }

	public void setFirstAware(int first_aware) {
		this.first_aware = first_aware;
	}

	public void setLastAware(int last_aware) {
		this.last_aware = last_aware;
	}

}
