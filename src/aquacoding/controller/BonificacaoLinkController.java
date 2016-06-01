package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Bonificacao;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class BonificacaoLinkController implements Initializable {
	
	@FXML
	Button cancelar, linkar;
	
	@FXML
	Label bonInfo;
	
	@FXML
	ListView<Funcionario> funcionarioListagem;

	private Bonificacao b;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setFuncionarioCellFactory();
		funcionarioListagem.setItems(FXCollections.observableArrayList(Funcionario.getAll()));
		funcionarioListagem.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		linkar.setOnMouseClicked((MouseEvent e) -> {
			if(funcionarioListagem.getSelectionModel().getSelectedItems().size() == 0) {
				CustomAlert.showAlert("Linkar Bonificação", "Ao menos um funcionário precisa ser selecionado", AlertType.WARNING);
			} else {
				for(Funcionario f: funcionarioListagem.getSelectionModel().getSelectedItems()) {
					if(!Bonificacao.linkar(b, f)) {
						CustomAlert.showAlert("Linkar Bonificação", "Um erro ocorreu a linkar a bonificação de " + f.getNome() + " " + f.getSobrenome(), AlertType.WARNING);
					}
				}
				
				CustomAlert.showAlert("Linkar Bonificação", "Encerrado link das bonificações.", AlertType.WARNING);
				Main.loadBonificacaoListarView();
			}
		});
	}
	
	public void setBonificacao(Bonificacao b) {
		this.b = b;
		bonInfo.setText("Bonificação: " + b.getNome() + "(" + b.getValor() + "%) - Selecione mais de um funcionário se preciso");
	}

	private void setFuncionarioCellFactory() {
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
							setText(item.getNome() + " (" + item.getCpf() + ")");
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
