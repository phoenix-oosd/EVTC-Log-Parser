package enums;

public enum IFF {

	// Constants
	FRIEND(0),
	FOE(1),
	UNKNOWN(2);

	// Fields
	private int ID;

	// Constructors
	private IFF(int ID) {
		this.ID = ID;
	}

	// Public Methods
	public static IFF getEnum(int ID) {
		for (IFF i : values()) {
			if (i.getID() == ID) {
				return i;
			}
		}
		return null;
	}

	// Getters
	public int getID() {
		return ID;
	}
}
