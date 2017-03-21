package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class TableBuilder
{

	// Fields
	private String title = "";
	private List<String[]> rows = new ArrayList<String[]>();
	private String nl = System.lineSeparator();

	// Public Methods
	public void addTitle(String title)
	{
		this.title = ' ' + title + ' ';
	}

	public void addRow(String... cols)
	{
		rows.add(cols);
		String[] row = rows.get(rows.size() - 1);
		for (int i = 0; i < row.length; i++)
		{
			row[i] = ' ' + row[i] + ' ';
		}
	}

	public void addSeparator()
	{
		if (rows.size() > 2)
		{
			String[] separator = new String[rows.get(0).length];
			Arrays.fill(separator, "");
			rows.add(separator);
		}
	}

	public void sortAsDouble(int col)
	{
		// Get body
		int separator = 0;
		for (String[] row : rows)
		{
			if (row[0].equals(""))
			{
				break;
			}
			separator++;
		}
		List<String[]> body = rows.subList(1, separator);

		// Sort by column
		Collections.sort(body, new Comparator<String[]>()
		{
			@Override
			public int compare(String[] x, String[] y)
			{
				double x_dbl = Double.parseDouble(x[col].replaceAll("\\s+", ""));
				double y_dbl = Double.parseDouble(y[col].replaceAll("\\s+", ""));
				if (x_dbl > y_dbl)
				{
					return -1;
				}
				else if (x_dbl == y_dbl)
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
		});
	}

	public void clear()
	{
		title = "";
		rows = new ArrayList<String[]>();
	}

	@Override
	public String toString()
	{
		// Initialize
		removeEmptyColumns();
		StringBuilder output = new StringBuilder();
		int[] colWidths = getWidths();
		int numCols = rows.get(0).length;

		// Title
		if (!title.equals(""))
		{
			output.append('\u250C' + Utility.fillWithChar(title.length(), '\u2500') + '\u2510' + nl);
			output.append('\u2502' + title + '\u2502' + nl);
			output.append('\u2514' + Utility.fillWithChar(title.length(), '\u2500') + '\u2518');
		}

		// Empty
		if (rows.size() <= 1)
		{
			return output.toString();
		}

		// Header
		output.append(nl + '\u250C');
		for (int colNum = 0; colNum < rows.get(0).length; colNum++)
		{
			output.append(Utility.fillWithChar(colWidths[colNum], '\u2500'));
			if (colNum != numCols - 1)
			{
				output.append('\u252C');
			}
			else
			{
				output.append('\u2510' + nl + '\u2502');
			}
		}
		for (int colNum = 0; colNum < numCols; colNum++)
		{
			output.append(Utility.centerString(rows.get(0)[colNum], colWidths[colNum]) + '\u2502');
		}
		output.append(nl + '\u251C');
		for (int colNum = 0; colNum < rows.get(0).length; colNum++)
		{
			output.append(Utility.fillWithChar(colWidths[colNum], '\u2550'));
			if (colNum != numCols - 1)
			{
				output.append('\u253C');
			}
			else
			{
				output.append('\u2524' + nl);
			}
		}

		// Body
		for (ListIterator<String[]> iter = rows.listIterator(1); iter.hasNext();)
		{
			String[] row = iter.next();
			for (int colNum = 0; colNum < row.length; colNum++)
			{
				if (row[0].equals(""))
				{
					if (colNum == 0)
					{
						output.append('\u251C');
					}
					else
					{
						output.append('\u253C');
					}
					output.append(Utility.fillWithChar(colWidths[colNum], '\u2500'));
				}
				else
				{
					output.append('\u2502' + Utility.centerString(row[colNum], colWidths[colNum]));
				}
			}
			if (row[0].equals(""))
			{
				output.append('\u2524' + nl);
			}
			else
			{
				output.append('\u2502' + nl);
			}
		}
		output.append('\u2514');
		for (int colNum = 0; colNum < rows.get(0).length; colNum++)
		{
			output.append(Utility.fillWithChar(colWidths[colNum], '\u2500'));
			if (colNum != numCols - 1)
			{
				output.append('\u2534');
			}
			else
			{
				output.append('\u2518');
			}
		}

		return output.toString();
	}

	// Private Methods
	private int[] getWidths()
	{
		int cols = 0;
		for (String[] row : rows)
			cols = Math.max(cols, row.length);
		int[] widths = new int[cols];
		for (String[] row : rows)
		{
			for (int i = 0; i < row.length; i++)
			{
				widths[i] = Math.max(widths[i], row[i].length());
			}
		}
		return widths;
	}

	private void removeEmptyColumns()
	{

		// Columns that contain all "0.00" will have existance[i] = false
		int cols = rows.get(0).length;
		boolean[] existance = new boolean[cols];
		for (ListIterator<String[]> iter = rows.listIterator(1); iter.hasNext();)
		{
			String[] row = iter.next();
			for (int i = 0; i < cols; i++)
			{
				if (!existance[i] && !row[i].equals(" 0.00 "))
				{
					existance[i] = true;
				}
			}
		}

		// Check if there are any false values
		int exist_count = 0;
		for (boolean exists : existance)
		{
			if (exists)
			{
				exist_count++;
			}
		}
		if (exist_count == cols)
		{
			return;
		}

		// Create new table with no empty columns
		List<String[]> new_rows = new ArrayList<String[]>();
		for (String[] row : rows)
		{
			int i = 0;
			int j = 0;
			String[] new_row = new String[exist_count];
			for (boolean exists : existance)
			{
				if (exists)
				{
					new_row[i] = row[j];
					i++;
				}
				j++;
			}
			new_rows.add(new_row);
		}
		rows = new_rows;

	}

}