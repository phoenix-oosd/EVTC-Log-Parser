package data;

public class damageLog {

	// Fields
	private long time;
	private int damage;
	private int skill_id;
	private boolean is_condi;
	private boolean is_crit;
	private boolean is_ninety;
	private boolean is_moving;

	// Constructor
	public damageLog(long time, int damage, int skill_id, boolean is_condi, boolean is_crit, boolean is_ninety,
			boolean is_moving) {
		this.time = time;
		this.damage = damage;
		this.skill_id = skill_id;
		this.is_condi = is_condi;
		this.is_crit = is_crit;
		this.is_ninety = is_ninety;
		this.is_moving = is_moving;
	}

	// Getters
	public int getTime() {
		return (int) time;
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

	public boolean is_crit() {
		return is_crit;
	}

	public boolean is_ninety() {
		return is_ninety;
	}

	public boolean is_moving() {
		return is_moving;
	}

}
