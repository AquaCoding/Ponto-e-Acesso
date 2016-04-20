package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;

import aquacoding.utils.DatabaseConnect;
import logs.ActionsCode;
import logs.ObjectCode;

public class Log {

	private int id;
	private int idUsuario;
	private ObjectCode objeto;
	private int objectoId;
	private ActionsCode acao;
	private Instant data;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public ObjectCode getObjeto() {
		return objeto;
	}

	public void setObjeto(ObjectCode objeto) {
		this.objeto = objeto;
	}

	public int getObjectoId() {
		return objectoId;
	}

	public void setObjectoId(int objectoId) {
		this.objectoId = objectoId;
	}

	public ActionsCode getAcao() {
		return acao;
	}

	public void setAcao(ActionsCode acao) {
		this.acao = acao;
	}
	
	public Instant getData() {
		return data;
	}

	public void setData(Instant data) {
		this.data = data;
	}

	// CONSTRUTOR
	public Log(int id, int idUsuario, ObjectCode objeto, int objectoId, ActionsCode acao, Instant data) {
		setId(id);
		setIdUsuario(idUsuario);
		setObjeto(objeto);
		setObjectoId(objectoId);
		setAcao(acao);
		setData(data);
	}
	
	public Log(int idUsuario, ObjectCode objeto, int objectoId, ActionsCode acao) {
		setIdUsuario(idUsuario);
		setObjeto(objeto);
		setObjectoId(objectoId);
		setAcao(acao);
	}

	// CREATE
	public void create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Logs (idUsuario, objeto, objetoId, acao) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setInt(1, idUsuario);
			statement.setString(2, objeto.getValor());
			statement.setInt(3, objectoId);
			statement.setString(4, acao.getValor());

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Retorna resultado
			if (ret == 1) {
				// Encerra conexao
				connect.close();
			} else {
				// Encerra conexao
				connect.close();
				throw new RuntimeException("Um erro ocorre ao criar o log.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao criar o log.");
		}
	}
	
	// Obtem todos os logs
	public static ArrayList<Log> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Logs");

			ArrayList<Log> logs = new ArrayList<Log>();
			while (resultSet.next()) {
				// Adiciona o logs ao retorno
				logs.add(new Log(
								resultSet.getInt("idLog"), 
								resultSet.getInt("idUsuario"), 
								ObjectCode.getObjectCode(resultSet.getString("objeto")), 
								resultSet.getInt("objetoId"),
								ActionsCode.getActionsCode(resultSet.getString("acao")), 
								resultSet.getTimestamp("data").toInstant()
								)
						);
			}

			// Retorna os logs
			return logs;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu obter os logs.");
		}
	}

}
