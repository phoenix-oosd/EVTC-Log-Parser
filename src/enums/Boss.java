package enums;

public enum Boss {

	// Constants
	VALE_GUARDIAN(15438, "Vale Guardian", 22400000),
	GORSEVAL_THE_MULTIFARIOUS(15429, "Gorseval the Multifarious", 21600000),
	SABETHA_THE_SABOTEUR(15375, "Sabetha the Saboteur", 34000000),
	SLOTHASOR(16123, "Slothasor", 19000000),
	BERG(16088, "Berg", 6900000),
	ZANE(16137, "Zane", 5900000),
	NARELLA(16125, "Narella", 4900000),
	MATTHIAS_GABREL(16115, "Matthias Gabrel", 25900000),
	KEEP_CONSTRUCT(16235, "Keep Construct", 55053600),
	XERA(16246, "Xera", 22611300),
	CAIRN_THE_INDOMITABLE(17194, "Cairn the Indomitable", 20000000),
	MURSAAT_OVERSEER(17172, "Mursaat Overseer", 20000000),
	SAMAROG(17188, "Samarog", 29840000),
	DEIMOS(17154, "Deimos", 32382000);

	// Fields
	private String name;
	private int species_id;
	private int health;

	// Constructor
	Boss(int instid, String name, int health) {
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
