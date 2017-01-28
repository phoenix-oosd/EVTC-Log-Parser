package boon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Intensity extends Boon {

	// Constructor
	public Intensity(int max_stacks, String type) {
		super(max_stacks, type);
	}

	// Public Methods
	@Override
	public int get_stack_value() {
		return stacks.size();
	}

	@Override
	public void update(int time_passed) {

		// Subtract from each
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, stacks.get(i) - time_passed);
		}

		// Remove negatives
		for (Iterator<Integer> iter = stacks.listIterator(); iter.hasNext();) {
			Integer stack = iter.next();
			if (stack <= 0) {
				iter.remove();
			}
		}
	}

	@Override
	public void add_stacks_between(List<Integer> boon_stacks, int start, int end) {

		Boon boon_copy = new Intensity(this.max_stacks, this.type);
		boon_copy.stacks = new ArrayList<Integer>(this.stacks);
		List<Integer> stacks = boon_copy.stacks;

		int loops = end - start;

		if (!stacks.isEmpty()) {
			int t_passed = 0;
			int minimum = Collections.min(stacks);

			for (int i = 1; i < loops; i++) {
				if ((i - t_passed) >= minimum) {
					boon_copy.update(i - t_passed);
					if (!stacks.isEmpty()) {
						minimum = Collections.min(stacks);
					}
					t_passed = i;
				}
				boon_stacks.add(boon_copy.get_stack_value());
			}
		} else {
			for (int i = 1; i < loops; i++) {
				boon_stacks.add(0);
			}

		}

	}

}
