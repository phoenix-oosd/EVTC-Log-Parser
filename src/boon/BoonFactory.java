package boon;

import enums.Boon;

public class BoonFactory {

	// Factory
	public AbstractBoon makeBoon(Boon boon) {
		if (boon.getType().equals("intensity")) {
			return new Intensity(boon.getCapacity());
		} else if (boon.getType().equals("duration")) {
			return new Duration(boon.getCapacity());
		} else {
			return null;
		}
	}
}