package boon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractBoon {

	// Fields
	protected List<Integer> stacks = new ArrayList<Integer>();
	protected int max_stacks;

	// Constructor
	public AbstractBoon(int max_stacks) {
		this.max_stacks = max_stacks;
	}

	// Abstract Methods
	public abstract int get_stack_value();

	public abstract void update(int time_passed);

	public abstract void add_stacks_between(List<Integer> boon_stacks, int start, int stop);

	// Public Methods
	public void add(int duration) {

		// Find an empty slot
		if (!is_full()) {
			stacks.add(duration);
		}
		// Replace lowest value
		else if (stacks.get(stacks.size() - 1) < duration) {
			stacks.set(stacks.size() - 1, duration);
		}
		sort();
	}

	// Protected Methods
	protected boolean is_full() {
		return stacks.size() >= max_stacks;
	}

	protected void sort() {
		Collections.sort(stacks, Collections.reverseOrder());
	}
}
