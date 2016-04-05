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
	private Boolean segunda;
	private Boolean terca;
	private Boolean quarta;
	private Boolean quinta;
	private Boolean sexta;
	private Boolean sabado;
	private Boolean domingo;

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
	
	public void setSegunda(Boolean segunda) {
		this.segunda = segunda;
	}

	public void setTerca(Boolean terca) {
		this.terca = terca;
	}

	public void setQuarta(Boolean quarta) {
		this.quarta = quarta;
	}

	public void setQuinta(Boolean quinta) {
		this.quinta = quinta;
	}

	public void setSexta(Boolean sexta) {
		this.sexta = sexta;
	}

	public void setSabado(Boolean sabado) {
		this.sabado = sabado;
	}

	public void setDomingo(Boolean domingo) {
		this.domingo = domingo;
	}

	// CONSTRUTORES
	public Horario(int id, String nome, Time inicio, Time termino, Time inicioAlmoco, Time terminoAlmoco, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado, Boolean domingo) {
		setId(id);
		setNome(nome);
		setInicio(inicio);
		setTermino(termino);
		setInicioAlmoco(inicioAlmoco);
		setTerminoAlmoco(terminoAlmoco);
		setSegunda(segunda);
		setTerca(terca);
		setQuarta(quarta);
		setQuinta(quinta);
		setSexta(sexta);
		setSabado(sabado);
		setDomingo(domingo);
	}

	public Horario(String nome, Time inicio, Time termino, Time inicioAlmoco, Time terminoAlmoco, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado, Boolean domingo) {
		setNome(nome);
		setInicio(inicio);
		setTermino(termino);
		setInicioAlmoco(inicioAlmoco);
		setTerminoAlmoco(terminoAlmoco);
		setSegunda(segunda);
		setTerca(terca);
		setQuarta(quarta);
		setQuinta(quinta);
		setSexta(sexta);
		setSabado(sabado);
		setDomingo(domingo);
	}

	// Cria um novo horario no banco de dados
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Horario (nome, inicio, termino, almocoInicio, almocoTermino, segunda, terca, quarta, quinta, sexta, sabado, domingo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setTime(2, this.inicio);
			statement.setTime(3, this.termino);
			statement.setTime(4, this.inicioAlmoco);
			statement.setTime(5, this.terminoAlmoco);
			statement.setBoolean(6, this.segunda);
			statement.setBoolean(7, this.terca);
			statement.setBoolean(8, this.quarta);
			statement.setBoolean(9, this.quinta);
			statement.setBoolean(10, this.sexta);
			statement.setBoolean(11, this.sabado);
			statement.setBoolean(12, this.domingo);

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
				Time inicio = resultSet.getTime("inicio");
				Time termino = resultSet.getTime("termino");
				Time almocoInicio = resultSet.getTime("almocoInicio");
				Time almocoTermino = resultSet.getTime("almocoTermino");
				Boolean segunda = resultSet.getBoolean("segunda");
				Boolean terca = resultSet.getBoolean("terca");
				Boolean quarta = resultSet.getBoolean("quarta");
				Boolean quinta = resultSet.getBoolean("quinta");
				Boolean sexta = resultSet.getBoolean("sexta");
				Boolean sabado = resultSet.getBoolean("sabado");
				Boolean domingo = resultSet.getBoolean("domingo");

				Horario c = new Horario(resultSet.getInt("idHorario"), resultSet.getString("nome"), inicio, termino, almocoInicio, almocoTermino, segunda, terca, quarta, quinta, sexta, sabado, domingo);

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
					"UPDATE Horario SET nome = ?, inicio = ?, termino = ?, almocoInicio = ?, almocoTermino = ? WHERE idHorario = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setTime(2, this.inicio);
			statement.setTime(3, this.termino);
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
				return new Horario(resultSet.getInt("idHorario"), resultSet.getString("nome"), resultSet.getTime("inicio"), resultSet.getTime("termino"), resultSet.getTime("almocoInicio"), resultSet.getTime("almocoTermino"), resultSet.getBoolean("segunda"), resultSet.getBoolean("terca"), resultSet.getBoolean("quarta"), resultSet.getBoolean("quinta"), resultSet.getBoolean("sexta"), resultSet.getBoolean("sabado"), resultSet.getBoolean("domingo"));

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
