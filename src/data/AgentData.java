package data;

import java.util.ArrayList;
import java.util.List;

import enums.Agent;

public class AgentData {

	// Fields
	private List<AgentItem> playerAgents;
	private List<AgentItem> NPCAgents;
	private List<AgentItem> gadgetAgents;
	private List<AgentItem> allAgents;

	// Constructors
	public AgentData() {
		this.playerAgents = new ArrayList<AgentItem>();
		this.NPCAgents = new ArrayList<AgentItem>();
		this.gadgetAgents = new ArrayList<AgentItem>();
		this.allAgents = new ArrayList<AgentItem>();
	}

	// Public Methods
	public void addItem(Agent agent, AgentItem item) {
		if (agent.equals(Agent.NPC)) {
			NPCAgents.add(item);
		} else if (agent.equals(Agent.GADGET)) {
			gadgetAgents.add(item);
		} else {
			playerAgents.add(item);
		}
		allAgents.add(item);
	}

	// Getters
	public List<AgentItem> getPlayerAgents() {
		return playerAgents;
	}

	public List<AgentItem> getNPCAgents() {
		return NPCAgents;
	}

	public List<AgentItem> getGadgetAgents() {
		return gadgetAgents;
	}

	public List<AgentItem> getAllAgents() {
		return allAgents;
	}

}
