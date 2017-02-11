package data;

public class CombatItem {

	// Fields
	private int time;
	private int src_agent;
	private int dst_agent;
	private int value;
	private int buff_dmg;
	private int overstack_value;
	private int skill_id;
	private int src_cid;
	private int dst_cid;
	private int src_master_cid;
	private boolean iff;
	private boolean is_buff;
	private int result;
	private int is_activation;
	private boolean is_buffremove;
	private boolean is_ninety;
	private boolean is_fifty;
	private boolean is_moving;
	private int is_statechange;

	// Constructor
	public CombatItem(int time, int src_agent, int dst_agent, int value, int buff_dmg, int overstack_value,
			int skill_id, int src_cid, int dst_cid, int src_master_cid, boolean iff, boolean is_buff, int result,
			int is_activation, boolean is_buffremove, boolean is_ninety, boolean is_fifty, boolean is_moving,
			int is_statechange) {
		this.time = time;
		this.src_agent = src_agent;
		this.dst_agent = dst_agent;
		this.value = value;
		this.buff_dmg = buff_dmg;
		this.overstack_value = overstack_value;
		this.skill_id = skill_id;
		this.src_cid = src_cid;
		this.dst_cid = dst_cid;
		this.src_master_cid = src_master_cid;
		this.iff = iff;
		this.is_buff = is_buff;
		this.result = result;
		this.is_activation = is_activation;
		this.is_buffremove = is_buffremove;
		this.is_ninety = is_ninety;
		this.is_fifty = is_fifty;
		this.is_moving = is_moving;
		this.is_statechange = is_statechange;
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[19];
		array[0] = String.valueOf(time);
		array[1] = String.valueOf(src_agent);
		array[2] = String.valueOf(dst_agent);
		array[3] = String.valueOf(value);
		array[4] = String.valueOf(buff_dmg);
		array[5] = String.valueOf(overstack_value);
		array[6] = String.valueOf(skill_id);
		array[7] = String.valueOf(src_cid);
		array[8] = String.valueOf(dst_cid);
		array[9] = String.valueOf(src_master_cid);
		array[10] = String.valueOf(iff);
		array[11] = String.valueOf(is_buff);
		array[12] = String.valueOf(result);
		array[13] = String.valueOf(is_activation);
		array[14] = String.valueOf(is_buffremove);
		array[15] = String.valueOf(is_ninety);
		array[16] = String.valueOf(is_fifty);
		array[17] = String.valueOf(is_moving);
		array[18] = String.valueOf(is_statechange);
		return array;
	}

	// Getters
	public int get_time() {
		return time;
	}

	public int get_src_agent() {
		return src_agent;
	}

	public int get_dst_agent() {
		return dst_agent;
	}

	public int get_value() {
		return value;
	}

	public int get_buff_dmg() {
		return buff_dmg;
	}

	public int get_overstack_value() {
		return overstack_value;
	}

	public int get_skill_id() {
		return skill_id;
	}

	public int get_src_cid() {
		return src_cid;
	}

	public int get_dst_cid() {
		return dst_cid;
	}

	public int get_src_master_cid() {
		return src_master_cid;
	}

	public boolean iff() {
		return iff;
	}

	public boolean is_buff() {
		return is_buff;
	}

	public int get_result() {
		return result;
	}

	public int is_activation() {
		return is_activation;
	}

	public boolean is_buffremove() {
		return is_buffremove;
	}

	public boolean is_ninety() {
		return is_ninety;
	}

	public boolean is_fifty() {
		return is_fifty;
	}

	public boolean is_moving() {
		return is_moving;
	}

	public int is_statechange() {
		return is_statechange;
	}

	// Setters
	public void set_src_agent(int src_agent) {
		this.src_agent = src_agent;
	}

	public void set_dst_agent(int dst_agent) {
		this.dst_agent = dst_agent;
	}

	public void set_src_cid(int src_cid) {
		this.src_cid = src_cid;
	}

	public void set_dst_cid(int dst_cid) {
		this.dst_cid = dst_cid;
	}
}