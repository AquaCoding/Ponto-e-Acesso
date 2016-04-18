package aquacoding.controller;

import java.io.File;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class FuncionarioVerController implements Initializable {

	@FXML
	ImageView profileImage;

	@FXML
	Label nomeShow, sobrenomeShow, rgShow, cpfShow, cptsShow, salarioHorasShow,
	telefoneShow, ruaShow, numeroShow, bairroShow, estadoShow, cidadeShow, statusShow;

	@FXML
	ListView<Bonificacao> bonificacoesListagem;

	@FXML
	Button cancelar, editar, bonificaEditar, bonificaRemover;

	private Funcionario func;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// A��o do bot�o de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaFuncionarioView();
		});

		editar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadFuncionarioEditarView(func);
		});


	}

	public void setFuncionario(Funcionario func) {
		this.func = func;

		nomeShow.setText(func.getNome());
		sobrenomeShow.setText(func.getSobrenome());
		rgShow.setText(func.getRg());
		cpfShow.setText(func.getCpf());
		cptsShow.setText(func.getCtps());
		salarioHorasShow.setText(""+func.getSalarioHoras());
		telefoneShow.setText(func.getTelefone());
		ruaShow.setText(func.getRua());
		numeroShow.setText(""+func.getNumero());
		bairroShow.setText(func.getBairro());
		estadoShow.setText(func.getEstado());
		cidadeShow.setText(func.getCidade());

		if(func.getSuspensao() == true){
			statusShow.setText("Suspenso");
		}else{
			statusShow.setText("Regular");
		}

		File f = func.getProfileImage();
		if(f.exists()) {
			Image i = new Image(f.toURI().toString(), 200, 200, false, true);
			profileImage.setImage(i);
		}

		bonificaRemover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se alguma bonifica��o foi selecionado e pergunta se
				// ele realmente o que remover
				if (bonificacoesListagem.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert(
						"Remover Bonifica��o", "Voc� tem certeza que deseja remover essa bonifica��o?")) {
					// Obtem a bonifica��o selecionado
					Bonificacao b = bonificacoesListagem.getSelectionModel().getSelectedItem();

					// Tenta remover a bonifica��o no BD
					if (b.delete()) {
						// bonifica��o removido com sucesso
						CustomAlert.showAlert("Remover Bonifica��o", "Bonifica��o removido com sucesso.", AlertType.WARNING);
						Main.loadListaFuncionarioView();
					} else {
						// Erro ao remover a bonifica��o
						CustomAlert.showAlert("Remover Bonifica��o", "Algo deu errado.", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de valida��o
				CustomAlert.showAlert("Remover Bonifica��o", ex.getMessage(), AlertType.WARNING);
			}
		});

		// Define a listview de bonifica��es
		bonificacoesListagem.setItems(FXCollections.observableArrayList(func.getBonificacoes()));
		setBonificacoesListagemCellFactory();

		bonificaEditar.setOnMouseClicked((MouseEvent e) -> {
			// Verifica se um funcionario foi selecionado
			if (bonificacoesListagem.getSelectionModel().getSelectedItem() != null)
				Main.loadbinificacaoEditarView(bonificacoesListagem.getSelectionModel().getSelectedItem());
		});
	}

	private void setBonificacoesListagemCellFactory() {
		// Define um novo cell factory
		bonificacoesListagem.setCellFactory(new Callback<ListView<Bonificacao>, ListCell<Bonificacao>>() {
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
							setText(item.getNome() + " (" + item.getValor() +")");
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
