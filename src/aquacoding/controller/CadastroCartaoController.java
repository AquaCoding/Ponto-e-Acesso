package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import aquacoding.model.Cartao;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.Serial;

public class CadastroCartaoController implements Initializable {

	@FXML
	Button cancelar, cadastrar;
	
	private Funcionario func;

	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do bot�o cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaFuncionarioView();
		});

		CustomAlert.showAlert("Cadastro de Cart�o", "Aproxime seu cart�o do leitor e clique em cadastrar. O sensor ficar desabilitado para leitura por 15 segundos.", AlertType.INFORMATION);
		Serial serial = Serial.getInstance();
		serial.stopLogic(15000);
		
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			// Serial
			
			String code = serial.getCode();
			
			if(code.equals("")) {
				CustomAlert.showAlert("Cadastro de Cart�o", "Nenhum cart�o detectado.", AlertType.INFORMATION);
			} else {
				try {
					if(Cartao.create(func, code)) {
						CustomAlert.showAlert("Cadastro de Cart�o", "Cart�o cadastrado com sucesso.", AlertType.INFORMATION);
					} else {
						CustomAlert.showAlert("Cadastro de cart�o", "Algo deu errado.", AlertType.INFORMATION);
					}
				}catch(RuntimeException ex) {
					CustomAlert.showAlert("Cadastro de Cart�o", ex.getMessage(), AlertType.INFORMATION);
				}
				
			}			
		});
	}

	public void setFuncionario(Funcionario funcionario) {
		this.func = funcionario;
	}
}
