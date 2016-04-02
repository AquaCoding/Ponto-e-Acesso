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
	TableColumn<Horario, String> horarioNome, horarioTurno1Inicio, horarioTurno1Termino, horarioTurno2Inicio, horarioTurno2Termino, horarioAlmocoInicio, horarioAlmocoTermino;

	@FXML
	Button cancelar, alterar, remover;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Ação do botão de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Tenta realizar a remoção
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se algum horario foi selecionado e pergunta se ele realmente o que remover
				if(horarioTable.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert("Remover Horario", "Você tem certeza que deseja remover esse Horario?")) {
					// Obtem o horario selecionado
					Horario h = horarioTable.getSelectionModel().getSelectedItem();

					// Tenta remover o Horario no BD
					if (h.delete()) {
						// horario removido com sucesso
						CustomAlert.showAlert("Remover Horario", "Horario removido com sucesso", AlertType.WARNING);
						Main.loadListaHorarioView();
					} else {
						// Erro ao remover o horario
						CustomAlert.showAlert("Remover Horario", "Algo deu errado", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Remover Horario", ex.getMessage(), AlertType.WARNING);
			}
		});
		
		// Evento do botão alterar
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
		horarioTurno1Inicio.setCellValueFactory(new PropertyValueFactory<>("inicioTurno1"));
		horarioTurno1Termino.setCellValueFactory(new PropertyValueFactory<>("terminoTurno1"));
		horarioTurno2Inicio.setCellValueFactory(new PropertyValueFactory<>("inicioTurno2"));
		horarioTurno2Termino.setCellValueFactory(new PropertyValueFactory<>("terminoTurno2"));
		horarioAlmocoInicio.setCellValueFactory(new PropertyValueFactory<>("inicioAlmoco"));
		horarioAlmocoTermino.setCellValueFactory(new PropertyValueFactory<>("terminoAlmoco"));
	}

}
