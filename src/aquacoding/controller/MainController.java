package aquacoding.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.Relatorio;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class MainController implements Initializable {

	@FXML
	Label lblBemVindo, lblFaltantes;
	
	@FXML
	ListView<Funcionario> faltantesListagem;
	
	private static int MAXIMO_FALTAS_MES = 5;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Oculta todos os itens desncessarios
		lblFaltantes.setVisible(false);
		faltantesListagem.setVisible(false);
		
		// Verifica por usuarios faltantes
		faltantes();
	}
	
	private void faltantes() {
		// Armazena todos os funcionários
		ArrayList<Funcionario> func = Funcionario.getAll();
		
		// Armaze os funcionarios com excesso de faltas
		ArrayList<Funcionario> excessoFalta = new ArrayList<Funcionario>();
		for(Funcionario f: func) {
			ArrayList<LocalDate> faltas = Relatorio.contaFaltas(LocalDate.of(2016, 4, 1), LocalDate.now(), f);
			if(faltas.size() >= MAXIMO_FALTAS_MES) {
				excessoFalta.add(f);
			}
		}
		
		if(excessoFalta.size() > 0)
			showFaltantes(excessoFalta);
	}
	
	private void showFaltantes(ArrayList<Funcionario> excessoFalta) {
		// Preenche a lista de faltantes e define a criação de celulas
		faltantesListagem.setItems(FXCollections.observableArrayList(excessoFalta));
		setFaltantesListagemCellFactory();
		
		// Abre a exibição de funcionario ao dar dois click no funcionario
		faltantesListagem.setOnMouseClicked((MouseEvent e) -> {
			if(e.getClickCount() >= 2) {
				Main.loadFuncionarioVerView(faltantesListagem.getSelectionModel().getSelectedItem());
			}
		});
		
		// Oculta Bem Vindo
		lblBemVindo.setVisible(false);
		
		// Exibe faltantes
		lblFaltantes.setVisible(true);
		faltantesListagem.setVisible(true);
	}
	
	private void setFaltantesListagemCellFactory() {
		// Define um novo cell factory
		faltantesListagem.setCellFactory(new Callback<ListView<Funcionario>, ListCell<Funcionario>>() {
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
