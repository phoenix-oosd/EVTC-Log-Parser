package data;

public class boonLog {

	// Fields
	private long time = 0;
	private int value = 0;

	// Constructor
	public boonLog(long time, int value) {
		this.time = time;
		this.value = value;
	}

	// Getters
	public int getTime() {
		return (int) time;
	}

	public int getValue() {
		return value;
	}

}
