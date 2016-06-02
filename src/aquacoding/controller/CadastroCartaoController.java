package aquacoding.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.DatabaseConnect;
import aquacoding.utils.Serial;

public class CadastroCartaoController implements Initializable {

	@FXML
	Button cancelar;

	@FXML
	TextArea textHelp;
	
	private static Thread serialThread;

	private Funcionario func;

	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaFuncionarioView();
		});

		textHelp.setText("Instruções: \n"
				+ "Aproxime o cartão ou tag do leitor \n"
				+ "Aguarde confirmação na tela \n"
				+ "Retire o cartão \n"
				+ "Caso o procedimento for bem sucedido, prossiga \n"
				+ "Caso contrário contate o suporte");
		
		CustomAlert.showAlert("Cadastro de Cartão", "Aproxime seu cartão do leitor", AlertType.INFORMATION);

		// Serial
		Serial serial = Serial.getInstance();
		serialThread = new Thread(() -> {
			try {				
				serial.SerialLeitura();	
				serial.wait(2000);
				String code = serial.getCode();
				System.out.println(code);
				cadastro(func.getId(), code);
			} catch (Exception e) {
				System.out.println("Erro Serial: " + e.getMessage());
			}
		});
		serialThread.start();

	}

	public void setFuncionario(Funcionario funcionario) {
		this.func = funcionario;
	}
	
	public void cadastro(int id, String code) {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"INSERT INTO FuncionarioTag (code, ativo, idFuncionario) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, code);
			statement.setInt(2, 1);
			statement.setInt(3, id);
			
			// Executa o SQL
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao registrar ponto");
		}
	}
}
