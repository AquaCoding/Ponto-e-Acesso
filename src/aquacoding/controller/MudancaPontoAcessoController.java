package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.Serial;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class MudancaPontoAcessoController implements Initializable {

	@FXML
	Button cancelar, setarCOM, mudarPontoAcesso;
	
	@FXML
	Label statusCOM;
	
	@FXML
	TextField editCOM, statusPontoAcesso;
	
	private Serial serial = Serial.getInstance();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do botão cancelar		
		
		statusCOM.setText(serial.getPort());
		
		if(serial.getStatus()) {
			statusPontoAcesso.setText("Ponto");
		} else {
			statusPontoAcesso.setText("Acesso");
		}
		
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		setarCOM.setOnMouseClicked((MouseEvent e) -> {
			String COM = editCOM.getText();			
			serial.setPort(COM);
			statusCOM.setText(serial.getPort());
			CustomAlert.showAlert("Mudança Ponto - Acesso", "Porta COM setada com sucesso: "+ statusCOM.getText(), AlertType.INFORMATION);
		});
		
		mudarPontoAcesso.setOnMouseClicked((MouseEvent e) -> {
			if(statusPontoAcesso.getText().equals("Ponto")) {
				serial.setStatus(false);
				statusPontoAcesso.setText("Acesso");
				CustomAlert.showAlert("Mudança Ponto - Acesso", "Sistema alterado para registro de acesso", AlertType.INFORMATION);
			}else {
				serial.setStatus(true);
				statusPontoAcesso.setText("Ponto");
				CustomAlert.showAlert("Mudança Ponto - Acesso", "Sistema alterado para registro de ponto", AlertType.INFORMATION);
			}
		});
		
	}
	
}
