package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Backup;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebViewController implements Initializable {

	@FXML
	WebView webView;
	
	@FXML
	Label title;
	
	@FXML 
	TextField code;
	
	@FXML
	Button confirmCode;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void openPage(String fileToOpen) {
		WebEngine engine = webView.getEngine();
		engine.load(fileToOpen);
	}

	public void showCodeConfirm(boolean restaurarDropBox) {
		title.setText("Insira seu código e confirme");
		
		code.setVisible(true);
		confirmCode.setVisible(true);
		
		confirmCode.setOnMouseClicked((MouseEvent e) -> {
			if(!code.equals("")) {
				if (restaurarDropBox) {
					Backup.restaurarDropBox(code.getText());
				} else {
					Backup.criarSalvarBackup(code.getText());
				}				
			} else {
				CustomAlert.showAlert("Backup", "O seu codigo é inválido", AlertType.WARNING);
			}
		});
	}

}
