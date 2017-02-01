package enums;

public enum Boss {

	// Constants
	VALE_GUARDIAN(15438, 22400000, "Vale Guardian"),
	GORSEVAL(15429, 21600000, "Gorseval"),
	SABETHA(15375, 34000000, "Sabetha"),
	SLOTHASOR(16123, 19000000, "Slothasor"),
	BERG(16088, 6900000, "Berg"),
	ZANE(16137, 5900000, "Zane"),
	NARELLA(16125, 4900000, "Narella"),
	MATTHIAS(16115, 25900000, "Matthias"),
	KEEP_CONSTRUCT(16235, 55053600, "Keep Construct"),
	XERA(16246, 22611300, "Xera");

	// Fields
	private int ID;
	private int HP;
	private String name;

	// Constructor
	Boss(int ID, int HP, String name) {
		this.ID = ID;
		this.HP = HP;
		this.name = name;
	}

	// Public Methods
	public static Boss getBoss(int ID) {
		for (Boss b : values()) {
			if (b.getID() == ID) {
				return b;
			}
		}
		return null;
	}

	// Getters
	public int getID() {
		return this.ID;
	}

	public int getHP() {
		return this.HP;
	}

	public String getName() {
		return this.name;
	}

}
