package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class HoleriteNovoController implements Initializable {

	@FXML
	Button cancelar, gerar;

	@FXML
	DatePicker Inicio, Termino;

	@FXML
	ListView<Funcionario> listFuncionaios;

	@FXML
	CheckBox checkTodos;

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
							setText(item.getNome() + " " + item.getSobrenome()  + " - " + item.getFuncao().getNome());
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
