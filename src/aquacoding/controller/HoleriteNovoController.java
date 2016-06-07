package aquacoding.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import aquacoding.model.Funcionario;
import aquacoding.model.Holerite;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
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
import javafx.scene.control.Alert.AlertType;
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

	//Funcionario funcionario;

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

		gerar.setOnMouseClicked((MouseEvent e) -> {
			ObservableList<Funcionario> fun = FXCollections.observableArrayList(listFuncionaios.getSelectionModel().getSelectedItems());
			Holerite h;
			
			// Verifica se uma data foi fornecida
			if(Inicio.getValue() != null && Termino.getValue() != null) {
				
				// Verifica se deve gerar holerite para todos os funcionarios
				if(checkTodos.isSelected()) {
					fun = FXCollections.observableArrayList(Funcionario.getAll());
				}
				
				// Verifica se ao menos um funcionario foi selecionado
				if(fun.size() > 0) {
					
					// Percorre por cada um dos funcionarios
					for(int k = 0; k < fun.size(); k ++){
						// Obtem o funcionarios
						Funcionario funcionario = fun.get(k);
						
						// Obtem a data de inicio e fim
						LocalDate inicio = Inicio.getValue();
						LocalDate termino = Termino.getValue();
						
						// Obtem o numero de dias entre as duas datas
						long dias = ChronoUnit.DAYS.between(inicio, termino);
						
						// Tranforma o numero de dias pra int
						int i = Integer.valueOf(""+dias);
						
						// Calcula o salario
						double salario = Holerite.getSalario(i, inicio, funcionario);
						
						// Gera um holerite
						h = new Holerite.Builder().setSalario(salario)
								.setFuncionario(funcionario)
								.setMes(termino.getMonthValue() + "/" + termino.getYear())
								.build();
						h.gerarHolerite();
					}
				} else {
					CustomAlert.showAlert("Gerar holerite", "Ao menos um funcionário deve ser selecionado.", AlertType.WARNING);
				}
			} else {
				CustomAlert.showAlert("Gerar holerite", "Uma data de início e termino deve ser dada.", AlertType.WARNING);
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
							setText(item.getNome() + " " + item.getSobrenome()  + " - " + item.getFuncao().getNome());
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
