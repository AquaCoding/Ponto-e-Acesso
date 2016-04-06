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

		// Define o nome método de criação de células
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


				// Salva novo horario no BD
				if(f.create()) {
					CustomAlert.showAlert("Nova Férias", "Nova férias cadastrado com sucesso.", AlertType.WARNING);
					Main.loadListaFeriasView();
				} else {
					CustomAlert.showAlert("Nova Férias", "Algo de errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Nova Férias", ex.getMessage(), AlertType.WARNING);
			}
		});


	}

	private void setFuncionarioListagemCellFactory() {
		// Define um novo cell factory
		listFuncionaios.setCellFactory(new Callback<ListView<Funcionario>, ListCell<Funcionario>>() {
			// Realiza o override do método padrão
			@Override
			public ListCell<Funcionario> call(ListView<Funcionario> param) {
				// Cria uma nova célula da lista
				ListCell<Funcionario> cell = new ListCell<Funcionario>() {
					// Realiza o overrida do método padrão para definição do
					// nome de exibição na lista
					@Override
					protected void updateItem(Funcionario item, boolean empty) {
						// Chama o construtor padrão
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome() + " " + item.getSobrenome());
						} else {
							setText("");
						}
					}
				};

				// Retorna a célula
				return cell;
			}
		});
	}


	}



