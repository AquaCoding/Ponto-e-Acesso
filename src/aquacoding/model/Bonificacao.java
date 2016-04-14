package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import aquacoding.utils.DatabaseConnect;

public class Bonificacao {

	private int id;
	private String nome;
	private float valor;
	private Funcionario f;

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

	public Funcionario getF() {
		return f;
	}

	public void setF(Funcionario f) {
		if(f == null)
			throw new RuntimeException("O funcion�rio n�o pode ser nulo.");
		this.f = f;
	}

	// CONSTRUCTOR
	public Bonificacao(String nome, float valor, Funcionario f) {
		this.nome = nome;
		this.valor = valor;
		this.f = f;
	}

	public Bonificacao(int id,String nome, float valor, Funcionario f) {
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.f = f;
	}

	// Cria uma bonifica��o
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Bonificacao (nome, valor, idFuncionario) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setFloat(2, this.valor);
			statement.setInt(3, this.f.getId());

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
			throw new RuntimeException("Um erro ocorreu ao criar a bonifica��o");
		}
	}

	public boolean update() {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"UPDATE Bonificacao SET nome = ?, valor = ? WHERE idFuncionario = ? AND idBonificacao = ?");


			// Realiza o bind dos valores
			statement.setString(1, nome);
			statement.setFloat(2, valor);
			statement.setInt(3, f.getId());
			statement.setInt(4, id);

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
			throw new RuntimeException("Um erro ocorreu ao atualizar a Bonifica��o.");
		}
	}

	public static ArrayList<Bonificacao> getAllByFuncionario(Funcionario f) {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM Bonificacao WHERE idFuncionario = ?");

			// Realiza o bind dos valores
			statement.setInt(1, f.getId());

			// Executa o SQL
			ResultSet rs = statement.executeQuery();

			// Armazena as bonifica��es
			ArrayList<Bonificacao> bonificacoes = new ArrayList<Bonificacao>();
			while(rs.next()) {
				bonificacoes.add(new Bonificacao(rs.getInt("idBonificacao"), rs.getString("nome"), rs.getFloat("valor"), Funcionario.getByID(rs.getInt("idFuncionario"))));
			}

			// Encerra conex�o
			connect.close();

			// Retorna bonifica��es
			return bonificacoes;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao buscar bonifica��es");
		}
	}
}
