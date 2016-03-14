package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import aquacoding.utils.DatabaseConnect;

public class Funcao {
	private int id;
	private String nome;
	private Setor setor;
	
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
			throw new RuntimeException("O nome da função não pode ser vazio.");
		this.nome = nome;
	}
	
	public Setor getSetor() {
		return setor;
	}
	
	public void setSetor(Setor setor) {
		if(setor == null)
			throw new RuntimeException("Uma função precisa ter um setor.");
		this.setor = setor;
	}
	
	// CONSTRUTORES
	public Funcao(int id, String nome, Setor setor) {
		setId(id);
		setNome(nome);
		setSetor(setor);
	}
	
	public Funcao(String nome, Setor setor) {
		setNome(nome);
		setSetor(setor);
	}
	
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Funcao (nome, idSetor) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setInt(2, this.setor.getId());

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
			throw new RuntimeException("Um erro ocorreu ao criar a função");
		}
	}
	
	// Retorna todas as funções cadastradas
	public static ArrayList<Funcao> getAll() {
		try{
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();
			
			// Cria um statement
			Statement statement = connect.createStatement();
			
			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Funcao");
			
			ArrayList<Funcao> funcoes = new ArrayList<Funcao>();
			while(resultSet.next()) {
				// Cria um cliente com os dados do BD
				Funcao c = new Funcao(resultSet.getInt("idSetor"), resultSet.getString("nome"), Setor.getByID(resultSet.getInt("idSetor")));
				
				// Adiciona o cliente ao retorno
				funcoes.add(c);
			}
			
			// Retorna os clientes
			return funcoes;
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
					.prepareStatement("DELETE FROM Funcao WHERE idFuncao = ?");

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
			throw new RuntimeException("Um erro ocorreu ao deletar o Funcao");
		}
	}
}
