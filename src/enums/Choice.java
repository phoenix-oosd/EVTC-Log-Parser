package enums;

public enum Choice {

	// Constants
	DUMP_EVTC(0, "text"),
	FINAL_DPS(1, "damage"),
	PHASE_DPS(2, "damage"),
	DMG_DIST(3, "damage"),
	G_TOTAL_DMG(4, "damage"),
	MISC_STATS(5, "damage"),
	FINAL_BOONS(6, "boons"),
	PHASE_BOONS(7, "boons"),
	DUMP_TABLES(8, "text"),
	QUIT(9, "");

	// Fields
	private int value;
	private String type;

	// Constructor
	Choice(int value, String type) {
		this.value = value;
		this.type = type;
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

}
