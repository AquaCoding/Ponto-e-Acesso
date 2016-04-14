package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javax.xml.bind.ParseConversionEvent;

import aquacoding.model.Bonificacao;
import aquacoding.model.Ferias;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	Label titulo;

	@FXML
	ListView<Funcionario> funcionarioListagem;

	@FXML
	Button cancelar, cadastrar;

	private Bonificacao bonificacao;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Define mascaras aos campos
		MaskField.doubleMask(valor);
		funcionarioListagem.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Define o nome método de criação de células
		setFuncionarioListagemCellFactory();

		// Adiciona a lista
		funcionarioListagem.setItems(FXCollections.observableArrayList(Funcionario.getAll()));

		// Cancela a exibição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

			cadastrar.setOnMouseClicked((MouseEvent e) -> {
			// Verificações manuais para evitar entrar no for
			if(nome.getText().equals("") || valor.getText().equals("")) {
				CustomAlert.showAlert("Cadastro de bonificação", "Nome e valor devem ser preenchidos.", AlertType.WARNING);
			} else {
				if(funcionarioListagem.getSelectionModel().getSelectedItem() != null) {
					// Obtem uma lista de todos os funcionários selecionados
					ObservableList<Funcionario> selectedFuncionarios = funcionarioListagem.getSelectionModel().getSelectedItems();

					// Percorre por todos os funcionários cadastrando a bonificação
					int cadastros = 0;
					for(Funcionario f: selectedFuncionarios) {
						Bonificacao b = new Bonificacao(nome.getText(), Float.parseFloat(valor.getText()), f);
						if(b.create()) {
							cadastros++;
						} else {
							break;
						}
					}

					// Verifica se todas as bonificações foram cadastradas
					if(cadastros == selectedFuncionarios.size()) {
						CustomAlert.showAlert("Cadastro de bonificação", "Bonificações cadastradas com sucesso.", AlertType.WARNING);
					} else {
						CustomAlert.showAlert("Cadastro de bonificação", "Algo deu errado ao cadastrar uma ou mais bonificações.", AlertType.WARNING);
					}
				} else {
					CustomAlert.showAlert("Cadastro de bonificação", "Ao menos 1 funcionário deve ser selecionado.", AlertType.WARNING);
				}
			}
				Main.loadMainView();;
		});
	}

	// Define a bonificação que esta sendo usada para a edição
	public void setBonificacao(Bonificacao bonificacao) {
		titulo.setText("Editar Bonificação");
		System.out.println(bonificacao.getId());
		this.bonificacao = bonificacao;
		nome.setText(bonificacao.getNome());
		valor.setText(String.valueOf(bonificacao.getValor()));

		cadastrar.setText("Editar");

		cadastrar.setOnMouseClicked((MouseEvent e) -> {
		// Verificações manuais para evitar entrar no for
		if(nome.getText().equals("") || valor.getText().equals("")) {
			CustomAlert.showAlert("Cadastro de bonificação", "Nome e valor devem ser preenchidos.", AlertType.WARNING);
		} else {
			if(funcionarioListagem.getSelectionModel().getSelectedItem() != null) {
				// Obtem uma lista de todos os funcionários selecionados
				ObservableList<Funcionario> selectedFuncionarios = funcionarioListagem.getSelectionModel().getSelectedItems();

				// Percorre por todos os funcionários cadastrando a bonificação
				int update = 0;
				for(Funcionario f: selectedFuncionarios) {
					bonificacao.setNome(nome.getText());
					bonificacao.setValor(Float.parseFloat(valor.getText()));
					bonificacao.setF(f);
					if(bonificacao.update()) {
						update++;
					} else {
						break;
					}
				}

				// Verifica se todas as bonificações foram cadastradas
				if(update == selectedFuncionarios.size()) {
					CustomAlert.showAlert("Cadastro de bonificação", "Bonificações cadastradas com sucesso.", AlertType.WARNING);
				} else {
					CustomAlert.showAlert("Cadastro de bonificação", "Algo deu errado ao cadastrar uma ou mais bonificações.", AlertType.WARNING);
				}
			} else {
				CustomAlert.showAlert("Cadastro de bonificação", "Ao menos 1 funcionário deve ser selecionado.", AlertType.WARNING);
			}
		}
			Main.loadMainView();;
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
