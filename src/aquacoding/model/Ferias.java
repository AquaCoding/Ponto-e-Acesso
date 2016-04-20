package aquacoding.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.DatabaseConnect;

public class Ferias {

	private int id;
	private String nome;
	private java.sql.Date inicio;
	private java.sql.Date termino;
	private ArrayList<Funcionario> funcionarios = new ArrayList<Funcionario>();

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
		this.nome = nome;
	}

	public String getInicio() {
		SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy");
		return format.format(inicio);
	}

	public void setInicio(java.sql.Date inicio) {
		this.inicio = inicio;
	}

	public String getTermino() {
		SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy");
		return format.format(termino);
	}

	public void setTermino(java.sql.Date termino) {
		this.termino = termino;
	}

	public ArrayList<Funcionario> getFuncionario() {
	     return funcionarios;
	}

	public void setFuncionario(Funcionario funcionario) {
		funcionarios.add(funcionario);

	}


	// CONSTRUTORES
	public Ferias(int id, String nome, java.sql.Date inicio, java.sql.Date termino) {
		setId(id);
		setNome(nome);
		setInicio(inicio);
		setTermino(termino);
	}

	public Ferias(String nome, java.sql.Date inicio, java.sql.Date termino) {
		setNome(nome);
		setInicio(inicio);
		setTermino(termino);
	}

	// Cria uma nova ferias no banco de dados
		public boolean create() {
			try {
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um prepared statement
				PreparedStatement statement = (PreparedStatement) connect
						.prepareStatement("INSERT INTO Ferias (nome, inicio, termino) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

				// Realiza o bind dos valores
				statement.setString(1, this.nome);
				statement.setDate(2, this.inicio);
				statement.setDate(3, this.termino);

				// Executa o SQL
				int ret = statement.executeUpdate();

				// Retorna resultado
				if (ret == 1) {
					// Define o id a classe
					ResultSet id = statement.getGeneratedKeys();
					while (id.next())
						setId(id.getInt(1));


					for(int i = 0; i < funcionarios.size(); i++) {
						// Cria um prepared statement
						PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement(
								"INSERT INTO FuncionarioFerias (idFuncionario, idFerias) VALUES (?, ?)",
								Statement.RETURN_GENERATED_KEYS);
						statement2.setInt(1, funcionarios.get(i).getId());
						statement2.setInt(2, this.id);
						statement2.executeUpdate();

					}

					// Encerra conexao
					connect.close();
					
					// Gera log
					Logs.makeLog(Main.loggedUser.getId(), ObjectCode.FERIAS, this.id, ActionsCode.CADASTROU);
					
					return true;
				} else {
					// Encerra conexao
					connect.close();
					return false;
				}
			} catch (SQLException e) {
				throw new RuntimeException("Um erro ocorreu ao criar as férias.");
			}
		}

		// Retorna todas as ferias
		public static ArrayList<Ferias> getAll() {
			try {
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um statement
				Statement statement = connect.createStatement();

				// Executa um SQL
				ResultSet resultSet = statement.executeQuery("SELECT * FROM Ferias");

				ArrayList<Ferias> Ferias = new ArrayList<Ferias>();
				while (resultSet.next()) {

					// Cria uma ferias com os dados do BD
					java.sql.Date inicio = resultSet.getDate("inicio");

					java.sql.Date termino = resultSet.getDate("termino");

					Ferias c = new Ferias(resultSet.getInt("idFerias"), resultSet.getString("nome"), inicio, termino);

					// Adiciona a ferias ao retorno
					Ferias.add(c);
				}

				// Retorna as ferias
				return Ferias;
			} catch (SQLException e) {
				throw new RuntimeException("Um erro ocorreu ao obter as férias.");
			}
		}

		public boolean update() {
			try {
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um prepared statement
				PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
						"UPDATE Ferias SET nome = ?, inicio = ?, termino = ? WHERE idFerias = ?");


				// Realiza o bind dos valores
				statement.setString(1, this.nome);
				statement.setDate(2, this.inicio);
				statement.setDate(3, this.termino);
				statement.setInt(4, this.id);

				// Executa o SQL
				int ret = statement.executeUpdate();

				for(int i = 0; i < funcionarios.size(); i++) {
					// Cria um prepared statement
					PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement(
							"INSERT INTO FuncionarioFerias (idFuncionario, idFerias) VALUES (?, ?)",
							Statement.RETURN_GENERATED_KEYS);
					statement2.setInt(1, funcionarios.get(i).getId());
					statement2.setInt(2, this.id);
					statement2.executeUpdate();

				}

				// Encerra conexao
				connect.close();

				// Retorna resultado
				if (ret == 1) {
					// Gera log
					Logs.makeLog(Main.loggedUser.getId(), ObjectCode.FERIAS, this.id, ActionsCode.EDITOU);
					
					return true;
				} else {
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Um erro ocorreu ao atualizar as férias.");
			}
		}

		public static Ferias getByID(int id) {
			try{
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um prepared statement
				PreparedStatement statement = (PreparedStatement) connect
						.prepareStatement("SELECT * FROM Ferias WHERE idFerias = ?", Statement.RETURN_GENERATED_KEYS);

				// Realiza o bind dos valores
				statement.setInt(1, id);

				// Executa o SQL
				ResultSet resultSet = statement.executeQuery();

				// Obtem o primeiro resultado e o retorna
				if(resultSet.next())
					return new Ferias (resultSet.getInt("idFerias"), resultSet.getString("nome"), resultSet.getDate("inicio"), resultSet.getDate("termino"));

				// Se nada for achado, retorna nulo
				return null;
			} catch (SQLException e) {
				throw new RuntimeException("Um erro ocorreu ao obter as férías.");
			}
		}

		public static ArrayList<Ferias> getAllByFuncionario(int funcionarioID) {
			try{
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um prepared statement
				PreparedStatement statement = (PreparedStatement) connect
						.prepareStatement("SELECT * FROM FuncionarioTodasFerias WHERE idFuncionario = ?", Statement.RETURN_GENERATED_KEYS);

				// Realiza o bind dos valores
				statement.setInt(1, funcionarioID);

				// Executa o SQL
				ResultSet resultSet = statement.executeQuery();

				ArrayList<Ferias> ferias = new ArrayList<Ferias>();
				// Obtem o primeiro resultado e o retorna
				while(resultSet.next())
					ferias.add(new Ferias (resultSet.getInt("idFerias"), resultSet.getString("nome"), resultSet.getDate("inicio"), resultSet.getDate("termino")));

				// Se nada for achado, retorna nulo
				return ferias;
			} catch (SQLException e) {
				throw new RuntimeException("Um erro ocorreu ao obter as férías.");
			}
		}
}
