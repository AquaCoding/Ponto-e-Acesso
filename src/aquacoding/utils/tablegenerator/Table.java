package aquacoding.utils.tablegenerator;

import java.util.ArrayList;

public class Table {
	
	private ArrayList<TableRow> tr = new ArrayList<TableRow>();
	
	// Create a empty Table
	public Table() {}
	
	// Create and populate a table
	public Table(ArrayList<TableRow> values) {
		tr.addAll(values);
	}
	
	// Add a row to table
	public void add(TableRow value) {
		if(value != null)
			tr.add(value);
	}
	
	// Add a row list to a table
	public void addAll(ArrayList<TableRow> values) {
		tr.addAll(values);
	}
	
	// Return the table as a HTML <table>
	public String getAsHTML() {
		String table = "<table>";
		for(TableRow row: tr) {
			table += row.getAsHTML();
		}
		table += "</table>";
		
		return table;
	}
}
