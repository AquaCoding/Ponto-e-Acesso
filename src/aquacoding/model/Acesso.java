package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import aquacoding.utils.DatabaseConnect;

public class Acesso {
	private int idAcesso;
	private String horario;
	private int idFuncionario;
	private int idFuncionarioTag;

	public int getIdPonto() {
		return idAcesso;
	}

	public void setIdAcesso(int idAcesso) {
		this.idAcesso = idAcesso;
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

	public Acesso(int idAcesso, String horario, int idFuncionario, int idFuncionarioTag) {
		setIdAcesso(idAcesso);
		setHorario(horario);
		setIdFuncionario(idFuncionario);
		setIdFuncionarioTag(idFuncionarioTag);
	}

	public Acesso(int idFuncionario, int idFuncionarioTag) {
		setHorario(horario);
		setIdFuncionario(idFuncionario);
		setIdFuncionarioTag(idFuncionarioTag);
	}

	// Registra o ponto no banco de dados
	public boolean create() {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"INSERT INTO Acesso (horario, idFuncionario, idFuncionaritag) VALUES (now(), ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

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
					setIdAcesso(id.getInt(1));

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

}
