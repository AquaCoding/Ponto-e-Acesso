package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Setor;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class SetorEditarController implements Initializable {

	// Obtem os elementos FXML
	@FXML
	Button cancelar, alterar;

	@FXML
	TextField setorNome;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a edi��o e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Tenta realizar a edi��o
		alterar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Cria um novo objeto do setor
				Setor s = new Setor(setorNome.getText());

				// Tenta alterar o setor no BD
				if (s.update()) {
					// Setor alterado com sucesso
					CustomAlert.showAlert("Editar Setor", "Setor alterado com sucesso", AlertType.WARNING);
					Main.loadListaSetorView();
				} else {
					// Erro ao cria o setor
					CustomAlert.showAlert("Editar Setor", "Algo deu errado", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				// Erro de valida��o
				CustomAlert.showAlert("Editar Setor", ex.getMessage(), AlertType.WARNING);
			}
		});

		
	}

}
