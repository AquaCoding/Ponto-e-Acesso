package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Imposto;
import aquacoding.pontoacesso.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ImpostoVerController implements Initializable {

	@FXML
	ListView<Imposto> impostoListagem;
	
	@FXML
	Button cancelar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Obtem os setores
		impostoListagem.setItems(FXCollections.observableArrayList(Imposto.getAll()));
		
		// Define o nome método de criação de células
		setImpostoListagemCellFactory();
	}
	
	private void setImpostoListagemCellFactory() {
		// Define um novo cell factory
		impostoListagem.setCellFactory(new Callback<ListView<Imposto>, ListCell<Imposto>>() {
			// Realiza o override do método padrão
			@Override
			public ListCell<Imposto> call(ListView<Imposto> param) {
				// Cria uma nova célula da lista
				ListCell<Imposto> cell = new ListCell<Imposto>() {
					// Realiza o overrida do método padrão para definição do
					// nome de exibição na lista
					@Override
					protected void updateItem(Imposto item, boolean empty) {
						// Chama o construtor padrão
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome() + " (" + item.getValor() + "%)");
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
