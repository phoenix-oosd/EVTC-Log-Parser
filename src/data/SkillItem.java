package data;

public class SkillItem {

	// Fields
	private int id;
	private String name;

	// Constructor
	public SkillItem(int id, String name) {
		this.id = id;
		this.name = name;
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[2];
		array[0] = String.valueOf(id);
		array[1] = String.valueOf(name);
		return array;
	}

	// Getters
	public int get_id() {
		return id;
	}

	public String get_name() {
		return name;
	}

}
