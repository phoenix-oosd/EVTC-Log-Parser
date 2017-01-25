package boon;

public class BoonFactory {

	// Factory
	public Boon makeBoon(String boon) {
		if (boon.equals("Might") || boon.equals("Stability")) {
			return new Intensity(25, "Intensity");
		} else if (boon.equals("Grace of the Land")) {
			return new Intensity(5, "Intensity");
		} else if (boon.equals("Fury") || boon.equals("Swiftness") || boon.equals("Alacrity")) {
			return new Duration(9, "Duration");
		} else if (boon.equals("Protection") || boon.equals("Regeneration") || boon.equals("Resistance")
				|| boon.equals("Retaliation") || boon.equals("Quickness") || boon.equals("Vigor")) {
			return new Duration(5, "Duration");
		} else if (boon.equals("Spotter") || boon.equals("Spirit of Frost") || boon.equals("Spirit of Sun")
				|| boon.equals("Glyph of Empowerment") || boon.equals("Empower Allies")
				|| boon.equals("Banner of Strength") || boon.equals("Banner of Discipline")) {
			return new Duration(1, "Duration");
		} else {
			return null;
		}

	}
}