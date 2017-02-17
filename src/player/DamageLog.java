package player;

import enums.Activation;
import enums.Result;
import enums.StateChange;

public class DamageLog {

	// Fields
	private int time;
	private int damage;
	private int skill_id;
	private boolean is_condi;
	private Result result;
	private boolean is_ninety;
	private boolean is_moving;
	private StateChange is_statechange;
	private Activation is_activation;
	private boolean is_flanking;

	// Constructor
	public DamageLog(int time, int damage, int skill_id, boolean is_condi, Result result, boolean is_ninety,
			boolean is_moving, StateChange is_statechange, Activation activation, boolean is_flanking) {
		this.time = time;
		this.damage = damage;
		this.skill_id = skill_id;
		this.is_condi = is_condi;
		this.result = result;
		this.is_ninety = is_ninety;
		this.is_moving = is_moving;
		this.is_statechange = is_statechange;
		this.is_activation = activation;
		this.is_flanking = is_flanking;
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

	public boolean isCondi() {
		return is_condi;
	}

	public Result getResult() {
		return result;
	}

	public boolean isNinety() {
		return is_ninety;
	}

	public boolean isMoving() {
		return is_moving;
	}

	public StateChange isStatechange() {
		return is_statechange;
	}

	public Activation isActivation() {
		return is_activation;
	}

	public boolean isFlanking() {
		return is_flanking;
	}

}
