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
				CustomAlert.showAlert("Linkar Bonifica��o", "Ao menos um funcion�rio precisa ser selecionado", AlertType.WARNING);
			} else {
				for(Funcionario f: funcionarioListagem.getSelectionModel().getSelectedItems()) {
					if(!Bonificacao.linkar(b, f)) {
						CustomAlert.showAlert("Linkar Bonifica��o", "Um erro ocorreu a linkar a bonifica��o de " + f.getNome() + " " + f.getSobrenome(), AlertType.WARNING);
					}
				}
				
				CustomAlert.showAlert("Linkar Bonifica��o", "Encerrado link das bonifica��es.", AlertType.WARNING);
				Main.loadBonificacaoListarView();
			}
		});
	}
	
	public void setBonificacao(Bonificacao b) {
		this.b = b;
		bonInfo.setText("Bonifica��o: " + b.getNome() + "(" + b.getValor() + "%) - Selecione mais de um funcion�rio se preciso");
	}

	private void setFuncionarioCellFactory() {
		// Define um novo cell factory
		funcionarioListagem.setCellFactory(new Callback<ListView<Funcionario>, ListCell<Funcionario>>() {
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
							setText(item.getNome() + " (" + item.getCpf() + ")");
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
