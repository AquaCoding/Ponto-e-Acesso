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

public class UsuarioEditarController implements Initializable {

	@FXML
	PasswordField usuarioPassword, usuarioConPassword, usuarioAtualPassword;

	@FXML
	TextField usuarionome;

	@FXML
	Button cancelar, alterar;

	private Usuario usuario;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Define evento no botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});


		alterar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Salva as alterações no objeto
				usuario.setNome(usuarionome.getText());
				if(usuarioPassword.getText().equals(usuarioAtualPassword.getText())){
					usuario.setSenha(usuarionome.getText());
				}


				// Tenta atualizar
				if(usuario.update()) {
					CustomAlert.showAlert("Editar Usuário", "Usuário editada com sucesso.", AlertType.WARNING);
					Main.loadListaFuncaoView();
				} else {
					CustomAlert.showAlert("Editar Usuário", "Algo errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Editar Usuário", ex.getMessage(), AlertType.WARNING);
			}
		});

	}

	// Define a função que esta sendo usada para a edição
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		usuarionome.setText(usuario.getNome());

	}

}
