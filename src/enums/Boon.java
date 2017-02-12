package enums;

import java.util.ArrayList;
import java.util.List;

public enum Boon {

	// Constants
	MIGHT("Might", "MGHT", "intensity", 25),
	QUICKNESS("Quickness", "QCKN", "duration", 5),
	FURY("Fury", "FURY", "duration", 9),
	PROTECTION("Protection", "PROT", "duration", 5),
	ALACRITY("Alacrity", "ALAC", "duration", 9),
	SPOTTER("Spotter", "SPOT", "duration", 1),
	SPIRIT_OF_FROST("Spirit of Frost", "FRST", "duration", 1),
	SUN_SPIRIT("Sun Spirit", "SUNS", "duration", 1),
	GLYPH_OF_EMPOWERMENT("Glyph of Empowerment", "GoE", "duration", 1),
	GRACE_OF_THE_LAND("Grace of the Land", "GoTL", "intensity", 5),
	EMPOWER_ALLIES("Empower Allies", "EA", "duration", 1),
	BANNER_OF_STRENGTH("Banner of Strength", "BoS", "duration", 1),
	BANNER_OF_DISCIPLINE("Banner of Discipline", "BoD", "duration", 1);

	// Fields
	private String name;
	private String abrv;
	private String type;
	private int capacity;

	// Constructor
	private Boon(String name, String abrv, String type, int capacity) {
		this.name = name;
		this.abrv = abrv;
		this.type = type;
		this.capacity = capacity;
	}

	// Public Methods
	public static Boon getEnum(String name) {
		for (Boon b : values()) {
			if (b.getName() == name) {
				return b;
			}
		}
		return null;
	}

	public static String[] getArray() {
		List<String> boonList = new ArrayList<String>();
		for (Boon b : values()) {
			boonList.add(b.getAbrv());
		}
		return boonList.toArray(new String[boonList.size()]);
	}

	public static List<String> getList() {
		List<String> boonList = new ArrayList<String>();
		for (Boon b : values()) {
			boonList.add(b.getName());
		}
		return boonList;
	}

	// Getters
	public String getName() {
		return this.name;
	}

	public String getAbrv() {
		return this.abrv;
	}

	public String getType() {
		return this.type;
	}

	public int getCapacity() {
		return this.capacity;
	}

}
