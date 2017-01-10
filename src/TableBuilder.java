import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
 
public class TableBuilder {
 
	String title = "";
	List<String[]> rows = new LinkedList<String[]>();
	
	
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
    
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
 
        int[] colWidths = getWidths();
        
        // Discord
        buf.append("```\n");
        
        // Title
        if (!title.equals("")) {
        	buf.append(fill(title.length(), '*') + "\n");
        	buf.append(title + "\n");
        	buf.append(fill(title.length(), '*') + "\n");
        }
 
        // Header
        for(int colNum = 0; colNum < rows.get(0).length; colNum++) {
            buf.append(center(rows.get(0)[colNum], colWidths[colNum]));
        }
        buf.append("\n");
        for(int colNum = 0; colNum < rows.get(0).length; colNum++) {
            buf.append(center(fill(colWidths[colNum] - 2, '-'), colWidths[colNum]));
        }
        buf.append("\n");
        
        // Body
        for (ListIterator<String[]> iter = rows.listIterator(1); iter.hasNext();) {
        	String[] row = iter.next();
            for(int colNum = 0; colNum < row.length; colNum++) {
                buf.append(center(row[colNum], colWidths[colNum]));
            }
 
            buf.append('\n');
        }
        
        // Discord
        buf.append("```\n");
 
        return buf.toString();
    }
 
}