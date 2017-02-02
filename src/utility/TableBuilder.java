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

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		int[] colWidths = getWidths();
		// Title
		if (!title.equals("")) {
			output.append(Utility.fill(title.length(), '_') + System.lineSeparator() + System.lineSeparator());
			output.append(title + System.lineSeparator());
			output.append(Utility.fill(title.length(), '_') + System.lineSeparator() + System.lineSeparator());
		}
		// Header
		for (int colNum = 0; colNum < rows.get(0).length; colNum++) {
			output.append(Utility.center(rows.get(0)[colNum], colWidths[colNum]));
			output.append("   ");
		}
		output.append(System.lineSeparator());
		for (int colNum = 0; colNum < rows.get(0).length; colNum++) {
			output.append(Utility.center(Utility.fill(colWidths[colNum], '_'), colWidths[colNum]));
			output.append("   ");
		}
		output.append(System.lineSeparator() + System.lineSeparator());
		// Body
		for (ListIterator<String[]> iter = rows.listIterator(1); iter.hasNext();) {
			String[] row = iter.next();
			for (int colNum = 0; colNum < row.length; colNum++) {
				output.append(Utility.center(row[colNum], colWidths[colNum]));
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