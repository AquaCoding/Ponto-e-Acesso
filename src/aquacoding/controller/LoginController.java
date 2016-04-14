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

public class LoginController implements Initializable {
	
	@FXML
	private TextField tfUsuario;
	
	@FXML
	private PasswordField pfSenha;
	
	@FXML
	private Button bEntrar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bEntrar.setOnMouseClicked((MouseEvent e) -> {
			boolean senhaCorreta = Usuario.isValidSenha(this.tfUsuario.getText(), this.pfSenha.getText());
			
			if(senhaCorreta) {
				Main.initRootLayout();
				Main.endLoginLayout();
			} else {
				CustomAlert.showAlert("Caixa - Entrar", "Nome de usuário ou senha incorretos.", AlertType.INFORMATION);
			}
		});
	}

}
