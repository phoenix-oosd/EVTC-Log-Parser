package statistics;

import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

import boon.AbstractBoon;
import boon.BoonFactory;
import data.AgentItem;
import data.BossData;
import data.CombatData;
import data.SkillData;
import enums.Boon;
import enums.CustomSkill;
import enums.Result;
import enums.StateChange;
import player.BoonLog;
import player.DamageLog;
import player.Player;
import utility.TableBuilder;
import utility.Utility;

public class Statistics
{
	// Fields
	public static boolean hiding_players;
	BossData b_data;
	SkillData s_data;
	CombatData c_data;
	List<Player> p_list;

	// Constructor
	public Statistics(Parse parsed)
	{
		// Data
		this.b_data = parsed.getBossData();
		this.s_data = parsed.getSkillData();
		this.c_data = parsed.getCombatData();

		// Players
		p_list = new ArrayList<Player>();
		List<AgentItem> playerAgentList = parsed.getAgentData().getPlayerAgentList();
		for (AgentItem playerAgent : playerAgentList)
		{
			this.p_list.add(new Player(playerAgent));
		}

		// Sort
		p_list.sort((a, b) -> Integer.parseInt(a.getGroup()) - Integer.parseInt(b.getGroup()));
	}

	// Final DPS
	public String getFinalDPS()
	{
		double total_dps = 0.0;
		double total_damage = 0.0;
		double fight_duration = (b_data.getLastAware() - b_data.getFirstAware()) / 1000.0;

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final DPS - " + b_data.getName());

		// Header
		table.addRow("Name", "Profession", "DPS", "Damage");

		// Body
		for (Player p : p_list)
		{
			// Damage and DPS
			double damage = p.getDamageLogs(b_data, c_data.getCombatList()).stream().mapToDouble(DamageLog::getDamage)
					.sum();
			double dps = 0.0;
			if (fight_duration > 0)
			{
				dps = damage / fight_duration;
			}

			// Totals
			total_dps += dps;
			total_damage += damage;

			// Add Row
			table.addRow(p.getCharacter(), p.getProf(), String.format("%.2f", dps), String.format("%.0f", damage));
		}

		// Sort by DPS
		table.sortAsDouble(2);

		// Footer
		table.addSeparator();
		table.addRow("GROUP TOTAL", "-", String.format("%.2f", total_dps), String.format("%.0f", total_damage));
		table.addRow("TARGET HEALTH", "-", "-", String.format("%d", b_data.getHealth()));

		return table.toString();
	}

	// Phase DPS
	public String getPhaseDPS()
	{
		double total_time = 0.0;
		List<Point> fight_intervals = getPhaseIntervals();
		int n = fight_intervals.size();
		String[] phase_names = b_data.getPhaseNames();

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Phase DPS - " + b_data.getName());

		// Header
		String[] header = new String[n + 3];
		header[0] = "Name";
		header[1] = "Profession";
		for (int i = 2; i < n + 2; i++)
		{
			header[i] = phase_names[i - 2];
		}
		header[header.length - 1] = "Summary";
		table.addRow(header);

		// Body
		for (Player p : p_list)
		{
			total_time = 0.0;
			double total_damage = 0.0;
			String[] phase_dps = new String[n + 1];

			for (int i = 0; i < n; i++)
			{
				List<DamageLog> damage_logs = p.getDamageLogs(b_data, c_data.getCombatList());
				Point interval = fight_intervals.get(i);

				// Damage and DPS
				double phase_damage = 0.0;
				for (DamageLog log : damage_logs)
				{
					if ((log.getTime() >= interval.x) && (log.getTime() <= interval.y))
					{
						phase_damage += log.getDamage();
					}
				}
				total_time += (interval.getY() - interval.getX());
				total_damage += phase_damage;
				double dps = (phase_damage / (interval.getY() - interval.getX())) * 1000.0;
				phase_dps[i] = String.format("%.2f", dps);

			}
			// Row
			phase_dps[n] = String.format("%.2f", (total_damage / total_time) * 1000.0);
			table.addRow(Utility.concatStringArray(new String[] { p.getCharacter(), p.getProf() }, phase_dps));
		}

		// Sort
		table.sortAsDouble(n + 2);

		// Footer
		table.addSeparator();
		String[] durations = new String[n + 3];
		durations[0] = "PHASE DURATION";
		durations[1] = "-";
		for (int i = 2; i < n + 2; i++)
		{
			Point p = fight_intervals.get(i - 2);
			double time = (p.getY() - p.getX()) / 1000.0;
			durations[i] = String.format("%.3f", time);
		}
		durations[durations.length - 1] = String.format("%.3f", total_time / 1000.0);
		table.addRow(durations);

		String[] intervals = new String[n + 3];
		intervals[0] = "PHASE INTERVAL";
		intervals[1] = "-";
		for (int i = 2; i < n + 2; i++)
		{
			Point p = fight_intervals.get(i - 2);
			intervals[i] = String.format("%.3f", p.getX() / 1000.0) + " - " + String.format("%.3f", p.getY() / 1000.0);
		}
		intervals[intervals.length - 1] = String.format("%.3f", fight_intervals.get(0).getX() / 1000.0) + " - "
				+ String.format("%.3f", fight_intervals.get(fight_intervals.size() - 1).getY() / 1000.0);
		table.addRow(intervals);
		return table.toString();
	}

	// Damage Distribution
	public String getDamageDistribution()
	{
		// Heading
		StringBuilder output = new StringBuilder();
		String title = " Damage Distribution - " + b_data.getName() + ' ';
		output.append('\u250C' + Utility.fillWithChar(title.length(), '\u2500') + '\u2510' + System.lineSeparator());
		output.append('\u2502' + title + '\u2502' + System.lineSeparator());
		output.append('\u2514' + Utility.fillWithChar(title.length(), '\u2500') + '\u2518');

		// Table
		TableBuilder table = new TableBuilder();

		// Body
		for (Player p : p_list)
		{
			List<DamageLog> damage_logs = p.getDamageLogs(b_data, c_data.getCombatList());

			// Skill Damage Map
			Map<Integer, Integer> skill_damage = new HashMap<Integer, Integer>();
			for (DamageLog log : damage_logs)
			{
				if (skill_damage.containsKey(log.getID()))
				{
					skill_damage.put(log.getID(), skill_damage.get(log.getID()) + log.getDamage());
				}
				else
				{
					skill_damage.put(log.getID(), log.getDamage());
				}
			}

			// Title and Header
			table.clear();
			table.addTitle(p.getCharacter() + " - " + p.getProf());
			table.addRow("Skill", "Damage", "%");

			// Sort
			skill_damage = Utility.sortByValue(skill_damage);

			// Calculate distribution
			double total_damage = skill_damage.values().stream().reduce(0, Integer::sum);
			for (Map.Entry<Integer, Integer> entry : skill_damage.entrySet())
			{
				String skill_name = s_data.getName(entry.getKey());
				double damage = entry.getValue();
				table.addRow(skill_name, String.format("%.0f", damage),
						String.format("%.2f", (damage / total_damage * 100.0)));
			}

			// Add table
			output.append(System.lineSeparator());
			output.append(table.toString());
		}
		return output.toString();
	}

	// Total Damage Graph
	public String getTotalDamageGraph(String base)
	{
		// Build
		XYChartBuilder chartBuilder = new XYChartBuilder().width(1600).height(900);
		chartBuilder.title("Total Damage - " + b_data.getName());
		chartBuilder.xAxisTitle("Time (seconds)").yAxisTitle("Damage (K)").build();
		XYChart chart = chartBuilder.build();

		// Style
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setMarkerSize(1);
		chart.getStyler().setXAxisMin(0.0);
		chart.getStyler().setYAxisMin(0.0);
		chart.getStyler().setLegendFont(new Font("Dialog", Font.PLAIN, 16));

		// Series
		for (Player p : p_list)
		{
			List<DamageLog> damage_logs = p.getDamageLogs(b_data, c_data.getCombatList());
			int n = damage_logs.size();
			if (n > 0)
			{
				double[] x = new double[n];
				double[] y = new double[n];
				double total_damage = 0.0;
				for (int i = 0; i < n; i++)
				{
					total_damage += damage_logs.get(i).getDamage();
					x[i] = damage_logs.get(i).getTime() / 1000.0;
					y[i] = total_damage / 1000.0;
				}
				chart.addSeries(p.getCharacter() + " - " + p.getProf(), x, y);
			}
		}

		// Write
		try
		{
			String file_name = "./graphs/" + base + "_" + b_data.getName() + "_TDG.png";
			BitmapEncoder.saveBitmapWithDPI(chart, file_name, BitmapFormat.PNG, 300);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return base + "_" + b_data.getName() + "_TDG.png";
	}

	// Combat Statistics
	public String getCombatStatistics()
	{
		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Combat Statistics - " + b_data.getName());

		// Header
		table.addRow("Account", "Character", "Profession", "SUBG", "CRIT", "SCHL", "MOVE", "FLNK", "TGHN", "HEAL",
				"COND", "SWAP", "DOGE", "RESS", "DOWN", "DIED");

		// Body
		for (Player p : p_list)
		{
			List<DamageLog> damage_logs = p.getDamageLogs(b_data, c_data.getCombatList());
			int instid = p.getInstid();

			// Rates
			double power_loop_count = 0.0;
			double critical_rate = 0.0;
			double scholar_rate = 0.0;
			double moving_rate = 0.0;
			double flanking_rate = 0.0;
			for (DamageLog log : damage_logs)
			{
				if (log.isCondi() == 0)
				{
					critical_rate += (log.getResult().equals(Result.CRIT)) ? 1 : 0;
					scholar_rate += log.isNinety();
					moving_rate += log.isMoving();
					flanking_rate += log.isFlanking();
					power_loop_count++;
				}
			}
			power_loop_count = (power_loop_count == 0) ? 1 : power_loop_count;

			// Counts
			int swap = c_data.getStates(instid, StateChange.WEAPON_SWAP).size();
			int down = c_data.getStates(instid, StateChange.CHANGE_DOWN).size();
			int dodge = c_data.getSkillCount(instid, CustomSkill.DODGE.getID());
			int ress = c_data.getSkillCount(instid, CustomSkill.RESURRECT.getID());

			// R.I.P
			List<Point> dead = c_data.getStates(instid, StateChange.CHANGE_DEAD);
			double died = 0.0;
			if (!dead.isEmpty())
			{
				died = dead.get(0).getX() - b_data.getFirstAware();
			}

			// Add row
			table.addRow(new String[] { p.getAccount(), p.getCharacter(), p.getProf(), p.getGroup(),
					String.format("%.2f", critical_rate / power_loop_count),
					String.format("%.2f", scholar_rate / power_loop_count),
					String.format("%.2f", moving_rate / power_loop_count),
					String.format("%.2f", flanking_rate / power_loop_count), String.valueOf(p.getToughness()),
					String.valueOf(p.getHealing()), String.valueOf(p.getCondition()), String.valueOf(swap),
					String.valueOf(dodge), String.valueOf(ress), String.valueOf(down),
					String.format("%.2f", died / 1000.0) });
		}
		return table.toString();
	}

	// Final Boons
	public String getFinalBoons()
	{
		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final Boon Rates - " + b_data.getName());

		// Header
		String[] boon_array = Boon.getArray();
		table.addRow(Utility.concatStringArray(new String[] { "Name", "Profession" }, boon_array));

		// Body
		List<String> boon_list = Boon.getList();
		int n = boon_list.size();
		BoonFactory boonFactory = new BoonFactory();

		for (Player p : p_list)
		{
			Map<String, List<BoonLog>> boon_logs = p.getBoonMap(b_data, s_data, c_data.getCombatList());
			String[] rates = new String[n];
			for (int i = 0; i < n; i++)
			{
				Boon boon = Boon.getEnum(boon_list.get(i));
				AbstractBoon boon_object = boonFactory.makeBoon(boon);
				List<BoonLog> logs = boon_logs.get(boon.getName());
				String rate = "0.00";
				if (!logs.isEmpty())
				{
					if (boon.getType().equals("duration"))
					{
						rate = getBoonDuration(getBoonIntervalsList(boon_object, logs));
					}
					else if (boon.getType().equals("intensity"))
					{
						rate = getAverageStacks(getBoonStacksList(boon_object, logs));
					}
				}
				rates[i] = rate;
			}
			table.addRow(Utility.concatStringArray(new String[] { p.getCharacter(), p.getProf() }, rates));
		}
		return table.toString();
	}

	// Phase Boons
	public String getPhaseBoons()
	{
		BoonFactory boonFactory = new BoonFactory();
		List<String[][]> all_rates = new ArrayList<String[][]>();
		List<String> boon_list = Boon.getList();
		List<Point> fight_intervals = getPhaseIntervals();
		int n = fight_intervals.size();
		int m = boon_list.size();

		for (Player p : p_list)
		{
			Map<String, List<BoonLog>> boon_logs = p.getBoonMap(b_data, s_data, c_data.getCombatList());
			String[][] rates = new String[m][];
			for (int j = 0; j < m; j++)
			{
				Boon boon = Boon.getEnum(boon_list.get(j));
				String[] rate = new String[fight_intervals.size()];
				Arrays.fill(rate, "0.00");
				List<BoonLog> logs = boon_logs.get(boon.getName());
				if (!logs.isEmpty())
				{
					AbstractBoon boon_object = boonFactory.makeBoon(boon);
					if (boon.getType().equals("duration"))
					{
						List<Point> boon_intervals = getBoonIntervalsList(boon_object, logs);
						rate = getBoonDuration(boon_intervals, fight_intervals);
					}
					else if (boon.getType().equals("intensity"))
					{
						List<Integer> boon_stacks = getBoonStacksList(boon_object, logs);
						rate = getAverageStacks(boon_stacks, fight_intervals);
					}
				}
				rates[j] = rate;
			}
			all_rates.add(rates);
		}

		// Heading
		StringBuilder output = new StringBuilder();
		String title = " Phase Boon Rates - " + b_data.getName() + ' ';
		output.append('\u250C' + Utility.fillWithChar(title.length(), '\u2500') + '\u2510' + System.lineSeparator());
		output.append('\u2502' + title + '\u2502' + System.lineSeparator());
		output.append('\u2514' + Utility.fillWithChar(title.length(), '\u2500') + '\u2518');

		// Table
		TableBuilder table = new TableBuilder();

		// Body
		String[] boon_array = Boon.getArray();
		String[] phase_names = b_data.getPhaseNames();
		for (int i = 0; i < n; i++)
		{
			table.clear();
			table.addTitle(phase_names[i]);
			table.addRow(Utility.concatStringArray(new String[] { "Name", "Profession" }, boon_array));
			int l = p_list.size();
			for (int j = 0; j < l; j++)
			{
				Player p = p_list.get(j);
				String[][] player_rates = all_rates.get(j);
				String[] row_rates = new String[m];
				for (int k = 0; k < m; k++)
				{
					row_rates[k] = player_rates[k][i];
				}
				table.addRow(Utility.concatStringArray(new String[] { p.getCharacter(), p.getProf() }, row_rates));
			}
			output.append(System.lineSeparator() + table.toString());
		}
		return output.toString();
	}

	// Private Methods
	private List<Point> getPhaseIntervals()
	{
		List<Point> fight_intervals = new ArrayList<Point>();
		int log_start = c_data.getCombatList().get(0).getTime();
		int time_start = b_data.getFirstAware() - log_start;
		int time_end = b_data.getLastAware() - log_start;

		// Thresholds
		int time_threshold = 0;
		int[] health_thresholds = null;
		if (b_data.getName().equals("Vale Guardian"))
		{
			time_threshold = 20000;
			health_thresholds = new int[] { 6600, 3300 };
		}
		else if (b_data.getName().equals("Gorseval the Multifarious"))
		{
			time_threshold = 20000;
			health_thresholds = new int[] { 6600, 3300 };
		}
		else if (b_data.getName().equals("Sabetha the Saboteur"))
		{
			time_threshold = 20000;
			health_thresholds = new int[] { 7500, 5000, 2500 };
		}
		else if (b_data.getName().equals("Slothasor"))
		{
			health_thresholds = new int[] { 8000, 6000, 4000, 2000, 1000 };
		}
		else if (b_data.getName().equals("Matthias Gabrel"))
		{
			health_thresholds = new int[] { 8000, 6000, 4000 };
		}
		else if (b_data.getName().equals("Keep Construct"))
		{
			time_threshold = 20000;
			health_thresholds = new int[] { 6600, 3300 };
		}
		else if (b_data.getName().equals("Xera"))
		{
			time_threshold = 20000;
			health_thresholds = new int[] { 5000 };
		}
		else if (b_data.getName().equals("Cairn the Indomitable"))
		{
			health_thresholds = new int[] { 7500, 5000, 2500 };
		}
		else if (b_data.getName().equals("Mursaat Overseer"))
		{
			health_thresholds = new int[] { 7500, 5000, 2500 };
		}
		else if (b_data.getName().equals("Samarog"))
		{
			time_threshold = 20000;
			health_thresholds = new int[] { 6600, 3300 };
		}
		else if (b_data.getName().equals("Deimos"))
		{
			health_thresholds = new int[] { 7500, 5000, 2500 };
		}
		else
		{
			fight_intervals.add(new Point(time_start, time_end));
			return fight_intervals;
		}

		// Generate intervals with health updates
		ListIterator<Point> iter = c_data.getStates(b_data.getInstid(), StateChange.HEALTH_UPDATE).listIterator();
		Point previous_update = null;
		if (iter.hasNext())
		{
			previous_update = iter.next();
		}
		if (previous_update != null)
		{
			main: for (int threshold : health_thresholds)
			{
				while (iter.hasNext())
				{
					Point current_update = iter.next();
					if ((current_update.y <= threshold) && (time_threshold == 0))
					{
						fight_intervals.add(new Point(time_start, previous_update.x - log_start));
						time_start = previous_update.x - log_start;
						previous_update = current_update;
						continue main;
					}
					else if ((current_update.y <= threshold)
							&& ((current_update.x - previous_update.x) > time_threshold))
					{
						fight_intervals.add(new Point(time_start, previous_update.x - log_start));
						time_start = current_update.x - log_start;
						previous_update = current_update;
						continue main;

					}
					previous_update = current_update;
				}
			}
		}
		fight_intervals.add(new Point(time_start, time_end));
		return fight_intervals;
	}

	private List<Point> getBoonIntervalsList(AbstractBoon boon, List<BoonLog> boon_logs)
	{
		// Initialise variables
		int t_prev = 0;
		int t_curr = 0;
		List<Point> boon_intervals = new ArrayList<Point>();

		// Loop: update then add durations
		for (BoonLog log : boon_logs)
		{
			t_curr = log.getTime();
			boon.update(t_curr - t_prev);
			boon.add(log.getValue());
			boon_intervals.add(new Point(t_curr, t_curr + boon.getStackValue()));
			t_prev = t_curr;
		}

		// Merge intervals
		boon_intervals = Utility.mergeIntervals(boon_intervals);

		// Trim duration overflow
		int fight_duration = b_data.getLastAware() - b_data.getFirstAware();
		int last = boon_intervals.size() - 1;
		if (boon_intervals.get(last).getY() > fight_duration)
		{
			boon_intervals.get(last).y = fight_duration;
		}

		return boon_intervals;
	}

	private String getBoonDuration(List<Point> boon_intervals)
	{
		// Calculate average duration
		double average_duration = 0.0;
		for (Point p : boon_intervals)
		{
			average_duration = average_duration + (p.getY() - p.getX());
		}
		return String.format("%.2f", (average_duration / (b_data.getLastAware() - b_data.getFirstAware())));
	}

	private String[] getBoonDuration(List<Point> boon_intervals, List<Point> fight_intervals)
	{
		// Phase durations
		String[] phase_durations = new String[fight_intervals.size()];

		// Loop: add intervals in between, merge, calculate duration
		for (int i = 0; i < fight_intervals.size(); i++)
		{
			Point p = fight_intervals.get(i);
			List<Point> boons_intervals_during_phase = new ArrayList<Point>();
			for (Point b : boon_intervals)
			{
				if (b.x < p.y && p.x < b.y)
				{
					if (p.x <= b.x && b.y <= p.y)
					{
						boons_intervals_during_phase.add(b);
					}
					else if (b.x < p.x && p.y < b.y)
					{
						boons_intervals_during_phase.add(p);
					}
					else if (b.x < p.x && b.y <= p.y)
					{
						boons_intervals_during_phase.add(new Point(p.x, b.y));
					}
					else if (p.x <= b.x && p.y < b.y)
					{
						boons_intervals_during_phase.add(new Point(b.x, p.y));
					}
				}
			}
			double duration = 0.0;
			for (Point b : boons_intervals_during_phase)
			{
				duration = duration + (b.getY() - b.getX());
			}
			phase_durations[i] = String.format("%.2f", (duration / (p.getY() - p.getX())));
		}
		return phase_durations;
	}

	private List<Integer> getBoonStacksList(AbstractBoon boon, List<BoonLog> boon_logs)
	{
		// Initialise variables
		int t_prev = 0;
		int t_curr = 0;
		List<Integer> boon_stacks = new ArrayList<Integer>();
		boon_stacks.add(0);

		// Loop: fill, update, and add to stacks
		for (BoonLog log : boon_logs)
		{
			t_curr = log.getTime();
			boon.addStacksBetween(boon_stacks, t_curr - t_prev);
			boon.update(t_curr - t_prev);
			boon.add(log.getValue());
			if (t_curr != t_prev)
			{
				boon_stacks.add(boon.getStackValue());
			}
			else
			{
				boon_stacks.set(boon_stacks.size() - 1, boon.getStackValue());
			}
			t_prev = t_curr;
		}

		// Fill in remaining stacks
		boon.addStacksBetween(boon_stacks, b_data.getLastAware() - b_data.getFirstAware() - t_prev);
		boon.update(1);
		boon_stacks.add(boon.getStackValue());
		return boon_stacks;
	}

	private String getAverageStacks(List<Integer> boon_stacks)
	{
		// Calculate average stacks
		double average_stacks = boon_stacks.stream().mapToInt(Integer::intValue).sum();
		double average = average_stacks / boon_stacks.size();
		if (average > 10.0)
		{
			return String.format("%.1f", average);
		}
		else
		{
			return String.format("%.2f", average);
		}
	}

	private String[] getAverageStacks(List<Integer> boon_stacks, List<Point> fight_intervals)
	{
		// Phase stacks
		String[] phase_stacks = new String[fight_intervals.size()];

		// Loop: get sublist and calculate average stacks
		for (int i = 0; i < fight_intervals.size(); i++)
		{
			Point p = fight_intervals.get(i);
			List<Integer> phase_boon_stacks = new ArrayList<Integer>(boon_stacks.subList(p.x, p.y));
			double average_stacks = phase_boon_stacks.stream().mapToInt(Integer::intValue).sum();
			double average = average_stacks / phase_boon_stacks.size();
			if (average > 10.0)
			{
				phase_stacks[i] = String.format("%.1f", average);
			}
			else
			{
				phase_stacks[i] = String.format("%.2f", average);
			}
		}
		return phase_stacks;
	}
}
