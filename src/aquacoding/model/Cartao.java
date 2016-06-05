package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.DatabaseConnect;
import javafx.scene.control.Alert.AlertType;
import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;

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
	
	public static boolean create(Funcionario func, String code) {
		try {
			if(Cartao.getAll(code).size() > 0)
				throw new RuntimeException("Esse cartão já foi cadastrado");
			
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"INSERT INTO FuncionarioTag (codigo, ativo, idFuncionario) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, code);
			statement.setInt(2, 1);
			statement.setInt(3, func.getId());
			
			// Executa o SQL
			int ret = statement.executeUpdate();
			
			if(ret == 1) {
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.CARTAO, 0, ActionsCode.CADASTROU);
				return true;
			}
				
			
			return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao registrar ponto");
		}
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
				cartoes.add(new Cartao(resultSet.getInt("idFuncionarioTag"), resultSet.getString("nome"),
						resultSet.getString("cpf"), resultSet.getString("codigo"), resultSet.getBoolean("ativo")));
			}

			// Retorna os cartões
			return cartoes;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao obter os cartões.");
		}
	}
	
	public static ArrayList<Cartao> getAll(String codigo) {
		try {			
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Executa um SQL
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement("SELECT * FROM ShowTags WHERE codigo = ?");

			statement.setString(1, codigo);

			ResultSet resultSet = statement.executeQuery();

			ArrayList<Cartao> cartoes = new ArrayList<>();
			while (resultSet.next()) {
				// Adiciona o cartão ao retorno
				cartoes.add(new Cartao(resultSet.getInt("idFuncionarioTag"), resultSet.getString("nome"),
						resultSet.getString("cpf"), resultSet.getString("codigo"), resultSet.getBoolean("ativo")));
			}

			// Retorna os cartões
			return cartoes;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao obter os cartões.");
		}
	}

	public static void revogarpermitir(int idFuncionario) {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Executa um SQL
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"SELECT * FROM FuncionarioTag WHERE idFuncionario = ?", Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, idFuncionario);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (resultSet.getBoolean("ativo") == true) {
					PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement(
							"UPDATE FuncionarioTag SET ativo = ? WHERE idFuncionario = ?");

					statement2.setBoolean(1, false);
					statement2.setInt(2, idFuncionario);
					
					statement2.executeUpdate();
					CustomAlert.showAlert("Cartão", "Cartão revogado com sucesso.", AlertType.WARNING);
					
					// Gera log
					Logs.makeLog(Main.loggedUser.getId(), ObjectCode.CARTAO, 0, ActionsCode.REVOGOU);
				} else {
					PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement(
							"UPDATE FuncionarioTag SET ativo = ? WHERE idFuncionario = ?");

					statement2.setBoolean(1, true);
					statement2.setInt(2, idFuncionario);
					
					statement2.executeUpdate();
					CustomAlert.showAlert("Cartão", "Cartão permitido com sucesso.", AlertType.WARNING);
					
					// Gera log
					Logs.makeLog(Main.loggedUser.getId(), ObjectCode.CARTAO, 0, ActionsCode.PERMITIU);
				}
			}
			;

			// encerra conexão
			connect.close();
		} catch (Exception e) {

		}
	}

	public static ArrayList<Cartao> getAll(Funcionario funcionario) {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();
			
			// Executa um SQL
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"SELECT * FROM FuncionarioTag WHERE idFuncionario = ?");

			statement.setInt(1, funcionario.getId());

			ResultSet resultSet = statement.executeQuery();

			ArrayList<Cartao> cartoes = new ArrayList<>();
			while (resultSet.next()) {
				// Adiciona o cartão ao retorno
				cartoes.add(new Cartao(resultSet.getInt("idFuncionarioTag"), funcionario.getNome() + " " + funcionario.getSobrenome(),
						funcionario.getCpf(), resultSet.getString("codigo"), resultSet.getBoolean("ativo")));
			}

			// Retorna os cartões
			return cartoes;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao obter os cartões.");
		}
	}
}
