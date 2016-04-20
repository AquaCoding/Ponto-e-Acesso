package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import aquacoding.model.Bonificacao;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.DatabaseConnect;
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
	telefoneShow, ruaShow, numeroShow, bairroShow, estadoShow, cidadeShow, nomeFeriasShow, inicioFeriasShow, terminoFeriasShow;

	@FXML
	ListView<Bonificacao> bonificacoesListagem;

	@FXML
	Button cancelar, editar, bonificaEditar, bonificaRemover;

	private Funcionario func;
	
	private String nome, inicio, termino;

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
		
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();
			
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement("SELECT * FROM funcionarioFerias WHERE idFuncionario = ?");

			//Bind
			statement.setInt(1, this.func.getId());
			
			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();											
			
			PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement("SELECT * FROM ferias WHERE idFerias = ?");

			//Bind
			statement2.setInt(1, resultSet.getInt("idFerias"));					
			
			// Executa o SQL
			ResultSet resultSet2 = statement2.executeQuery();
			
			this.nome = resultSet2.getString("nome");			
			this.inicio = resultSet2.getString("inicio");			
			this.termino = resultSet2.getString("termino");	
			
			connect.close();
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao obter as férias.");
		}
		
		nomeFeriasShow.setText(this.nome);
		inicioFeriasShow.setText(this.inicio);
		terminoFeriasShow.setText(this.termino);

		File f = func.getProfileImage();
		if(f.exists()) {
			Image i = new Image(f.toURI().toString(), 200, 200, false, true);
			profileImage.setImage(i);
		}

		bonificaRemover.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Verifica se alguma bonificação foi selecionado e pergunta se
				// ele realmente o que remover
				if (bonificacoesListagem.getSelectionModel().getSelectedItem() != null && CustomAlert.showConfirmationAlert(
						"Remover Bonificação", "Você tem certeza que deseja remover essa bonificação?")) {
					// Obtem a bonificação selecionado
					Bonificacao b = bonificacoesListagem.getSelectionModel().getSelectedItem();

					// Tenta remover a bonificação no BD
					if (b.delete()) {
						// bonificação removido com sucesso
						CustomAlert.showAlert("Remover Bonificação", "Bonificação removido com sucesso.", AlertType.WARNING);
						Main.loadListaFuncionarioView();
					} else {
						// Erro ao remover a bonificação
						CustomAlert.showAlert("Remover Bonificação", "Algo deu errado.", AlertType.WARNING);
					}
				}
			} catch (RuntimeException ex) {
				// Erro de validação
				CustomAlert.showAlert("Remover Bonificação", ex.getMessage(), AlertType.WARNING);
			}
		});

		// Define a listview de bonificações
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
