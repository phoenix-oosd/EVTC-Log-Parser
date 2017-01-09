
public enum BoonEnum {
	
	MGHT("Might", "Might", 25), STAB("Stability", "Stab", 25),
	GOTL("Grace of the Land", "GotL", 5),
	FURY("Fury", "Fury", 9) , SWFT("Swiftness", "Swift", 9) , ALAC("Alacrity", "Alac", 9),
	PROT("Protection", "Prot", 5), REGN("Regeneration", "Regen", 5), RSTC("Resistance", "Rstnc", 5), RETL("Retaliation", "Retal", 5), QCKN("Quickness", "Quick", 5), VIGR("Vigor", "Vigor", 5),
	SPOT("Spotter", "Spot", 1), FRST("Spirit of Frost", "Frst", 1), SUNS("Spirit of Sun", "Sun", 1), GOFE("Glyph of Empowerment", "GoE", 1),
	EALL("Empower Allies", "EA", 1), BOFS("Banner of Strength", "BoS", 1), BOFD("Banner of Discipline", "BoD", 1);
	
	
	private final String name;
	private final String abrv;
	private final int max_stacks;
	
	BoonEnum(String name, String abrv, int max_stacks) {
		this.name = name;
		this.abrv = abrv;
		this.max_stacks = max_stacks;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAbrv() {
		return this.abrv;
	}
	
	public int getMaxStacks() {
		return this.max_stacks;
	}

}
