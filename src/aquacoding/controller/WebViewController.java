package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebViewController implements Initializable {

	@FXML
	WebView webView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void openPage(String fileToOpen) {
		WebEngine engine = webView.getEngine();
		File a = new File(fileToOpen);
		engine.load("file:///" + a.getAbsolutePath());
	}

}
