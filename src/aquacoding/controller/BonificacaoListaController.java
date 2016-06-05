package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Bonificacao;
import aquacoding.model.Funcao;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class BonificacaoListaController implements Initializable{

	// Carrega os elementos do layout
	@FXML
	ListView<Bonificacao> bonificacaoListagem;

	@FXML
	Button cancelar, alterar, remover, linkar;

	// Atributos
	ObservableList<Funcao> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a exibição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Inicia a view de edição de uma função
		alterar.setOnMouseClicked((MouseEvent e) -> {
			if(bonificacaoListagem.getSelectionModel().getSelectedItem() != null)
				Main.loadBonificacaoEditarView(bonificacaoListagem.getSelectionModel().getSelectedItem());
		});

		// Define o nome método de criação de células
		setListagemCellFactory();

		// Adiciona a lista
		bonificacaoListagem.setItems(FXCollections.observableArrayList(Bonificacao.getAll()));

		// Tenta realizar a remoção
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se alguma Bonificação foi selecionada e pergunta se ele
				// realmente o que remover
				if (bonificacaoListagem.getSelectionModel().getSelectedItem() != null && CustomAlert
						.showConfirmationAlert("Remover Bonificação", "Você tem certeza que deseja remover essa bonificação?")) {
					// Obtem a bonificação selecionada
					Bonificacao b = bonificacaoListagem.getSelectionModel().getSelectedItem();

					// Tenta remover
					if (b.delete()) {
						// Removido com sucesso
						CustomAlert.showAlert("Remover Bonificação", "Bonificação removida com sucesso.", AlertType.WARNING);
						Main.loadBonificacaoListarView();
					} else {
						// Erro ao remover
						CustomAlert.showAlert("Remover Bonificação", "Algo deu errado", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Remover Setor", ex.getMessage(), AlertType.WARNING);
			}
		});

		linkar.setOnMouseClicked((MouseEvent e) -> {
			if(bonificacaoListagem.getSelectionModel().getSelectedItem() != null)
				Main.loadBonificacaoLink(bonificacaoListagem.getSelectionModel().getSelectedItem());
		});

	}

	private void setListagemCellFactory() {
		// Define um novo cell factory
		bonificacaoListagem.setCellFactory(new Callback<ListView<Bonificacao>, ListCell<Bonificacao>>() {
			// Realiza o override do método padrão
			@Override
			public ListCell<Bonificacao> call(ListView<Bonificacao> param) {
				// Cria uma nova célula da lista
				ListCell<Bonificacao> cell = new ListCell<Bonificacao>() {
					// Realiza o overrida do método padrão para definição do
					// nome de exibição na lista
					@Override
					protected void updateItem(Bonificacao item, boolean empty) {
						// Chama o construtor padrão
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome() + " (" + item.getValor() + "%)");
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
