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
import data.AgentData;
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
	AgentData a_data;
	SkillData s_data;
	CombatData c_data;
	List<Player> player_list;

	// Constructor
	public Statistics(Parse parsed)
	{
		// Parsed data
		this.b_data = parsed.getBossData();
		this.a_data = parsed.getAgentData();
		this.s_data = parsed.getSkillData();
		this.c_data = parsed.getCombatData();

		// Generate player list
		player_list = new ArrayList<Player>();
		List<AgentItem> playerAgentList = a_data.getPlayerAgentList();
		for (AgentItem playerAgent : playerAgentList)
		{
			this.player_list.add(new Player(playerAgent));
		}

		// Sort by group
		player_list.sort((a, b) -> Integer.parseInt(a.getGroup()) - Integer.parseInt(b.getGroup()));
	}

	// Final DPS
	public String getFinalDPS()
	{
		double total_dps = 0.0;
		double total_damage = 0.0;
		double fight_duration = (b_data.getLastAware() - b_data.getFirstAware()) / 1000;

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final DPS - " + b_data.getName());

		// Header
		table.addRow("Name", "Profession", "DPS", "Damage");

		// Body
		for (Player p : player_list)
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
			table.addRow(p.getCharacter(), p.getProf(), String.format("%.2f", dps), String.valueOf((int) damage));
		}

		// Sort by DPS
		table.sortAsDouble(2);

		// Footer
		table.addSeparator();
		table.addRow("GROUP TOTAL", "-", String.format("%.2f", total_dps), String.valueOf((int) total_damage));
		table.addRow("TARGET HEALTH", "-", "-", String.valueOf(b_data.getHealth()));

		return table.toString();
	}

	// Phase DPS
	public String getPhaseDPS()
	{
		List<Point> fight_intervals = getPhaseIntervals();
		int number_of_intervals = fight_intervals.size();
		String[] phase_names = b_data.getPhaseNames();

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Phase DPS - " + b_data.getName());

		// Header
		String[] header = new String[number_of_intervals + 3];
		header[0] = "Name";
		header[1] = "Profession";
		for (int i = 2; i < number_of_intervals + 2; i++)
		{
			header[i] = phase_names[i - 2];
		}
		header[header.length - 1] = "Average";
		table.addRow(header);

		// Body
		for (Player p : player_list)
		{
			double average_dps = 0;
			String[] phase_dps = new String[number_of_intervals + 1];

			for (int i = 0; i < number_of_intervals; i++)
			{
				Point interval = fight_intervals.get(i);
				List<DamageLog> damage_logs = p.getDamageLogs(b_data, c_data.getCombatList());

				// Damage and DPS
				double phase_damage = 0;
				for (DamageLog log : damage_logs)
				{
					if ((log.getTime() >= interval.x) && (log.getTime() <= interval.y))
					{
						phase_damage += log.getDamage();
					}
				}
				double dps = phase_damage / (interval.getY() - interval.getX()) * 1000;
				phase_dps[i] = String.format("%.2f", dps);

				// Adjust moving average
				average_dps = (((average_dps * i) + dps) / (i + 1));
			}

			// Add row
			phase_dps[number_of_intervals] = String.format("%.2f", average_dps);
			table.addRow(Utility.concatStringArray(new String[] { p.getCharacter(), p.getProf() }, phase_dps));
		}

		// Sort by DPS
		table.sortAsDouble(number_of_intervals + 2);

		// Footer
		table.addSeparator();
		String[] durations = new String[fight_intervals.size() + 3];
		double total_time = 0.0;
		durations[0] = "PHASE DURATION";
		durations[1] = "-";
		for (int i = 2; i < fight_intervals.size() + 2; i++)
		{
			Point p = fight_intervals.get(i - 2);
			double time = (p.getY() - p.getX()) / 1000.0;
			durations[i] = String.format("%.2f", time);
			total_time += time;
		}
		durations[durations.length - 1] = String.format("%.2f", total_time);
		table.addRow(durations);

		String[] intervals = new String[fight_intervals.size() + 3];
		intervals[0] = "PHASE INTERVAL";
		intervals[1] = "-";
		for (int i = 2; i < fight_intervals.size() + 2; i++)
		{
			Point p = fight_intervals.get(i - 2);
			intervals[i] = "(" + String.format("%06.2f", p.getX() / 1000.0) + ", "
					+ String.format("%06.2f", p.getY() / 1000.0) + ")";
		}
		intervals[intervals.length - 1] = "-";
		table.addRow(intervals);

		return table.toString();
	}

	// Damage Distribution
	public String getDamageDistribution()
	{
		// Table
		TableBuilder table = new TableBuilder();
		StringBuilder output = new StringBuilder();

		String title = " Damage Distribution - " + b_data.getName() + ' ';
		output.append('\u250C' + Utility.fillWithChar(title.length(), '\u2500') + '\u2510' + System.lineSeparator());
		output.append('\u2502' + title + '\u2502' + System.lineSeparator());
		output.append('\u2514' + Utility.fillWithChar(title.length(), '\u2500') + '\u2518');

		// Body
		for (Player p : player_list)
		{
			List<DamageLog> damage_logs = p.getDamageLogs(b_data, c_data.getCombatList());
			Map<Integer, Integer> skill_damage = new HashMap<Integer, Integer>();
			for (DamageLog log : damage_logs)
			{
				if (skill_damage.containsKey(log.getID()))
				{
					skill_damage.put(log.getID(), skill_damage.get(log.getID()) + log.getDamage());
				}
				else
				{
					if (log.getID() > 0)
					{
						skill_damage.put(log.getID(), log.getDamage());
					}
				}
			}
			double damage_sum = skill_damage.values().stream().reduce(0, Integer::sum);

			table.clear();
			table.addTitle(p.getCharacter() + " - " + p.getProf());
			table.addRow("Skill", "Damage", "%");

			// Calculate % of each skill
			skill_damage = Utility.sortByValue(skill_damage);
			for (Map.Entry<Integer, Integer> entry : skill_damage.entrySet())
			{
				String skill_name = s_data.getName(entry.getKey());
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
	public String getTotalDamageGraph(String base)
	{

		// Build chart
		XYChartBuilder chartBuilder = new XYChartBuilder().width(1600).height(900);
		chartBuilder.title("Total Damage - " + b_data.getName());
		chartBuilder.xAxisTitle("Time (seconds)").yAxisTitle("Damage (K)").build();
		XYChart chart = chartBuilder.build();

		// Add style to chart
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setMarkerSize(1);
		chart.getStyler().setXAxisMin(0.0);
		chart.getStyler().setYAxisMin(0.0);
		chart.getStyler().setLegendFont(new Font("Dialog", Font.PLAIN, 16));

		// Add series to chart
		for (Player p : player_list)
		{
			List<DamageLog> damage_logs = p.getDamageLogs(b_data, c_data.getCombatList());
			double[] x = new double[damage_logs.size()];
			double[] y = new double[damage_logs.size()];
			double total_damage = 0.0;
			for (int i = 0; i < damage_logs.size(); i++)
			{
				if (CustomSkill.getEnum(damage_logs.get(i).getID()) == null)
				{
					total_damage = total_damage + damage_logs.get(i).getDamage();
					x[i] = damage_logs.get(i).getTime() / 1000.0;
					y[i] = total_damage / 1000;
				}
			}
			chart.addSeries(p.getCharacter() + " - " + p.getProf(), x, y);
		}

		// Write chart to .png

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
		table.addRow("Account", "Character", "Group", "Profession", "CRIT", "SCHL", "MOVE", "FLNK", "TGHN", "HEAL",
				"COND", "SWAP", "DOGE", "RESS", "DOWN", "DIED");

		// Body
		for (Player p : player_list)
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
			table.addRow(new String[] { p.getAccount(), p.getCharacter(), p.getGroup(), p.getProf(),
					String.format("%.2f", critical_rate / power_loop_count),
					String.format("%.2f", scholar_rate / power_loop_count),
					String.format("%.2f", moving_rate / power_loop_count),
					String.format("%.2f", flanking_rate / power_loop_count), String.valueOf(p.getToughness()),
					String.valueOf(p.getHealing()), String.valueOf(p.getCondition()), String.valueOf(swap),
					String.valueOf(dodge), String.valueOf(ress), String.valueOf(down),
					String.format("%06.2f", died / 1000.0) });
		}

		return table.toString();
	}

	// Final Boons
	public String getFinalBoons()
	{

		List<String> boon_list = Boon.getList();
		BoonFactory boonFactory = new BoonFactory();
		List<String[]> all_rates = new ArrayList<String[]>();

		for (int i = 0; i < player_list.size(); i++)
		{
			Player p = player_list.get(i);
			Map<String, List<BoonLog>> boon_logs = p.getBoonMap(b_data, s_data, c_data.getCombatList());
			String[] rates = new String[boon_list.size()];
			for (int j = 0; j < boon_list.size(); j++)
			{
				Boon boon = Boon.getEnum(boon_list.get(j));
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
				rates[j] = rate;
			}
			all_rates.add(rates);
		}

		// Table
		TableBuilder table = new TableBuilder();
		table.addTitle("Final Boon Rates - " + b_data.getName());

		// Header
		String[] boon_array = Boon.getArray();
		table.addRow(Utility.concatStringArray(new String[] { "Name", "Profession" }, boon_array));

		// Body
		for (int i = 0; i < player_list.size(); i++)
		{
			Player p = player_list.get(i);
			table.addRow(Utility.concatStringArray(new String[] { p.getCharacter(), p.getProf() }, all_rates.get(i)));
		}

		return table.toString();
	}

	// Phase Boons
	public String getPhaseBoons()
	{

		List<String> boon_list = Boon.getList();
		BoonFactory boonFactory = new BoonFactory();
		List<String[][]> all_rates = new ArrayList<String[][]>();
		List<Point> fight_intervals = getPhaseIntervals();

		for (int i = 0; i < player_list.size(); i++)
		{

			Player p = player_list.get(i);
			Map<String, List<BoonLog>> boon_logs = p.getBoonMap(b_data, s_data, c_data.getCombatList());

			String[][] rates = new String[boon_logs.size()][];

			for (int j = 0; j < boon_list.size(); j++)
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

		StringBuilder output = new StringBuilder();
		TableBuilder table = new TableBuilder();
		String[] boon_array = Boon.getArray();

		String title = " Phase Boon Rates - " + b_data.getName() + ' ';
		output.append('\u250C' + Utility.fillWithChar(title.length(), '\u2500') + '\u2510' + System.lineSeparator());
		output.append('\u2502' + title + '\u2502' + System.lineSeparator());
		output.append('\u2514' + Utility.fillWithChar(title.length(), '\u2500') + '\u2518');

		for (int i = 0; i < fight_intervals.size(); i++)
		{

			table.clear();
			table.addTitle("Phase " + (i + 1));
			table.addRow(Utility.concatStringArray(new String[] { "Name", "Profession" }, boon_array));
			for (int j = 0; j < player_list.size(); j++)
			{
				Player p = player_list.get(j);

				String[][] player_rates = all_rates.get(j);
				String[] row_rates = new String[boon_array.length];
				for (int k = 0; k < boon_array.length; k++)
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
			time_threshold = 5000;
			health_thresholds = new int[] { 8000, 6000, 4000, 2000, 1000 };
		}
		else if (b_data.getName().equals("Matthias Gabrel"))
		{
			time_threshold = 1000;
			health_thresholds = new int[] { 8000, 6000, 4000 };
		}

		else if (b_data.getName().equals("Keep Construct"))
		{
			time_threshold = 20000;
			health_thresholds = new int[] { 6600, 3300 };
		}
		// else if (b_data.getName().equals("Xera"))
		// {
		// time_threshold = 20000;
		// health_thresholds = new int[] { 5000 };
		// }
		else if (b_data.getName().equals("Samarog"))
		{
			time_threshold = 20000;
			health_thresholds = new int[] { 6600, 3300 };
		}
		// else if (b_data.getName().equals("Deimos"))
		// {
		// time_threshold = 1000;
		// health_thresholds = new int[] { 7500, 5000, 2500 };
		// }
		else
		{
			fight_intervals.add(new Point(time_start, time_end));
			return fight_intervals;
		}

		// Generate intervals with health updates
		ListIterator<Point> iter = c_data.getStates(b_data.getInstid(), StateChange.HEALTH_UPDATE).listIterator();

		// for (Point x : c_data.getStates(b_data.getInstid(),
		// StateChange.HEALTH_UPDATE))
		// {
		// System.out.println(x.x - log_start + " " + x.y);
		// }

		Point previous_update = iter.next();
		main: for (int threshold : health_thresholds)
		{
			while (iter.hasNext())
			{
				Point current_update = iter.next();
				if ((current_update.y < threshold) && ((current_update.x - previous_update.x) > time_threshold))
				{
					fight_intervals.add(new Point(time_start, previous_update.x - log_start));
					time_start = current_update.x - log_start;
					previous_update = current_update;
					continue main;
				}
				previous_update = current_update;
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
		double average_duration = 0;
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
			double duration = 0;
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
		return String.format("%.2f", average_stacks / boon_stacks.size());
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
			phase_stacks[i] = String.format("%.2f", average_stacks / phase_boon_stacks.size());
		}
		return phase_stacks;
	}

}
