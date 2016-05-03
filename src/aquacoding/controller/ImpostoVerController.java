package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Imposto;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ImpostoVerController implements Initializable {

	@FXML
	ListView<Imposto> impostoListagem;

	@FXML
	Button cancelar, remover, alterar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do bot�o cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Obtem os setores
		impostoListagem.setItems(FXCollections.observableArrayList(Imposto.getAll()));

		// Define o nome m�todo de cria��o de c�lulas
		setImpostoListagemCellFactory();

		// Tenta realizar a remo��o
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se algum imposto foi selecionado e pergunta se
				// ele realmente o que remover
				if (impostoListagem.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert(
						"Remover Imposto", "Voc� tem certeza que deseja remover esse imposto?")) {
					// Obtem o imposto selecionado
					Imposto i = impostoListagem.getSelectionModel().getSelectedItem();

					// Tenta remover o imposto no BD
					if (i.delete()) {
						// imposto removido com sucesso
						CustomAlert.showAlert("Remover Imposto", "Imposto removido com sucesso.", AlertType.WARNING);
						Main.loadImpostoVerView();
					} else {
						// Erro ao remover o imposto
						CustomAlert.showAlert("Remover Imposto", "Algo deu errado.", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de valida��o
				CustomAlert.showAlert("Remover Funcionario", ex.getMessage(), AlertType.WARNING);
			}
		});

		alterar.setOnMouseClicked((MouseEvent e) -> {
			// Verifica se um imposto foi selecionado
			if (impostoListagem.getSelectionModel().getSelectedItem() != null)
				Main.loadImpostoEditarView(impostoListagem.getSelectionModel().getSelectedItem());
		});

	}

	private void setImpostoListagemCellFactory() {
		// Define um novo cell factory
		impostoListagem.setCellFactory(new Callback<ListView<Imposto>, ListCell<Imposto>>() {
			// Realiza o override do m�todo padr�o
			@Override
			public ListCell<Imposto> call(ListView<Imposto> param) {
				// Cria uma nova c�lula da lista
				ListCell<Imposto> cell = new ListCell<Imposto>() {
					// Realiza o overrida do m�todo padr�o para defini��o do
					// nome de exibi��o na lista
					@Override
					protected void updateItem(Imposto item, boolean empty) {
						// Chama o construtor padr�o
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome() + " (" + item.getValor() + "%)");
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
