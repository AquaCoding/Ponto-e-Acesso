package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.model.Abono;
import aquacoding.model.Funcionario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class FuncionarioAbonoController implements Initializable {

	@FXML
	Button cancelar, cadastrar;

	@FXML
	TextArea descricaoFalta;

	@FXML
	DatePicker faltaData;

	@FXML
	ComboBox<Funcionario> funcionarioSelect;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		// Cancela o cadastro e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Preenche o campo de seleção do funcionario
		funcionarioSelect.setItems(FXCollections.observableArrayList(Funcionario.getAll()));

		// Tenta realizar o cadastro
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				java.sql.Date dataFalta = java.sql.Date.valueOf(faltaData.getValue());
				
				// Cria um novo objeto do abono
				Abono a = new Abono(dataFalta, descricaoFalta.getText(), funcionarioSelect.getSelectionModel().getSelectedItem());

				// Tenta registar o abono no BD
				if (a.create()) {
					// Abono criado com sucesso
					CustomAlert.showAlert("Novo Abono", "Novo abono cadastrado com sucesso",
							AlertType.WARNING);
					Main.loadListaFuncionarioView();
				} else {
					// Erro ao criar o abono
					CustomAlert.showAlert("Novo Abono", "Algo deu errado", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Novo Abono", ex.getMessage(), AlertType.WARNING);
			} catch (Exception ex) {

			}
		});

	}

}
