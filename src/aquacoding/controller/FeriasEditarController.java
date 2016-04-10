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

public class FeriasEditarController implements Initializable {

	@FXML
	TextField feriasNome;

	@FXML
	Button cancelar, alterar;

	@FXML
	DatePicker feriasInicio, feriasTermino;

	@FXML
	ListView<Funcionario> listFuncionaios;

	// Atributos
	ObservableList<Funcionario> items;

	private Ferias ferias;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Retorna para o menu de listagem ao clicar em cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaFeriasView();;
		});

		// Define o nome método de criação de células
		setFuncionarioListagemCellFactory();

		// Obtem todos os funcionario
		items = FXCollections.observableArrayList(Funcionario.getAll());
		listFuncionaios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Adiciona a lista
		listFuncionaios.setItems(items);

		// Realiza a edição da função
		alterar.setOnMouseClicked((MouseEvent e) -> {
			try {
				//inicio
				java.sql.Date inicio = java.sql.Date.valueOf(feriasInicio.getValue());
				//termino
				java.sql.Date termino = java.sql.Date.valueOf(feriasTermino.getValue());

				// Salva as alterações no objeto
				ferias.setNome(feriasNome.getText());
				ferias.setInicio(inicio);
				ferias.setTermino(termino);

				ObservableList<Funcionario> fun;
				fun = FXCollections.observableArrayList(listFuncionaios.getSelectionModel().getSelectedItems());

				for(int i = 0; i < fun.size(); i++){
					ferias.setFuncionario(fun.get(i));
					}

				// Tenta atualizar
				if(ferias.update()) {
					CustomAlert.showAlert("Editar Férias", "Férias editada com sucesso.", AlertType.WARNING);
					Main.loadListaFeriasView();;
				} else {
					CustomAlert.showAlert("Editar Férias", "Algo errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Editar Férias", ex.getMessage(), AlertType.WARNING);
			}
		});

	}

	// Define a férias que esta sendo usada para a edição
	public void setFerias(Ferias ferias) {
		this.ferias = ferias;
		feriasNome.setText(ferias.getNome());
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
