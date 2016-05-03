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

public class Bonificacao {

	private int id;
	private String nome;
	private float valor;

	// GETTER and SETTERS
	public String getNome() {
		return nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		if(nome == null || nome.equals(""))
			throw new RuntimeException("O nome da bonifica��o n�o pode estar vazio.");
		this.nome = nome;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		if(valor <= 0)
			throw new RuntimeException("O valor precisa ser positivo.");
		this.valor = valor;
	}

	// CONSTRUCTOR
	public Bonificacao(String nome, float valor) {
		this.nome = nome;
		this.valor = valor;
	}

	public Bonificacao(int id,String nome, float valor) {
		this.id = id;
		this.nome = nome;
		this.valor = valor;
	}

	// Cria uma bonifica��o
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Bonificacao (nome, valor) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setFloat(2, this.valor);;

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
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BONIFICACAO, this.id, ActionsCode.CADASTROU);
				
				return true;
			} else {
				// Encerra conexao
				connect.close();
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao criar a bonifica��o.");
		}
	}

	public boolean update() {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"UPDATE Bonificacao SET nome = ?, valor = ? WHERE idBonificacao = ?");


			// Realiza o bind dos valores
			statement.setString(1, nome);
			statement.setFloat(2, valor);
			statement.setInt(3, id);

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			// Retorna resultado
			if (ret == 1) {
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BONIFICACAO, this.id, ActionsCode.EDITOU);
				
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Um erro ocorreu ao atualizar a Bonifica��o.");
		}
	}

	public static ArrayList<Bonificacao> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM Bonificacao");

			// Executa o SQL
			ResultSet rs = statement.executeQuery();

			// Armazena as bonifica��es
			ArrayList<Bonificacao> bonificacoes = new ArrayList<Bonificacao>();
			while(rs.next()) {
				bonificacoes.add(new Bonificacao(rs.getInt("idBonificacao"), rs.getString("nome"), rs.getFloat("valor")));
			}

			// Encerra conex�o
			connect.close();

			// Retorna bonifica��es
			return bonificacoes;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao buscar bonifica��es.");
		}
	}
	
	public static ArrayList<Bonificacao> getAllByFuncionario(Funcionario f) {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM FuncionarioBonificacao WHERE idFuncionario = ?");

			// Realiza o bind dos valores
			statement.setInt(1, f.getId());

			// Executa o SQL
			ResultSet rs = statement.executeQuery();

			// Armazena as bonifica��es
			ArrayList<Bonificacao> bonificacoes = new ArrayList<Bonificacao>();
			while(rs.next()) {
				bonificacoes.add(new Bonificacao(rs.getInt("idBonificacao"), rs.getString("nome"), rs.getFloat("valor")));
			}

			// Encerra conex�o
			connect.close();

			// Retorna bonifica��es
			return bonificacoes;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao buscar bonifica��es.");
		}
	}

	public boolean delete() {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("DELETE FROM Bonificacao WHERE idBonificacao = ?");

			// Realiza o bind dos valores
			statement.setInt(1, this.id);

			// Executa o SQL
			int resp = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			if (resp == 1) {
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BONIFICACAO, this.id, ActionsCode.REMOVEU);
				
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao deletar a bonifica��o");
		}
	}

	public static boolean linkar(Bonificacao b, Funcionario f) {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO BonificacaoFuncionario (idBonificacao, idFuncionario) VALUES (?, ?)");

			// Realiza o bind dos valores
			statement.setInt(1, b.getId());
			statement.setInt(2, f.getId());

			// Executa o SQL
			int resp = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			if (resp == 1) {
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BONIFICACAO, b.getId(), ActionsCode.LINK);
				
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao linkar a bonifica��o");
		}
	}

	public static boolean removerLink(Bonificacao b, Funcionario f) {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("DELETE FROM BonificacaoFuncionario WHERE idBonificacao = ? AND idFuncionario = ?");

			// Realiza o bind dos valores
			statement.setInt(1, b.getId());
			statement.setInt(2, f.getId());

			// Executa o SQL
			int resp = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			if (resp == 1) {
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BONIFICACAO, b.getId(), ActionsCode.REMOVEU_LINK);
				
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao linkar a bonifica��o");
		}
	}
}
