package data;

import enums.Boss;

public class BossData {

	// Fields
	private int agent = 0;
	private int instid;
	private int fight_aware = 0;
	private int last_aware = Integer.MAX_VALUE;
	private String name;
	private int health;
	private String build_version;

	// Constructors
	public BossData(Boss boss, String build_version) {
		this.instid = boss.getInstid();
		this.name = boss.getName();
		this.health = boss.getHealth();
		this.build_version = build_version;
	}

	public BossData(int instid, String build_version) {
		this.instid = instid;
		this.name = "UNKNOWN";
		this.health = -1;
		this.build_version = build_version;
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[7];
		array[0] = String.valueOf(agent);
		array[1] = String.valueOf(instid);
		array[2] = String.valueOf(fight_aware);
		array[3] = String.valueOf(last_aware);
		array[4] = String.valueOf(name);
		array[5] = String.valueOf(health);
		array[6] = String.valueOf(build_version);
		return array;
	}

	// Getters
	public int get_agent() {
		return agent;
	}

	public int get_instid() {
		return instid;
	}

	public int get_first_aware() {
		return fight_aware;
	}

	public int get_last_aware() {
		return last_aware;
	}

	public String get_name() {
		return name;
	}

	public int get_health() {
		return health;
	}

	public String get_build_version() {
		return build_version;
	}

	// Setters
	public void set_agent(int agent) {
		this.agent = agent;
	}

	public void set_instid(int instid) {
		this.instid = instid;
	}

	public void set_first_aware(int time) {
		this.fight_aware = time;
	}

	public void set_last_aware(int time) {
		this.last_aware = time;
	}

}
