package enums;

public enum StateChange {

	// Constants
	NORMAL(0),
	ENTER_COMBAT(1),
	EXIT_COMBAT(2),
	CHANGE_UP(3),
	CHANGE_DEAD(4),
	CHANGE_DOWN(5),
	SPAWN(6),
	DESPAWN(7),
	HEALTH_UPDATE(8),
	LOG_START(9),
	LOG_END(10);

	// Fields
	private int ID;

	// Constructors
	StateChange(int ID) {
		this.ID = ID;
	}

	// Public Methods
	public static StateChange getEnum(int ID) {
		for (StateChange s : values()) {
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
