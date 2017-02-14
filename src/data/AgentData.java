package data;

import java.util.ArrayList;
import java.util.List;

import enums.Agent;

public class AgentData {

	// Fields
	private List<AgentItem> playerAgents;
	private List<AgentItem> NPCAgents;
	private List<AgentItem> gadgetAgents;

	// Constructors
	public AgentData() {
		this.playerAgents = new ArrayList<AgentItem>();
		this.NPCAgents = new ArrayList<AgentItem>();
		this.gadgetAgents = new ArrayList<AgentItem>();
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
	}

	public void fillMissingData(List<CombatItem> combatList) {
		for (CombatItem c : combatList) {
			if (c.get_src_instid() != 0 || c.get_dst_instid() != 0) {
				for (AgentItem player : playerAgents) {
					if (!player.isSet()) {
						if (player.getAgent() == c.get_src_agent()) {
							player.setFirstAppeared(c.get_time());
							player.setinstid(c.get_src_instid());
							continue;
						} else if (player.getAgent() == c.get_dst_agent()) {
							player.setFirstAppeared(c.get_time());
							player.setinstid(c.get_dst_instid());
							continue;
						}
					}
				}
				for (AgentItem npc : NPCAgents) {
					if (!npc.isSet()) {
						if (npc.getAgent() == c.get_src_agent()) {
							npc.setFirstAppeared(c.get_time());
							npc.setinstid(c.get_src_instid());
							continue;
						} else if (npc.getAgent() == c.get_dst_agent()) {
							npc.setFirstAppeared(c.get_time());
							npc.setinstid(c.get_dst_instid());
							continue;
						}
					}
				}
				for (AgentItem gadget : gadgetAgents) {
					if (!gadget.isSet()) {
						if (gadget.getAgent() == c.get_src_agent()) {
							gadget.setFirstAppeared(c.get_time());
							gadget.setinstid(c.get_src_instid());
							continue;
						} else if (gadget.getAgent() == c.get_dst_agent()) {
							gadget.setFirstAppeared(c.get_time());
							gadget.setinstid(c.get_dst_instid());
							continue;
						}
					}
				}
			}
		}
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

}
