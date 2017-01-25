package boon;

import java.util.ArrayList;
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

		for (int i = start + 1; i < end; i++) {
			boon_copy.update(1);
			boon_stacks.add(boon_copy.get_stack_value());
		}

	}

}
