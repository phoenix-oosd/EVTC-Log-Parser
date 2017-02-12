package statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.List;

import data.AgentData;
import data.AgentItem;
import data.BossData;
import data.CombatData;
import data.CombatItem;
import data.SkillData;
import data.SkillItem;
import enums.Activation;
import enums.Agent;
import enums.Boss;
import enums.Result;
import enums.StateChange;
import utility.TableBuilder;
import utility.Utility;

public class Parse {

	// Fields
	private boolean willHidePlayers;
	private BossData bossData;
	private AgentData agentData;
	private SkillData skillData;
	private CombatData combatData;

	// Constructor
	public Parse(File file, boolean willHidePlayers) throws IOException {

		this.willHidePlayers = willHidePlayers;
		FileInputStream stream = null;
		MappedByteBuffer f = null;

		// Read file
		try {
			// Into memory
			stream = new FileInputStream(file);
			f = stream.getChannel().map(MapMode.READ_ONLY, 0, file.length());
			f.order(ByteOrder.LITTLE_ENDIAN);

			// Parse file
			getBossData(f);
			getAgentData(f);
			getSkillData(f);
			getCombatList(f);
			List<CombatItem> combatList = combatData.getCombatList();
			agentData.fillMissingData(combatList);
			List<AgentItem> NPCAgentList = agentData.getNPCAgents();
			bossData.fillMissingData(NPCAgentList, combatList);

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
	public BossData getBossData() {
		return bossData;
	}

	public AgentData getAgentData() {
		return agentData;
	}

	public SkillData getSkillData() {
		return skillData;
	}

	public CombatData getCombatData() {
		return combatData;
	}

	// Private Methods
	private void getBossData(MappedByteBuffer f) {

		// 12 bytes: version of build
		byte[] version_buffer = new byte[12];
		f.get(version_buffer);

		// 1 byte: skip
		f.position(f.position() + 1);

		// 2 bytes: Boss CID
		int cid = Short.toUnsignedInt(f.getShort());

		// 1 byte: position
		f.position(f.position() + 1);

		// BossData
		Boss b = Boss.getEnum(cid);
		if (b != null) {
			this.bossData = new BossData(0, cid, b.getName(), b.getHP(), 0, 0, Utility.getString(version_buffer));

		} else {
			this.bossData = new BossData(0, cid, String.valueOf(cid), -1, 0, 0, Utility.getString(version_buffer));
		}
	}

	private void getAgentData(MappedByteBuffer f) {

		// AgentData
		this.agentData = new AgentData();

		// 4 bytes: player count
		int player_count = f.getInt();

		// 96 bytes: each player
		for (int i = 0; i < player_count; i++) {

			// 8 bytes: agent
			int agent = (int) f.getLong();

			// 4 bytes: profession
			int prof_id = f.getInt();

			// 4 bytes: is_elite
			int is_elite = f.getInt();

			// 4 bytes: toughness
			int toughness = f.getInt();

			// 4 bytes: healing
			int healing = f.getInt();

			// 4 bytes: condition
			int condition = f.getInt();

			// 68 bytes: name
			byte[] name_buffer = new byte[68];
			f.get(name_buffer);

			// Agent
			Agent a = Agent.getEnum(prof_id, is_elite);

			// Add an agent
			if (willHidePlayers) {
				agentData.addItem(a, new AgentItem(agent, 0, "Player " + String.valueOf(i), a.getName(), toughness,
						healing, condition));
			} else if (a != null) {
				agentData.addItem(a, new AgentItem(agent, 0, Utility.getString(name_buffer), a.getName(), toughness,
						healing, condition));
				System.out.println(Utility.getString(name_buffer));
			} else {
				agentData.addItem(a, new AgentItem(agent, 0, Utility.getString(name_buffer), String.valueOf(prof_id),
						toughness, healing, condition));
			}
		}
	}

	private void getSkillData(MappedByteBuffer f) {

		// SkillData
		this.skillData = new SkillData();

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
			skillData.addItem(new SkillItem(id, Utility.getString(name_buffer)));
		}
	}

	private void getCombatList(MappedByteBuffer f) {

		// CombatData
		this.combatData = new CombatData();

		// 64 bytes: each combat
		while (f.remaining() >= 64) {

			// 8 bytes: time
			int time = (int) f.getLong();

			// 8 bytes: src_agent
			int src_agent = (int) f.getLong();

			// 8 bytes: dst_agent
			int dst_agent = (int) f.getLong();

			// 4 bytes: value
			int value = f.getInt();

			// 4 bytes: buff_dmg
			int buff_dmg = f.getInt();

			// 2 bytes: overstack_value
			int overstack_value = Short.toUnsignedInt(f.getShort());

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
			boolean iff = Utility.getBool(f.get());

			// 1 byte: buff
			boolean buff = Utility.getBool(f.get());

			// 1 byte: result
			Result result = Result.getEnum(f.get());

			// 1 byte: is_activation
			Activation is_activation = Activation.getEnum(f.get());

			// 1 byte: is_buffremove
			boolean is_buffremove = Utility.getBool(f.get());

			// 1 byte: is_ninety
			boolean is_ninety = Utility.getBool(f.get());

			// 1 byte: is_fifty
			boolean is_fifty = Utility.getBool(f.get());

			// 1 byte: is_moving
			boolean is_moving = Utility.getBool(f.get());

			// 1 byte: is_statechange
			StateChange is_statechange = StateChange.getEnum(f.get());

			// 4 bytes: garbage
			f.position(f.position() + 4);

			// Add combat
			combatData.addItem(new CombatItem(time, src_agent, dst_agent, value, buff_dmg, overstack_value, skill_id,
					src_cid, dst_cid, src_master_cid, iff, buff, result, is_activation, is_buffremove, is_ninety,
					is_fifty, is_moving, is_statechange));
		}
	}

	// Override
	@Override
	public String toString() {

		// Build tables
		StringBuilder output = new StringBuilder();
		TableBuilder table = new TableBuilder();

		// Boss Data Table
		table.addTitle("BOSS DATA");
		table.addRow("agent", "CID", "name", "HP", "fight_start", "fight_end", "is_kill", "build_version");
		table.addRow(bossData.toStringArray());
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Player Data
		List<AgentItem> playerAgents = agentData.getPlayerAgents();
		List<AgentItem> NPCAgents = agentData.getNPCAgents();
		List<AgentItem> gadgetAgents = agentData.getGadgetAgents();
		table.addTitle("AGENT DATA");
		table.addRow("agent", "CID", "name", "prof", "toughness", "healing", "condition");
		for (AgentItem player : playerAgents) {
			table.addRow(player.toStringArray());
		}
		for (AgentItem npc : NPCAgents) {
			table.addRow(npc.toStringArray());
		}
		for (AgentItem gadget : gadgetAgents) {
			table.addRow(gadget.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Skill Data
		List<SkillItem> skillList = skillData.getSkillList();
		table.addTitle("SKILL DATA");
		table.addRow("ID", "name");
		for (SkillItem s : skillList) {
			table.addRow(s.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Combat Data Table
		List<CombatItem> combatList = combatData.getCombatList();
		table.addTitle("COMBAT DATA");
		table.addRow("time", "src_agent", "dst_agent", "value", "buff_dmg", "overstack_value", "skill_id", "src_cid",
				"dst_cid", "src_master_cid", "iff", "buff", "is_crit", "is_activation", "is_buffremove",
				"boolean is_ninety", "is_fifty", "is_moving", "is_statechange");
		for (CombatItem c : combatList) {
			table.addRow(c.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());

		return output.toString();
	}

}