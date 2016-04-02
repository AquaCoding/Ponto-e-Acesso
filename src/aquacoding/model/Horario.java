package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

import aquacoding.utils.DatabaseConnect;

public class Horario {

	private int id;
	private String nome;
	private Time inicioTurno1;
	private Time terminoTurno1;
	private Time inicioTurno2;
	private Time terminoTurno2;
	private Time inicioAlmoco;
	private Time terminoAlmoco;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if(nome.equals(""))
			throw new RuntimeException("O nome do hor�rio n�o pode ser vazio.");
		this.nome = nome;
	}

	public Time getInicioTurno1() {
		return inicioTurno1;
	}

	public void setInicioTurno1(Time inicioTurno1) {
		if(inicioTurno1 == null)
			throw new RuntimeException("Um horário de inicio deve ser selecionado.");
		this.inicioTurno1 = inicioTurno1;
	}

	public Time getTerminoTurno1() {
		return terminoTurno1;
	}

	public void setTerminoTurno1(Time terminoTurno1) {
		if(terminoTurno1 == null)
			throw new RuntimeException("Um horário de termino deve ser selecionado.");
		this.terminoTurno1 = terminoTurno1;
	}
	
	public Time getInicioTurno2() {
		return inicioTurno2;
	}

	public void setInicioTurno2(Time inicioTurno2) {
		if(inicioTurno2 == null)
			throw new RuntimeException("Um horário de inicio deve ser selecionado.");
		this.inicioTurno2 = inicioTurno2;
	}

	public Time getTerminoTurno2() {
		return terminoTurno2;
	}

	public void setTerminoTurno2(Time terminoTurno2) {
		if(terminoTurno2 == null)
			throw new RuntimeException("Um horário de termino deve ser selecionado.");
		this.terminoTurno2 = terminoTurno2;
	}

	public Time getInicioAlmoco() {
		return inicioAlmoco;
	}

	public void setInicioAlmoco(Time inicioAlmoco) {
		if(inicioAlmoco == null)
			throw new RuntimeException("Um horário de inicio ao almoço deve ser selecionado.");
		this.inicioAlmoco = inicioAlmoco;
	}

	public Time getTerminoAlmoco() {
		return terminoAlmoco;
	}

	public void setTerminoAlmoco(Time terminoAlmoco) {
		if(terminoAlmoco == null)
			throw new RuntimeException("Um horário de inicio ao almoço deve ser selecionado.");
		this.terminoAlmoco = terminoAlmoco;
	}

	// CONSTRUTORES
	public Horario(int id, String nome, Time inicioTurno1, Time terminoTurno1, Time inicioTurno2, Time terminoTurno2, Time inicioAlmoco, Time terminoAlmoco) {
		setId(id);
		setNome(nome);
		setInicioTurno1(inicioTurno1);
		setTerminoTurno1(terminoTurno1);
		setInicioTurno2(inicioTurno2);
		setTerminoTurno2(terminoTurno2);
		setInicioAlmoco(inicioAlmoco);
		setTerminoAlmoco(terminoAlmoco);
	}

	public Horario(String nome, Time inicioTurno1, Time terminoTurno1, Time inicioTurno2, Time terminoTurno2, Time inicioAlmoco, Time terminoAlmoco) {
		setNome(nome);
		setInicioTurno1(inicioTurno1);
		setTerminoTurno1(terminoTurno1);
		setInicioTurno2(inicioTurno2);
		setTerminoTurno2(terminoTurno2);
		setInicioAlmoco(inicioAlmoco);
		setTerminoAlmoco(terminoAlmoco);
	}

	// Cria um novo horario no banco de dados
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Horario (nome, inicioTurno1, terminoTurno1, inicioTurno2, terminoTurno2, almocoInicio, almocoTermino) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setTime(2, this.inicioTurno1);
			statement.setTime(3, this.terminoTurno1);
			statement.setTime(4, this.inicioTurno2);
			statement.setTime(5, this.terminoTurno2);
			statement.setTime(6, this.inicioAlmoco);
			statement.setTime(7, this.terminoAlmoco);

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Retorna resultado
			if (ret == 1) {
				// Define o id a classe
				ResultSet id = statement.getGeneratedKeys();
				while (id.next())
					setId(id.getInt(1));

				// Encerra conexao
				connect.close();
				return true;
			} else {
				// Encerra conexao
				connect.close();
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao criar o Hor�rio");
		}
	}

	// Retorna todos os horarios
	public static ArrayList<Horario> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Horario");

			ArrayList<Horario> horarios = new ArrayList<Horario>();
			while (resultSet.next()) {
				// Cria um cliente com os dados do BD
				Time inicioTurno1 = resultSet.getTime("inicioTurno1");
				Time terminoTurno1 = resultSet.getTime("terminoTurno1");
				Time inicioTurno2 = resultSet.getTime("inicioTurno2");
				Time terminoTurno2 = resultSet.getTime("terminoTurno2");
				Time almocoInicio = resultSet.getTime("almocoInicio");
				Time almocoTermino = resultSet.getTime("almocoTermino");

				Horario c = new Horario(resultSet.getInt("idHorario"), resultSet.getString("nome"), inicioTurno1, terminoTurno1, inicioTurno2, terminoTurno2, almocoInicio, almocoTermino);

				// Adiciona o cliente ao retorno
				horarios.add(c);
			}

			// Retorna os clientes
			return horarios;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}

	public boolean delete() {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("DELETE FROM Horario WHERE idHorario = ?");

			// Realiza o bind dos valores
			statement.setInt(1, this.id);

			// Executa o SQL
			int resp = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			if(resp == 1) {
				return true;
			}else {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao deletar o Horario");
		}
	}

	public boolean update() {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"UPDATE Horario SET nome = ?, inicioTurno1 = ?, terminoTurno1 = ?, inicioTurno2 = ?, terminoTurno2 = ?, almocoInicio = ?, almocoTermino = ? WHERE idHorario = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setTime(2, this.inicioTurno1);
			statement.setTime(3, this.terminoTurno1);
			statement.setTime(2, this.inicioTurno2);
			statement.setTime(3, this.terminoTurno2);
			statement.setTime(4, this.inicioAlmoco);
			statement.setTime(5, this.terminoAlmoco);
			statement.setInt(6, this.id);

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			// Retorna resultado
			if (ret == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Um erro ocorreu ao atualizar o Hor�rio");
		}
	}

	public static Horario getByID(int id) {
		try{
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM Horario WHERE idHorario = ?", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setInt(1, id);

			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();

			// Obtem o primeiro resultado e o retorna
			if(resultSet.next())
				return new Horario(resultSet.getInt("idHorario"), resultSet.getString("nome"), resultSet.getTime("inicioTurno1"), resultSet.getTime("terminoTurno1"), resultSet.getTime("inicioTurno2"), resultSet.getTime("terminoTurno2"), resultSet.getTime("almocoInicio"), resultSet.getTime("almocoTermino"));

			// Se nada for achado, retorna nulo
			return null;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}
	
	public String toString(){
		return " " + nome;
	}
}
