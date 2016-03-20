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
	TextField usuarioNome;

	@FXML
	PasswordField usuarioPassword, usuarioConPassword, usuarioAtualPassword;

	@FXML
	Button alterar, cancelar;

	private Usuario usuario;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		alterar.setOnMouseClicked((MouseEvent e) -> {
			// Verifica se a senha atual esta correta
			if (Usuario.isValidSenha(usuario.getNome(), usuarioAtualPassword.getText())) {
				try {
					boolean update = true;

					// Obtem os dados originais do usuario
					Usuario u = Usuario.getByID(usuario.getId());

					// Define os dados modificados
					u.setNome(usuarioNome.getText());
					if (!usuarioPassword.equals("")) {
						if (usuarioPassword.getText().equals(usuarioConPassword.getText())) {
							u.setSenha(usuarioPassword.getText());
						} else {
							CustomAlert.showAlert("Usuário - Atualização", "As senhas não conferem",
									AlertType.INFORMATION);
							update = false;
						}
					}

					// Atualiza o usuario
					if (update && u.update()) {
						CustomAlert.showAlert("Usuário - Atualização", "Usuário atualizado com sucesso",
								AlertType.INFORMATION);
						Main.loadListaUsuarioView();
					}
				} catch (RuntimeException h) {
					CustomAlert.showAlert("Usuário - Atualização", h.getMessage(), AlertType.INFORMATION);
				}

			} else {
				CustomAlert.showAlert("Usuário - Atualização",
						"A senha informada está incorreta. Caso esteja modificando sua senha utilize a senha antiga",
						AlertType.INFORMATION);
			}

		});

	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		usuarioNome.setText(usuario.getNome());
	}

}
