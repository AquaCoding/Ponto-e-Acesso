package aquacoding.model;

import java.sql.Connection;

import aquacoding.pontoacesso.Main;
import aquacoding.utils.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;
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
			throw new RuntimeException("O nome do usuário não pode estar vazio.");
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		if (!senha.matches("[\\S ]{6,}"))
			throw new RuntimeException("O valor de senha é inválido.");

		if(senha.equals(""))
			throw new RuntimeException("A senha não pode ser vazia.");

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
			// Realiza o hash da senha
			this.senha = BCrypt.hashpw(this.senha, BCrypt.gensalt(12));

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
				
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.USUARIO, this.id, ActionsCode.CADASTROU);
				
				return true;
			} else {
				// Encerra conexão
				connect.close();
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao criar o usuário");
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
			throw new RuntimeException("Um erro ocorreu ao obter os usuários.");
		}
	}

	public static Usuario getByID(int id) {
		try{
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM Usuario WHERE idUsuario = ?", Statement.RETURN_GENERATED_KEYS);

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
			throw new RuntimeException("Um erro ocorreu ao obter o usuário.");
		}
	}

	public static boolean isValidSenha(String nome, String senha) {
		try {
			Usuario u = Usuario.getByNome(nome);
			boolean result = BCrypt.checkpw(senha, u.getSenha());

			if(result) {
				Logs.makeLog(u.getId(), ObjectCode.USUARIO, u.getId(), ActionsCode.VALID_LOGIN);
				Main.loggedUser = u;
			} else {
				Logs.makeLog(u.getId(), ObjectCode.USUARIO, u.getId(), ActionsCode.INVALID_LOGIN);
			}
			return result;
		} catch (RuntimeException e) {
			return false;
		}
	}

	public boolean update() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Realiza o hash da senha se necessario
			Usuario c = Usuario.getByID(this.id);
			if(!c.getSenha().equals(this.senha)) {
			this.senha = BCrypt.hashpw(this.senha, BCrypt.gensalt(12));
			}

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
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.USUARIO, this.id, ActionsCode.EDITOU);
				
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Um erro ocorreu ao atualizar o usuário.");
		}
	}

	public boolean delete() {
		try {
			// Obtem uma conexão com o banco de dados
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
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.USUARIO, this.id, ActionsCode.REMOVEU);
				
				return true;
			}else {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao deletar o usuário.");
		}
	}

	// Obtem um usuario pelo nome
		public static Usuario getByNome(String nome) {
			// Result set get the result of the SQL query
			try {
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um prepared statement
				PreparedStatement statement = (PreparedStatement) connect
						.prepareStatement("SELECT idUsuario, nome, senha FROM Usuario WHERE nome = ?");

				// Realiza o bind dos valores
				statement.setString(1, nome);

				// Executa o SQL
				ResultSet resultSet = statement.executeQuery();

				// Percorre pelo resultado
				if (resultSet.next()) {
					Usuario u = new Usuario(resultSet.getInt("idUsuario"),
							resultSet.getString("nome"),
							resultSet.getString("senha"));
					return u;
				} else {
					throw new RuntimeException("O usuário não existe");
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("Um erro ocorreu ao buscar o usuário.");
			}
		}
		
		// Verifica a existencia de um usuario cadastrado
		public static boolean haveUsuario() {
			if(Usuario.getAll().size() == 0)
				return false;
			
			return true;
		}

	}



