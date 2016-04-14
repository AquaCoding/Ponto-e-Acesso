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
	
	private Setor setor;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a edição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Tenta realizar a edição
		alterar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Atualiza a info do setor
				setor.setNome(setorNome.getText());

				// Tenta alterar o setor no BD
				if (setor.update()) {
					// Setor alterado com sucesso
					CustomAlert.showAlert("Editar Setor", "Setor alterado com sucesso.", AlertType.WARNING);
					Main.loadListaSetorView();
				} else {
					// Erro ao cria o setor
					CustomAlert.showAlert("Editar Setor", "Algo deu errado.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Editar Setor", ex.getMessage(), AlertType.WARNING);
			}
		});

		
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
		setorNome.setText(setor.getNome());
	}

}
