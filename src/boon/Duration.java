package boon;

import java.util.List;

public class Duration extends Boon {

	// Constructor
	public Duration(int max_stacks, String type) {
		super(max_stacks, type);
	}

	// Public Methods
	@Override
	public int get_stack_value() {
		return stacks.stream().mapToInt(Integer::intValue).sum();
	}

	@Override
	public void update(int time_passed) {
		// Lose all stacks
		if (time_passed >= get_stack_value()) {
			stacks.clear();
			return;
		}
		// Remove from the highest stack
		else {
			stacks.set(0, Integer.valueOf(stacks.get(0) - time_passed));
			if (stacks.get(0) <= 0) {
				// Spend leftover time
				time_passed = Math.abs(stacks.get(0));
				stacks.remove(0);
				this.update(time_passed);
			}
		}
	}

	@Override
	public void add_stacks_between(List<Integer> boon_stacks, int start, int stop) {
	}

}
