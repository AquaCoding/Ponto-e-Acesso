package aquacoding.utils.tablegenerator;

import java.util.ArrayList;

public class TableRow {

	private ArrayList<TableData> td = new ArrayList<TableData>();
	
	// Create a empty row
	public TableRow() {}
	
	// Create and populate a row
	public TableRow(ArrayList<TableData> values) {
		td.addAll(values);
	}
	
	// Add a data to a row
	public void add(TableData value) {
		if(value != null)
			td.add(value);
	}
	
	// Add a list of datas to a row
	public void addAll(ArrayList<TableData> values) {
		td.addAll(values);
	}
	
	// Get all table data in the row
	public ArrayList<TableData> getData() {
		return td;
	}

	// Return the row as a HTML <tr>
	public String getAsHTML() {
		String row = "<tr>";
		for(TableData data: td) {
			row += data.getAsHTML();
		}
		row += "</tr>";
		
		return row;
	}
}
