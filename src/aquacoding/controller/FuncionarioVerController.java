package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Bonificacao;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class FuncionarioVerController implements Initializable {

	@FXML
	ImageView profileImage;
	
	@FXML
	Label nomeShow, sobrenomeShow, rgShow, cpfShow, cptsShow, salarioHorasShow,
	telefoneShow, ruaShow, numeroShow, bairroShow, estadoShow, cidadeShow;
	
	@FXML
	ListView<Bonificacao> bonificacoesListagem;
	
	@FXML
	Button cancelar, editar;
	
	private Funcionario func;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Ação do botão de cancelar
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
		
		File f = func.getProfileImage();
		if(f.exists()) {
			Image i = new Image(f.toURI().toString(), 200, 200, false, true);
			profileImage.setImage(i);
		}
		
		// Define a listview de bonificações
		bonificacoesListagem.setItems(FXCollections.observableArrayList(func.getBonificacoes()));
		setBonificacoesListagemCellFactory();
	}
	
	private void setBonificacoesListagemCellFactory() {
		// Define um novo cell factory
		bonificacoesListagem.setCellFactory(new Callback<ListView<Bonificacao>, ListCell<Bonificacao>>() {
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
							setText(item.getNome() + " (" + item.getValor() +")");
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
