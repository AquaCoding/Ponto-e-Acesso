package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import aquacoding.utils.DatabaseConnect;

public class Setor {
	
	private int id;
	private String nome;
	
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
			throw new RuntimeException("O nome do setor não pode ser vazio.");
		this.nome = nome;
	}
	
	public Setor(int id, String nome) {
		setId(id);
		setNome(nome);
	}
	
	public Setor(String nome) {
		setNome(nome);
	}
	
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Setor (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);

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
			throw new RuntimeException("Um erro ocorreu ao criar o setor");
		}
	}

	public static ArrayList<Setor> getAll() {
		try{
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();
			
			// Cria um statement
			Statement statement = connect.createStatement();
			
			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Setor");
			
			ArrayList<Setor> setores = new ArrayList<Setor>();
			while(resultSet.next()) {
				// Cria um cliente com os dados do BD
				Setor c = new Setor(resultSet.getInt("idSetor"), resultSet.getString("nome"));
				
				// Adiciona o cliente ao retorno
				setores.add(c);
			}
			
			// Retorna os clientes
			return setores;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}

}
