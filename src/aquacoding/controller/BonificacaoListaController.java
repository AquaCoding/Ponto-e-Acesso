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
		// Cancela a exibi��o e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Inicia a view de edi��o de uma fun��o
		alterar.setOnMouseClicked((MouseEvent e) -> {
			if(bonificacaoListagem.getSelectionModel().getSelectedItem() != null)
				Main.loadBonificacaoEditarView(bonificacaoListagem.getSelectionModel().getSelectedItem());
		});

		// Define o nome m�todo de cria��o de c�lulas
		setListagemCellFactory();

		// Adiciona a lista
		bonificacaoListagem.setItems(FXCollections.observableArrayList(Bonificacao.getAll()));

		// Tenta realizar a remo��o
		remover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se alguma Bonifica��o foi selecionada e pergunta se ele
				// realmente o que remover
				if (bonificacaoListagem.getSelectionModel().getSelectedItem() != null && CustomAlert
						.showConfirmationAlert("Remover Bonifica��o", "Voc� tem certeza que deseja remover essa bonifica��o?")) {
					// Obtem a bonifica��o selecionada
					Bonificacao b = bonificacaoListagem.getSelectionModel().getSelectedItem();

					// Tenta remover
					if (b.delete()) {
						// Removido com sucesso
						CustomAlert.showAlert("Remover Bonifica��o", "Bonifica��o removida com sucesso.", AlertType.WARNING);
						Main.loadBonificacaoListarView();
					} else {
						// Erro ao remover
						CustomAlert.showAlert("Remover Bonifica��o", "Algo deu errado", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de valida��o
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
			// Realiza o override do m�todo padr�o
			@Override
			public ListCell<Bonificacao> call(ListView<Bonificacao> param) {
				// Cria uma nova c�lula da lista
				ListCell<Bonificacao> cell = new ListCell<Bonificacao>() {
					// Realiza o overrida do m�todo padr�o para defini��o do
					// nome de exibi��o na lista
					@Override
					protected void updateItem(Bonificacao item, boolean empty) {
						// Chama o construtor padr�o
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.getNome() + " (" + item.getValor() + "%)");
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
