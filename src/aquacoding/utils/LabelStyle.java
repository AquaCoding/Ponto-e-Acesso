package aquacoding.utils;

public class LabelStyle {
	private int fontSize = 0;
	private boolean bold = false;
	private boolean italic = false;
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public boolean isBold() {
		return bold;
	}
	
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	public boolean isItalic() {
		return italic;
	}
	
	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public String toStyle() {
		String style = "-fx-font-size: "+ fontSize + ";";
		
		if(bold) {
			style += "-fx-font-weight: bold;";
		} else {
			style += "-fx-font-weight: none;";
		}
		
		if(italic) {
			style += "-fx-font-style: italic;";
		} else {
			style += "-fx-font-style: none;";
		}
		
		return style;
	}
	
	
}
