package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Setor;
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
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class FuncionarioListaController implements Initializable {

	// Carrega os elementos do layout
	@FXML
	ListView<Funcionario> funcionarioListagem;

	@FXML
	Button cancelar, alterar, remover;

	// Atributos
	ObservableList<Funcionario> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a exibição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Define o nome método de criação de células
		setFuncionarioListagemCellFactory();

		// Obtem todos os setores
		items = FXCollections.observableArrayList(Funcionario.getAll());

		// Adiciona a lista
		funcionarioListagem.setItems(items);

		alterar.setOnMouseClicked((MouseEvent e) -> {
			// Verifica se um setor foi selecionado
			if(funcionarioListagem.getSelectionModel().getSelectedItem() != null)
					Main.loadSetorEditarView(funcionarioListagem.getSelectionModel().getSelectedItem());
		});
		
		// Tenta realizar a remoção
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se algum setor foi selecionado e pergunta se ele realmente o que remover
				if(funcionarioListagem.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert("Remover Funcionario", "Você tem certeza que deseja remover esse Funcionário?")) {
					// Obtem o funcionario selecionado
					Setor s = funcionarioListagem.getSelectionModel().getSelectedItem();

					// Tenta remover o funcionario no BD
					if (s.delete()) {
						// Funcionario removido com sucesso
						CustomAlert.showAlert("Remover Funcionario", "Funcionario removido com sucesso", AlertType.WARNING);
						Main.loadListaSetorView();
					} else {
						// Erro ao remover o funcionario
						CustomAlert.showAlert("Remover Funcionario", "Algo deu errado", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Remover Funcionario", ex.getMessage(), AlertType.WARNING);
			}
		});

	}

	private void setSetorListagemCellFactory() {
		// Define um novo cell factory
		funcionarioListagem.setCellFactory(new Callback<ListView<Setor>, ListCell<Setor>>() {
			// Realiza o override do método padrão
			@Override
			public ListCell<Setor> call(ListView<Setor> param) {
				// Cria uma nova célula da lista
				ListCell<Setor> cell = new ListCell<Setor>() {
					// Realiza o overrida do método padrão para definição do
					// nome de exibição na lista
					@Override
					protected void updateItem(Setor item, boolean empty) {
						// Chama o construtor padrão
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome());
						} else {
							setText("");
						}
					}
				};

				// Retorna a célula
				return cell;
			}
		});
	}

}
