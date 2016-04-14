package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.model.Bonificacao;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class BonificacaoCadastroController implements Initializable {
	
	@FXML
	TextField nome, valor;
	
	@FXML
	ListView<Funcionario> funcionarioListagem;
	
	@FXML
	Button cancelar, cadastrar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Define mascaras aos campos
		MaskField.doubleMask(valor);
		funcionarioListagem.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// Define o nome m�todo de cria��o de c�lulas
		setFuncionarioListagemCellFactory();

		// Adiciona a lista
		funcionarioListagem.setItems(FXCollections.observableArrayList(Funcionario.getAll()));
		
		// Cancela a exibi��o e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			// Verifica��es manuais para evitar entrar no for
			if(nome.getText().equals("") || valor.getText().equals("")) {
				CustomAlert.showAlert("Cadastro de bonifica��o", "Nome e valor devem ser preenchidos.", AlertType.WARNING);
			} else {
				if(funcionarioListagem.getSelectionModel().getSelectedItem() != null) {
					// Obtem uma lista de todos os funcion�rios selecionados
					ObservableList<Funcionario> selectedFuncionarios = funcionarioListagem.getSelectionModel().getSelectedItems();
					
					// Percorre por todos os funcion�rios cadastrando a bonifica��o
					int cadastros = 0;
					for(Funcionario f: selectedFuncionarios) {
						Bonificacao b = new Bonificacao(nome.getText(), Float.parseFloat(valor.getText()), f);
						if(b.create()) {
							cadastros++;
						} else {
							break;
						}
					}
					
					// Verifica se todas as bonifica��es foram cadastradas
					if(cadastros == selectedFuncionarios.size()) {
						CustomAlert.showAlert("Cadastro de bonifica��o", "Bonifica��es cadastradas com sucesso.", AlertType.WARNING);
					} else {
						CustomAlert.showAlert("Cadastro de bonifica��o", "Algo deu errado ao cadastrar uma ou mais bonifica��es.", AlertType.WARNING);
					}
				} else {
					CustomAlert.showAlert("Cadastro de bonifica��o", "Ao menos 1 funcion�rio deve ser selecionado.", AlertType.WARNING);
				}
			}
		});
	}
	
	private void setFuncionarioListagemCellFactory() {
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
							setText(item.getNome() + " " + item.getSobrenome() + " ("+ item.getCpf() +")");
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
