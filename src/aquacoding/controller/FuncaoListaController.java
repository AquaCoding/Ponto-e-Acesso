package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
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
import aquacoding.model.Funcao;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;

public class FuncaoListaController implements Initializable {

	// Carrega os elementos do layout
	@FXML
	ListView<Funcao> funcaoListagem;

	@FXML
	Button cancelar, alterar, remover;

	// Atributos
	ObservableList<Funcao> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a exibi��o e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Inicia a view de edi��o de uma fun��o
		alterar.setOnMouseClicked((MouseEvent e) -> {
			if(funcaoListagem.getSelectionModel().getSelectedItem() != null)
				Main.loadFuncaoEditarView(funcaoListagem.getSelectionModel().getSelectedItem());
		});

		// Define o nome m�todo de cria��o de c�lulas
		setListagemCellFactory();

		// Obtem todos os setores
		items = FXCollections.observableArrayList(Funcao.getAll());

		// Adiciona a lista
		funcaoListagem.setItems(items);

		// Tenta realizar a remo��o
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se algum Funcao foi selecionado e pergunta se ele
				// realmente o que remover
				if (funcaoListagem.getSelectionModel().getSelectedItem() != null && CustomAlert
						.showConfirmationAlert("Remover Cargo", "Voc� tem certeza que deseja remover esse cargo?")) {
					// Obtem o Funcao selecionado
					Funcao f = funcaoListagem.getSelectionModel().getSelectedItem();

					// Tenta remover o Funcao no BD
					if (f.delete()) {
						// Funcao removido com sucesso
						CustomAlert.showAlert("Remover Cargo", "Cargo removido com sucesso.", AlertType.WARNING);
						Main.loadListaFuncaoView();
					} else {
						// Erro ao remover o Funcao
						CustomAlert.showAlert("Remover Cargo", "Algo deu errado", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de valida��o
				CustomAlert.showAlert("Remover Cargo", ex.getMessage(), AlertType.WARNING);
			}
		});

	}

	private void setListagemCellFactory() {
		// Define um novo cell factory
		funcaoListagem.setCellFactory(new Callback<ListView<Funcao>, ListCell<Funcao>>() {
			// Realiza o override do m�todo padr�o
			@Override
			public ListCell<Funcao> call(ListView<Funcao> param) {
				// Cria uma nova c�lula da lista
				ListCell<Funcao> cell = new ListCell<Funcao>() {
					// Realiza o overrida do m�todo padr�o para defini��o do
					// nome de exibi��o na lista
					@Override
					protected void updateItem(Funcao item, boolean empty) {
						// Chama o construtor padr�o
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome() + " (" + item.getSetor().getNome() + ")");
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
