package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.Relatorio;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class RelatorioTrabalhoController implements Initializable {
	
	@FXML
	DatePicker dataInicio, dataFim;
	
	@FXML
	ListView<Funcionario> funcionarioListagem;
	
	@FXML
	Button cancelar, gerar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Define o nome método de criação de células
		setFuncionarioListagemCellFactory();
		
		// Adiciona a lista
		funcionarioListagem.setItems(FXCollections.observableArrayList(Funcionario.getAll()));
				
		// Cancela a exibição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		gerar.setOnMouseClicked((MouseEvent e) -> {
			try {
				Relatorio.gerarRelatorioTrabalho(dataInicio, dataFim, funcionarioListagem.getSelectionModel().getSelectedItem());
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Relatório Trabalho", ex.getMessage(), AlertType.WARNING);
			}
		});
	}
	
	private void setFuncionarioListagemCellFactory() {
		// Define um novo cell factory
		funcionarioListagem.setCellFactory(new Callback<ListView<Funcionario>, ListCell<Funcionario>>() {
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
							setText(item.getNome() + " " + item.getSobrenome() + " ("+ item.getCpf() +")");
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
