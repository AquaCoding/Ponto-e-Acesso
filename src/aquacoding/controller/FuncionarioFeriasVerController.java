package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Ferias;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FuncionarioFeriasVerController implements Initializable {

	@FXML
	TableView<Ferias> feriasTable;

	@FXML
	TableColumn<Ferias, String> feriasNome, feriasInicio, feriasTermino;

	@FXML
	Button cancelar;

	@FXML
	Label lbTitulo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Ação do botão de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			//Main.loadFuncionarioVerView();
		});


	}

		private void setTable() {
			feriasNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
			feriasInicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
			feriasTermino.setCellValueFactory(new PropertyValueFactory<>("termino"));
		}

		public void setFuncionario(Funcionario funcionario) {
			lbTitulo.setText("Férias do " + funcionario.getNome());
			feriasTable.setItems(FXCollections.observableArrayList(funcionario.getFerias()));
			feriasTable.refresh();

			setTable();

			// Ação do botão de cancelar
			cancelar.setOnMouseClicked((MouseEvent e) -> {
				Main.loadFuncionarioVerView(funcionario);
			});
		}
}
