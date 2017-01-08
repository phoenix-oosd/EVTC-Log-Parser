import java.util.Collections;

public class BoonDuration extends Boon {

	public BoonDuration(int max_stacks, String type) {
		super(max_stacks, type);
	}

	@Override
	void update(int time_passed) {
		
		// Lose all stacks
		if (time_passed >= super.get_stack_duration()) {
			super.clear();
			return;
		}
		else {
			// Recursively remove from the highest stack
			super.sort();
			stacks.set(0, Integer.valueOf(stacks.get(0) - time_passed));	
			if (stacks.get(0) < 0) {
				
				
				// Get leftover time
				time_passed = Math.abs(stacks.get(0));

				// Rotate left
				stacks.set(0, Integer.valueOf(0));
				Collections.rotate(stacks, 1);
				
				// Spend remaining time
				this.update(time_passed);
				
			}
		}
	}
}
