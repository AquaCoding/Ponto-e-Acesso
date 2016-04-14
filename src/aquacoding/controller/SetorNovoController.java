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

public class SetorNovoController implements Initializable {
	
	// Obtem os elementos FXML
	@FXML
	Button cancelar, cadastrar;
	
	@FXML
	TextField setorNome;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela o cadastro e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Tenta realizar o cadastro
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Cria um novo objeto do setor
				Setor s = new Setor(setorNome.getText());
				
				// Tenta registar o setor no BD
				if(s.create()) {
					// Setor criado com sucesso
					CustomAlert.showAlert("Novo Setor", "Novo setor cadastrado com sucesso.", AlertType.WARNING);
					Main.loadListaSetorView();
				} else {
					// Erro ao cria o setor
					CustomAlert.showAlert("Novo Setor", "Algo deu errado.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Novo Setor", ex.getMessage(), AlertType.WARNING);
			}
		});
	}

}
