package enums;

public enum Agent {

	// Constants
	NPC("NPC", -1),
	GADGET("Gadget", 0),
	GUARDIAN("Guardian", 1),
	WARRIOR("Warrior", 2),
	ENGINEER("Engineer", 3),
	RANGER("Ranger", 4),
	THIEF("Thief", 5),
	ELEMENTALIST("Elementalist", 6),
	MESMER("Mesmer", 7),
	NECROMANCER("Necromancer", 8),
	REVENANT("Revenant", 9),
	DRAGONHUNTER("Dragonhunter", 10),
	BERSERKER("Berserker", 11),
	SCRAPPER("Scrapper", 12),
	DRUID("Druid", 13),
	DAREDEVIL("Daredevil", 14),
	TEMPEST("Tempest", 15),
	CHRONOMANCER("Chronomancer", 16),
	REAPER("Reaper", 17),
	HERALD("Herald", 18);

	// Fields
	private String name;
	private int ID;

	// Constructor
	Agent(String name, int ID) {
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
