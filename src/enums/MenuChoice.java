package enums;

public enum MenuChoice {

	// Constants
	DUMP_EVTC(0, false),
	FINAL_DPS(1, true),
	PHASE_DPS(2, true),
	DMG_DIST(3, true),
	G_TOTAL_DMG(4, false),
	MISC_STATS(5, true),
	FINAL_BOONS(6, true),
	PHASE_BOONS(7, true),
	DUMP_TABLES(8, false),
	QUIT(9, false);

	// Fields
	private int ID;
	private boolean canBeAssociated;

	// Constructor
	MenuChoice(int ID, boolean canBeAssociated) {
		this.ID = ID;
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

	public boolean canBeAssociated() {
		return canBeAssociated;
	}

}
