package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Cartao;
import aquacoding.pontoacesso.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class CartoesVerController implements Initializable{
	
	@FXML
	TableView<Cartao> cartoesTable;
	
	@FXML
	TableColumn<Cartao, String> funcionarioTC, cpfTC, codigoTC, ativoTC;
	
	@FXML
	Button cancelar, revogarPermitir;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		loadContent();
		setTable();
		
		revogarPermitir.setOnMouseClicked((MouseEvent e) -> {
			int id = cartoesTable.getSelectionModel().getSelectedItem().getId();
			
			Cartao.revogarpermitir(id);
			Main.loadCartoesVerView();
		});
	}
	
	private void loadContent() {
		cartoesTable.setItems(FXCollections.observableArrayList(Cartao.getAll()));
		cartoesTable.refresh();
	}

	private void setTable() {
		funcionarioTC.setCellValueFactory(new PropertyValueFactory<>("funcionarioNome"));
		cpfTC.setCellValueFactory(new PropertyValueFactory<>("funcionarioCPF"));
		codigoTC.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		ativoTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cartao, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Cartao, String> item) {
				if (item.getValue().isAtivo()) {
		            return new SimpleStringProperty("Sim");
		        } else {
		            return new SimpleStringProperty("Não");
		        }
			}
		});
	}

}
