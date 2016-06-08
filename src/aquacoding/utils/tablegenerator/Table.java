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
		String table = "<head>";
				table += "\n <meta charset='UTF-8'>";
				table += "\n <title>Document</title>";
				table += "\n </head>" + " \n <body>";

		table += " \n <style>* {margin: 0; padding: 0} p {font-size: 10px;} table {border-collapse: collapse;} .left {float: left} .right {float: right} td, th {padding: 5px; border: 1px solid #666;} th {background-color: #4CAF50; color: #FFF;}</style> "
				+ "\n <table>";
		for(TableRow row: tr) {
			table += row.getAsHTML();
		}
		table += "</table>";
		table += " \n </body>";
		table += "\n </hmtl>";

		return table;
	}
}
