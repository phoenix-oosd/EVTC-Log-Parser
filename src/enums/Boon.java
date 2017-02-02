package enums;

import java.util.ArrayList;
import java.util.List;

public enum Boon {

	// Constants
	MIGHT(25, "intensity", "Might", "MGHT"),
	QUICKNESS(5, "duration", "Quickness", "QCKN"),
	FURY(9, "duration", "Fury", "FURY"),
	PROTECTION(5, "duration", "Protection", "PROT"),
	ALACRITY(9, "duration", "Alacrity", "ALAC"),
	SPOTTER(1, "duration", "Spotter", "SPOT"),
	SPIRIT_OF_FROST(1, "duration", "Spirit of Frost", "FRST"),
	SUN_SPIRIT(1, "duration", "Sun Spirit", "SUNS"),
	GLYPH_OF_EMPOWERMENT(1, "duration", "Glyph of Empowerment", "GoE"),
	GRACE_OF_THE_LAND(5, "intensity", "Grace of the Land", "GoTL"),
	EMPOWER_ALLIES(1, "duration", "Empower Allies", "EA"),
	BANNER_OF_STRENGTH(1, "duration", "Banner of Strength", "BoS"),
	BANNER_OF_DISCIPLINE(1, "duration", "Banner of Discipline", "BoD");

	// Fields
	private int stack;
	private String type;
	private String name;
	private String abrv;

	// Constructor
	Boon(int stack, String type, String name, String abrv) {
		this.stack = stack;
		this.type = type;
		this.name = name;
		this.abrv = abrv;
	}

	// Public Methods
	public static Boon getBoon(String name) {
		for (Boon b : values()) {
			if (b.getName() == name) {
				return b;
			}
		}
		return null;
	}

	public static String[] getArray() {
		List<String> boon_list = new ArrayList<String>();
		for (Boon b : values()) {
			boon_list.add(b.getAbrv());
		}
		String[] boon_array = boon_list.toArray(new String[boon_list.size()]);
		return boon_array;
	}

	public static List<String> getList() {
		List<String> boon_list = new ArrayList<String>();
		for (Boon b : values()) {
			boon_list.add(b.getName());
		}
		return boon_list;
	}

	// Getters
	public int getStack() {
		return this.stack;
	}

	public String getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public String getAbrv() {
		return this.abrv;
	}
}
