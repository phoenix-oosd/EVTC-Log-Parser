package data;

import java.util.ArrayList;
import java.util.List;

import enums.Agent;

public class AgentData {

	// Fields
	private List<AgentItem> player_agent_list = new ArrayList<AgentItem>();
	private List<AgentItem> NPC_agent_list = new ArrayList<AgentItem>();
	private List<AgentItem> gadget_agent_list = new ArrayList<AgentItem>();
	private List<AgentItem> all_agents_list = new ArrayList<AgentItem>();

	// Constructors
	public AgentData() {
	}

	// Public Methods
	public void addItem(Agent agent, AgentItem item) {
		if (agent.equals(Agent.NPC)) {
			NPC_agent_list.add(item);
		} else if (agent.equals(Agent.GADGET)) {
			gadget_agent_list.add(item);
		} else {
			player_agent_list.add(item);
		}
		all_agents_list.add(item);
	}

	// Getters
	public List<AgentItem> getPlayerAgentList() {
		return player_agent_list;
	}

	public List<AgentItem> getNPCAgentList() {
		return NPC_agent_list;
	}

	public List<AgentItem> getGadgetAgentList() {
		return gadget_agent_list;
	}

	public List<AgentItem> getAllAgentsList() {
		return all_agents_list;
	}

}
