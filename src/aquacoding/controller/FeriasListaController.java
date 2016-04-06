package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Ferias;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FeriasListaController implements Initializable {

	@FXML
	TableView<Ferias> feriasTable;

	@FXML
	TableColumn<Ferias, String> feriasNome, feriasInicio, feriasTermino;

	@FXML
	Button cancelar, alterar, remover;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// A��o do bot�o de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			System.out.print("cancelar");
			Main.loadMainView();
		});

		// Tenta realizar a remo��o
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se alguma ferias foi selecionado e pergunta se ele realmente o que remover
				if(feriasTable.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert("Remover F�rias", "Voc� tem certeza que deseja remover essa f�rias?")) {
					// Obtem o horario selecionado
					Ferias f = feriasTable.getSelectionModel().getSelectedItem();

					// Tenta remover o Ferias no BD
					if (f.delete()) {
						// ferias removido com sucesso
						CustomAlert.showAlert("Remover F�rias", "F�rias removido com sucesso", AlertType.WARNING);
						Main.loadListaFeriasView();
					} else {
						// Erro ao remover o horario
						CustomAlert.showAlert("Remover F�rias", "Algo deu errado", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de valida��o
				CustomAlert.showAlert("Remover F�rias", ex.getMessage(), AlertType.WARNING);
			}
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
