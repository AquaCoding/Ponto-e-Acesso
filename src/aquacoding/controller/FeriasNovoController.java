package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Ferias;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class FeriasNovoController implements Initializable {

	@FXML
	TextField feriasNome;

	@FXML
	Button cancelar, cadastrar;

	@FXML
	DatePicker feriasInicio, feriasTermino;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				//inicio
				java.sql.Date inicio = java.sql.Date.valueOf(feriasInicio.getValue());
				//termino
				java.sql.Date termino = java.sql.Date.valueOf(feriasTermino.getValue());

				// Cria novo horario
				Ferias f = new Ferias(feriasNome.getText(), inicio, termino);

				// Salva novo horario no BD
				if(f.create()) {
					CustomAlert.showAlert("Nova Férias", "Nova férias cadastrado com sucesso.", AlertType.WARNING);
					Main.loadListaFeriasView();
				} else {
					CustomAlert.showAlert("Nova Férias", "Algo de errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Nova Férias", ex.getMessage(), AlertType.WARNING);
			}
		});
	}
	}



