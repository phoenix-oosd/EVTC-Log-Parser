package boon;

import java.util.Collections;

public class BoonDuration extends Boon {

	// Constructor
	public BoonDuration(int max_stacks, String type) {
		super(max_stacks, type);
	}

	// Public Methods
	@Override
	public void update(int time_passed) {
		// Lose all stacks
		if (time_passed >= super.get_stack_duration()) {
			super.clear();
			return;
		} else {
			// Remove from the highest stack
			super.sort();
			stacks.set(0, Integer.valueOf(stacks.get(0) - time_passed));
			if (stacks.get(0) <= 0) {
				// Spend leftover time
				time_passed = Math.abs(stacks.get(0));
				stacks.set(0, Integer.valueOf(0));
				Collections.rotate(stacks, 1);
				this.update(time_passed);
			}
		}
	}

}
