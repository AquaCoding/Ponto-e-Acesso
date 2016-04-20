package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;





import aquacoding.model.Imposto;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class ImpostoNovoController implements Initializable {

	@FXML
	TextField nome, valor;
	
	@FXML
	Button cancelar, cadastrar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Adiciona mascaras no campo
		MaskField.doubleMask(valor);
		
		// Evento do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Evento do botão cadastrar
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				Imposto i = new Imposto(nome.getText(), Double.parseDouble(valor.getText()));
				if(i.create()) {
					CustomAlert.showAlert("Novo Imposto", "Imposto cadastrado com sucesso.", AlertType.WARNING);
				} else {
					CustomAlert.showAlert("Novo Imposto", "Algo deu errado.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Novo Imposto", ex.getMessage(), AlertType.WARNING);
			}
		});
	}

}
