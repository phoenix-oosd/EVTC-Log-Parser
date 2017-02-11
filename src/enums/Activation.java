package enums;

public enum Activation {

	// Constants
	NONE(0),
	NORMAL(1),
	QUICKNESS(2),
	CANCEL_FIRE(3),
	CANCEL_CANCEL(4);

	// Fields
	private int ID;

	// Constructors
	private Activation(int ID) {
		this.ID = ID;
	}

	// Public Methods
	public static Activation getEnum(int ID) {
		for (Activation a : values()) {
			if (a.getID() == ID) {
				return a;
			}
		}
		return null;
	}

	// Getters
	public int getID() {
		return ID;
	}

}
