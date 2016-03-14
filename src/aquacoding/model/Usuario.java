package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

	public static ArrayList<Usuario> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Usuario");

			ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
			while (resultSet.next()) {
				// Cria um cliente com os dados do BD
				Usuario u = new Usuario(resultSet.getInt("idUsuario"), resultSet.getString("nome"),resultSet.getString("senha"));

				// Adiciona o cliente ao retorno
				usuarios.add(u);
			}

			// Retorna os clientes
			return usuarios;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}

	public static Usuario getByID(int id) {
		try{
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM Setor WHERE idUsuario = ?", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setInt(1, id);

			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();

			// Obtem o primeiro resultado e o retorna
			if(resultSet.next())
				return new Usuario(resultSet.getInt("idUsuario"), resultSet.getString("nome"), resultSet.getString("senha"));

			// Se nada for achado, retorna nulo
			return null;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}

	public boolean update() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			String sql = "UPDATE Usuario SET ";
			boolean comma = false;
			ArrayList<String> binds = new ArrayList<String>();
			if(!this.nome.equals("")) {
				sql += "nome = ?";
				comma = true;
				binds.add(this.nome);
			}
			if(!this.senha.equals("")) {
				if(comma) {
					sql += ", ";
				}
				sql += "senha = ?";
				binds.add(this.senha);
			}
			sql += "WHERE idUsuario = ?";

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement(sql);

			// Realiza o bind dos valores
			for(int i = 0; i < binds.size(); i++) {
				statement.setString(i + 1, binds.get(i));
			}
			statement.setInt(binds.size() + 1, this.id);

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
			throw new RuntimeException("Um erro ocorreu ao atualizar o Usuário");
		}
	}

	public boolean delete() {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("DELETE FROM Usuario WHERE idUsuario = ?");

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
			throw new RuntimeException("Um erro ocorreu ao deletar o Usuário");
		}
	}

	}



