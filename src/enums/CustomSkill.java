package enums;

public enum CustomSkill {

	// Constants
	RESURRECT(1066, "Resurrect"),
	BANDAGE(1175, "Bandage"),
	DODGE(65001, "Dodge");

	// Fields
	private int id;
	private String name;

	// Constructors
	private CustomSkill(int id, String name) {
		this.id = id;
		this.name = name;
	}

	// Public Methods
	public static CustomSkill getEnum(int id) {
		for (CustomSkill c : values()) {
			if (c.get_id() == id) {
				return c;
			}
		}
		return null;
	}

	// Getters
	public int get_id() {
		return id;
	}

	public String get_name() {
		return name;
	}

}
