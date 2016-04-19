package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Ferias;
import aquacoding.pontoacesso.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FeriasListaController implements Initializable {

	@FXML
	TableView<Ferias> feriasTable;

	@FXML
	TableColumn<Ferias, String> feriasNome, feriasInicio, feriasTermino;

	@FXML
	Button cancelar, alterar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Ação do botão de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Inicia a view de edição de uma função
		alterar.setOnMouseClicked((MouseEvent e) -> {
			if (feriasTable.getSelectionModel().getSelectedItem() != null)
				Main.loadFeriasEditarView(feriasTable.getSelectionModel().getSelectedItem());
		});

		loadContent();
		setTable();
	}

		private void loadContent() {
			feriasTable.setItems(FXCollections.observableArrayList(Ferias.getAll()));
			feriasTable.refresh();
		}

		private void setTable() {
			feriasNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
			feriasInicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
			feriasTermino.setCellValueFactory(new PropertyValueFactory<>("termino"));
		}


}
