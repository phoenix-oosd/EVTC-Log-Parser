package parse;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
 
public class TableBuilder {
 
	// Fields
	private String title = "";
	private List<String[]> rows = new LinkedList<String[]>();
	
	// Public Methods
    public void addTitle(String title) {
        this.title = title;
    }
	
    public void addRow(String... cols) {
        rows.add(cols);
    }

    // Private Methods
    private int[] getWidths() {
    	// Get number of columns
        int cols = -1;
        for(String[] row : rows)
            cols = Math.max(cols, row.length);
        // Get the widths of each column
        int[] widths = new int[cols];
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                widths[i] = Math.max(widths[i], row[i].length()) + 1;
            }
        }
        return widths;
    }
 
    private String center(String text, int len){
        String out = String.format("%"+len+"s%s%"+len+"s", "",text,"");
        float mid = (out.length()/2);
        float start = mid - (len/2);
        float end = start + len; 
        return out.substring((int)start, (int)end);
    }
    
    private String fill(int length, char charToFill) {
    	  if (length > 0) {
    	    char[] array = new char[length];
    	    Arrays.fill(array, charToFill);
    	    return new String(array);
    	  }
    	  return "";
    }
    
    // Public Methods
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int[] colWidths = getWidths();
        // Discord
        str.append("```\n");
        // Title
        if (!title.equals("")) {
        	str.append(fill(title.length(), '*') + "\n");
        	str.append(title + "\n");
        	str.append(fill(title.length(), '*') + "\n");
        }
        // Header
        for(int colNum = 0; colNum < rows.get(0).length; colNum++) {
            str.append(center(rows.get(0)[colNum], colWidths[colNum]));
        }
        str.append("\n");
        for(int colNum = 0; colNum < rows.get(0).length; colNum++) {
            str.append(center(fill(colWidths[colNum] - 2, '-'), colWidths[colNum]));
        }
        // Body
        for (ListIterator<String[]> iter = rows.listIterator(1); iter.hasNext();) {
        	String[] row = iter.next();
            for(int colNum = 0; colNum < row.length; colNum++) {
                str.append(center(row[colNum], colWidths[colNum]));
            }
 
            str.append('\n');
        }
        // Discord
        str.append("```\n");
        return str.toString();
    }
 
}