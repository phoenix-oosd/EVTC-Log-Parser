package enums;

public enum State {

	// Constants
	NORMAL(0),
	ENTER(1),
	EXIT(2),
	UP(3),
	DIED(4),
	DOWNED(5),
	SPAWNED(6),
	DESPAWNED(7);

	// Fields
	private int ID;

	// Constructors
	State(int ID) {
		this.ID = ID;
	}

	// Public Methods
	public static State getEnum(int ID) {
		for (State s : values()) {
			if (s.getID() == ID) {
				return s;
			}
		}
		return null;
	}

	// Getters
	public int getID() {
		return ID;
	}

}
