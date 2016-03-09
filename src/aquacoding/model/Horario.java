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
	private Time inicio;
	private Time termino;
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
			throw new RuntimeException("O nome do horário não pode ser vazio.");
		this.nome = nome;
	}
	
	public Time getInicio() {
		return inicio;
	}
	
	public void setInicio(Time inicio) {
		if(inicio == null)
			throw new RuntimeException("Um horário de inicio deve ser selecionado.");
		this.inicio = inicio;
	}
	
	public Time getTermino() {
		return termino;
	}
	
	public void setTermino(Time termino) {
		if(termino == null)
			throw new RuntimeException("Um horário de termino deve ser selecionado.");
		this.termino = termino;
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
	public Horario(int id, String nome, Time inicio, Time termino, Time inicioAlmoco, Time terminoAlmoco) {
		setId(id);
		setNome(nome);
		setInicio(inicio);
		setTermino(termino);
		setInicioAlmoco(inicioAlmoco);
		setTerminoAlmoco(terminoAlmoco);
	}
	
	public Horario(String nome, Time inicio, Time termino, Time inicioAlmoco, Time terminoAlmoco) {
		setNome(nome);
		setInicio(inicio);
		setTermino(termino);
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
					.prepareStatement("INSERT INTO Horario (nome, inicio, termino, almocoInicio, almocoTermino) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setTime(2, this.inicio);
			statement.setTime(3, this.termino);
			statement.setTime(4, this.inicioAlmoco);
			statement.setTime(5, this.terminoAlmoco);

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
			throw new RuntimeException("Um erro ocorreu ao criar o setor");
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
				Time inicio = resultSet.getTime("inicio");
				Time termino = resultSet.getTime("termino");
				Time almocoInicio = resultSet.getTime("almocoInicio");
				Time almocoTermino = resultSet.getTime("almocoTermino");
				
				Horario c = new Horario(resultSet.getInt("idHorario"), resultSet.getString("nome"), inicio, termino, almocoInicio, almocoTermino);

				// Adiciona o cliente ao retorno
				horarios.add(c);
			}

			// Retorna os clientes
			return horarios;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}
}
