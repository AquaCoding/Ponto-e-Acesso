package aquacoding.controller;

import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import aquacoding.model.Log;
import aquacoding.model.Usuario;
import aquacoding.pontoacesso.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class AuditoriaVerController implements Initializable {

	@FXML
	TableView<Log> auditoriaTable;
	
	@FXML
	TableColumn<Log, String> idLogTC, idUsuarioTC, objetoTC, idObjetoTC, acaoTC, dataTC;
	
	@FXML
	Button cancelar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Obtem todos os logs e adiciona na tabela
		loadContent();
		setTable();
	}
	
	private void loadContent() {
		auditoriaTable.setItems(FXCollections.observableArrayList(Log.getAll()));
		auditoriaTable.refresh();
	}

	private void setTable() {
		idLogTC.setCellValueFactory(new PropertyValueFactory<>("id"));
		idObjetoTC.setCellValueFactory(new PropertyValueFactory<>("objectoId"));
		idUsuarioTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Log, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Log, String> item) {
				return new SimpleStringProperty(Usuario.getByID(item.getValue().getIdUsuario()).getNome());
			}
		});
		objetoTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Log, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Log, String> item) {
				return new SimpleStringProperty(item.getValue().getObjeto().getValor());
			}
		});
		acaoTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Log, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Log, String> item) {
				return new SimpleStringProperty(item.getValue().getAcao().getValor());
			}
		});
		dataTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Log, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Log, String> item) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());
				return new SimpleStringProperty(formatter.format(item.getValue().getData()));
			}
		});
	}
}
