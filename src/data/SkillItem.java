package data;

public class SkillItem {

	// Fields
	private int ID;
	private String name;

	// Constructor
	public SkillItem(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[2];
		array[0] = String.valueOf(ID);
		array[1] = String.valueOf(name);
		return array;
	}

	// Getters
	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

}
