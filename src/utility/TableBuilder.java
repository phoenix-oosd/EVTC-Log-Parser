package utility;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TableBuilder {

	// Fields
	private String title = "";
	private List<String[]> rows = new ArrayList<String[]>();

	// Public Methods
	public void addTitle(String title) {
		this.title = title;
	}

	public void addRow(String... cols) {
		rows.add(cols);
	}

	public void clear() {
		title = "";
		rows = new ArrayList<String[]>();
	}

	public void removeEmptyColumns() {

		// Columns that contain all "0.00" will have existance[i] = false
		int cols = rows.get(0).length;
		boolean[] existance = new boolean[cols];
		for (ListIterator<String[]> iter = rows.listIterator(1); iter.hasNext();) {
			String[] row = iter.next();
			for (int i = 0; i < cols; i++) {
				if (!existance[i] && !row[i].equals("0.00")) {
					existance[i] = true;
				}
			}
		}

		// Check if there are any false values
		int exist_count = 0;
		for (boolean exists : existance) {
			if (exists) {
				exist_count++;
			}
		}
		if (exist_count == cols) {
			return;
		}

		// Create new table with no empty columns
		List<String[]> new_rows = new ArrayList<String[]>();
		for (String[] row : rows) {
			int i = 0;
			int j = 0;
			String[] new_row = new String[exist_count];
			for (boolean exists : existance) {
				if (exists) {
					new_row[i] = row[j];
					i++;
				}
				j++;
			}
			new_rows.add(new_row);
		}
		rows = new_rows;

	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		int[] colWidths = getWidths();
		// Title
		if (!title.equals("")) {
			output.append(Utility.fillWithChar(title.length(), '_') + System.lineSeparator() + System.lineSeparator());
			output.append(title + System.lineSeparator());
			output.append(Utility.fillWithChar(title.length(), '_') + System.lineSeparator() + System.lineSeparator());
		}
		// Header
		for (int colNum = 0; colNum < rows.get(0).length; colNum++) {
			output.append(Utility.centerString(rows.get(0)[colNum], colWidths[colNum]));
			output.append("   ");
		}
		output.append(System.lineSeparator());
		for (int colNum = 0; colNum < rows.get(0).length; colNum++) {
			output.append(Utility.centerString(Utility.fillWithChar(colWidths[colNum], '_'), colWidths[colNum]));
			output.append("   ");
		}
		output.append(System.lineSeparator() + System.lineSeparator());
		// Body
		for (ListIterator<String[]> iter = rows.listIterator(1); iter.hasNext();) {
			String[] row = iter.next();
			for (int colNum = 0; colNum < row.length; colNum++) {
				output.append(Utility.centerString(row[colNum], colWidths[colNum]));
				output.append("   ");
			}
			output.append(System.lineSeparator());
		}
		return output.toString();
	}

	// Private Methods
	private int[] getWidths() {
		int cols = 0;
		for (String[] row : rows)
			cols = Math.max(cols, row.length);
		int[] widths = new int[cols];
		for (String[] row : rows) {
			for (int i = 0; i < row.length; i++) {
				widths[i] = Math.max(widths[i], row[i].length());
			}
		}
		return widths;
	}

}