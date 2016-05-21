package aquacoding.utils.tablegenerator;

public class TableData {

	private Object value;
	private int rowSpan = 0;
	private int colSpan = 0;
	private boolean isTH = false;
	
	// Create a empty table data
	public TableData(){}
	
	// Create and populate a table data
	public TableData(Object value) {
		if(value != null)
			this.value = value;
	}
	
	// Set the table data value
	public void setValue(Object value) {
		if(value != null)
			this.value = value;
	}
	
	// Get table data value
	public Object getValue() {
		return value;
	}
	
	// Set the rowSpan property
	public void setRowSpan(int value) {
		this.rowSpan = value;
	}
	
	// Get the rowSpan property
	public int getRowSpan() {
		return rowSpan;
	}
	
	// Set the colSpan property
	public void setColSpan(int value) {
		this.colSpan = value;
	}
	
	// Get the colSpan property
	public int getColSpan() {
		return colSpan;
	}
	
	// Set the table data to work as a table head
	public void setIsTH(boolean value) {
		isTH = value;
	}
	
	// Get the isTH value
	public boolean isTH() {
		return isTH;
	}
	
	// Return the data formated as a HTML <td> or <th>
	public String getAsHTML() {
		if(isTH)
			return "<th colSpan=\""+ getColSpan() +"\" rowSpan=\""+ getRowSpan() +"\">" + getValue() + "</th>";
		
		return "<td colSpan=\""+ getColSpan() +"\" rowSpan=\""+ getRowSpan() +"\">" + getValue() + "</td>";
	}
	
}
