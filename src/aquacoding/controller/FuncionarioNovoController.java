package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Funcionario;
import aquacoding.model.Funcionario.Builder;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class FuncionarioNovoController implements Initializable {
	
	// Obtem os elementos FXML
	@FXML
	Button cancelar, cadastrar;
	
	@FXML
	TextField funcionarioNome;
	TextField funcionarioSobreNome;
	TextField funcionarioRG;
	TextField funcionarioCPF;
	TextField funcionarioCTPS;
	TextField funcionarioTelefone;
	TextField funcionarioRua;
	TextField funcionarioNumero;
	TextField funcionarioBairro;
	TextField funcionarioCidade;
	TextField funcionarioSalarioTotal;
	TextField funcionarioSalarioHoras;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela o cadastro e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Tenta realizar o cadastro
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Cria um novo objeto do Funcionario
				Funcionario f = new Funcionario.Builder()
						.setNome(funcionarioNome.getText())
						.setSobrenome(funcionarioSobreNome.getText())
						.setRg(funcionarioRG.getText())
						.setCpf(funcionarioCPF.getText())
						.setCtps(funcionarioCTPS.getText())
						.setTelefone(Integer.parseInt(funcionarioTelefone.getText()))
						.setRua(funcionarioRua.getText())
						.setNumero(Integer.parseInt(funcionarioNumero.getText()))
						.setBairro(funcionarioBairro.getText())
						.setCidade(funcionarioCidade.getText())
						.setSalarioTotal(Double.parseDouble(funcionarioSalarioTotal.getText()))
						.setSalarioHoras(Double.parseDouble(funcionarioSalarioHoras.getText()))
						.build();
				
				// Tenta registar o Funcionario no BD
				if(f.create()) {
					// Funcionario criado com sucesso
					CustomAlert.showAlert("Novo Funcionario", "Novo Funcionario cadastrado com sucesso", AlertType.WARNING);
					Main.loadListaFuncionarioView();
				} else {
					// Erro ao criar o Funcionario
					CustomAlert.showAlert("Novo Funcionario", "Algo deu errado", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Novo Funcionario", ex.getMessage(), AlertType.WARNING);
			}
		});
	}

}
