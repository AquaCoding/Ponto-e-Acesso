package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Funcao;
import aquacoding.model.Funcionario;
import aquacoding.model.Log;
import aquacoding.model.Usuario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class FuncionarioListaController implements Initializable {

	// Carrega os elementos do layout
	@FXML
	TableView<Funcionario> funcionarioListagem;

	@FXML
	Button cancelar, alterar, remover;

	@FXML
	TextField buscar;

	@FXML
	TableColumn<Funcionario, String> tcNome, tcSobrenome, tcCPF, tcFuncao,tcStatus;

	// Atributos
	ObservableList<Funcionario> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a exibição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		alterar.setOnMouseClicked((MouseEvent e) -> {
			// Verifica se um funcionario foi selecionado
			if (funcionarioListagem.getSelectionModel().getSelectedItem() != null)
				Main.loadFuncionarioEditarView(funcionarioListagem.getSelectionModel().getSelectedItem());
		});

		// Tenta realizar a remoção
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se algum funcionario foi selecionado e pergunta se
				// ele realmente o que remover
				if (funcionarioListagem.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert(
						"Remover funcionario", "Você tem certeza que deseja remover esse funcionario?")) {
					// Obtem o funcionario selecionado
					Funcionario f = funcionarioListagem.getSelectionModel().getSelectedItem();

					// Tenta remover o funcionario no BD
					if (f.delete()) {
						// funcionario removido com sucesso
						CustomAlert.showAlert("Remover Funcionario", "Funcionario removido com sucesso.", AlertType.WARNING);
						Main.loadListaFuncionarioView();
					} else {
						// Erro ao remover o funcionario
						CustomAlert.showAlert("Remover Funcionario", "Algo deu errado.", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Remover Funcionario", ex.getMessage(), AlertType.WARNING);
			}
		});

		// Abre a exibição de funcionario ao dar dois click no funcionario
		funcionarioListagem.setOnMouseClicked((MouseEvent e) -> {
			if(e.getClickCount() >= 2) {
				Main.loadFuncionarioVerView(funcionarioListagem.getSelectionModel().getSelectedItem());
			}
		});

		// Obtem todos os funcionario
		items = FXCollections.observableArrayList(Funcionario.getAll());

		buscar.setOnKeyReleased((KeyEvent e) -> {
			items = FXCollections.observableArrayList(Funcionario.getAll(buscar.getText()));
			funcionarioListagem.setItems(items);
		});

		// Obtem todos os funcionarios e adiciona na tabela
		loadContent();
		setTable();
	}

	private void loadContent() {
		funcionarioListagem.setItems(FXCollections.observableArrayList(Funcionario.getAll()));
		funcionarioListagem.refresh();
	}

	private void setTable() {
		tcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tcSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
		tcCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tcFuncao.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Funcionario, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Funcionario, String> item) {
				return new SimpleStringProperty(item.getValue().getFuncao().getNome());
			}
		});
		tcStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Funcionario, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Funcionario, String> item) {
				return new SimpleStringProperty(item.getValue().getStatus());
			}
		});
	}

}
