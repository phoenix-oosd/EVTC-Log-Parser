package enums;

public enum Choice {

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
	QUIT(9, "", false);

	// Fields
	private int value;
	private String type;
	private boolean can_be_associated;

	// Constructor
	Choice(int value, String type, boolean can_be_associated) {
		this.value = value;
		this.type = type;
		this.can_be_associated = can_be_associated;
	}

	// Public Methods
	public static Choice getChoice(int value) {
		for (Choice c : values()) {
			if (c.getValue() == value) {
				return c;
			}
		}
		return null;
	}

	// Getters
	public int getValue() {
		return value;
	}

	public String getType() {
		return type;
	}

	public boolean canBeAssociated() {
		return can_be_associated;
	}

}
