package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import aquacoding.model.Cartao;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.Serial;

public class CadastroCartaoController implements Initializable {

	@FXML
	Button cancelar, cadastrar;

	@FXML
	TextArea textHelp;

	private Funcionario func;

	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaFuncionarioView();
		});

		textHelp.setText("Instruções: \n"
				+ "Aproxime o cartão ou tag do leitor \n"
				+ "Click em cadastrar\n"
				+ "Aguarde confirmação na tela \n"
				+ "Retire o cartão \n"
				+ "Caso o procedimento for bem sucedido, prossiga \n"
				+ "Caso contrário contate o suporte\n"
				+ "(O funcionamento de ponto fica desativado por 10 segundos durante esse procedimento)");
		
		CustomAlert.showAlert("Cadastro de Cartão", "Aproxime seu cartão do leitor", AlertType.INFORMATION);

		// Serial
		Serial serial = Serial.getInstance();
		serial.stopLogic(10000);
		
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			// Serial
			
			String code = serial.getCode();
			
			if(code.equals("")) {
				CustomAlert.showAlert("Cadastro de Cartão", "Nenhum cartão detectado.", AlertType.INFORMATION);
			} else {
				try {
					if(Cartao.create(func, code)) {
						CustomAlert.showAlert("Cadastro de Cartão", "Cartão cadastrado com sucesso.", AlertType.INFORMATION);
					} else {
						CustomAlert.showAlert("Cadastro de cartão", "Algo deu errado.", AlertType.INFORMATION);
					}
				}catch(RuntimeException ex) {
					CustomAlert.showAlert("Cadastro de Cartão", ex.getMessage(), AlertType.INFORMATION);
				}
				
			}			
		});
	}

	public void setFuncionario(Funcionario funcionario) {
		this.func = funcionario;
	}
}
