package statistics;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import data.AgentData;
import data.AgentItem;
import data.BossData;
import data.CombatData;
import data.CombatItem;
import data.LogData;
import data.SkillData;
import data.SkillItem;
import enums.Activation;
import enums.Agent;
import enums.IFF;
import enums.Result;
import enums.StateChange;
import utility.TableBuilder;
import utility.Utility;

public class Parse
{
	// Fields
	private BufferedInputStream f = null;
	private LogData log_data;
	private BossData boss_data;
	private AgentData agent_data = new AgentData();
	private SkillData skill_data = new SkillData();
	private CombatData combat_data = new CombatData();

	// Constructor
	public Parse(String file_path) throws IOException
	{

		// Read .evtc file into buffered input stream
		ZipFile zip_file = null;
		if (file_path.endsWith(".zip"))
		{
			zip_file = new ZipFile(file_path);
			ZipEntry evtc_file = zip_file.entries().nextElement();
			f = new BufferedInputStream(zip_file.getInputStream(evtc_file));
		}
		else if (file_path.endsWith(".evtc"))
		{
			f = new BufferedInputStream(new FileInputStream(new File(file_path)));
		}

		// Parse file
		try
		{
			parseBossData();
			parseAgentData();
			parseSkillData();
			parseCombatList();
			fillMissingData();
		}

		// Close streams
		finally
		{
			try
			{
				if (zip_file != null)
				{
					zip_file.close();
				}
				f.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// Public Methods
	public LogData getLogData()
	{
		return log_data;
	}

	public BossData getBossData()
	{
		return boss_data;
	}

	public AgentData getAgentData()
	{
		return agent_data;
	}

	public SkillData getSkillData()
	{
		return skill_data;
	}

	public CombatData getCombatData()
	{
		return combat_data;
	}

	// Private Methods
	private void parseBossData() throws IOException
	{
		// 12 bytes: arc build version
		String build_version = getString(12);
		this.log_data = new LogData(build_version);

		// 1 byte: skip
		safeSkip(1);

		// 2 bytes: boss instance ID
		int instid = getShort();

		// 1 byte: position
		safeSkip(1);

		// BossData
		this.boss_data = new BossData(instid);
	}

	private void parseAgentData() throws IOException
	{
		// 4 bytes: player count
		int player_count = getInt();

		// 96 bytes: each player
		for (int i = 0; i < player_count; i++)
		{
			// 8 bytes: agent
			long agent = getLong();

			// 4 bytes: profession
			int prof = getInt();

			// 4 bytes: is_elite
			int is_elite = getInt();

			// 4 bytes: toughness
			int toughness = getInt();

			// 4 bytes: healing
			int healing = getInt();

			// 4 bytes: condition
			int condition = getInt();

			// 68 bytes: name
			String name = getString(68);

			// Agent
			Agent a = Agent.getEnum(prof, is_elite);

			// Add an agent
			if (a != null)
			{
				// NPC
				if (a.equals(Agent.NPC))
				{
					agent_data.addItem(a, new AgentItem(agent, name, a.getName() + ":" + String.format("%05d", prof)));
				}
				// Gadget
				else if (a.equals(Agent.GADGET))
				{
					agent_data.addItem(a, new AgentItem(agent, name, a.getName()));
				}
				// Player
				else
				{
					agent_data.addItem(a, new AgentItem(agent, name, a.getName(), toughness, healing, condition));
				}
			}
			// Unknown
			else
			{
				agent_data.addItem(a, new AgentItem(agent, name, String.valueOf(prof), toughness, healing, condition));
			}
		}
	}

	private void parseSkillData() throws IOException
	{
		// 4 bytes: player count
		int skill_count = getInt();

		// 68 bytes: each skill
		for (int i = 0; i < skill_count; i++)
		{
			// 4 bytes: skill ID
			int skill_id = getInt();

			// 64 bytes: name
			String name = getString(64);

			// Add skill
			skill_data.addItem(new SkillItem(skill_id, name));
		}
	}

	private void parseCombatList() throws IOException
	{
		// 64 bytes: each combat
		while (f.available() >= 64)
		{
			// 8 bytes: time
			int time = (int) getLong();

			// 8 bytes: src_agent
			long src_agent = getLong();

			// 8 bytes: dst_agent
			long dst_agent = getLong();

			// 4 bytes: value
			int value = getInt();

			// 4 bytes: buff_dmg
			int buff_dmg = getInt();

			// 2 bytes: overstack_value
			int overstack_value = getShort();

			// 2 bytes: skill_id
			int skill_id = getShort();

			// 2 bytes: src_instid
			int src_instid = getShort();

			// 2 bytes: dst_instid
			int dst_instid = getShort();

			// 2 bytes: src_master_instid
			int src_master_instid = getShort();

			// 9 bytes: garbage
			safeSkip(9);

			// 1 byte: iff
			IFF iff = IFF.getEnum(f.read());

			// 1 byte: buff
			boolean buff = Utility.toBool(f.read());

			// 1 byte: result
			Result result = Result.getEnum(f.read());

			// 1 byte: is_activation
			Activation is_activation = Activation.getEnum(f.read());

			// 1 byte: is_buffremove
			boolean is_buffremove = Utility.toBool(f.read());

			// 1 byte: is_ninety
			boolean is_ninety = Utility.toBool(f.read());

			// 1 byte: is_fifty
			boolean is_fifty = Utility.toBool(f.read());

			// 1 byte: is_moving
			boolean is_moving = Utility.toBool(f.read());

			// 1 byte: is_statechange
			StateChange is_statechange = StateChange.getEnum(f.read());

			// 1 byte: is_flanking
			boolean is_flanking = Utility.toBool(f.read());

			// 3 bytes: garbage
			safeSkip(3);

			// Add combat
			combat_data.addItem(new CombatItem(time, src_agent, dst_agent, value, buff_dmg, overstack_value, skill_id,
					src_instid, dst_instid, src_master_instid, iff, buff, result, is_activation, is_buffremove,
					is_ninety, is_fifty, is_moving, is_statechange, is_flanking));
		}
	}

	private void fillMissingData()
	{
		// Set Agent instid, first_aware and last_aware
		List<AgentItem> agent_list = agent_data.getAllAgentsList();
		List<CombatItem> combat_list = combat_data.getCombatList();
		for (AgentItem a : agent_list)
		{
			boolean assigned_first = false;
			for (CombatItem c : combat_list)
			{
				if (a.getAgent() == c.getSrcAgent() && c.getSrcInstid() != 0)
				{
					if (!assigned_first)
					{
						a.setInstid(c.getSrcInstid());
						a.setFirstAware(c.getTime());
						assigned_first = true;
					}
					a.setLastAware(c.getTime());
				}
				else if (a.getAgent() == c.getDstAgent() && c.getDstInstid() != 0)
				{
					if (!assigned_first)
					{
						a.setInstid(c.getDstInstid());
						a.setFirstAware(c.getTime());
						assigned_first = true;
					}
					a.setLastAware(c.getTime());
				}
				else if (c.isStateChange().equals(StateChange.LOG_START))
				{
					log_data.setLogStart(c.getValue());
				}
				else if (c.isStateChange().equals(StateChange.LOG_END))
				{
					log_data.setLogEnd(c.getValue());
				}
			}
		}

		// Set Boss data agent, instid, first_aware, last_aware and name
		List<AgentItem> NPC_list = agent_data.getNPCAgentList();
		for (AgentItem NPC : NPC_list)
		{
			// An automatic log
			if (NPC.getProf().endsWith(String.valueOf(boss_data.getID())))
			{
				if (boss_data.getAgent() == 0)
				{
					boss_data.setAgent(NPC.getAgent());
					boss_data.setInstid(NPC.getInstid());
					boss_data.setFirstAware(NPC.getFirstAware());
					boss_data.setName(NPC.getName());
				}
				boss_data.setLastAware(NPC.getLastAware());
			}
			// A manual log
			else if (boss_data.getID() == 1)
			{
				boss_data.setAgent(NPC.getAgent());
				boss_data.setInstid(NPC.getInstid());
				boss_data.setFirstAware(NPC.getFirstAware());
				boss_data.setName(NPC.getName());
				boss_data.setLastAware(NPC.getLastAware());
				break;
			}
		}

		// Set Boss health
		for (CombatItem c : combat_list)
		{
			if (c.getSrcInstid() == boss_data.getInstid() && c.isStateChange().equals(StateChange.MAX_HEALTH_UPDATE))
			{
				boss_data.setHealth((int) c.getDstAgent());
				break;
			}
		}

		// Dealing with second half of Xera
		int xera_2_instid = 0;
		for (AgentItem NPC : NPC_list)
		{
			if (NPC.getProf().contains("16286"))
			{
				xera_2_instid = NPC.getInstid();
				boss_data.setLastAware(NPC.getLastAware());
				for (CombatItem c : combat_list)
				{
					if (c.getSrcInstid() == xera_2_instid)
					{
						c.setSrcInstid(boss_data.getInstid());
					}
					if (c.getDstInstid() == xera_2_instid)
					{
						c.setDstInstid(boss_data.getInstid());
					}
				}
				break;
			}
		}

	}

	// Override
	@Override
	public String toString()
	{

		// Build tables
		StringBuilder output = new StringBuilder();
		TableBuilder table = new TableBuilder();

		// Log Data Table
		table.addTitle("LOG DATA");
		table.addRow("build_version", "log_start", "log_end");
		table.addRow(log_data.toStringArray());
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Boss Data Table
		table.addTitle("BOSS DATA");
		table.addRow("agent", "instid", "first_aware", "last_aware", "id", "name", "health");
		table.addRow(boss_data.toStringArray());
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Player Data
		List<AgentItem> playerAgents = agent_data.getPlayerAgentList();
		List<AgentItem> NPCAgents = agent_data.getNPCAgentList();
		List<AgentItem> gadgetAgents = agent_data.getGadgetAgentList();
		table.addTitle("AGENT DATA");
		table.addRow("agent", "instid", "first_aware", "last_aware", "name", "prof", "toughness", "healing",
				"condition");
		for (AgentItem player : playerAgents)
		{
			table.addRow(player.toStringArray());
		}
		for (AgentItem npc : NPCAgents)
		{
			table.addRow(npc.toStringArray());
		}
		for (AgentItem gadget : gadgetAgents)
		{
			table.addRow(gadget.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Skill Data
		List<SkillItem> skillList = skill_data.getSkillList();
		table.addTitle("SKILL DATA");
		table.addRow("ID", "name");
		for (SkillItem s : skillList)
		{
			table.addRow(s.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());
		table.clear();

		// Combat Data Table
		List<CombatItem> combatList = combat_data.getCombatList();
		table.addTitle("COMBAT DATA");
		table.addRow("time", "src_agent", "dst_agent", "value", "buff_dmg", "overstack_value", "skill_id", "src_instid",
				"dst_instid", "src_master_instid", "iff", "buff", "is_crit", "is_activation", "is_buffremove",
				"is_ninety", "is_fifty", "is_moving", "is_statechange", "is_flanking");
		for (CombatItem c : combatList)
		{
			table.addRow(c.toStringArray());
		}
		output.append(table.toString() + System.lineSeparator());

		return output.toString();
	}

	// Private Methods
	private void safeSkip(long bytes_to_skip) throws IOException
	{
		while (bytes_to_skip > 0)
		{
			long bytes_actually_skipped = f.skip(bytes_to_skip);
			if (bytes_actually_skipped > 0)
			{
				bytes_to_skip -= bytes_actually_skipped;
			}
			else if (bytes_actually_skipped == 0)
			{
				if (f.read() == -1)
				{
					break;
				}
				else
				{
					bytes_to_skip--;
				}
			}
		}
	}

	private int getShort() throws IOException
	{
		byte[] bytes = new byte[2];
		f.read(bytes);
		return Short.toUnsignedInt(ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}

	private int getInt() throws IOException
	{
		byte[] bytes = new byte[4];
		f.read(bytes);
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	private long getLong() throws IOException
	{
		byte[] bytes = new byte[8];
		f.read(bytes);
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getLong();
	}

	private String getString(int length) throws IOException
	{
		byte[] bytes = new byte[length];
		f.read(bytes);
		try
		{
			return new String(bytes, "UTF-8").trim();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return "UNKNOWN";
	}

}