package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import aquacoding.model.Bonificacao;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;

public class BonificacaoCadastroController implements Initializable {

	@FXML
	TextField nome, valor;

	@FXML
	Label titulo;

	@FXML
	Button cancelar, cadastrar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Define mascaras aos campos
		MaskField.doubleMask(valor);

		// Cancela a exibição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			Bonificacao b = new Bonificacao(nome.getText(), Float.parseFloat(valor.getText()));
			if(b.create()) {
				CustomAlert.showAlert("Cadastro de bonificação", "Bonificação cadastrada com sucesso.", AlertType.WARNING);
				Main.loadBonificacaoListarView();
			} else {
				CustomAlert.showAlert("Cadastro de bonificação", "Algo deu errado ao cadastrar a bonificação.", AlertType.WARNING);
			}
		});
	}

	// Define a bonificação que esta sendo usada para a edição
	public void setBonificacao(Bonificacao bonificacao) {
		titulo.setText("Editar Bonificação");
		nome.setText(bonificacao.getNome());
		valor.setText(String.valueOf(bonificacao.getValor()));

		cadastrar.setText("Editar");

		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			bonificacao.setNome(nome.getText());
			bonificacao.setValor(Float.parseFloat(valor.getText()));
			
			if(bonificacao.update()) {
				CustomAlert.showAlert("Editar Bonificação", "Bonificação editada com sucesso.", AlertType.WARNING);
				Main.loadBonificacaoListarView();
			} else {
				CustomAlert.showAlert("Editar Bonificação", "Algo deu errado ao editar a bonificação.", AlertType.WARNING);
			}
		});
	}
}
