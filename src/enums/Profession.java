package enums;

public enum Profession {

	// Constants
	GUARDIAN(1, "Guardian"),
	WARRIOR(2, "Warrior"),
	ENGINEER(3, "Engineer"),
	RANGER(4, "Ranger"),
	THIEF(5, "Thief"),
	ELEMENTALIST(6, "Elementalist"),
	MESMER(7, "Mesmer"),
	NECROMANCER(8, "Necromancer"),
	REVENANT(9, "Revenant"),
	DRAGONHUNTER(10, "Dragonhunter"),
	BERSERKER(11, "Berserker"),
	SCRAPPER(12, "Scrapper"),
	DRUID(13, "Druid"),
	DAREDEVIL(14, "Daredevil"),
	TEMPEST(15, "Tempest"),
	CHRONOMANCER(16, "Chronomancer"),
	REAPER(17, "Reaper"),
	HERALD(18, "Herald");

	// Fields
	private int ID;
	private String name;

	// Constructor
	Profession(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	// Public Methods
	public static Profession getProfession(int ID, boolean is_elite) {
		for (Profession p : values()) {
			if (!is_elite) {
				if (p.getID() == ID) {
					return p;
				}
			} else {
				if (p.getID() == ID + 9) {
					return p;
				}
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
