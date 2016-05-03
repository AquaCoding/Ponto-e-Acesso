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

		// Cancela a exibi��o e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			Bonificacao b = new Bonificacao(nome.getText(), Float.parseFloat(valor.getText()));
			if(b.create()) {
				CustomAlert.showAlert("Cadastro de bonifica��o", "Bonifica��o cadastrada com sucesso.", AlertType.WARNING);
				Main.loadBonificacaoListarView();
			} else {
				CustomAlert.showAlert("Cadastro de bonifica��o", "Algo deu errado ao cadastrar a bonifica��o.", AlertType.WARNING);
			}
		});
	}

	// Define a bonifica��o que esta sendo usada para a edi��o
	public void setBonificacao(Bonificacao bonificacao) {
		titulo.setText("Editar Bonifica��o");
		nome.setText(bonificacao.getNome());
		valor.setText(String.valueOf(bonificacao.getValor()));

		cadastrar.setText("Editar");

		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			bonificacao.setNome(nome.getText());
			bonificacao.setValor(Float.parseFloat(valor.getText()));
			
			if(bonificacao.update()) {
				CustomAlert.showAlert("Editar Bonifica��o", "Bonifica��o editada com sucesso.", AlertType.WARNING);
				Main.loadBonificacaoListarView();
			} else {
				CustomAlert.showAlert("Editar Bonifica��o", "Algo deu errado ao editar a bonifica��o.", AlertType.WARNING);
			}
		});
	}
}
