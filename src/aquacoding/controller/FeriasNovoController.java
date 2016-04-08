package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Ferias;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class FeriasNovoController implements Initializable {

	@FXML
	TextField feriasNome;

	@FXML
	Button cancelar, cadastrar;

	@FXML
	DatePicker feriasInicio, feriasTermino;

	@FXML
	ListView<Funcionario> listFuncionaios;

	// Atributos
	ObservableList<Funcionario> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Define o nome m�todo de cria��o de c�lulas
		setFuncionarioListagemCellFactory();

		// Obtem todos os funcionario
		items = FXCollections.observableArrayList(Funcionario.getAll());
		listFuncionaios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Adiciona a lista
		listFuncionaios.setItems(items);

		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				//inicio
				java.sql.Date inicio = java.sql.Date.valueOf(feriasInicio.getValue());
				//termino
				java.sql.Date termino = java.sql.Date.valueOf(feriasTermino.getValue());

				// Cria novo horario
				Ferias f = new Ferias(feriasNome.getText(), inicio, termino);

				ObservableList<Funcionario> fun;
				fun = FXCollections.observableArrayList(listFuncionaios.getSelectionModel().getSelectedItems());

				for(int i = 0; i < fun.size(); i++){
					f.setFuncionario(fun.get(i));
					}

				// Salva novo horario no BD
				if(f.create()) {
					CustomAlert.showAlert("Nova F�rias", "Nova f�rias cadastrado com sucesso.", AlertType.WARNING);
					Main.loadListaFeriasView();
				} else {
					CustomAlert.showAlert("Nova F�rias", "Algo de errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Nova F�rias", ex.getMessage(), AlertType.WARNING);
			}
		});


	}

	private void setFuncionarioListagemCellFactory() {
		// Define um novo cell factory
		listFuncionaios.setCellFactory(new Callback<ListView<Funcionario>, ListCell<Funcionario>>() {
			// Realiza o override do m�todo padr�o
			@Override
			public ListCell<Funcionario> call(ListView<Funcionario> param) {
				// Cria uma nova c�lula da lista
				ListCell<Funcionario> cell = new ListCell<Funcionario>() {
					// Realiza o overrida do m�todo padr�o para defini��o do
					// nome de exibi��o na lista
					@Override
					protected void updateItem(Funcionario item, boolean empty) {
						// Chama o construtor padr�o
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome() + " " + item.getSobrenome());
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



