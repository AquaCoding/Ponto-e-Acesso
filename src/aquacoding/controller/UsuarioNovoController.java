package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Usuario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class UsuarioNovoController implements Initializable {

	@FXML
	PasswordField usuarioPassword, usuarioConPassword;

	@FXML
	TextField usuarionome;

	@FXML
	Button cancelar, cadastrar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Define evento no bot�o cancelar
				cancelar.setOnMouseClicked((MouseEvent e) -> {
					Main.loadMainView();
				});

				// Define o evento do bot�o cadastrar
				cadastrar.setOnMouseClicked((MouseEvent e) -> {
					try {

						// Cria um novo Usu�rio
						Usuario u = new Usuario(usuarionome.getText(), usuarioPassword.getText());

						// Registra nova fun��o no BD
						if(u.create()) {
							CustomAlert.showAlert("Novo Usu�rio", "Novo usu�rio cadastrada com sucesso.", AlertType.WARNING);
							Main.loadListaFuncaoView();
						} else {
							CustomAlert.showAlert("Novo Usu�rio", "Um erro ocorreu.", AlertType.WARNING);
						}

					} catch (RuntimeException ex) {
						CustomAlert.showAlert("Novo Usu�rio", ex.getMessage(), AlertType.WARNING);
					}
				});
			}

}
