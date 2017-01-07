public class combatData {

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
	private boolean is_crit;
	private boolean is_activation;
	private boolean is_buffremove;
	private boolean is_ninety;
	private boolean is_fifty;
	private boolean is_moving;
	private boolean is_statechange;
	
	public combatData(int time, int src_agent, int dst_agent, int value, int buff_dmg, int overstack_value,
			int skill_id, int src_cid, int dst_cid, int src_master_cid, boolean iff, boolean is_buff, boolean is_crit,
			boolean is_activation, boolean is_buffremove, boolean is_ninety, boolean is_fifty, boolean is_moving,
			boolean is_statechange) {
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
		this.is_crit = is_crit;
		this.is_activation = is_activation;
		this.is_buffremove = is_buffremove;
		this.is_ninety = is_ninety;
		this.is_fifty = is_fifty;
		this.is_moving = is_moving;
		this.is_statechange = is_statechange;
	}
	
}
