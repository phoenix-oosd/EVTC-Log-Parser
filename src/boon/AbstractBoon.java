package boon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractBoon {

	// Fields
	protected int capacity;
	protected List<Integer> boon_stack = new ArrayList<Integer>();

	// Constructor
	public AbstractBoon(int capacity) {
		this.capacity = capacity;
	}

	// Abstract Methods
	public abstract int getStackValue();

	public abstract void update(int time_passed);

	public abstract void addStacksBetween(List<Integer> boon_stacks, int time_between);

	// Public Methods
	public void add(int boon_duration) {

		// Find empty slot
		if (!isFull()) {
			boon_stack.add(boon_duration);
			sort();
			return;
		}

		// Replace lowest value
		int index = boon_stack.size() - 1;
		if (boon_stack.get(index) < boon_duration) {
			boon_stack.set(index, boon_duration);
			sort();
			return;
		}
	}

	// Protected Methods
	protected boolean isFull() {
		return boon_stack.size() >= capacity;
	}

	protected void sort() {
		Collections.sort(boon_stack, Collections.reverseOrder());
	}
}
