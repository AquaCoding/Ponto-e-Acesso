package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.pontoacesso.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class SuporteController implements Initializable {

	@FXML
	Button cancelar;
	
	@FXML
	TextArea suporte;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		suporte.setText("Ponto Acesso \n"
				+ "Sistema desenvolvido pela AquaCoding");
		
	}
	
}
