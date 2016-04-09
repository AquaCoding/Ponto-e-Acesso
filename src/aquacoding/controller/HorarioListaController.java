package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Horario;
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

public class HorarioListaController implements Initializable {

	@FXML
	TableView<Horario> horarioTable;

	@FXML
	TableColumn<Horario, String> horarioNome, horarioTurnoInicio, horarioTurnoTermino, horarioAlmocoInicio, horarioAlmocoTermino;

	@FXML
	Button cancelar, alterar, remover;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// A��o do bot�o de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Tenta realizar a remo��o
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se algum horario foi selecionado e pergunta se ele realmente o que remover
				if(horarioTable.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert("Remover Turno", "Voc� tem certeza que deseja remover esse turno?")) {
					// Obtem o horario selecionado
					Horario h = horarioTable.getSelectionModel().getSelectedItem();

					// Tenta remover o Horario no BD
					if (h.delete()) {
						// horario removido com sucesso
						CustomAlert.showAlert("Remover Turno", "Turno removido com sucesso", AlertType.WARNING);
						Main.loadListaHorarioView();
					} else {
						// Erro ao remover o horario
						CustomAlert.showAlert("Remover Turno", "Algo deu errado", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de valida��o
				CustomAlert.showAlert("Remover Turno", ex.getMessage(), AlertType.WARNING);
			}
		});
		
		// Evento do bot�o alterar
		alterar.setOnMouseClicked((MouseEvent e) -> {
			if(horarioTable.getSelectionModel().getSelectedItem() != null)
				Main.loadHorarioEditarView(horarioTable.getSelectionModel().getSelectedItem());
		});

		loadContent();
		setTable();
	}

	private void loadContent() {
		horarioTable.setItems(FXCollections.observableArrayList(Horario.getAll()));
		horarioTable.refresh();
	}

	private void setTable() {
		horarioNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		horarioTurnoInicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
		horarioTurnoTermino.setCellValueFactory(new PropertyValueFactory<>("termino"));
		horarioAlmocoInicio.setCellValueFactory(new PropertyValueFactory<>("inicioAlmoco"));
		horarioAlmocoTermino.setCellValueFactory(new PropertyValueFactory<>("terminoAlmoco"));
	}

}
