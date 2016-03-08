package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Setor;
import aquacoding.pontoacesso.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class SetorListaController implements Initializable {
	
	// Carrega os elementos do layout
	@FXML
	ListView<Setor> setorListagem;
	
	@FXML
	Button cancelar;
	
	// Atributos
	ObservableList<Setor> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a exibição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
				
		// Define o nome método de criação de células
		setSetorListagemCellFactory();
		
		// Obtem todos os setores
		items = FXCollections.observableArrayList(Setor.getAll());
		
		// Adiciona a lista
		setorListagem.setItems(items);
	}
	
	private void setSetorListagemCellFactory() {
		// Define um novo cell factory
		setorListagem.setCellFactory(new Callback<ListView<Setor>, ListCell<Setor>>() {
			// Realiza o override do método padrão
			@Override
			public ListCell<Setor> call(ListView<Setor> param) {
				// Cria uma nova célula da lista
				ListCell<Setor> cell = new ListCell<Setor>() {
					// Realiza o overrida do método padrão para definição do nome de exibição na lista
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
