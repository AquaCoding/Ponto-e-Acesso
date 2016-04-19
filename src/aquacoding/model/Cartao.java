package aquacoding.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import aquacoding.utils.DatabaseConnect;


public class Cartao {

	private int id;
	private String funcionarioNome;
	private String funcionarioCPF;
	private String codigo;
	private boolean ativo;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFuncionarioNome() {
		return funcionarioNome;
	}

	public void setFuncionarioNome(String funcionarioNome) {
		this.funcionarioNome = funcionarioNome;
	}

	public String getFuncionarioCPF() {
		return funcionarioCPF;
	}

	public void setFuncionarioCPF(String funcionarioCPF) {
		this.funcionarioCPF = funcionarioCPF;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Cartao(int id, String funcionarioNome, String funcionarioCPF, String codigo, boolean ativo) {
		this.id = id;
		this.funcionarioNome = funcionarioNome;
		this.funcionarioCPF = funcionarioCPF;
		this.codigo = codigo;
		this.ativo = ativo;
	}
	
	public static ArrayList<Cartao> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM ShowTags");

			ArrayList<Cartao> cartoes = new ArrayList<>();
			while (resultSet.next()) {				
				// Adiciona o cartão ao retorno
				cartoes.add(new Cartao(resultSet.getInt("idFuncionarioTag"), resultSet.getString("nome"), resultSet.getString("cpf"), resultSet.getString("codigo"), resultSet.getBoolean("ativo")));
			}

			// Retorna os cartões
			return cartoes;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao obter os cartões.");
		}
	}
}
