

public class damageLog {
	
    private int time = 0;
	private int damage = 0;
	private boolean is_condi;
    private boolean is_crit;
    private boolean is_ninety;
    private boolean is_moving;

    public damageLog(int time, int damage, boolean is_condi, boolean is_crit, boolean is_ninety, boolean is_moving) {
		super();
		this.time = time;
		this.damage = damage;
		this.is_condi = is_condi;
		this.is_crit = is_crit;
		this.is_ninety = is_ninety;
		this.is_moving = is_moving;
	}

	public int getTime() {
		return time;
	}

	public int getDamage() {
		return damage;
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
