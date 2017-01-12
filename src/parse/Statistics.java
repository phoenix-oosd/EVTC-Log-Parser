package parse;

import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import boon.Boon;
import boon.BoonFactory;
import data.boonLog;
import data.bossData;
import data.combatData;
import data.damageLog;
import data.playerData;
import data.skillData;

public class Statistics {

	// Fields
	private bossData b_data = null;
	private List<playerData> p_data = null;
	private List<skillData> s_data = null;
	private List<combatData> c_data = null;

	// Constructor
	public Statistics(bossData b_data, List<playerData> p_data, List<skillData> s_data, List<combatData> c_data) {
		this.b_data = b_data;
		this.p_data = p_data;
		this.s_data = s_data;
		this.c_data = c_data;
	}

	// Public Methods
	public void get_damage_logs() {

		// Start time of the fight
		int t_start = c_data.get(0).get_time();

		// Add damage logs for each player
		for (playerData p : p_data) {
			// Check all combat logs
			for (combatData c : c_data) {
				// The target is the boss and the player is an enemy
				if ((c.get_dst_cid() == b_data.getCID()) && c.iff()) {
					// The player or their pets is the source
					if ((p.getCID() == c.get_src_cid()) || (p.getCID() == c.get_src_master_cid())) {
						// Physical or condition damage
						if ((!c.is_buff() && (c.get_value() > 0)) || (c.is_buff() && (c.get_buff_dmg() > 0))) {
							int time = c.get_time() - t_start;
							int damage;
							if (c.is_buff()) {
								damage = c.get_buff_dmg();
							} else {
								damage = c.get_value();
							}
							p.get_damage_logs().add(new damageLog(time, damage, c.is_buff(), c.is_crit(), c.is_ninety(),
									c.is_moving()));
						}
					}
				}
			}
		}
	}

	public void get_boon_logs(List<String> boon_list) {

		// Start time of the fight
		int t_start = c_data.get(0).get_time();

		// Add boon logs for each player
		for (playerData p : p_data) {
			// Initialize boon list
			p.setBoons(boon_list);
			// Check all combat logs
			for (combatData c : c_data) {
				// The player is the target
				if (p.getCID() == c.get_dst_cid()) {
					// If the skill is a buff and in the boon list
					String skill_name = get_skill_name(c.get_skill_id());
					if ((c.is_buff() && (c.get_value() > 0)) && (boon_list.contains(skill_name))) {
						p.get_boon_logs().get(skill_name).add(new boonLog(c.get_time() - t_start, c.get_value()));
					}
				}
			}
		}
	}

	public String get_final_dps() {

		// Final DPS
		List<String> dps = new ArrayList<String>();
		List<String> dmg = new ArrayList<String>();

		double total_dps = 0.0;
		int total_damage = 0;

		double fight_duration = b_data.getFightDuration() / 1000.0;

		for (playerData p : p_data) {
			double player_damage = 0.0;

			List<damageLog> damage_logs = p.get_damage_logs();
			for (damageLog log : damage_logs) {
				player_damage = player_damage + log.getDamage();
			}

			dps.add(String.format("%.2f", (player_damage / fight_duration)));
			dmg.add(String.valueOf((int) player_damage));

			total_dps = total_dps + (player_damage / fight_duration);
			total_damage = (int) (total_damage + player_damage);
		}

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final DPS | " + b_data.getName() + " | " + b_data.getDate());

		// Header
		table.addRow("Character Name", "Profession", "Final DPS", "Damage");

		// Body
		for (int i = 0; i < p_data.size(); i++) {
			playerData p = p_data.get(i);
			table.addRow(p.getName(), p.getProf(), dps.get(i), dmg.get(i));
		}

		// Footer
		table.addRow("-", "-", String.format("%.2f", total_dps), String.valueOf(total_damage));
		table.addRow("-", "-", "-", String.valueOf(b_data.getHP()));

		return table.toString();
	}

	public String get_phase_dps() {

		// Phase DPS
		List<Point> fight_intervals = get_fight_intervals();
		List<String[]> all_phase_dps = new ArrayList<String[]>();

		for (int i = 0; i < p_data.size(); i++) {

			playerData p = p_data.get(i);
			String[] phase_dps = new String[fight_intervals.size()];

			for (int j = 0; j < fight_intervals.size(); j++) {

				Point interval = fight_intervals.get(j);
				List<damageLog> damage_logs = p.get_damage_logs();

				double phase_damage = 0;

				for (damageLog log : damage_logs) {
					if ((log.getTime() >= interval.x) && (log.getTime() <= interval.y)) {
						phase_damage = phase_damage + log.getDamage();
					}
				}
				phase_dps[j] = String.format("%.2f", (phase_damage / (interval.getY() - interval.getX()) * 1000));
			}
			all_phase_dps.add(phase_dps);
		}

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Phase DPS | " + b_data.getName() + " | " + b_data.getDate());

		// Header
		String[] header = new String[2 + fight_intervals.size()];
		header[0] = "Character Name";
		header[1] = "Profession";
		for (int i = 2; i < fight_intervals.size() + 2; i++) {
			header[i] = "Phase " + String.valueOf(i - 1);
		}
		table.addRow(header);

		// Body
		for (int i = 0; i < p_data.size(); i++) {
			playerData p = p_data.get(i);
			table.addRow(concat(new String[] { p.getName(), p.getProf() }, all_phase_dps.get(i)));
		}

		// Footer
		String[] durations = new String[fight_intervals.size() + 2];
		durations[0] = "-";
		durations[1] = "-";
		for (int i = 2; i < fight_intervals.size() + 2; i++) {
			Point p = fight_intervals.get(i - 2);
			durations[i] = String.format("%.2f", (p.getY() - p.getX()) / 1000.0);
		}
		table.addRow(durations);

		String[] intervals = new String[fight_intervals.size() + 2];
		intervals[0] = "-";
		intervals[1] = "-";
		for (int i = 2; i < fight_intervals.size() + 2; i++) {
			Point p = fight_intervals.get(i - 2);
			intervals[i] = "(" + String.format("%.2f", p.getX() / 1000.0) + ", "
					+ String.format("%.2f", p.getY() / 1000.0) + ")";
		}
		table.addRow(intervals);

		return table.toString();
	}

	public void get_total_damage_graph(String base) {

		// Generate a graph
		final XYChart chart = new XYChartBuilder().width(1600).height(900)
				.title("Total Damage | " + b_data.getName() + " | " + b_data.getDate()).xAxisTitle("Time (s)")
				.yAxisTitle("Damage (k)").build();
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setMarkerSize(1);
		chart.getStyler().setXAxisMin(0.0);
		chart.getStyler().setYAxisMin(0.0);
		chart.getStyler().setLegendFont(new Font("Dialog", Font.PLAIN, 16));

		// Map<String, XYSeries> series = chart.getSeriesMap();
		// for (Map.Entry<String, XYSeries> entry : series.entrySet()) {
		// entry.getValue().setMarker(SeriesMarkers.NONE);
		// }

		for (playerData p : p_data) {
			List<damageLog> logs = p.get_damage_logs();
			if (logs.size() > 0) {
				double[] x = new double[logs.size()];
				double[] y = new double[logs.size()];
				double total_damage = 0;

				for (int i = 0; i < logs.size(); i++) {
					total_damage = total_damage + logs.get(i).getDamage();
					x[i] = logs.get(i).getTime() / 1000.0;
					y[i] = total_damage / 1000.0;
				}
				chart.addSeries(p.getName() + " | " + p.getProf(), x, y);
			}
		}

		try {
			// BitmapEncoder.saveBitmap(chart, "./graphs/" + base + ".png",
			// BitmapFormat.PNG);
			BitmapEncoder.saveBitmapWithDPI(chart, "./graphs/" + base + ".png", BitmapFormat.PNG, 300);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String get_combat_stats() {

		// Combat Statistics
		List<String[]> all_combat_stats = new ArrayList<String[]>();

		for (playerData p : p_data) {

			List<damageLog> damage_logs = p.get_damage_logs();
			double i = 0.0, crit = 0.0, schl = 0.0, move = 0.0;

			for (damageLog log : damage_logs) {
				if (!log.is_condi()) {
					if (log.is_crit()) {
						crit++;
					}
					if (log.is_ninety()) {
						schl++;
					}
					if (log.is_moving()) {
						move++;
					}
					i++;
				}
			}
			String[] combat_stats = new String[] { String.format("%.2f", crit / i), String.format("%.2f", schl / i),
					String.format("%.2f", move / i), String.valueOf(p.getToughness()), String.valueOf(p.getHealing()),
					String.valueOf(p.getCondition()) };

			all_combat_stats.add(combat_stats);
		}

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Combat Statistics | " + b_data.getName() + " | " + b_data.getDate());

		// Header
		table.addRow("Character Name", "Profession", "CRIT", "SCHL", "MOVE", "TGHN", "HEAL", "COND");

		// Body
		for (int i = 0; i < p_data.size(); i++) {
			playerData p = p_data.get(i);
			table.addRow(concat(new String[] { p.getName(), p.getProf() }, all_combat_stats.get(i)));
		}

		return table.toString();

	}

	public String get_final_boons(List<String> boon_list) {

		// Final boons
		BoonFactory boonFactory = new BoonFactory();
		List<String[]> all_rates = new ArrayList<String[]>();

		for (int i = 0; i < p_data.size(); i++) {

			playerData p = p_data.get(i);
			Map<String, List<boonLog>> boon_logs = p.get_boon_logs();

			String[] rates = new String[boon_list.size()];

			for (int j = 0; j < boon_list.size(); j++) {

				String boon = boon_list.get(j);
				Boon boon_object = boonFactory.makeBoon(boon);
				String rate = "0.00";

				if (boon_logs.get(boon).size() > 1) {
					if (boon_object.get_type().equals("Duration")) {
						rate = get_boon_duration(boon_object, boon_logs.get(boon));
					} else if (boon_object.get_type().equals("Intensity")) {
						rate = get_average_stacks(boon_object, boon_logs.get(boon));
					}
				}
				rates[j] = rate;
			}
			all_rates.add(rates);
		}

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final Boon Rates | " + b_data.getName() + " | " + b_data.getDate());

		// Header
		String[] boon_array = new String[] { "MGHT", "QCKN", "FURY", "PROT", "ALAC", "SPOT", "FRST", "GoE", "GotL",
				"EA", "BoS", "BoD" };
		table.addRow(concat(new String[] { "Character Name", "Profession" }, boon_array));

		// Body
		for (int i = 0; i < p_data.size(); i++) {
			playerData p = p_data.get(i);
			table.addRow(concat(new String[] { p.getName(), p.getProf() }, all_rates.get(i)));
		}

		return table.toString();
	}

	public String get_phase_boons(List<String> boon_list) {

		// Phase Boons
		BoonFactory boonFactory = new BoonFactory();
		List<String[][]> all_rates = new ArrayList<String[][]>();
		List<Point> fight_intervals = get_fight_intervals();

		for (int i = 0; i < p_data.size(); i++) {

			playerData p = p_data.get(i);
			Map<String, List<boonLog>> boon_logs = p.get_boon_logs();

			String[][] rates = new String[boon_logs.size()][];

			for (int j = 0; j < boon_list.size(); j++) {

				String boon = boon_list.get(j);

				String[] rate = new String[fight_intervals.size()];
				Arrays.fill(rate, "0.00");

				if (boon_logs.get(boon).size() > 1) {
					Boon boon_object = boonFactory.makeBoon(boon);
					if (boon_object.get_type().equals("Duration")) {
						rate = get_boon_duration(boon_object, boon_logs.get(boon), fight_intervals);
					} else if (boon_object.get_type().equals("Intensity")) {
						rate = get_average_stacks(boon_object, boon_logs.get(boon), fight_intervals);
					}
				}
				rates[j] = rate;
			}
			all_rates.add(rates);
		}

		StringBuilder all_tables = new StringBuilder();
		String[] boon_array = new String[] { "MGHT", "QCKN", "FURY", "PROT", "ALAC", "SPOT", "FRST", "GoE", "GotL",
				"EA", "BoS", "BoD" };

		for (int i = 0; i < fight_intervals.size(); i++) {

			// Table
			TableBuilder table = new TableBuilder();
			table.addTitle("Phase " + (i + 1) + " Boon Rates | " + b_data.getName() + " | " + b_data.getDate());

			// Header
			table.addRow(concat(new String[] { "Character Name", "Profession" }, boon_array));

			// Body
			for (int j = 0; j < p_data.size(); j++) {
				playerData p = p_data.get(j);

				String[][] player_rates = all_rates.get(j);
				String[] row_rates = new String[boon_array.length];
				for (int k = 0; k < boon_array.length; k++) {
					row_rates[k] = player_rates[k][i];
				}
				table.addRow(concat(new String[] { p.getName(), p.getProf() }, row_rates));
			}

			all_tables.append(table.toString());
		}

		return all_tables.toString();
	}

	// Private Methods
	private String[] concat(String[] a, String[] b) {
		int i = a.length;
		int j = b.length;
		String[] str = new String[i + j];
		System.arraycopy(a, 0, str, 0, i);
		System.arraycopy(b, 0, str, i, j);
		return str;
	}

	private String get_skill_name(int ID) {
		for (skillData s : s_data) {
			if (s.getID() == ID) {
				return s.getName();
			}
		}
		return null;
	}

	private List<Point> get_fight_intervals() {

		List<Point> fight_intervals = new ArrayList<Point>();

		int i_count;
		int t_invuln;

		if (b_data.getName().equals("Vale Guardian")) {
			i_count = 2;
			t_invuln = 20000;
		} else if (b_data.getName().equals("Gorseval")) {
			i_count = 2;
			t_invuln = 30000;
		} else if (b_data.getName().equals("Sabetha")) {
			i_count = 3;
			t_invuln = 25000;
		} else if (b_data.getName().equals("Xera")) {
			i_count = 1;
			t_invuln = 60000;
		} else if (b_data.getName().equals("Slothasor")) {
			i_count = 5;
			t_invuln = 7000;
		} else {
			fight_intervals.add(new Point(0, b_data.getFightDuration()));
			return fight_intervals;
		}

		// Get the interval when the boss is invulnerable
		List<List<Point>> i_intervals = new ArrayList<List<Point>>();

		for (playerData p : p_data) {
			List<damageLog> damage_logs = p.get_damage_logs();
			int t_curr = 0;
			int t_prev = 0;
			List<Point> player_intervals = new ArrayList<Point>();
			for (damageLog log : damage_logs) {
				if (!log.is_condi()) {
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
			fight_intervals.add(new Point(0, b_data.getFightDuration()));
			real_fight_intervals.add(new Point(0, b_data.getFightDuration()));
		}
		real_fight_intervals.add(new Point(0, b_data.getFightDuration()));
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

	private List<Point> merge_intervals(List<Point> intervals) {

		if (intervals.size() <= 1) {
			return intervals;
		}

		List<Point> merged = new ArrayList<Point>();
		int x = intervals.get(0).x;
		int y = intervals.get(0).y;

		for (int i = 1; i < intervals.size(); i++) {
			Point current = intervals.get(i);
			if (current.x <= y) {
				y = Math.max(current.y, y);
			} else {
				merged.add(new Point(x, y));
				x = current.x;
				y = current.y;
			}
		}

		merged.add(new Point(x, y));

		return merged;

	}

	private String get_boon_duration(Boon boon, List<boonLog> boon_logs) {

		// Simulate in game mechanics
		List<Point> boon_intervals = new ArrayList<Point>();
		int t_prev = 0, t_curr = boon_logs.get(0).getTime();
		boon.add(boon_logs.get(0).getValue());
		boon_intervals.add(new Point(t_curr, t_curr + boon.get_stack_duration()));

		for (ListIterator<boonLog> iter = boon_logs.listIterator(2); iter.hasNext();) {
			boonLog log = iter.next();
			t_curr = log.getTime();
			boon.update(t_curr - t_prev);
			boon.add(log.getValue());
			boon_intervals.add(new Point(t_curr, t_curr + boon.get_stack_duration()));
			t_prev = t_curr;
		}

		// Merge intervals
		boon_intervals = merge_intervals(boon_intervals);

		// Check if last element is longer than the fight duration then merge
		if ((boon_intervals.get(boon_intervals.size() - 1).getY()) > b_data.getFightDuration()) {
			boon_intervals.get(boon_intervals.size() - 1).y = b_data.getFightDuration();
		}

		// Calculate average duration
		double average_duration = 0;

		for (Point p : boon_intervals) {
			average_duration = (average_duration + (p.getY() - p.getX()));
		}

		return String.format("%.2f", (average_duration / b_data.getFightDuration()));
	}

	private String[] get_boon_duration(Boon boon, List<boonLog> boon_logs, List<Point> fight_intervals) {

		// Simulate in game mechanics
		List<Point> boon_intervals = new ArrayList<Point>();
		int t_prev = 0, t_curr = boon_logs.get(0).getTime();
		boon.add(boon_logs.get(0).getValue());
		boon_intervals.add(new Point(t_curr, t_curr + boon.get_stack_duration()));

		for (ListIterator<boonLog> iter = boon_logs.listIterator(2); iter.hasNext();) {
			boonLog log = iter.next();
			t_curr = log.getTime();
			boon.update(t_curr - t_prev);
			boon.add(log.getValue());
			boon_intervals.add(new Point(t_curr, t_curr + boon.get_stack_duration()));
			t_prev = t_curr;
		}

		// Merge intervals and remove boon overflow
		boon_intervals = merge_intervals(boon_intervals);
		if (boon_intervals.get(boon_intervals.size() - 1).getY() > b_data.getFightDuration()) {
			boon_intervals.get(boon_intervals.size() - 1).y = b_data.getFightDuration();
		}

		// Calculate boon duration for each phase
		String[] phase_durations = new String[fight_intervals.size()];
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

	private String get_average_stacks(Boon boon, List<boonLog> boon_logs) {

		List<Point> boon_intervals = new ArrayList<Point>();
		int t_prev = 0, t_curr = boon_logs.get(0).getTime();
		boon.add(boon_logs.get(0).getValue());
		boon_intervals.add(new Point(t_curr, boon.get_stack_count()));

		for (ListIterator<boonLog> iter = boon_logs.listIterator(2); iter.hasNext();) {
			boonLog log = iter.next();
			t_curr = log.getTime();
			boon.update(t_curr - t_prev);
			boon.add(log.getValue());
			boon_intervals.add(new Point(t_curr, boon.get_stack_count()));
			t_prev = t_curr;
		}

		// Calculate average stacks
		double average_boon = 0;
		int prev_time = 0;
		int prev_stack = 0;

		for (Point p : boon_intervals) {
			average_boon = (average_boon + (prev_stack * (p.getX() - prev_time)));
			prev_time = p.x;
			prev_stack = p.y;
		}

		return String.format("%.2f", (average_boon / b_data.getFightDuration()));
	}

	private String[] get_average_stacks(Boon boon, List<boonLog> boon_logs, List<Point> fight_intervals) {

		List<Point> boon_intervals = new ArrayList<Point>();
		int t_prev = 0, t_curr = boon_logs.get(0).getTime();
		boon.add(boon_logs.get(0).getValue());
		boon_intervals.add(new Point(t_curr, boon.get_stack_count()));

		for (ListIterator<boonLog> iter = boon_logs.listIterator(2); iter.hasNext();) {
			boonLog log = iter.next();
			t_curr = log.getTime();
			boon.update(t_curr - t_prev);
			boon.add(log.getValue());
			boon_intervals.add(new Point(t_curr, boon.get_stack_count()));
			t_prev = t_curr;
		}

		String[] phase_stacks = new String[fight_intervals.size()];
		List<Point> boons_intervals_during_phase = new ArrayList<Point>();

		for (int i = 0; i < fight_intervals.size(); i++) {

			// Clear intervals
			Point p = fight_intervals.get(i);
			boons_intervals_during_phase.clear();

			// Find intervals during phase
			for (Point b : boon_intervals) {

				if (p.x <= b.x && b.x <= p.y) {
					boons_intervals_during_phase.add(b);
				}
			}

			double average_stacks = 0;
			int prev_time = 0;
			int prev_stack = 0;

			for (Point interval : boons_intervals_during_phase) {
				average_stacks = (average_stacks + (prev_stack * (interval.getX() - prev_time)));
				prev_time = interval.x;
				prev_stack = interval.y;
			}

			phase_stacks[i] = String.format("%.2f", (average_stacks / (p.getY() - p.getX())));

		}

		return phase_stacks;
	}

}
