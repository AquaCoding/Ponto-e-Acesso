package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.DatabaseConnect;

public class Ponto {

	private int idPonto;
	private String horario;
	private int idFuncionario;
	private int idFuncionarioTag;

	public int getIdPonto() {
		return idPonto;
	}

	public void setIdPonto(int idPonto) {
		this.idPonto = idPonto;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}
	
	public int getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(int idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	
	public int getIdFuncionarioTag() {
		return idFuncionarioTag;
	}

	public void setIdFuncionarioTag(int idFuncionarioTag) {
		this.idFuncionarioTag = idFuncionarioTag;
	}

	// Construtores

	public Ponto(int idPonto, String horario, int idFuncionario, int idFuncionarioTag) {
		setIdPonto(idPonto);
		setHorario(horario);
		setIdFuncionario(idFuncionario);
		setIdFuncionarioTag(idFuncionarioTag);
	}

	public Ponto(int idFuncionario, int idFuncionarioTag) {
		setHorario(horario);
		setIdFuncionario(idFuncionario);
		setIdFuncionarioTag(idFuncionarioTag);
	}

	// Registra o ponto no banco de dados
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"INSERT INTO Ponto (horario, idFuncionario, idFuncionariotag) VALUES (now(), ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setInt(1, this.idFuncionario);
			statement.setInt(2, this.idFuncionarioTag);
			
			// Executa o SQL
			int ret = statement.executeUpdate();

			// Retorna resultado
			if (ret == 1) {
				// Define o id a classe
				ResultSet id = statement.getGeneratedKeys();
				while (id.next())
					setIdPonto(id.getInt(1));

				// Encerra conexao
				connect.close();
				return true;
			} else {
				// Encerra conexao
				connect.close();
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao registrar ponto");
		}
	}
	
	public static boolean criarManual(LocalDate data, String horario, int idFuncionario, int idCartao) {
		try {
			if(data == null)
				throw new RuntimeException("A data não pode ser fazia.");
			
			if(horario == null || horario.equals("") || !horario.matches("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$"))
				throw new RuntimeException("O horário não pode ser fazio.");
			
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();
			
			// Executa um SQL
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"INSERT INTO Ponto (horario, idFuncionario, idFuncionarioTag) VALUES (?, ?, ?)");

			statement.setString(1, data.toString() + " " + horario);
			statement.setInt(2, idFuncionario);
			statement.setInt(3, idCartao);

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Retorna resultado
			if (ret == 1) {
				// Encerra conexao
				connect.close();
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.CARTAO, 0, ActionsCode.CRIOU_PONTO_MANUAL);
				return true;
			} else {
				// Encerra conexao
				connect.close();
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new RuntimeException("Um erro ocorreu ao obter os cartões.");
		}
	}
}
