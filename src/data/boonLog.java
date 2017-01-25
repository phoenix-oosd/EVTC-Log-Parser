package data;

public class boonLog {

	// Fields
	private int time = 0;
	private int value = 0;
	private int src_cid = 0;

	// Constructor
	public boonLog(int time, int value, int src_cid) {
		this.time = time;
		this.value = value;
		this.src_cid = src_cid;
	}

	// Getters
	public int getTime() {
		return time;
	}

	public int getValue() {
		return value;
	}

	public int getCID() {
		return src_cid;
	}

}
