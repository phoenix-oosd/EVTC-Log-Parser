package enums;

public enum Boss {

	// Constants
	VALE_GUARDIAN("Vale Guardian", 15438, 22400000),
	GORSEVAL_THE_MULTIFARIOUS("Gorseval the Multifarious", 15429, 21600000),
	SABETHA_THE_SABOTEUR("Sabetha the Saboteur", 15375, 34000000),
	SLOTHASOR("Slothasor", 16123, 19000000),
	BERG("Berg", 16088, 6900000),
	ZANE("Zane", 16137, 5900000),
	NARELLA("Narella", 16125, 4900000),
	MATTHIAS_GABREL("Matthias Gabrel", 16115, 25900000),
	KEEP_CONSTRUCT("Keep Construct", 16235, 55053600),
	XERA("Xera", 16246, 22611300),
	CAIRN_THE_INDOMITABLE("Cairn the Indomitable", 17194, 20000000),
	MURSAAT_OVERSEER("Mursaat Overseer", 17172, 20000000),
	SAMAROG("Samarog", 17188, 29840000),
	DEIMOS("Deimos", 17154, 32382000);

	// Fields
	private String name;
	private int species_id;
	private int health;

	// Constructor
	Boss(String name, int instid, int health) {
		this.name = name;
		this.species_id = instid;
		this.health = health;
	}

	// Public Methods
	public static Boss getEnum(int instid) {
		for (Boss b : values()) {
			if (b.getSpeciesID() == instid) {
				return b;
			}
		}
		return null;
	}

	// Getters
	public String getName() {
		return this.name;
	}

	public int getSpeciesID() {
		return this.species_id;
	}

	public int getHealth() {
		return this.health;
	}

}
