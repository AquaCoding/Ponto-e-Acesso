package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import aquacoding.utils.DatabaseConnect;

public class Usuario {
	private int id;
	private String nome;
	private String senha;

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
			throw new RuntimeException("O nome da usuário não pode ser vazio.");
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		if (!senha.matches("[\\S ]{8,}"))
			throw new RuntimeException("O valor de senha é inválido");

		if(senha.equals(""))
			throw new RuntimeException("O nome da usuário não pode ser vazio.");

		this.senha = senha;
	}

	// CONSTRUTORES
	public Usuario(int id,String nome, String senha) {
		setId(id);
		setNome(nome);
		setSenha(senha);
	}

	public Usuario(String nome, String senha) {
		setNome(nome);
		setSenha(senha);
	}

	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Usuario (nome, senha) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setString(2, this.senha);

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Retorna resultado
			if (ret == 1) {
				//Define o id a classe
				ResultSet id = statement.getGeneratedKeys();
				while(id.next())
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
			throw new RuntimeException("Um erro ocorreu ao criar a usuário");
		}
	}

	}



