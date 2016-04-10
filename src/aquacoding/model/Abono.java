package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import aquacoding.utils.DatabaseConnect;

public class Abono {

	private int id;
	private java.sql.Date dataFalta;
	private String descricao;
	private Funcionario funcionario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public java.sql.Date getDataFalta() {
		return dataFalta;
	}

	public void setDataFalta(java.sql.Date dataFalta) {
		this.dataFalta = dataFalta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	// CONSTRUTORES
	public Abono(int id, java.sql.Date dataFalta, String descricao, Funcionario funcionario) {
		setId(id);
		setDataFalta(dataFalta);
		setDescricao(descricao);
	}

	public Abono(java.sql.Date dataFalta, String descricao, Funcionario funcionario) {
		setDataFalta(dataFalta);
		setDescricao(descricao);
	}

	// Cria um novo abono no banco de dados
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"INSERT INTO Abono (dataFalta, descricao, idFuncionario) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setDate(1, this.dataFalta);
			statement.setString(2, this.descricao);
			statement.setInt(3, this.funcionario.getId());
			
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
			throw new RuntimeException("Um erro ocorreu ao criar o abono");
		}
	}

	// Retorna todas os abono
	public static ArrayList<Abono> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Abono");

			ArrayList<Abono> abono = new ArrayList<Abono>();
			while (resultSet.next()) {
				// Cria um abono com os dados do BD
				java.sql.Date data = resultSet.getDate("dataFalta");

				Abono a = new Abono(resultSet.getInt("idAbono"), data, resultSet.getString("descricao"), Funcionario.getByID(resultSet.getInt("idSetor")));

				// Adiciona o abono ao retorno
				abono.add(a);
			}

			// Retorna as ferias
			return abono;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}

}
