
public class BoonIntensity extends Boon {

	public BoonIntensity(int max_stacks, String type) {
		super(max_stacks, type);
	}

	@Override
	void update(int time_passed) {
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, Integer.valueOf(stacks.get(i) - time_passed));
			if (stacks.get(i) <= 0) {
				stacks.set(i, Integer.valueOf(0));
			}
		}
	}

}
