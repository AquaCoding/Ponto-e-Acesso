package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.DataHora;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class DataHoraController implements Initializable {

	@FXML
	Button cancelar, confirmar;
	
	@FXML
	TextField  data;
	
	@FXML
	TextField hora, minuto;
	
	private DataHora dataHora;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		confirmar.setOnMouseClicked((MouseEvent e) -> {
			if(hora.getText().equals("")) { 
				CustomAlert.showAlert("Data Hora Ponto - Acesso", "Hora não especificada", AlertType.INFORMATION);
			} else if(minuto.getText().equals("")) { 
				CustomAlert.showAlert("Data Hora Ponto - Acesso", "Minuto não especificado", AlertType.INFORMATION);
			} else {															
				dataHora.setar(data.getText(), hora.getText(), minuto.getText());
			}
		});
		
	}

}
