package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;






import aquacoding.model.Cartao;
import aquacoding.model.Funcionario;
import aquacoding.model.Ponto;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class PontoNovoController implements Initializable{
	
	@FXML
	DatePicker dataSeletor;
	
	@FXML
	TextField horario;
	
	@FXML
	ListView<Funcionario> funcionarioListagem;
	
	@FXML
	ListView<Cartao> cartaoListagem;
	
	@FXML
	Button cancelar, cadastrar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MaskField.horarioMask(horario);
		
		// Evento do botão de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Evento do botão de cadastrar
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				if(cartaoListagem.getSelectionModel().getSelectedItem() != null && funcionarioListagem.getSelectionModel().getSelectedItem() != null) {
					if(Ponto.criarManual(dataSeletor.getValue(), horario.getText(), funcionarioListagem.getSelectionModel().getSelectedItem().getId(), cartaoListagem.getSelectionModel().getSelectedItem().getId())) {
						CustomAlert.showAlert("Cadastrar Ponto Manual", "Ponto adicionado com sucesso.", AlertType.WARNING);
					} else {
						CustomAlert.showAlert("Cadastrar Ponto Manual", "Um erro ocorreu.", AlertType.WARNING);
					}
				} else {
					CustomAlert.showAlert("Cadastrar Ponto Manual", "Um funcionário e um cartão deve ser selecionado.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Cadastrar Ponto Manual", ex.getMessage(), AlertType.WARNING);
			}
		});
		
		// Popula funcioionarios
		funcionarioListagem.setItems(FXCollections.observableArrayList(Funcionario.getAll()));
		
		// Popula cartão conforme funcionario selecionado
		funcionarioListagem.setOnMouseClicked((MouseEvent e) -> {
			if(funcionarioListagem.getSelectionModel().getSelectedItem() != null)
				cartaoListagem.setItems(FXCollections.observableArrayList(Cartao.getAll(funcionarioListagem.getSelectionModel().getSelectedItem())));
		});
		
		// Define o padrão de exibição das listagens
		setFuncionariosListagemCellFactory();
		setCartaoListagemCellFactory();
	}
	
	private void setFuncionariosListagemCellFactory() {
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
	
	private void setCartaoListagemCellFactory() {
		// Define um novo cell factory
		cartaoListagem.setCellFactory(new Callback<ListView<Cartao>, ListCell<Cartao>>() {
			// Realiza o override do método padrão
			@Override
			public ListCell<Cartao> call(ListView<Cartao> param) {
				// Cria uma nova célula da lista
				ListCell<Cartao> cell = new ListCell<Cartao>() {
					// Realiza o overrida do método padrão para definição do
					// nome de exibição na lista
					@Override
					protected void updateItem(Cartao item, boolean empty) {
						// Chama o construtor padrão
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getCodigo());
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
