package enums;

public enum Agent {

	// Constants
	NPC(-1, "NPC"),
	GADGET(0, "Gadget"),
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
	private String name;
	private int ID;

	// Constructor
	Agent(int ID, String name) {
		this.name = name;
		this.ID = ID;
	}

	// Public Methods
	public static Agent getEnum(int ID, int is_elite) {
		for (Agent p : values()) {
			if (is_elite == -1) {
				if (ID == -1) {
					return Agent.GADGET;
				} else {
					return Agent.NPC;
				}
			} else if (is_elite == 0) {
				if (p.getID() == ID) {
					return p;
				}
			} else if (is_elite == 1) {
				if (p.getID() == ID + 9) {
					return p;
				}
			}
		}
		return null;
	}

	// Getters
	public String getName() {
		return name;
	}

	public int getID() {
		return ID;
	}

}
