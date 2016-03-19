package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Funcao;
import aquacoding.model.Setor;
import aquacoding.model.Usuario;
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

public class UsuarioController implements Initializable {

	@FXML
	ListView<Usuario> usuariosLista;

	@FXML
	Button cancelar, alterar, remover;

	// Atributos
	ObservableList<Usuario> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Define o nome m�todo de cria��o de c�lulas
		setUsuarioListagemCellFactory();

		// Obtem todos os setores
		items = FXCollections.observableArrayList(Usuario.getAll());

		// Adiciona a lista
		usuariosLista.setItems(items);


		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		alterar.setOnMouseClicked((MouseEvent e) -> {
		// Verifica se um usu�rio foi selecionado
					if(usuariosLista.getSelectionModel().getSelectedItem() != null)
							Main.loadListaUsuarioView();
				});

				// Tenta realizar a remo��o
				remover.setOnMouseClicked((MouseEvent e) -> {
					try {
						// Verifica se algum setor foi selecionado e pergunta se ele realmente o que remover
						if(usuariosLista.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert("Remover Usu�rio", "Voc� tem certeza que deseja remover esse usu�rio?")) {
							// Obtem o setor selecionado
							Usuario u = usuariosLista.getSelectionModel().getSelectedItem();

							// Tenta remover o setor no BD
							if (u.delete()) {
								// Setor removido com sucesso
								CustomAlert.showAlert("Remover Usu�rio", "Usu�rio removido com sucesso", AlertType.WARNING);
								Main.loadListaUsuarioView();
							} else {
								// Erro ao remover o usu�rio
								CustomAlert.showAlert("Remover Usu�rio", "Algo deu errado", AlertType.WARNING);
							}
						}
					} catch (RuntimeException ex) {
						// Erro de valida��o
						CustomAlert.showAlert("Remover Usu�rio", ex.getMessage(), AlertType.WARNING);
					}
				});

}

	private void setUsuarioListagemCellFactory() {
		// Define um novo cell factory
		usuariosLista.setCellFactory(new Callback<ListView<Usuario>, ListCell<Usuario>>() {
			// Realiza o override do m�todo padr�o
			public ListCell<Usuario> call(ListView<Usuario> param) {
				// Cria uma nova c�lula da lista
				ListCell<Usuario> cell = new ListCell<Usuario>() {
					// Realiza o overrida do m�todo padr�o para defini��o do
					// nome de exibi��o na lista
					@Override
					protected void updateItem(Usuario item, boolean empty) {
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
