package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class FuncionarioListaController implements Initializable {

	// Carrega os elementos do layout
	@FXML
	ListView<Funcionario> funcionarioListagem;

	@FXML
	Button cancelar, alterar, remover;
	
	@FXML
	TextField buscar;

	// Atributos
	ObservableList<Funcionario> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a exibi��o e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Define o nome m�todo de cria��o de c�lulas
		setFuncionarioListagemCellFactory();

		// Obtem todos os funcionario
		items = FXCollections.observableArrayList(Funcionario.getAll());

		// Adiciona a lista
		funcionarioListagem.setItems(items);

		alterar.setOnMouseClicked((MouseEvent e) -> {
			// Verifica se um funcionario foi selecionado
			if (funcionarioListagem.getSelectionModel().getSelectedItem() != null)
				Main.loadFuncionarioEditarView(funcionarioListagem.getSelectionModel().getSelectedItem());
		});

		// Tenta realizar a remo��o
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se algum funcionario foi selecionado e pergunta se
				// ele realmente o que remover
				if (funcionarioListagem.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert(
						"Remover funcionario", "Voc� tem certeza que deseja remover esse funcionario?")) {
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
				// Erro de valida��o
				CustomAlert.showAlert("Remover Funcionario", ex.getMessage(), AlertType.WARNING);
			}
		});
		
		// Abre a exibi��o de funcionario ao dar dois click no funcionario
		funcionarioListagem.setOnMouseClicked((MouseEvent e) -> {
			if(e.getClickCount() >= 2) {
				Main.loadFuncionarioVerView(funcionarioListagem.getSelectionModel().getSelectedItem());
			}
		});
		
		buscar.setOnKeyReleased((KeyEvent e) -> {
			items = FXCollections.observableArrayList(Funcionario.getAll(buscar.getText()));
			funcionarioListagem.setItems(items);
		});
	}

	private void setFuncionarioListagemCellFactory() {
		// Define um novo cell factory
		funcionarioListagem.setCellFactory(new Callback<ListView<Funcionario>, ListCell<Funcionario>>() {
			// Realiza o override do m�todo padr�o
			@Override
			public ListCell<Funcionario> call(ListView<Funcionario> param) {
				// Cria uma nova c�lula da lista
				ListCell<Funcionario> cell = new ListCell<Funcionario>() {
					// Realiza o overrida do m�todo padr�o para defini��o do
					// nome de exibi��o na lista
					@Override
					protected void updateItem(Funcionario item, boolean empty) {
						// Chama o construtor padr�o
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome() + " " + item.getSobrenome() + " ("+ item.getCpf() +")");
						} else {
							setText("");
						}
					}
				};

				// Retorna a c�lula
				return cell;
			}
		});
	}
}
