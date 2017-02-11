package enums;

public enum MenuChoice {

	// Constants
	DUMP_EVTC(0, "text", false),
	FINAL_DPS(1, "damage", true),
	PHASE_DPS(2, "damage", true),
	DMG_DIST(3, "damage", true),
	G_TOTAL_DMG(4, "damage", false),
	MISC_STATS(5, "damage", true),
	FINAL_BOONS(6, "boons", true),
	PHASE_BOONS(7, "boons", true),
	DUMP_TABLES(8, "text", false),
	QUIT(9, "none", false);

	// Fields
	private int ID;
	private String type;
	private boolean canBeAssociated;

	// Constructor
	MenuChoice(int ID, String type, boolean canBeAssociated) {
		this.ID = ID;
		this.type = type;
		this.canBeAssociated = canBeAssociated;
	}

	// Public Methods
	public static MenuChoice getEnum(int ID) {
		for (MenuChoice c : values()) {
			if (c.getID() == ID) {
				return c;
			}
		}
		return null;
	}

	// Getters
	public int getID() {
		return ID;
	}

	public String getType() {
		return type;
	}

	public boolean canBeAssociated() {
		return canBeAssociated;
	}

}
