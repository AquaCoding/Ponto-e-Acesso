package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Usuario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class UsuarioNovoController implements Initializable {
	private boolean closeAfterCreate = false;
	@FXML
	TextField usuarioNome;

	@FXML
	PasswordField usuarioPassword, usuarioConPassword;

	@FXML
	Button cadastrar, cancelar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			if (usuarioPassword.getText().equals("") || usuarioConPassword.getText().equals("")
					|| usuarioNome.getText().equals("")) {
				CustomAlert.showAlert("Usuario - Cadastro", "Todos os campos são obrigatórios", AlertType.INFORMATION);
			} else {
				if (usuarioPassword.getText().equals(usuarioConPassword.getText())) {
					try {
						Usuario u = new Usuario(usuarioNome.getText(), usuarioPassword.getText());
						if (u.create()) {
							CustomAlert.showAlert("Usuario - Cadastro", "Um usuário foi criado com sucesso",
									AlertType.INFORMATION);
							Main.loadListaUsuarioView();
							;
						}
					} catch (RuntimeException h) {
						CustomAlert.showAlert("Usuario - Cadastro", h.getMessage(), AlertType.INFORMATION);
					}
				} else {
					CustomAlert.showAlert("Usuario - Cadastro", "As senhas informadas não batem", AlertType.INFORMATION);
				}
			}
		});

	}



}
