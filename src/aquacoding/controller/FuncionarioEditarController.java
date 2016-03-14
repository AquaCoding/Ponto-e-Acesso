package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Funcionario;
import aquacoding.model.Setor;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class FuncionarioEditarController implements Initializable {

	// Obtem os elementos FXML
	@FXML
	Button cancelar, alterar;

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
	
	private Funcionario funcionario;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a edição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Tenta realizar a edição
		alterar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Atualiza a info do funcionario
				funcionario.setNome(funcionarioNome.getText());

				// Tenta alterar o funcionario no BD
				if (funcionario.update()) {
					// funcionario alterado com sucesso
					CustomAlert.showAlert("Editar Funcionario", "Funcionario alterado com sucesso", AlertType.WARNING);
					Main.loadListaSetorView();
				} else {
					// Erro ao cria o funcionario
					CustomAlert.showAlert("Editar Funcionario", "Algo deu errado", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Editar Funcionario", ex.getMessage(), AlertType.WARNING);
			}
		});

		
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
		funcionarioNome.setText(funcionario.getNome());
	}

}
