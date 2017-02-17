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
import enums.IFF;
import enums.Result;
import enums.StateChange;
import utility.TableBuilder;
import utility.Utility;

public class Parse {

	// Fields
	private BossData bossData;
	private AgentData agentData = new AgentData();
	private SkillData skillData = new SkillData();
	private CombatData combatData = new CombatData();

	// Constructor
	public Parse(File file) throws IOException {

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
			fillMissingData();

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

		// 12 bytes: arc build version
		f.position(f.position() + 4);
		byte[] version_buffer = new byte[8];
		f.get(version_buffer);

		// 1 byte: skip
		f.position(f.position() + 1);

		// 2 bytes: boss instance id
		int instid = Short.toUnsignedInt(f.getShort());

		// 1 byte: position
		f.position(f.position() + 1);

		// BossData
		Boss boss = Boss.getEnum(instid);
		if (boss != null) {
			this.bossData = new BossData(boss, Utility.getString(version_buffer));
		} else {
			this.bossData = new BossData(instid, Utility.getString(version_buffer));
		}
	}

	private void getAgentData(MappedByteBuffer f) {

		// 4 bytes: player count
		int player_count = f.getInt();

		// 96 bytes: each player
		for (int i = 0; i < player_count; i++) {

			// 8 bytes: agent
			long agent = f.getLong();

			// 4 bytes: profession
			int prof = f.getInt();

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
			Agent a = Agent.getEnum(prof, is_elite);

			// Add an agent
			if (a != null) {
				if (a.equals(Agent.NPC)) {
					agentData.addItem(a, new AgentItem(agent, Utility.getString(name_buffer),
							a.getName() + ":" + String.format("%05d", prof)));
				} else if (a.equals(Agent.GADGET)) {
					agentData.addItem(a, new AgentItem(agent, Utility.getString(name_buffer), a.getName()));
				} else {
					agentData.addItem(a, new AgentItem(agent, Utility.getString(name_buffer), a.getName(), toughness,
							healing, condition));
				}
			} else {
				agentData.addItem(a, new AgentItem(agent, Utility.getString(name_buffer), String.valueOf(prof),
						toughness, healing, condition));
			}
		}
	}

	private void getSkillData(MappedByteBuffer f) {

		// 4 bytes: player count
		int skill_count = f.getInt();

		// 68 bytes: each skill
		for (int i = 0; i < skill_count; i++) {

			// 4 bytes: skill id
			int skill_id = f.getInt();

			// 64 bytes: name
			byte[] name_buffer = new byte[64];
			f.get(name_buffer);

			// Add skill
			skillData.addItem(new SkillItem(skill_id, Utility.getString(name_buffer)));
		}
	}

	private void getCombatList(MappedByteBuffer f) {

		// 64 bytes: each combat
		while (f.remaining() >= 64) {

			// 8 bytes: time
			int time = (int) f.getLong();

			// 8 bytes: src_agent
			long src_agent = f.getLong();

			// 8 bytes: dst_agent
			long dst_agent = f.getLong();

			// 4 bytes: value
			int value = f.getInt();

			// 4 bytes: buff_dmg
			int buff_dmg = f.getInt();

			// 2 bytes: overstack_value
			int overstack_value = Short.toUnsignedInt(f.getShort());

			// 2 bytes: skill_id
			int skill_id = Short.toUnsignedInt(f.getShort());

			// 2 bytes: src_instid
			int src_instid = Short.toUnsignedInt(f.getShort());

			// 2 bytes: dst_instid
			int dst_instid = Short.toUnsignedInt(f.getShort());

			// 2 bytes: src_master_instid
			int src_master_instid = Short.toUnsignedInt(f.getShort());

			// 9 bytes: garbage
			f.position(f.position() + 9);

			// 1 byte: iff
			IFF iff = IFF.getEnum(f.get());

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

			// 1 byte: is_flanking
			boolean is_flanking = Utility.getBool(f.get());

			// 3 bytes: garbage
			f.position(f.position() + 3);

			// Add combat
			combatData.addItem(new CombatItem(time, src_agent, dst_agent, value, buff_dmg, overstack_value, skill_id,
					src_instid, dst_instid, src_master_instid, iff, buff, result, is_activation, is_buffremove,
					is_ninety, is_fifty, is_moving, is_statechange, is_flanking));
		}
	}

	private void fillMissingData() {

		// Set Agent instid, first_aware and last_aware
		List<AgentItem> agentList = agentData.getAllAgents();
		List<CombatItem> combatList = combatData.getCombatList();
		for (AgentItem a : agentList) {
			boolean assigned_first = false;
			for (CombatItem c : combatList) {
				if (a.get_agent() == c.get_src_agent() && c.get_src_instid() != 0) {
					if (!assigned_first) {
						a.setInstid(c.get_src_instid());
						a.setFirstAware(c.get_time());
						assigned_first = true;
					}
					a.setLastAware(c.get_time());
				} else if (a.get_agent() == c.get_dst_agent() && c.get_dst_instid() != 0) {
					if (!assigned_first) {
						a.setInstid(c.get_dst_instid());
						a.setFirstAware(c.get_time());
						assigned_first = true;
					}
					a.setLastAware(c.get_time());
				}
			}
		}

		// Set Boss data agent, instid, first_aware and last_aware
		List<AgentItem> NPCList = agentData.getNPCAgents();
		for (AgentItem NPC : NPCList) {
			if (NPC.get_prof().endsWith(String.valueOf(bossData.getSpeciesID()))) {
				if (bossData.get_agent() == 0) {
					bossData.set_agent(NPC.get_agent());
					bossData.set_instid(NPC.get_instid());
					bossData.set_first_aware(NPC.get_first_aware());
				}
				bossData.set_last_aware(NPC.get_last_aware());
			}

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
		table.addRow("agent", "instid", "first_aware", "last_aware", "species_id", "name", "health", "build_version");
		table.addRow(bossData.toStringArray());
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Player Data
		List<AgentItem> playerAgents = agentData.getPlayerAgents();
		List<AgentItem> NPCAgents = agentData.getNPCAgents();
		List<AgentItem> gadgetAgents = agentData.getGadgetAgents();
		table.addTitle("AGENT DATA");
		table.addRow("agent", "instid", "first_aware", "last_aware", "name", "prof", "toughness", "healing",
				"condition");
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
		List<SkillItem> skillList = skillData.get_skill_list();
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
		table.addRow("time", "src_agent", "dst_agent", "value", "buff_dmg", "overstack_value", "skill_id", "src_instid",
				"dst_instid", "src_master_instid", "iff", "buff", "is_crit", "is_activation", "is_buffremove",
				"is_ninety", "is_fifty", "is_moving", "is_statechange", "is_flanking");
		for (CombatItem c : combatList) {
			table.addRow(c.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());

		return output.toString();
	}

}