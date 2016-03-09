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

public class SetorListaController implements Initializable {

	// Carrega os elementos do layout
	@FXML
	ListView<Setor> setorListagem;

	@FXML
	Button cancelar, alterar, remover;

	// Atributos
	ObservableList<Setor> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a exibi��o e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Define o nome m�todo de cria��o de c�lulas
		setSetorListagemCellFactory();

		// Obtem todos os setores
		items = FXCollections.observableArrayList(Setor.getAll());

		// Adiciona a lista
		setorListagem.setItems(items);

		alterar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadSetorEditarView();
		});
		
		// Tenta realizar a remo��o
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Cria um novo objeto do setor
				Setor s = new Setor(setorListagem.getId());

				// Tenta remover o setor no BD
				if (s.delete()) {
					// Setor removido com sucesso
					CustomAlert.showAlert("Remover Setor", "Setor removido com sucesso", AlertType.WARNING);
					Main.loadListaSetorView();
				} else {
					// Erro ao remover o setor
					CustomAlert.showAlert("Remover Setor", "Algo deu errado", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				// Erro de valida��o
				CustomAlert.showAlert("Remover Setor", ex.getMessage(), AlertType.WARNING);
			}
		});

	}

	private void setSetorListagemCellFactory() {
		// Define um novo cell factory
		setorListagem.setCellFactory(new Callback<ListView<Setor>, ListCell<Setor>>() {
			// Realiza o override do m�todo padr�o
			@Override
			public ListCell<Setor> call(ListView<Setor> param) {
				// Cria uma nova c�lula da lista
				ListCell<Setor> cell = new ListCell<Setor>() {
					// Realiza o overrida do m�todo padr�o para defini��o do
					// nome de exibi��o na lista
					@Override
					protected void updateItem(Setor item, boolean empty) {
						// Chama o construtor padr�o
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome());
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
