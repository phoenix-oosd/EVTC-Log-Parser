package log;

public class damageLog {

	// Fields
	private int time;
	private int damage;
	private int skill_id;
	private boolean is_condi;
	private int result;
	private boolean is_ninety;
	private boolean is_moving;
	private int is_statechange;
	private int is_activation;

	// Constructor
	public damageLog(int time, int damage, int skill_id, boolean is_condi, int result, boolean is_ninety,
			boolean is_moving, int is_statechange, int is_activation) {
		this.time = time;
		this.damage = damage;
		this.skill_id = skill_id;
		this.is_condi = is_condi;
		this.result = result;
		this.is_ninety = is_ninety;
		this.is_moving = is_moving;
		this.is_statechange = is_statechange;
		this.is_activation = is_activation;
	}

	// Getters
	public int getTime() {
		return time;
	}

	public int getDamage() {
		return damage;
	}

	public int getID() {
		return skill_id;
	}

	public boolean is_condi() {
		return is_condi;
	}

	public int get_result() {
		return result;
	}

	public boolean is_ninety() {
		return is_ninety;
	}

	public boolean is_moving() {
		return is_moving;
	}

	public int is_statechange() {
		return is_statechange;
	}

	public int is_activation() {
		return is_activation;
	}

}
