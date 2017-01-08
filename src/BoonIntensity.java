
public class BoonIntensity extends Boon {

	public BoonIntensity(int max_stacks) {
		super(max_stacks);
	}

	@Override
	void update(int time_passed) {
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, Integer.valueOf(stacks.get(i) - time_passed));
			if (stacks.get(0) <= 0) {
				stacks.set(i, Integer.valueOf(0));
			}
		}
	}

}
