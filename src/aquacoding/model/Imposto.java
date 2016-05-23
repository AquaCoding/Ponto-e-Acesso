package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.DatabaseConnect;

public class Imposto {

	// Atributos
	private int id;
	private String nome;
	private double valor;

	// GETTERS e SETTERS
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

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	// CONSTRUTOR
	public Imposto(int id, String nome, double valor) {
		setId(id);
		setNome(nome);
		setValor(valor);
	}

	public Imposto(String nome, double valor) {
		setNome(nome);
		setValor(valor);
	}

	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"INSERT INTO Imposto (nome, valor) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setDouble(2, this.valor);;

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

				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.IMPOSTO, this.id, ActionsCode.CADASTROU);

				return true;
			} else {
				// Encerra conexao
				connect.close();
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao criar o imposto.");
		}
	}

	public static ArrayList<Imposto> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Imposto");

			ArrayList<Imposto> impostos = new ArrayList<Imposto>();
			while (resultSet.next()) {
				// Adiciona o imposto ao retorno
				impostos.add(new Imposto(resultSet.getInt("idImposto"), resultSet.getString("nome"), resultSet.getDouble("valor")));
			}
			
			connect.close();

			// Retorna os clientes
			return impostos;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu obter os impostos.");
		}
	}

	public boolean delete() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("DELETE FROM Imposto WHERE idImposto = ?");

			// Realiza o bind dos valores
			statement.setInt(1, this.id);

			// Executa o SQL
			int resp = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			if(resp == 1) {
				// Gera log
			Logs.makeLog(Main.loggedUser.getId(), ObjectCode.IMPOSTO, this.id, ActionsCode.REMOVEU);

				return true;
			}else {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao deletar o imposto");
		}
	}

	public boolean update() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"UPDATE Imposto SET nome = ?, valor = ? WHERE idImposto = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setDouble(2, this.valor);
			statement.setInt(3, this.id);

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			// Retorna resultado
			if (ret == 1) {
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.IMPOSTO, this.id, ActionsCode.EDITOU);

				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Um erro ocorreu ao atualizar o imposto");
		}
	}
}
