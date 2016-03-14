package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Usuario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class UsuarioListaController implements Initializable{

	// Carrega os elementos do layout
		@FXML
		ListView<Usuario> usuariosLista;

		@FXML
		Button cancelar, alterar, remover;

		// Atributos
		ObservableList<Usuario> items;

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// Cancela a exibi��o e retorna pra tela inicial
			cancelar.setOnMouseClicked((MouseEvent e) -> {
				Main.loadMainView();
			});

			// Obtem todos os setores
			items = FXCollections.observableArrayList(Usuario.getAll());

			// Adiciona a lista
			usuariosLista.setItems(items);;

			alterar.setOnMouseClicked((MouseEvent e) -> {
				// Verifica se um setor foi selecionado
				if(usuariosLista.getSelectionModel().getSelectedItem() != null)
						Main.loadUsuarioEditarView(usuariosLista.getSelectionModel().getSelectedItem());
			});

			// Tenta realizar a remo��o
			remover.setOnMouseClicked((MouseEvent e) -> {
				try {
					// Verifica se algum usu�rio foi selecionado e pergunta se ele realmente o que remover
					if(usuariosLista.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert("Remover Usu�rio", "Voc� tem certeza que deseja remover esse usu�rio?")) {
						// Obtem o usu�rio selecionado
						Usuario u = usuariosLista.getSelectionModel().getSelectedItem();

						// Tenta remover o usu�rio no BD
						if (u.delete()) {
							// usu�rio removido com sucesso
							CustomAlert.showAlert("Remover Usu�rio", "Usu�rio removido com sucesso", AlertType.WARNING);
							Main.loadListaSetorView();
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



		}


