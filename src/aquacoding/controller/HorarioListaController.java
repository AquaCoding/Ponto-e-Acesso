package aquacoding.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import aquacoding.model.Horario;
import aquacoding.pontoacesso.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class HorarioListaController implements Initializable {
	
	@FXML
	TableView<Horario> horarioTable;
	
	@FXML
	TableColumn<Horario, String> horarioNome, horarioTurnoInicio, horarioTurnoTermino, horarioAlmocoInicio, horarioAlmocoTermino;
	
	@FXML
	Button cancelar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// A��o do bot�o de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		loadContent();
		setTable();
		System.out.println("TESTE");
	}
	
	private void loadContent() {
		ArrayList<Horario> horarios = Horario.getAll();
		System.out.println(horarios.size());
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
