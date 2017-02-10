package enums;

public enum Result {

	// Constants
	NORMAL(0),
	CRIT(1),
	GLANCE(2),
	BLOCK(3),
	EVADE(4),
	INTERRUPT(5),
	ABSORB(6),
	BLIND(7),
	KILLING_BLOW(8);

	// Fields
	private int ID;

	// Constructors
	private Result(int ID) {
		this.ID = ID;
	}

	// Public Methods
	public static Result getEnum(int ID) {
		for (Result r : values()) {
			if (r.getID() == ID) {
				return r;
			}
		}
		return null;
	}

	// Getters
	public int getID() {
		return ID;
	}

}
