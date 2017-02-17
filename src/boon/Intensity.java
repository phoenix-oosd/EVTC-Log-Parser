package boon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Intensity extends AbstractBoon {

	// Constructor
	public Intensity(int capacity) {
		super(capacity);
	}

	// Public Methods
	@Override
	public int getStackValue() {
		return boon_stack.size();
	}

	@Override
	public void update(int time_passed) {

		// Subtract from each
		for (int i = 0; i < boon_stack.size(); i++) {
			boon_stack.set(i, boon_stack.get(i) - time_passed);
		}
		// Remove negatives
		for (Iterator<Integer> iter = boon_stack.listIterator(); iter.hasNext();) {
			Integer stack = iter.next();
			if (stack <= 0) {
				iter.remove();
			}
		}
	}

	@Override
	public void addStacksBetween(List<Integer> boon_stacks, int time_between) {

		// Create copy of the boon
		Intensity boon_copy = new Intensity(this.capacity);
		boon_copy.boon_stack = new ArrayList<Integer>(this.boon_stack);
		List<Integer> stacks = boon_copy.boon_stack;

		// Simulate the boon stack decreasing
		if (!stacks.isEmpty()) {

			int time_passed = 0;
			int min_duration = Collections.min(stacks);

			// Remove minimum duration from stack
			for (int i = 1; i < time_between; i++) {
				if ((i - time_passed) >= min_duration) {
					boon_copy.update(i - time_passed);
					if (!stacks.isEmpty()) {
						min_duration = Collections.min(stacks);
					}
					time_passed = i;
				}
				boon_stacks.add(boon_copy.getStackValue());
			}
		}
		// Fill in remaining time with 0 values
		else {
			for (int i = 1; i < time_between; i++) {
				boon_stacks.add(0);
			}
		}
	}

}
