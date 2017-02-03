package statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import data.bossData;
import data.combatData;
import data.playerData;
import data.skillData;
import enums.Boss;
import enums.Profession;
import utility.TableBuilder;
import utility.Utility;

public class Parse {

	// Fields
	private boolean players_are_hidden = false;
	private bossData b_data = null;
	private List<playerData> p_data = null;
	private List<skillData> s_data = null;
	private List<combatData> c_data = null;

	// Constructor
	public Parse(File file, boolean players_are_hidden) throws IOException {

		this.players_are_hidden = players_are_hidden;
		FileInputStream stream = null;
		MappedByteBuffer f = null;

		// Read file
		try {
			// Into memory
			stream = new FileInputStream(file);
			f = stream.getChannel().map(MapMode.READ_ONLY, 0, file.length());
			f.order(ByteOrder.LITTLE_ENDIAN);

			// Parse file
			get_boss_data(f);
			get_player_data(f);
			get_skill_data(f);
			get_combat_data(f);
			fill_missing_data();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Close stream
		finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Public Methods
	public bossData getB() {
		return this.b_data;
	}

	public List<playerData> getP() {
		return this.p_data;
	}

	public List<skillData> getS() {
		return this.s_data;
	}

	public List<combatData> getC() {
		return this.c_data;
	}

	@Override
	public String toString() {

		// Build tables
		StringBuilder output = new StringBuilder();
		TableBuilder table = new TableBuilder();

		// Boss Data Table
		table.addTitle("BOSS DATA");
		table.addRow("agent", "CID", "name", "HP", "fight_duration", "version");
		table.addRow(b_data.toStringArray());
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Player Data
		table.addTitle("PLAYER DATA");
		table.addRow("agent", "CID", "name", "prof", "toughness", "healing", "condition");
		for (playerData p : p_data) {
			table.addRow(p.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Skill Data
		table.addTitle("SKILL DATA");
		table.addRow("ID", "name");
		for (skillData s : s_data) {
			table.addRow(s.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Combat Data Table
		table.addTitle("COMBAT DATA");
		table.addRow("time", "src_agent", "dst_agent", "value", "buff_dmg", "overstack_value", "skill_id", "src_cid",
				"dst_cid", "src_master_cid", "iff", "is_buff", "is_crit", "is_activation", "is_buffremove",
				"boolean is_ninety", "is_fifty", "is_moving", "is_statechange");
		for (combatData c : c_data) {
			table.addRow(c.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());

		return output.toString();
	}

	// Private Methods
	private void get_boss_data(MappedByteBuffer f) {

		// 12 bytes: version of build
		byte[] version_buffer = new byte[12];
		f.get(version_buffer);

		// 1 byte: skip
		f.position(f.position() + 1);

		// 2 bytes: Boss CID
		int cid = Short.toUnsignedInt(f.getShort());

		// 1 byte: position
		f.position(f.position() + 1);

		// Boss
		Boss b = Boss.getBoss(cid);
		if (b != null) {
			this.b_data = new bossData(0, cid, b.getName(), b.getHP(), 0, Utility.get_String(version_buffer));

		} else {
			this.b_data = new bossData(0, cid, "UNKNOWN", 0, 0, Utility.get_String(version_buffer));
		}

	}

	private void get_player_data(MappedByteBuffer f) {

		// playerData array
		this.p_data = new ArrayList<playerData>();

		// 4 bytes: player count
		int player_count = f.getInt();
		if (!((player_count >= 1) && (player_count <= 10))) {
			System.out.println("Invalid .evtc file. Version: " + b_data.getVersion());
			System.out.println("Player table has not been created properly: " + player_count + " players detected.");
			System.out.println("Send this log to deltaconnected if you suspect there is a bug with .evtc generation.");
			Scanner scan = null;
			try {
				scan = new Scanner(System.in);
				scan.nextLine();
			} finally {
				scan.close();
				System.exit(0);
			}
		}

		// 96 bytes: each player
		for (int i = 0; i < player_count; i++) {
			// 8 bytes: agent
			long agent = f.getLong();

			// 4 bytes: profession
			int prof_id = f.getInt();

			// 4 bytes: is_elite
			boolean is_elite = Utility.get_bool(f.getInt());

			// 4 bytes: toughness
			int toughness = f.getInt() + 1000;

			// 4 bytes: healing
			int healing = f.getInt();

			// 4 bytes: condition
			int condition = f.getInt();

			// 68 bytes: name
			byte[] name_buffer = new byte[68];
			f.get(name_buffer);

			// Profession
			Profession p = Profession.getProfession(prof_id, is_elite);

			// Add player
			if (players_are_hidden) {
				this.p_data.add(new playerData(agent, 0, "Player " + String.valueOf(i), p.getName(), toughness, healing,
						condition));
			} else if (p != null) {
				this.p_data.add(new playerData(agent, 0, Utility.get_String(name_buffer), p.getName(), toughness,
						healing, condition));
			} else {
				this.p_data.add(new playerData(agent, 0, Utility.get_String(name_buffer), "UNKNOWN", toughness, healing,
						condition));
			}
		}
	}

	private void get_skill_data(MappedByteBuffer f) {

		// skillData array
		this.s_data = new ArrayList<skillData>();

		// 4 bytes: player count
		int skill_count = f.getInt();

		// 68 bytes: each skill
		for (int i = 0; i < skill_count; i++) {
			// 4 bytes: id
			int id = f.getInt();

			// 64 bytes: name
			byte[] name_buffer = new byte[64];
			f.get(name_buffer);

			// Add skill
			this.s_data.add(new skillData(id, Utility.get_String(name_buffer)));
		}
	}

	private void get_combat_data(MappedByteBuffer f) {

		// combatData array
		this.c_data = new ArrayList<combatData>();

		// 64 bytes: each combat
		while (f.remaining() >= 64) {
			// 8 bytes: time
			long time = f.getLong();

			// 8 bytes: src_agent
			long src_agent = f.getLong();

			// 8 bytes: dst_agent
			long dst_agent = f.getLong();

			// 4 bytes: value
			int value = f.getInt();

			// 4 bytes: buff_dmg
			int buff_dmg = f.getInt();

			// 2 bytes: overstack_value
			int overstack_value = f.getShort();

			// 2 bytes: skill_id
			int skill_id = Short.toUnsignedInt(f.getShort());

			// 2 bytes: src_cid
			int src_cid = Short.toUnsignedInt(f.getShort());

			// 2 bytes: dst_cid
			int dst_cid = Short.toUnsignedInt(f.getShort());

			// 2 bytes: src_master_cid
			int src_master_cid = Short.toUnsignedInt(f.getShort());

			// 9 bytes: garbage
			f.position(f.position() + 9);

			// 1 byte: iff
			boolean iff = Utility.get_bool(f.get());

			// 1 byte: is_buff
			boolean is_buff = Utility.get_bool(f.get());

			// 1 byte: is_crit
			boolean is_crit = Utility.get_bool(f.get());

			// 1 byte: is_activation
			int is_activation = f.get();

			// 1 byte: is_buffremove
			boolean is_buffremove = Utility.get_bool(f.get());

			// 1 byte: is_ninety
			boolean is_ninety = Utility.get_bool(f.get());

			// 1 byte: is_fifty
			boolean is_fifty = Utility.get_bool(f.get());

			// 1 byte: is_moving
			boolean is_moving = Utility.get_bool(f.get());

			// 1 byte: is_statechange
			int is_statechange = f.get();

			// 4 bytes: garbage + compiler
			f.position(f.position() + 4);

			// Add combat
			this.c_data.add(new combatData(time, src_agent, dst_agent, value, buff_dmg, overstack_value, skill_id,
					src_cid, dst_cid, src_master_cid, iff, is_buff, is_crit, is_activation, is_buffremove, is_ninety,
					is_fifty, is_moving, is_statechange));
		}
	}

	private void fill_missing_data() {

		// Update boss agent
		for (combatData c : c_data) {
			if (c.get_src_cid() == b_data.getCID()) {
				b_data.setAgent(c.get_src_agent());
				break;
			}
		}

		// Update boss fight duration
		b_data.setFightDuration(c_data.get(c_data.size() - 1).get_time() - c_data.get(0).get_time());

		// Update player CIDs
		for (playerData p : p_data) {
			for (combatData c : c_data) {
				if (p.getAgent() == c.get_src_agent()) {
					if (c.get_src_master_cid() == 0) {
						p.setCID(c.get_src_cid());
					} else {
						p.setCID(c.get_src_master_cid());
					}
					break;
				}
			}
		}

		// Delete players with no CID
		Iterator<playerData> iter = p_data.iterator();
		while (iter.hasNext()) {
			playerData p = iter.next();
			if (p.getCID() == 0) {
				iter.remove();
			}
		}

		// Update combat for Xera logs
		if (b_data.getName().equals("Xera")) {
			long xera_50 = 16286;
			for (combatData c : c_data) {
				if (c.get_src_cid() == xera_50) {
					c.set_src_agent(b_data.getAgent());
					c.set_src_cid(b_data.getCID());
				} else if (c.get_dst_cid() == xera_50) {
					c.set_dst_agent(b_data.getAgent());
					c.set_dst_cid(b_data.getCID());
				}
			}
		}
	}

}