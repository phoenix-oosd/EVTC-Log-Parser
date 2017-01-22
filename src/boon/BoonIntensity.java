package boon;

public class BoonIntensity extends Boon {

	// Constructor
	public BoonIntensity(int max_stacks, String type) {
		super(max_stacks, type);
	}

	// Public Methods
	@Override
	public void update(int time_passed) {
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, stacks.get(i) - time_passed);
			if (stacks.get(i) <= 0) {
				stacks.set(i, 0);
			}
		}
	}

}
