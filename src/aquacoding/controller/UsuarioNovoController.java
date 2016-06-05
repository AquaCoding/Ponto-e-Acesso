package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Usuario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class UsuarioNovoController implements Initializable {
	@FXML
	TextField usuarioNome;

	@FXML
	PasswordField usuarioPassword, usuarioConPassword;

	@FXML
	Button cadastrar, cancelar;
	
	private boolean closeAfterCreate = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MaskField.submitForm(usuarioConPassword, cadastrar);
		
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		cadastrar.setOnAction((ActionEvent e) -> {
			if (usuarioPassword.getText().equals("") || usuarioConPassword.getText().equals("")
					|| usuarioNome.getText().equals("")) {
				CustomAlert.showAlert("Usuário - Cadastro", "Todos os campos são obrigatários.", AlertType.INFORMATION);
			} else {
				if (usuarioPassword.getText().equals(usuarioConPassword.getText())) {
					try {
						Usuario u = new Usuario(usuarioNome.getText(), usuarioPassword.getText());
						if (u.create()) {
							CustomAlert.showAlert("Usuário - Cadastro", "O usuário foi criado com sucesso.", AlertType.INFORMATION);
							
							if(closeAfterCreate) {
								Main.endLoginLayout();
								Main.initLoginLayout();
							} else {
								Main.loadListaUsuarioView();
							}
						}
					} catch (RuntimeException h) {
						CustomAlert.showAlert("Usuario - Cadastro", h.getMessage(), AlertType.INFORMATION);
					}
				} else {
					CustomAlert.showAlert("Usuário - Cadastro", "As senhas informadas não são iguais.", AlertType.INFORMATION);
				}
			}
		});

	}

	// Informa se apos cadastrar um usuario, precisa iniciar o login
	public void setCloseAfterCreate(boolean b) {
		this.closeAfterCreate = b;
	}



}
