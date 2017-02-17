package enums;

public enum CustomSkill {

	// Constants
	RESURRECT(1066, "Resurrect"),
	BANDAGE(1175, "Bandage"),
	DODGE(65001, "Dodge");

	// Fields
	private int ID;
	private String name;

	// Constructors
	private CustomSkill(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	// Public Methods
	public static CustomSkill getEnum(int ID) {
		for (CustomSkill c : values()) {
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

	public String getName() {
		return name;
	}

}
