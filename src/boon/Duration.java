package boon;

import java.util.List;

public class Duration extends AbstractBoon {

	// Constructor
	public Duration(int capacity) {
		super(capacity);
	}

	// Public Methods
	@Override
	public int getStackValue() {
		return boon_stack.stream().mapToInt(Integer::intValue).sum();
	}

	@Override
	public void update(int time_passed) {

		if (!boon_stack.isEmpty()) {
			// Clear stack
			if (time_passed >= getStackValue()) {
				boon_stack.clear();
				return;
			}
			// Remove from the longest duration
			else {
				boon_stack.set(0, boon_stack.get(0) - time_passed);
				if (boon_stack.get(0) <= 0) {
					// Spend leftover time
					time_passed = Math.abs(boon_stack.get(0));
					boon_stack.remove(0);
					update(time_passed);
				}
			}
		}
	}

	@Override
	public void addStacksBetween(List<Integer> boon_stacks, int time_between) {
	}

}
