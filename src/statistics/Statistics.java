package statistics;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

import data.AgentData;
import data.AgentItem;
import data.BossData;
import data.CombatData;
import data.SkillData;
import player.DamageLog;
import player.Player;
import utility.TableBuilder;
import utility.Utility;

public class Statistics {

	// Fields
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
		List<AgentItem> playerAgentList = agentData.getPlayerAgents();
		for (AgentItem playerAgent : playerAgentList) {
			this.playerList.add(new Player(playerAgent));
		}
	}

	// Final DPS
	public String get_final_dps() {

		List<String> dps = new ArrayList<String>();
		List<String> dmg = new ArrayList<String>();

		int total_damage = 0;
		double total_dps = 0.0;
		double fight_duration = (bossData.getFightEnd() - bossData.getFightStart()) / 1000;

		for (Player p : playerList) {
			double player_damage = 0.0;
			List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatData.getCombatData());
			for (DamageLog log : damage_logs) {
				player_damage = player_damage + log.getDamage();
			}

			dps.add(String.format("%.2f", (player_damage / fight_duration)));
			dmg.add(String.valueOf((int) player_damage));

			total_dps = total_dps + (player_damage / fight_duration);
			total_damage = (int) (total_damage + player_damage);
		}

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final DPS - " + bossData.getName());

		// Header
		table.addRow("Name", "Profession", "DPS", "Damage");

		// Body
		for (int i = 0; i < playerList.size(); i++) {
			AgentItem p = playerList.get(i).getAgent();
			table.addRow(p.getName(), p.getProf(), dps.get(i), dmg.get(i));
		}

		// Footer
		table.addRow("-", "-", String.format("%.2f", total_dps), String.valueOf(total_damage));
		table.addRow("-", "-", "-", String.valueOf(bossData.getHP()));

		return table.toString();
	}

	public String get_damage_distribution() {

		// Damage Distribution
		TableBuilder table = new TableBuilder();
		StringBuilder output = new StringBuilder();
		output.append("_________________________________________" + System.lineSeparator() + System.lineSeparator());
		output.append("Damage Distribution - " + bossData.getName() + System.lineSeparator());
		output.append("_________________________________________" + System.lineSeparator());

		//
		for (Player p : playerList) {

			List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatData.getCombatData());
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

			// Table
			table.clear();
			table.addTitle(p.getAgent().getName() + " - " + p.getAgent().getProf());
			table.addRow("SKILL", "DAMAGE", "%");
			skill_damage = Utility.sortByValue(skill_damage);
			for (Map.Entry<Integer, Integer> entry : skill_damage.entrySet()) {
				String skill_name = skillData.getName(entry.getKey());
				double damage = entry.getValue();
				table.addRow(skill_name, String.valueOf((int) damage),
						String.format("%.2f", (damage / damage_sum * 100)));
			}
			output.append(System.lineSeparator());
			output.append(table.toString());
		}

		return output.toString();
	}

	// Generate a graph
	public String get_total_damage_graph(String base) {

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
			List<DamageLog> damage_logs = p.getOutBossDamage(bossData, combatData.getCombatData());
			double[] x = new double[damage_logs.size()];
			double[] y = new double[damage_logs.size()];
			double total_damage = 0.0;
			for (int i = 0; i < damage_logs.size(); i++) {
				total_damage = total_damage + damage_logs.get(i).getDamage();
				x[i] = damage_logs.get(i).getTime() / 1000.0;
				y[i] = total_damage / 1000;
			}
			chart.addSeries(p.getAgent().getName() + " - " + p.getAgent().getProf(), x, y);
		}

		// Write chart to .png
		try {
			String file_name = "./graphs/" + base + "_" + bossData.getName() + "TDG.png";
			BitmapEncoder.saveBitmapWithDPI(chart, file_name, BitmapFormat.PNG, 300);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return base + "_" + bossData.getName() + ".png";
	}

	// public String get_phase_dps() {
	//
	// // Phase DPS
	// List<Point> fight_intervals = get_fight_intervals();
	// List<String[]> all_phase_dps = new ArrayList<String[]>();
	//
	// for (int i = 0; i < playerList.size(); i++) {
	//
	// Player p = playerList.get(i);
	// String[] phase_dps = new String[fight_intervals.size() + 1];
	// double average_dps = 0;
	//
	// for (int j = 0; j < fight_intervals.size(); j++) {
	//
	// Point interval = fight_intervals.get(j);
	// List<DamageLog> damage_logs = p.get_damage_logs();
	//
	// double phase_damage = 0;
	//
	// for (DamageLog log : damage_logs) {
	// if ((log.getTime() >= interval.x) && (log.getTime() <= interval.y)) {
	// phase_damage = phase_damage + log.getDamage();
	// }
	// }
	// double dps = phase_damage / (interval.getY() - interval.getX()) * 1000;
	// average_dps = (((average_dps * j) + dps) / (j + 1));
	// phase_dps[j] = String.format("%.2f", dps);
	// }
	//
	// phase_dps[fight_intervals.size()] = String.format("%.2f", average_dps);
	// all_phase_dps.add(phase_dps);
	// }
	//
	// // Table
	// TableBuilder table = new TableBuilder();
	// table.addTitle("Phase DPS - " + bossData.getName());
	//
	// // Header
	// String[] header = new String[fight_intervals.size() + 3];
	// header[0] = "Name";
	// header[1] = "Profession";
	// for (int i = 2; i < fight_intervals.size() + 2; i++) {
	// header[i] = "Phase " + String.valueOf(i - 1);
	// }
	// header[header.length - 1] = "Average";
	// table.addRow(header);
	//
	// // Body
	// for (int i = 0; i < playerList.size(); i++) {
	// Player p = playerList.get(i);
	// table.addRow(Utility.concatStringArray(new String[] { p.getName(),
	// p.getProf() }, all_phase_dps.get(i)));
	// }
	//
	// // Footer
	// String[] durations = new String[fight_intervals.size() + 3];
	// double total_time = 0.0;
	// durations[0] = "-";
	// durations[1] = "-";
	// for (int i = 2; i < fight_intervals.size() + 2; i++) {
	// Point p = fight_intervals.get(i - 2);
	// double time = (p.getY() - p.getX()) / 1000.0;
	// total_time += time;
	// durations[i] = String.format("%.2f", time);
	// }
	// durations[durations.length - 1] = String.format("%.2f", total_time);
	// table.addRow(durations);
	//
	// String[] intervals = new String[fight_intervals.size() + 3];
	// intervals[0] = "-";
	// intervals[1] = "-";
	// for (int i = 2; i < fight_intervals.size() + 2; i++) {
	// Point p = fight_intervals.get(i - 2);
	// intervals[i] = "(" + String.format("%.2f", p.getX() / 1000.0) + ", "
	// + String.format("%.2f", p.getY() / 1000.0) + ")";
	// }
	// intervals[intervals.length - 1] = "-";
	// table.addRow(intervals);
	//
	// return table.toString();
	// }
	//

	//

	//
	// public String get_combat_stats() {
	//
	// // Combat Statistics
	// List<String[]> all_combat_stats = new ArrayList<String[]>();
	//
	// for (Player p : playerList) {
	// List<DamageLog> damage_logs = p.get_damage_logs();
	// double i = 0.0, crit = 0.0, schl = 0.0, move = 0.0;
	// int down = 0, died = 0;
	// boolean is_dead = false;
	//
	// for (DamageLog log : damage_logs) {
	//
	// if (!log.is_condi()) {
	// if (Result.getEnum(log.get_result()).equals(Result.CRIT)) {
	// crit++;
	// }
	// if (log.is_ninety()) {
	// schl++;
	// }
	// if (log.is_moving()) {
	// move++;
	// }
	// i++;
	// }
	// if (log.is_statechange() == 5) {
	// down++;
	// } else if (!is_dead && log.is_statechange() == 4) {
	// died = log.getTime();
	// is_dead = true;
	// }
	// }
	//
	// String[] combat_stats = new String[] { String.format("%.2f", crit / i),
	// String.format("%.2f", schl / i),
	// String.format("%.2f", move / i), String.valueOf(p.getToughness()),
	// String.valueOf(p.getHealing()),
	// String.valueOf(p.getCondition()), String.valueOf(down),
	// String.valueOf((double) died / 1000) };
	//
	// all_combat_stats.add(combat_stats);
	// }
	//
	// // Table
	// TableBuilder table = new TableBuilder();
	// table.addTitle("Combat Statistics - " + bossData.getName());
	//
	// // Header
	// table.addRow("Name", "Profession", "CRIT", "SCHL", "MOVE", "TGHN",
	// "HEAL", "COND", "DOWN", "DIED");
	//
	// // Body
	// for (int i = 0; i < playerList.size(); i++) {
	// Player p = playerList.get(i);
	// table.addRow(Utility.concatStringArray(new String[] { p.getName(),
	// p.getProf() }, all_combat_stats.get(i)));
	// }
	//
	// return table.toString();
	//
	// }
	//
	// public String get_final_boons() {
	//
	// // Final boons
	// List<String> boon_list = Boon.getList();
	// BoonFactory boonFactory = new BoonFactory();
	// List<String[]> all_rates = new ArrayList<String[]>();
	//
	// for (int i = 0; i < playerList.size(); i++) {
	//
	// Player p = playerList.get(i);
	// Map<String, List<boonLog>> boon_logs = p.get_boon_logs();
	//
	// String[] rates = new String[boon_list.size()];
	//
	// for (int j = 0; j < boon_list.size(); j++) {
	//
	// Boon boon = Boon.getEnum(boon_list.get(j));
	// AbstractBoon boon_object = boonFactory.makeBoon(boon);
	// String rate = "0.00";
	// List<boonLog> logs = boon_logs.get(boon.getName());
	//
	// if (!logs.isEmpty()) {
	// if (boon.getType().equals("duration")) {
	// List<Point> boon_intervals = get_boon_intervals_list(boon_object, logs);
	// rate = get_boon_duration(boon_intervals);
	// } else if (boon.getType().equals("intensity")) {
	// List<Integer> boon_stacks = get_boon_stacks_list(boon_object, logs);
	// rate = get_average_stacks(boon_stacks);
	// }
	// }
	// rates[j] = rate;
	// }
	// all_rates.add(rates);
	// }
	//
	// // Table
	// TableBuilder table = new TableBuilder();
	// table.addTitle("Final Boon Rates - " + bossData.getName());
	//
	// // Header
	// String[] boon_array = Boon.getArray();
	// table.addRow(Utility.concatStringArray(new String[] { "Name",
	// "Profession" }, boon_array));
	//
	// // Body
	// for (int i = 0; i < playerList.size(); i++) {
	// Player p = playerList.get(i);
	// table.addRow(Utility.concatStringArray(new String[] { p.getName(),
	// p.getProf() }, all_rates.get(i)));
	// }
	//
	// return table.toString();
	// }
	//
	// public String get_phase_boons() {
	//
	// // Phase Boons
	// List<String> boon_list = Boon.getList();
	// BoonFactory boonFactory = new BoonFactory();
	// List<String[][]> all_rates = new ArrayList<String[][]>();
	// List<Point> fight_intervals = get_fight_intervals();
	//
	// for (int i = 0; i < playerList.size(); i++) {
	//
	// Player p = playerList.get(i);
	// Map<String, List<boonLog>> boon_logs = p.get_boon_logs();
	//
	// String[][] rates = new String[boon_logs.size()][];
	//
	// for (int j = 0; j < boon_list.size(); j++) {
	//
	// Boon boon = Boon.getEnum(boon_list.get(j));
	// String[] rate = new String[fight_intervals.size()];
	// Arrays.fill(rate, "0.00");
	//
	// List<boonLog> logs = boon_logs.get(boon.getName());
	//
	// if (!logs.isEmpty()) {
	// AbstractBoon boon_object = boonFactory.makeBoon(boon);
	// if (boon.getType().equals("duration")) {
	// List<Point> boon_intervals = get_boon_intervals_list(boon_object, logs);
	// rate = get_boon_duration(boon_intervals, fight_intervals);
	// } else if (boon.getType().equals("intensity")) {
	// List<Integer> boon_stacks = get_boon_stacks_list(boon_object, logs);
	// rate = get_average_stacks(boon_stacks, fight_intervals);
	// }
	// }
	// rates[j] = rate;
	// }
	// all_rates.add(rates);
	// }
	//
	// StringBuilder output = new StringBuilder();
	// TableBuilder table = new TableBuilder();
	// String[] boon_array = Boon.getArray();
	//
	// output.append("_________________________________________" +
	// System.lineSeparator() + System.lineSeparator());
	// output.append("Phase - " + bossData.getName() + System.lineSeparator());
	// output.append("_________________________________________" +
	// System.lineSeparator());
	//
	// for (int i = 0; i < fight_intervals.size(); i++) {
	//
	// table.clear();
	// table.addTitle("Phase " + (i + 1));
	// table.addRow(Utility.concatStringArray(new String[] { "Name",
	// "Profession" }, boon_array));
	// for (int j = 0; j < playerList.size(); j++) {
	// Player p = playerList.get(j);
	//
	// String[][] player_rates = all_rates.get(j);
	// String[] row_rates = new String[boon_array.length];
	// for (int k = 0; k < boon_array.length; k++) {
	// row_rates[k] = player_rates[k][i];
	// }
	// table.addRow(Utility.concatStringArray(new String[] { p.getName(),
	// p.getProf() }, row_rates));
	// }
	//
	// output.append(System.lineSeparator() + table.toString());
	//
	// }
	//
	// return output.toString();
	// }
	//

	//
	// private List<Point> get_fight_intervals() {
	//
	// List<Point> fight_intervals = new ArrayList<Point>();
	//
	// int i_count;
	// int t_invuln;
	//
	// if (bossData.getName().equals("Vale Guardian")) {
	// i_count = 2;
	// t_invuln = 20000;
	// } else if (bossData.getName().equals("Gorseval")) {
	// i_count = 2;
	// t_invuln = 30000;
	// } else if (bossData.getName().equals("Sabetha")) {
	// i_count = 3;
	// t_invuln = 25000;
	// } else if (bossData.getName().equals("Xera")) {
	// i_count = 1;
	// t_invuln = 60000;
	// } else if (bossData.getName().equals("Slothasor")) {
	// i_count = 5;
	// t_invuln = 7000;
	// } else if (bossData.getName().equals("Samarog")) {
	// i_count = 2;
	// t_invuln = 20000;
	// } else {
	// fight_intervals.add(new Point(0, bossData.getFightDuration()));
	// return fight_intervals;
	// }
	//
	// // Get the interval when the boss is invulnerable
	// List<List<Point>> i_intervals = new ArrayList<List<Point>>();
	//
	// for (Player p : playerList) {
	// List<DamageLog> damage_logs = p.get_damage_logs();
	// int t_curr = 0;
	// int t_prev = 0;
	// List<Point> player_intervals = new ArrayList<Point>();
	// for (DamageLog log : damage_logs) {
	// if (!log.is_condi()) {
	// t_curr = log.getTime();
	// if ((t_curr - t_prev) > t_invuln) {
	// player_intervals.add(new Point(t_prev, t_curr));
	// }
	// t_prev = t_curr;
	// }
	// }
	// if (player_intervals.size() == i_count) {
	// i_intervals.add(player_intervals);
	// }
	//
	// }
	//
	// // Derive the fight intervals
	// List<Point> real_fight_intervals = new ArrayList<Point>();
	// for (int i = 0; i < i_count; i++) {
	// fight_intervals.add(new Point(0, bossData.getFightDuration()));
	// real_fight_intervals.add(new Point(0, bossData.getFightDuration()));
	// }
	// real_fight_intervals.add(new Point(0, bossData.getFightDuration()));
	// for (List<Point> player_intervals : i_intervals) {
	// for (int i = 0; i < i_count; i++) {
	// Point new_point = player_intervals.get(i);
	// Point old_point = fight_intervals.get(i);
	// int t_begin = new_point.x;
	// int t_end = new_point.y;
	// if (t_begin > old_point.x) {
	// old_point.x = t_begin;
	// }
	// if (t_end < old_point.y) {
	// old_point.y = t_end;
	// }
	// }
	// }
	// // Shift points to the right
	// for (int i = 0; i < real_fight_intervals.size(); i++) {
	// // Start
	// if (i == 0) {
	// real_fight_intervals.get(i).y = fight_intervals.get(i).x;
	// }
	// // End
	// else if ((i + 1) == real_fight_intervals.size()) {
	// real_fight_intervals.get(i).x = fight_intervals.get(i - 1).y;
	// }
	// // Middle
	// else {
	// real_fight_intervals.get(i).x = fight_intervals.get(i - 1).y;
	// real_fight_intervals.get(i).y = fight_intervals.get(i).x;
	// }
	// }
	//
	// return real_fight_intervals;
	//
	// }
	//
	// private List<Point> get_boon_intervals_list(AbstractBoon boon,
	// List<boonLog> boon_logs) {
	//
	// // Initialize variables
	// int t_prev = 0;
	// int t_curr = 0;
	// List<Point> boon_intervals = new ArrayList<Point>();
	//
	// // Loop: update then add durations
	// for (boonLog log : boon_logs) {
	// t_curr = log.getTime();
	// boon.update(t_curr - t_prev);
	// boon.add(log.getValue());
	// boon_intervals.add(new Point(t_curr, t_curr + boon.get_stack_value()));
	// t_prev = t_curr;
	// }
	//
	// // Merge intervals
	// boon_intervals = Utility.mergeIntervals(boon_intervals);
	//
	// // Trim duration overflow
	// int last = boon_intervals.size() - 1;
	// if ((boon_intervals.get(last).getY()) > bossData.getFightDuration()) {
	// boon_intervals.get(last).y = bossData.getFightDuration();
	// }
	//
	// return boon_intervals;
	// }
	//
	// private String get_boon_duration(List<Point> boon_intervals) {
	//
	// // Calculate average duration
	// double average_duration = 0;
	// for (Point p : boon_intervals) {
	// average_duration = (average_duration + (p.getY() - p.getX()));
	// }
	//
	// return String.format("%.2f", (average_duration /
	// bossData.getFightDuration()));
	// }
	//
	// private String[] get_boon_duration(List<Point> boon_intervals,
	// List<Point> fight_intervals) {
	//
	// // Phase durations
	// String[] phase_durations = new String[fight_intervals.size()];
	//
	// // Loop: add intervals in between, merge, calculate duration
	// for (int i = 0; i < fight_intervals.size(); i++) {
	// Point p = fight_intervals.get(i);
	// List<Point> boons_intervals_during_phase = new ArrayList<Point>();
	// for (Point b : boon_intervals) {
	// if (b.x < p.y && p.x < b.y) {
	// if (p.x <= b.x && b.y <= p.y) {
	// boons_intervals_during_phase.add(b);
	// } else if (b.x < p.x && p.y < b.y) {
	// boons_intervals_during_phase.add(p);
	// } else if (b.x < p.x && b.y <= p.y) {
	// boons_intervals_during_phase.add(new Point(p.x, b.y));
	// } else if (p.x <= b.x && p.y < b.y) {
	// boons_intervals_during_phase.add(new Point(b.x, p.y));
	// }
	// }
	// }
	// double duration = 0;
	// for (Point b : boons_intervals_during_phase) {
	// duration = duration + (b.getY() - b.getX());
	// }
	// phase_durations[i] = String.format("%.2f", (duration / (p.getY() -
	// p.getX())));
	// }
	//
	// return phase_durations;
	// }
	//
	// private List<Integer> get_boon_stacks_list(AbstractBoon boon,
	// List<boonLog> boon_logs) {
	//
	// // Initialize variables
	// int t_prev = 0;
	// int t_curr = 0;
	// List<Integer> boon_stacks = new ArrayList<Integer>();
	// boon_stacks.add(0);
	//
	// // Loop: fill, update, and add to stacks
	// for (boonLog log : boon_logs) {
	// t_curr = log.getTime();
	// boon.add_stacks_between(boon_stacks, t_prev, t_curr);
	// boon.update(t_curr - t_prev);
	// boon.add(log.getValue());
	// if (t_curr != t_prev) {
	// boon_stacks.add(boon.get_stack_value());
	// } else {
	// boon_stacks.set(boon_stacks.size() - 1, boon.get_stack_value());
	// }
	// t_prev = t_curr;
	// }
	//
	// // Fill in remaining stacks
	// boon.add_stacks_between(boon_stacks, t_prev,
	// bossData.getFightDuration());
	// boon.update(1);
	// boon_stacks.add(boon.get_stack_value());
	//
	// return boon_stacks;
	// }
	//
	// private String get_average_stacks(List<Integer> boon_stacks) {
	//
	// // Calculate average stacks
	// double average_stacks =
	// boon_stacks.stream().mapToInt(Integer::intValue).sum();
	//
	// return String.format("%.2f", average_stacks / boon_stacks.size());
	// }
	//
	// private String[] get_average_stacks(List<Integer> boon_stacks,
	// List<Point> fight_intervals) {
	//
	// // Phase stacks
	// String[] phase_stacks = new String[fight_intervals.size()];
	//
	// // Loop: get sublist and calculate average stacks
	// for (int i = 0; i < fight_intervals.size(); i++) {
	// Point p = fight_intervals.get(i);
	// List<Integer> phase_boon_stacks = new
	// ArrayList<Integer>(boon_stacks.subList(p.x, p.y));
	// double average_stacks =
	// phase_boon_stacks.stream().mapToInt(Integer::intValue).sum();
	// phase_stacks[i] = String.format("%.2f", average_stacks /
	// phase_boon_stacks.size());
	// }
	//
	// return phase_stacks;
	// }

}
