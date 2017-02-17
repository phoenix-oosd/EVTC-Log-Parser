package data;

import enums.Boss;

public class BossData {

	// Fields
	private long agent = 0;
	private int instid = 0;
	private int first_aware = 0;
	private int last_aware = Integer.MAX_VALUE;
	private int species_id;
	private String name;
	private int health;
	private String build_version;

	// Constructors
	public BossData(Boss boss, String build_version) {
		this.species_id = boss.getSpeciesID();
		this.name = boss.getName();
		this.health = boss.getHealth();
		this.build_version = build_version;
	}

	public BossData(int species_id, String build_version) {
		this.species_id = species_id;
		this.name = "UNKNOWN";
		this.health = -1;
		this.build_version = build_version;
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[8];
		array[0] = String.valueOf(agent);
		array[1] = String.valueOf(instid);
		array[2] = String.valueOf(first_aware);
		array[3] = String.valueOf(last_aware);
		array[4] = String.valueOf(species_id);
		array[5] = String.valueOf(name);
		array[6] = String.valueOf(health);
		array[7] = String.valueOf(build_version);
		return array;
	}

	// Getters
	public long getAgent() {
		return agent;
	}

	public int getInstid() {
		return instid;
	}

	public int getFirstAware() {
		return first_aware;
	}

	public int getLastAware() {
		return last_aware;
	}

	public int getSpeciesID() {
		return species_id;
	}

	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}

	public String getBuildVersion() {
		return build_version;
	}

	// Setters
	public void setAgent(long agent) {
		this.agent = agent;
	}

	public void setInstid(int instid) {
		this.instid = instid;
	}

	public void setFirstAware(int first_aware) {
		this.first_aware = first_aware;
	}

	public void setLastAware(int last_aware) {
		this.last_aware = last_aware;
	}

}
