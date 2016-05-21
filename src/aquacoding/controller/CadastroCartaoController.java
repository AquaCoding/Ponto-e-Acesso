package aquacoding.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.DatabaseConnect;
import aquacoding.utils.Serial;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import jssc.SerialPortException;

public class CadastroCartaoController implements Initializable {

	@FXML
	Button cancelar;

	private static Thread serialThread;
	private static Thread timeoutThread;

	private Funcionario func;
	private Serial serial;

	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaFuncionarioView();
		});

		CustomAlert.showAlert("Cadastro de Cartão", "Aproxime seu cartão do leitor", AlertType.INFORMATION);

		// Serial
		Serial serial = Serial.getInstance();
		serialThread = new Thread(() -> {
			try {
				serial.SerialLeitura();
				wait(3000);
				String code = serial.getCode();
				
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
			int ret = statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao registrar ponto");
		}
	}
}
