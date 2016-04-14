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
import javafx.util.StringConverter;

public class FuncionarioAbonoController implements Initializable {

	@FXML
	Button cancelar, cadastrar;

	@FXML
	TextArea descricaoFalta;

	@FXML
	DatePicker dataFalta;

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
				
				System.out.println("Passou");
				
				System.out.println(java.sql.Date.valueOf(dataFalta.getValue()));
				
				java.sql.Date falta = java.sql.Date.valueOf(dataFalta.getValue());

				System.out.println("Passou 2");
				
				System.out.println(dataFalta);
				System.out.println(descricaoFalta.getText());
				System.out.println(funcionarioSelect.getSelectionModel().getSelectedItem().getId());

				// Cria um novo objeto do abono
				Abono a = new Abono(falta, descricaoFalta.getText(),
						funcionarioSelect.getSelectionModel().getSelectedItem().getId());

				// Tenta registar o abono no BD
				if (a.create()) {
					// Abono criado com sucesso
					CustomAlert.showAlert("Novo Abono", "Novo abono cadastrado com sucesso.", AlertType.WARNING);
					Main.loadListaFuncionarioView();
				} else {
					// Erro ao criar o abono
					CustomAlert.showAlert("Novo Abono", "Algo deu errado.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				System.out.println(ex);
				CustomAlert.showAlert("Novo Abono", ex.getMessage(), AlertType.WARNING);
			}
		});

		funcionarioSelect.setConverter(new StringConverter<Funcionario>() {
			@Override
			public String toString(Funcionario item) {
				if (item != null) {
					return item.getNome();
				} else {
					return null;
				}
			}

			@Override
			public Funcionario fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});

	}

}
