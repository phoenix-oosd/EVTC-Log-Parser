package boon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Boon {

	// Fields
	protected List<Integer> stacks = new ArrayList<Integer>();
	protected int max_stacks;
	private String type;

	// Constructor
	public Boon(int max_stacks, String type) {
		this.max_stacks = max_stacks;
		this.type = type;
		clear();
	}

	// Abstract Methods
	public abstract void update(int time_passed);

	// Public Methods
	public void add(int duration) {
		// Find an empty slot
		for (int i = 0; i < stacks.size(); i++) {
			if (stacks.get(i) == 0) {
				stacks.set(i, Integer.valueOf(duration));
				sort();
				return;
			}
		}
		// No empty slots so replace the lowest
		Collections.sort(stacks);
		if (stacks.get(0) < duration) {
			stacks.set(0, Integer.valueOf(duration));
		}
		sort();
		return;
	}

	// Protected Methods
	protected void sort() {
		Collections.sort(stacks, Collections.reverseOrder());
	}

	protected void clear() {
		stacks.clear();
		for (int i = 0; i < max_stacks; i++) {
			this.stacks.add(Integer.valueOf(0));
		}
	}

	// Getters
	public String get_type() {
		return this.type;
	}

	public int get_stack_duration() {
		return stacks.stream().mapToInt(Integer::intValue).sum();
	}

	public int get_stack_count() {
		int stack_count = 0;
		for (Integer i : stacks) {
			if (i != 0) {
				stack_count++;
			}
		}
		return stack_count;
	}

	// Get tuples in between
	public List<Integer> get_stacks_between(int time_start, int time_stop) {

		Boon boon_copy = new BoonIntensity(this.max_stacks, this.type);
		boon_copy.stacks = new ArrayList<Integer>(this.stacks);
		// System.out.println(boon_copy.stacks);

		List<Integer> boon_stacks = new ArrayList<Integer>();
		for (int i = time_start + 1; i < time_stop; i++) {
			boon_copy.update(1);
			// System.out.println(boon_copy.stacks);
			boon_stacks.add(boon_copy.get_stack_count());
		}

		return boon_stacks;

	}
}
