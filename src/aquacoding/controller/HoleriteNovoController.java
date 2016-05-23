package aquacoding.controller;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import aquacoding.model.Funcionario;
import aquacoding.model.Holerite;
import aquacoding.model.Horario;
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

	Funcionario funcionario;

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
			ObservableList<Funcionario> fun;
			fun = FXCollections.observableArrayList(listFuncionaios.getSelectionModel().getSelectedItems());
			Holerite h;

			if(checkTodos.isSelected() == false){
				if(fun.size() == 1){

					LocalDate inicio = Inicio.getValue();
					LocalDate termino = Termino.getValue();
					long dias = ChronoUnit.DAYS.between(inicio, termino);

					int i = Integer.valueOf(""+dias);
					double salario = 0;

					funcionario = fun.get(0);
					for(int j = 0; j <= i+1; j++ ){
						 LocalDate amanhas = inicio.plusDays(j);
						 int horas;

						 Duration aux = Horario.getHorasTrabalhadasByDateAndFuncionario(funcionario, String.valueOf(amanhas));
						 if(Horario.getHorasTrabalhadasByDateAndFuncionario(funcionario, String.valueOf(amanhas)) != null){
							  horas = Integer.valueOf(""+aux.toHours());
						 }else{
							  horas = 0;
						 }

						 double dinheiroHora = funcionario.getSalarioHoras();
						 salario = salario +  horas * dinheiroHora;

					}
					h = new Holerite.Builder().setSalario(salario)
							.setFuncionario(funcionario)
							.setMes(termino.getMonthValue() + "/" + termino.getYear())
							.build();
					h.gerarHolerite();
				}else{
					for(int k = 0; k < fun.size(); k ++){

						LocalDate inicio = Inicio.getValue();
						LocalDate termino = Termino.getValue();
						long dias = ChronoUnit.DAYS.between(inicio, termino);

						int i = Integer.valueOf(""+dias);
						double salario = 0;

						funcionario = fun.get(k);
						for(int j = 0; j <= i+1; j++ ){
							 LocalDate amanhas = inicio.plusDays(j);
							 int horas;

							 Duration aux = Horario.getHorasTrabalhadasByDateAndFuncionario(funcionario, String.valueOf(amanhas));
							 if(Horario.getHorasTrabalhadasByDateAndFuncionario(funcionario, String.valueOf(amanhas)) != null){
								  horas = Integer.valueOf(""+aux.toHours());
							 }else{
								  horas = 0;
							 }

							 double dinheiroHora = funcionario.getSalarioHoras();
							 salario = salario +  horas * dinheiroHora;

						}
						h = new Holerite.Builder().setSalario(salario)
								.setFuncionario(funcionario)
								.setMes(termino.getMonthValue() + "/" + termino.getYear())
								.build();
						h.gerarHolerite();
					}

				}
			}else{

				for(int k = 0; k < items.size(); k ++){

					LocalDate inicio = Inicio.getValue();
					LocalDate termino = Termino.getValue();
					long dias = ChronoUnit.DAYS.between(inicio, termino);

					int i = Integer.valueOf(""+dias);
					double salario = 0;

					funcionario = items.get(k);
					for(int j = 0; j <= i+1; j++ ){
						 LocalDate amanhas = inicio.plusDays(j);
						 int horas;

						 Duration aux = Horario.getHorasTrabalhadasByDateAndFuncionario(funcionario, String.valueOf(amanhas));
						 if(Horario.getHorasTrabalhadasByDateAndFuncionario(funcionario, String.valueOf(amanhas)) != null){
							  horas = Integer.valueOf(""+aux.toHours());
						 }else{
							  horas = 0;
						 }

						 double dinheiroHora = funcionario.getSalarioHoras();
						 salario = salario +  horas * dinheiroHora;

					}
					h = new Holerite.Builder().setSalario(salario)
							.setFuncionario(funcionario)
							.setMes(termino.getMonthValue() + "/" + termino.getYear())
							.build();
					h.gerarHolerite();
				}
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
