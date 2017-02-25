package statistics;

import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

import boon.AbstractBoon;
import boon.BoonFactory;
import data.AgentData;
import data.AgentItem;
import data.BossData;
import data.CombatData;
import data.CombatItem;
import data.SkillData;
import enums.Boon;
import enums.CustomSkill;
import enums.Result;
import enums.StateChange;
import player.BoonLog;
import player.DamageLog;
import player.Player;
import utility.FinalDpsHolder;
import utility.PhaseDpsHolder;
import utility.TableBuilder;
import utility.Utility;

public class Statistics {

	// Fields
	public static boolean willHidePlayers;
	BossData bossData;
	AgentData agentData;
	SkillData skillData;
	CombatData combatData;
	List<Player> playerList;

	// Constructor
	public Statistics(Parse parsed) {

		this.bossData = parsed.getBossData();
		this.agentData = parsed.getAgentData();
		this.skillData = parsed.getSkillData();
		this.combatData = parsed.getCombatData();

		playerList = new ArrayList<Player>();
		List<AgentItem> playerAgentList = agentData.getPlayerAgentList();
		for (AgentItem playerAgent : playerAgentList) {
			this.playerList.add(new Player(playerAgent));
		}
		// Sort players by subgroup - also added in to each function in case
		// different sorts are used in the future
		sortPlayerList();
	}

	// Final DPS
	public String getFinalDPS() {

		// Holder used to allow sorting by dps
		List<FinalDpsHolder> holder = new ArrayList<FinalDpsHolder>();

		int total_damage = 0;
		double total_dps = 0.0;
		double fight_duration = (bossData.getLastAware() - bossData.getFirstAware()) / 1000;

		for (Player p : playerList) {
			double player_damage = 0.0;
			List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatData.getCombatList());
			for (DamageLog log : damage_logs) {
				player_damage = player_damage + log.getDamage();
			}
			holder.add(new FinalDpsHolder(p, String.format("%.2f", (player_damage / fight_duration)), player_damage));

			total_dps = total_dps + (player_damage / fight_duration);
			total_damage = (int) (total_damage + player_damage);
		}

		// Sort players by damage (and therefore dps)
		holder.sort((a, b) -> (int) (b.getDamage() - a.getDamage()));

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final DPS - " + bossData.getName());

		// Header
		table.addRow("Name", "Profession", "DPS", "Damage");

		// Body
		for (int i = 0; i < holder.size(); i++) {
			FinalDpsHolder h = holder.get(i);
			Player p = h.getPlayer();
			table.addRow(p.getCharacter(), p.getProf(), h.getDps(), String.valueOf((int) h.getDamage()));
		}

		// Footer
		table.addRow("-", "-", String.format("%.2f", total_dps), String.valueOf(total_damage));
		table.addRow("-", "-", "-", String.valueOf(bossData.getHealth()));

		return table.toString();
	}

	// Phase DPS
	public String getPhaseDPS() {

		List<Point> fight_intervals = getFightIntervals();
		List<PhaseDpsHolder> holder = new ArrayList<PhaseDpsHolder>();

		for (int i = 0; i < playerList.size(); i++) {

			Player p = playerList.get(i);
			String[] phase_dps = new String[fight_intervals.size() + 1];
			double average_dps = 0;

			for (int j = 0; j < fight_intervals.size(); j++) {

				Point interval = fight_intervals.get(j);
				List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatData.getCombatList());

				double phase_damage = 0;

				for (DamageLog log : damage_logs) {
					if ((log.getTime() >= interval.x) && (log.getTime() <= interval.y)) {
						phase_damage = phase_damage + log.getDamage();
					}
				}
				double dps = phase_damage / (interval.getY() - interval.getX()) * 1000;
				average_dps = (((average_dps * j) + dps) / (j + 1));
				phase_dps[j] = String.format("%.2f", dps);
			}

			phase_dps[fight_intervals.size()] = String.format("%.2f", average_dps);
			holder.add(new PhaseDpsHolder(p, phase_dps, average_dps));
		}

		// Sort players by average dps
		holder.sort((a, b) -> (int) (b.getAverage_dps() - a.getAverage_dps()));

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Phase DPS - " + bossData.getName());

		// Header
		String[] header = new String[fight_intervals.size() + 3];
		header[0] = "Name";
		header[1] = "Profession";
		for (int i = 2; i < fight_intervals.size() + 2; i++) {
			header[i] = "Phase " + String.valueOf(i - 1);
		}
		header[header.length - 1] = "Average";
		table.addRow(header);

		// Body
		for (int i = 0; i < holder.size(); i++) {
			PhaseDpsHolder h = holder.get(i);
			Player p = h.getPlayer();
			table.addRow(
					Utility.concatStringArray(new String[] { p.getCharacter(), p.getProf() }, h.getAll_phase_dps()));
		}

		// Footer
		String[] durations = new String[fight_intervals.size() + 3];
		double total_time = 0.0;
		durations[0] = "-";
		durations[1] = "-";
		for (int i = 2; i < fight_intervals.size() + 2; i++) {
			Point p = fight_intervals.get(i - 2);
			double time = (p.getY() - p.getX()) / 1000.0;
			total_time += time;
			durations[i] = String.format("%.2f", time);
		}
		durations[durations.length - 1] = String.format("%.2f", total_time);
		table.addRow(durations);

		String[] intervals = new String[fight_intervals.size() + 3];
		intervals[0] = "-";
		intervals[1] = "-";
		for (int i = 2; i < fight_intervals.size() + 2; i++) {
			Point p = fight_intervals.get(i - 2);
			intervals[i] = "(" + String.format("%.2f", p.getX() / 1000.0) + ", "
					+ String.format("%.2f", p.getY() / 1000.0) + ")";
		}
		intervals[intervals.length - 1] = "-";
		table.addRow(intervals);

		return table.toString();
	}

	// Damage Distribution
	public String getDamageDistribution() {

		// Table
		TableBuilder table = new TableBuilder();
		StringBuilder output = new StringBuilder();
		output.append("_________________________________________" + System.lineSeparator() + System.lineSeparator());
		output.append("Damage Distribution - " + bossData.getName() + System.lineSeparator());
		output.append("_________________________________________" + System.lineSeparator());

		// Body
		for (Player p : playerList) {

			List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatData.getCombatList());
			Map<Integer, Integer> skill_damage = new HashMap<Integer, Integer>();

			for (DamageLog log : damage_logs) {
				if (skill_damage.containsKey(log.getID())) {
					skill_damage.put(log.getID(), skill_damage.get(log.getID()) + log.getDamage());
				} else {
					if (log.getID() > 0) {
						skill_damage.put(log.getID(), log.getDamage());
					}
				}
			}
			double damage_sum = skill_damage.values().stream().reduce(0, Integer::sum);

			table.clear();
			table.addTitle(p.getCharacter() + " - " + p.getProf());
			table.addRow("SKILL", "DAMAGE", "%");

			// Calculate % of each skill
			skill_damage = Utility.sortByValue(skill_damage);
			for (Map.Entry<Integer, Integer> entry : skill_damage.entrySet()) {
				String skill_name = skillData.getName(entry.getKey());
				double damage = entry.getValue();
				table.addRow(skill_name, String.valueOf((int) damage),
						String.format("%.2f", (damage / damage_sum * 100)));
			}

			// Append player table
			output.append(System.lineSeparator());
			output.append(table.toString());
		}

		return output.toString();
	}

	// Generate a graph
	public String getTotalDamageGraph(String base) {

		// Build chart
		XYChartBuilder chartBuilder = new XYChartBuilder().width(1600).height(900);
		chartBuilder.title("Total Damage - " + bossData.getName());
		chartBuilder.xAxisTitle("Time (seconds)").yAxisTitle("Damage (K)").build();
		XYChart chart = chartBuilder.build();

		// Add style to chart
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setMarkerSize(1);
		chart.getStyler().setXAxisMin(0.0);
		chart.getStyler().setYAxisMin(0.0);
		chart.getStyler().setLegendFont(new Font("Dialog", Font.PLAIN, 16));

		// Add series to chart
		for (Player p : playerList) {
			List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatData.getCombatList());
			double[] x = new double[damage_logs.size()];
			double[] y = new double[damage_logs.size()];
			double total_damage = 0.0;
			for (int i = 0; i < damage_logs.size(); i++) {
				total_damage = total_damage + damage_logs.get(i).getDamage();
				x[i] = damage_logs.get(i).getTime() / 1000.0;
				y[i] = total_damage / 1000;
			}
			chart.addSeries(p.getCharacter() + " - " + p.getProf(), x, y);
		}

		// Write chart to .png

		try {
			String file_name = "./graphs/" + base + "_" + bossData.getName() + "_TDG.png";
			BitmapEncoder.saveBitmapWithDPI(chart, file_name, BitmapFormat.PNG, 300);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return base + "_" + bossData.getName() + "_TDG.png";
	}

	// Combat Statistics
	public String getCombatStatistics() {

		// Table
		List<String[]> all_combat_stats = new ArrayList<String[]>();
		TableBuilder table = new TableBuilder();
		table.addTitle("Combat Statistics - " + bossData.getName());

		// Header
		table.addRow("Account", "Character", "Group", "Profession", "CRIT", "SCHL", "MOVE", "FLNK", "TGHN", "HEAL",
				"COND", "DOGE", "RESS", "DOWN", "DIED");

		// Sort players by subgroup
		sortPlayerList();

		// Body
		for (Player p : playerList) {

			double power_loops = 0.0, crit = 0.0, schl = 0.0, move = 0.0, flank = 0.0;
			int dodge = 0, ress = 0, down = 0, died = 0;
			boolean is_dead = false;

			List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatData.getCombatList());
			for (DamageLog log : damage_logs) {

				if (!log.isCondi()) {
					if (log.getResult().equals(Result.CRIT)) {
						crit++;
					}
					if (log.isNinety()) {
						schl++;
					}
					if (log.isMoving()) {
						move++;
					}
					if (log.isFlanking()) {
						flank++;
					}
					power_loops++;
				}
				if (log.isStatechange().equals(StateChange.CHANGE_DOWN)) {
					down++;
				} else if (!is_dead && log.isStatechange().equals(StateChange.CHANGE_DEAD)) {
					died = log.getTime();
					is_dead = true;
				}
				CustomSkill skill = CustomSkill.getEnum(log.getID());
				if (skill != null) {
					if (skill.equals(CustomSkill.DODGE)) {
						dodge++;
					} else if (skill.equals(CustomSkill.RESURRECT)) {
						ress++;
					}
				}
			}

			String[] combat_stats = new String[] { String.format("%.2f", crit / power_loops),
					String.format("%.2f", schl / power_loops), String.format("%.2f", move / power_loops),
					String.format("%.2f", flank / power_loops), String.valueOf(p.getToughness()),
					String.valueOf(p.getHealing()), String.valueOf(p.getCondition()), String.valueOf(dodge),
					String.valueOf(ress), String.valueOf(down), String.format("%.2f", (double) died / 1000) };
			all_combat_stats.add(combat_stats);
		}

		for (int i = 0; i < playerList.size(); i++) {
			Player p = playerList.get(i);
			table.addRow(Utility.concatStringArray(
					new String[] { p.getAccount(), p.getCharacter(), p.getGroup(), p.getProf() },
					all_combat_stats.get(i)));
		}
		return table.toString();
	}

	// Final Boons
	public String getFinalBoons() {

		List<String> boon_list = Boon.getList();
		BoonFactory boonFactory = new BoonFactory();
		List<String[]> all_rates = new ArrayList<String[]>();

		// Sort players by subgroup
		sortPlayerList();

		for (int i = 0; i < playerList.size(); i++) {
			Player p = playerList.get(i);
			Map<String, List<BoonLog>> boon_logs = p.getBoonMap(bossData, skillData, combatData.getCombatList());
			String[] rates = new String[boon_list.size()];
			for (int j = 0; j < boon_list.size(); j++) {
				Boon boon = Boon.getEnum(boon_list.get(j));
				AbstractBoon boon_object = boonFactory.makeBoon(boon);
				List<BoonLog> logs = boon_logs.get(boon.getName());
				String rate = "0.00";
				if (!logs.isEmpty()) {
					if (boon.getType().equals("duration")) {
						rate = getBoonDuration(getBoonIntervalsList(boon_object, logs));
					} else if (boon.getType().equals("intensity")) {
						rate = getAverageStacks(getBoonStacksList(boon_object, logs));
					}
				}
				rates[j] = rate;
			}
			all_rates.add(rates);
		}

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final Boon Rates - " + bossData.getName());

		// Header
		String[] boon_array = Boon.getArray();
		table.addRow(Utility.concatStringArray(new String[] { "Name", "Profession" }, boon_array));

		// Body
		for (int i = 0; i < playerList.size(); i++) {
			Player p = playerList.get(i);
			table.addRow(Utility.concatStringArray(new String[] { p.getCharacter(), p.getProf() }, all_rates.get(i)));
		}

		return table.toString();
	}

	// Phase Boons
	public String getPhaseBoons() {

		List<String> boon_list = Boon.getList();
		BoonFactory boonFactory = new BoonFactory();
		List<String[][]> all_rates = new ArrayList<String[][]>();
		List<Point> fight_intervals = getFightIntervals();

		// Sort players by subgroup
		sortPlayerList();

		for (int i = 0; i < playerList.size(); i++) {

			Player p = playerList.get(i);
			Map<String, List<BoonLog>> boon_logs = p.getBoonMap(bossData, skillData, combatData.getCombatList());

			String[][] rates = new String[boon_logs.size()][];

			for (int j = 0; j < boon_list.size(); j++) {

				Boon boon = Boon.getEnum(boon_list.get(j));
				String[] rate = new String[fight_intervals.size()];
				Arrays.fill(rate, "0.00");

				List<BoonLog> logs = boon_logs.get(boon.getName());

				if (!logs.isEmpty()) {
					AbstractBoon boon_object = boonFactory.makeBoon(boon);
					if (boon.getType().equals("duration")) {
						List<Point> boon_intervals = getBoonIntervalsList(boon_object, logs);
						rate = getBoonDuration(boon_intervals, fight_intervals);
					} else if (boon.getType().equals("intensity")) {
						List<Integer> boon_stacks = getBoonStacksList(boon_object, logs);
						rate = getAverageStacks(boon_stacks, fight_intervals);
					}
				}
				rates[j] = rate;
			}
			all_rates.add(rates);
		}

		StringBuilder output = new StringBuilder();
		TableBuilder table = new TableBuilder();
		String[] boon_array = Boon.getArray();

		output.append("_________________________________________" + System.lineSeparator() + System.lineSeparator());
		output.append("Phase - " + bossData.getName() + System.lineSeparator());
		output.append("_________________________________________" + System.lineSeparator());

		for (int i = 0; i < fight_intervals.size(); i++) {

			table.clear();
			table.addTitle("Phase " + (i + 1));
			table.addRow(Utility.concatStringArray(new String[] { "Name", "Profession" }, boon_array));
			for (int j = 0; j < playerList.size(); j++) {
				Player p = playerList.get(j);

				String[][] player_rates = all_rates.get(j);
				String[] row_rates = new String[boon_array.length];
				for (int k = 0; k < boon_array.length; k++) {
					row_rates[k] = player_rates[k][i];
				}
				table.addRow(Utility.concatStringArray(new String[] { p.getCharacter(), p.getProf() }, row_rates));
			}

			output.append(System.lineSeparator() + table.toString());

		}

		return output.toString();
	}

	// Private Methods
	private List<Point> getFightIntervals() {

		List<Point> fight_intervals = new ArrayList<Point>();
		List<CombatItem> combatList = combatData.getCombatList();
		int combatStart = combatList.get(0).getTime();
		int timeStart = bossData.getFirstAware() - combatStart;

		int i_count;
		int t_invuln;

		if (bossData.getName().equals("Vale Guardian")) {
			i_count = 2;
			t_invuln = 20000;
		} else if (bossData.getName().equals("Gorseval the Multifarious")) {
			i_count = 2;
			t_invuln = 30000;
		} else if (bossData.getName().equals("Sabetha the Saboteur")) {
			i_count = 3;
			t_invuln = 25000;
		} else if (bossData.getName().equals("Xera")) {
			i_count = 1;
			t_invuln = 60000;
		} else if (bossData.getName().equals("Slothasor")) {
			i_count = 5;
			t_invuln = 7000;
		} else if (bossData.getName().equals("Samarog")) {
			i_count = 2;
			t_invuln = 20000;
		} else if (bossData.getName().equals("Keep Construct")) {
			int t_curr = 0;
			int t_prev = timeStart;
			// Flag to show first phase before burn phases
			boolean first_phase_flag = true;
			// Flag to prevent multiple invuln phases causing more intervals
			boolean same_phase_flag = false;
			for (CombatItem c : combatList) {
				t_curr = c.getTime();
				if (c.getSrcAgent() == bossData.getAgent()) {
					// Start of invulnerability (757 is invulnerability skill
					// id)
					if (c.getSkillID() == 757 && c.getDstAgent() == bossData.getAgent()) {
						if (first_phase_flag) {
							fight_intervals.add(new Point(t_prev, t_curr - combatStart));
							first_phase_flag = false;
						}
						// End of invulnerability (DstAgent is not set)
					} else if (c.getSkillID() == 757) {
						if (!same_phase_flag) {
							t_prev = t_curr - combatStart;
						}
						same_phase_flag = true;
						// Start of red/white orb phase (35025 is Xera's Boon
						// skill id)
					} else if (c.getSkillID() == 35025 && c.getDstAgent() == bossData.getAgent()) {
						fight_intervals.add(new Point(t_prev, t_curr - combatStart));
						same_phase_flag = false;
						// End of red/white orb phase
					} else if (c.getSkillID() == 35025) {
						t_prev = t_curr - combatStart;
					}
				}
			}
			// Add last burn phase
			fight_intervals.add(new Point(t_prev, bossData.getLastAware() - combatStart));
			return fight_intervals;
		} else {
			fight_intervals.add(new Point(timeStart, bossData.getLastAware() - bossData.getFirstAware()));
			return fight_intervals;
		}

		// Get the interval when the boss is invulnerable
		List<List<Point>> i_intervals = new ArrayList<List<Point>>();

		for (Player p : playerList) {
			List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatList);
			int t_curr = 0;
			int t_prev = 0;
			List<Point> player_intervals = new ArrayList<Point>();
			for (DamageLog log : damage_logs) {
				if (!log.isCondi()) {
					t_curr = log.getTime();
					if ((t_curr - t_prev) > t_invuln) {
						player_intervals.add(new Point(t_prev, t_curr));
					}
					t_prev = t_curr;
				}
			}
			if (player_intervals.size() == i_count) {
				i_intervals.add(player_intervals);
			}

		}

		// Derive the fight intervals
		List<Point> real_fight_intervals = new ArrayList<Point>();

		for (int i = 0; i < i_count; i++) {
			fight_intervals.add(new Point(timeStart, bossData.getLastAware() - bossData.getFirstAware()));
			real_fight_intervals.add(new Point(timeStart, bossData.getLastAware() - bossData.getFirstAware()));
		}
		real_fight_intervals.add(new Point(0, bossData.getLastAware() - bossData.getFirstAware()));
		for (List<Point> player_intervals : i_intervals) {
			for (int i = 0; i < i_count; i++) {
				Point new_point = player_intervals.get(i);
				Point old_point = fight_intervals.get(i);
				int t_begin = new_point.x;
				int t_end = new_point.y;
				if (t_begin > old_point.x) {
					old_point.x = t_begin;
				}
				if (t_end < old_point.y) {
					old_point.y = t_end;
				}
			}
		}
		// Shift points to the right
		for (int i = 0; i < real_fight_intervals.size(); i++) {
			// Start
			if (i == 0) {
				real_fight_intervals.get(i).y = fight_intervals.get(i).x;
			}
			// End
			else if ((i + 1) == real_fight_intervals.size()) {
				real_fight_intervals.get(i).x = fight_intervals.get(i - 1).y;
			}
			// Middle
			else {
				real_fight_intervals.get(i).x = fight_intervals.get(i - 1).y;
				real_fight_intervals.get(i).y = fight_intervals.get(i).x;
			}
		}

		return real_fight_intervals;
	}

	private List<Point> getBoonIntervalsList(AbstractBoon boon, List<BoonLog> boon_logs) {

		// Initialise variables
		int t_prev = 0;
		int t_curr = 0;
		List<Point> boon_intervals = new ArrayList<Point>();

		// Loop: update then add durations
		for (BoonLog log : boon_logs) {
			t_curr = log.getTime();
			boon.update(t_curr - t_prev);
			boon.add(log.getValue());
			boon_intervals.add(new Point(t_curr, t_curr + boon.getStackValue()));
			t_prev = t_curr;
		}

		// Merge intervals
		boon_intervals = Utility.mergeIntervals(boon_intervals);

		// Trim duration overflow
		int fight_duration = bossData.getLastAware() - bossData.getFirstAware();
		int last = boon_intervals.size() - 1;
		if (boon_intervals.get(last).getY() > fight_duration) {
			boon_intervals.get(last).y = fight_duration;
		}

		return boon_intervals;
	}

	private String getBoonDuration(List<Point> boon_intervals) {

		// Calculate average duration
		double average_duration = 0;
		for (Point p : boon_intervals) {
			average_duration = average_duration + (p.getY() - p.getX());
		}
		return String.format("%.2f", (average_duration / (bossData.getLastAware() - bossData.getFirstAware())));
	}

	private String[] getBoonDuration(List<Point> boon_intervals, List<Point> fight_intervals) {

		// Phase durations
		String[] phase_durations = new String[fight_intervals.size()];

		// Loop: add intervals in between, merge, calculate duration
		for (int i = 0; i < fight_intervals.size(); i++) {
			Point p = fight_intervals.get(i);
			List<Point> boons_intervals_during_phase = new ArrayList<Point>();
			for (Point b : boon_intervals) {
				if (b.x < p.y && p.x < b.y) {
					if (p.x <= b.x && b.y <= p.y) {
						boons_intervals_during_phase.add(b);
					} else if (b.x < p.x && p.y < b.y) {
						boons_intervals_during_phase.add(p);
					} else if (b.x < p.x && b.y <= p.y) {
						boons_intervals_during_phase.add(new Point(p.x, b.y));
					} else if (p.x <= b.x && p.y < b.y) {
						boons_intervals_during_phase.add(new Point(b.x, p.y));
					}
				}
			}
			double duration = 0;
			for (Point b : boons_intervals_during_phase) {
				duration = duration + (b.getY() - b.getX());
			}
			phase_durations[i] = String.format("%.2f", (duration / (p.getY() - p.getX())));
		}

		return phase_durations;
	}

	private List<Integer> getBoonStacksList(AbstractBoon boon, List<BoonLog> boon_logs) {

		// Initialise variables
		int t_prev = 0;
		int t_curr = 0;
		List<Integer> boon_stacks = new ArrayList<Integer>();
		boon_stacks.add(0);

		// Loop: fill, update, and add to stacks
		for (BoonLog log : boon_logs) {
			t_curr = log.getTime();
			boon.addStacksBetween(boon_stacks, t_curr - t_prev);
			boon.update(t_curr - t_prev);
			boon.add(log.getValue());
			if (t_curr != t_prev) {
				boon_stacks.add(boon.getStackValue());
			} else {
				boon_stacks.set(boon_stacks.size() - 1, boon.getStackValue());
			}
			t_prev = t_curr;
		}

		// Fill in remaining stacks
		boon.addStacksBetween(boon_stacks, bossData.getLastAware() - bossData.getFirstAware() - t_prev);
		boon.update(1);
		boon_stacks.add(boon.getStackValue());
		return boon_stacks;
	}

	private String getAverageStacks(List<Integer> boon_stacks) {

		// Calculate average stacks
		double average_stacks = boon_stacks.stream().mapToInt(Integer::intValue).sum();
		return String.format("%.2f", average_stacks / boon_stacks.size());
	}

	private String[] getAverageStacks(List<Integer> boon_stacks, List<Point> fight_intervals) {

		// Phase stacks
		String[] phase_stacks = new String[fight_intervals.size()];

		// Loop: get sublist and calculate average stacks
		for (int i = 0; i < fight_intervals.size(); i++) {
			Point p = fight_intervals.get(i);
			List<Integer> phase_boon_stacks = new ArrayList<Integer>(boon_stacks.subList(p.x, p.y));
			double average_stacks = phase_boon_stacks.stream().mapToInt(Integer::intValue).sum();
			phase_stacks[i] = String.format("%.2f", average_stacks / phase_boon_stacks.size());
		}
		return phase_stacks;
	}

	// resorts the player list by subgroup - checks for "N/A" for logs before
	// subgroups were added
	private void sortPlayerList() {
		playerList.sort((a, b) -> Integer.parseInt(a.getGroup() != "N/A" ? a.getGroup() : "0")
				- Integer.parseInt(b.getGroup() != "N/A" ? b.getGroup() : "0"));
	}

}
