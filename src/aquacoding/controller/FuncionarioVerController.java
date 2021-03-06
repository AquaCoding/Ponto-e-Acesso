package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import aquacoding.model.Bonificacao;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;

public class FuncionarioVerController implements Initializable {

	@FXML
	ImageView profileImage;

	@FXML
	Label nomeShow, sobrenomeShow, rgShow, cpfShow, cptsShow, salarioHorasShow, telefoneShow, ruaShow, numeroShow,
			bairroShow, estadoShow, cidadeShow, statusShow,funcaoNomeShow,lblAdmissao,admissaoShow,lblDemissao,demissaoShow;

	@FXML
	ListView<Bonificacao> bonificacoesListagem;

	@FXML
	Button cancelar, editar, bonificaRemover, ferias, modeloCartao, cadastroCartao;

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

		ferias.setOnMouseClicked((MouseEvent e) -> {
			Main.loadFuncionarioFeriasView(func);
		});

		modeloCartao.setOnMouseClicked((MouseEvent e) -> {
			Main.loadParsedModeloCartao(func);
		});
		
		cadastroCartao.setOnMouseClicked((MouseEvent e) -> {
			Main.loadCadastroCartao(func);
		});
	}

	public void setFuncionario(Funcionario func) {
		this.func = func;

		nomeShow.setText(func.getNome());
		sobrenomeShow.setText(func.getSobrenome());
		rgShow.setText(func.getRg());
		cpfShow.setText(func.getCpf());
		cptsShow.setText(func.getCtps());
		salarioHorasShow.setText("" + func.getSalarioHoras());
		telefoneShow.setText(func.getTelefone());
		ruaShow.setText(func.getRua());
		numeroShow.setText("" + func.getNumero());
		bairroShow.setText(func.getBairro());
		estadoShow.setText(func.getEstado());
		cidadeShow.setText(func.getCidade());
		admissaoShow.setText(func.getAdmissaoString());
		if(func.getDemissaoString() != null){
			demissaoShow.setText(func.getDemissaoString());
		}else{
			demissaoShow.setText("");
			lblDemissao.setText("");
		}


		funcaoNomeShow.setText(func.getFuncao().getNome());

		statusShow.setText(func.getStatus());


		File f = func.getProfileImage();
		if (f.exists()) {
			Image i = new Image(f.toURI().toString(), 200, 200, false, true);
			profileImage.setImage(i);
		}

		bonificaRemover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se alguma bonifica��o foi selecionado e pergunta se
				// ele realmente o que remover
				if (bonificacoesListagem.getSelectionModel().getSelectedItem() != null
						&& CustomAlert.showConfirmationAlert("Remover Bonifica��o",
								"Voc� tem certeza que deseja remover esse link de bonifica��o?")) {
					// Obtem a bonifica��o selecionado
					Bonificacao b = bonificacoesListagem.getSelectionModel().getSelectedItem();

					// Tenta remover a bonifica��o no BD
					if (Bonificacao.removerLink(b, func)) {
						// bonifica��o removido com sucesso
						CustomAlert.showAlert("Remover Bonifica��o", "Bonifica��o removida com sucesso.", AlertType.WARNING);
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
