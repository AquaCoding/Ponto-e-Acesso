package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Imposto;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class ImpostoNovoController implements Initializable {

	@FXML
	TextField nome, valor;

	@FXML
	Button cancelar, cadastrar;

	@FXML
	Label titulo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Adiciona mascaras no campo
		MaskField.doubleMask(valor);

		// Evento do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadImpostoVerView();
		});

		// Evento do botão cadastrar
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				Imposto i = new Imposto(nome.getText(), Double.parseDouble(valor.getText()));
				if(i.create()) {
					CustomAlert.showAlert("Novo Imposto", "Imposto cadastrado com sucesso.", AlertType.WARNING);
					Main.loadImpostoVerView();
				} else {
					CustomAlert.showAlert("Novo Imposto", "Algo deu errado.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Novo Imposto", ex.getMessage(), AlertType.WARNING);
			}
		});
	}

	// Define o Imposto que esta sendo usada para a edição
		public void setImposto(Imposto imposto) {
			titulo.setText("Editar Imposto");
			nome.setText(imposto.getNome());
			valor.setText(String.valueOf(imposto.getValor()));

			cadastrar.setText("Editar");

			cadastrar.setOnMouseClicked((MouseEvent e) -> {
				try {
					// Atualiza a info do imposto
					imposto.setNome(nome.getText());
					imposto.setValor(Double.parseDouble(valor.getText()));

					// Tenta alterar o imposto no BD
					if (imposto.update()) {
						// imposto alterado com sucesso
						CustomAlert.showAlert("Editar Imposto", "Imposto alterado com sucesso.", AlertType.WARNING);
						Main.loadListaSetorView();
					} else {
						// Erro ao editar o imposto
						CustomAlert.showAlert("Editar Imposto", "Algo deu errado.", AlertType.WARNING);
					}
				} catch (RuntimeException ex) {
					// Erro de validação
					CustomAlert.showAlert("Editar Imposto", ex.getMessage(), AlertType.WARNING);
				}
				Main.loadImpostoVerView();
			});
		}

}
