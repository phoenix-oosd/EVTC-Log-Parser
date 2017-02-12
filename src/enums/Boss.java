package enums;

public enum Boss {

	// Constants
	VALE_GUARDIAN("Vale Guardian", 15438, 22400000),
	GORSEVAL("Gorseval", 15429, 21600000),
	SABETHA("Sabetha", 15375, 34000000),
	SLOTHASOR("Slothasor", 16123, 19000000),
	BERG("Berg", 16088, 6900000),
	ZANE("Zane", 16137, 5900000),
	NARELLA("Narella", 16125, 4900000),
	MATTHIAS("Matthias", 16115, 25900000),
	KEEP_CONSTRUCT("Keep Construct", 16235, 55053600),
	XERA("Xera", 16246, 22611300),
	CAIRN_THE_INDOMITABLE("Cairn the Indomitable", 17194, 20000000),
	MURSAAT_OVERSEER("Mursaat Overseer", 17172, 20000000),
	SAMAROG("Samarog", 17188, 29640000),
	DEIMOS("Deimos", 17154, 36000000);

	// Fields
	private String name;
	private int ID;
	private int HP;

	// Constructor
	Boss(String name, int ID, int HP) {
		this.name = name;
		this.ID = ID;
		this.HP = HP;
	}

	// Public Methods
	public static Boss getEnum(int ID) {
		for (Boss b : values()) {
			if (b.getID() == ID) {
				return b;
			}
		}
		return null;
	}

	// Getters
	public String getName() {
		return this.name;
	}

	public int getID() {
		return this.ID;
	}

	public int getHP() {
		return this.HP;
	}

}
