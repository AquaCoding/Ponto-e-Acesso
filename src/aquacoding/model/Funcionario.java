package aquacoding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import aquacoding.utils.DatabaseConnect;

public class Funcionario {

	private int id;
	private String nome;
	private String sobrenome;
	private String rg;
	private String cpf;
	private String ctps;
	private int telefone;
	private String rua;
	private int numero;
	private String bairro;
	private String cidade;
	private double salarioTotal;
	private double salarioHoras;
	
	
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
		if (nome.equals(""))
			throw new RuntimeException("O nome do Funcionario não pode ser vazio.");
		this.nome = nome;
	}
	
	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		this.ctps = ctps;
	}

	public int getTelefone() {
		return telefone;
	}

	public void setTelefone(int telefone) {
		this.telefone = telefone;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public double getSalarioTotal() {
		return salarioTotal;
	}

	public void setSalarioTotal(double salarioTotal) {
		this.salarioTotal = salarioTotal;
	}

	public double getSalarioHoras() {
		return salarioHoras;
	}

	public void setSalarioHoras(double salarioHoras) {
		this.salarioHoras = salarioHoras;
	}

	public Funcionario(int id, String nome, String sobrenome, String rg, String cpf, String ctps, int telefone, String rua, int numero, String bairro,	String cidade, double salarioTotal, double salarioHoras) {
		setId(id);
		setNome(nome);
		setSobrenome(sobrenome);
		setRg(rg);
		setCpf(cpf);
		setCtps(ctps);
		setTelefone(telefone);
		setRua(rua);
		setNumero(numero);
		setBairro(bairro);
		setCidade(cidade);
		setSalarioTotal(salarioTotal);
		setSalarioHoras(salarioHoras);		
	}

	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Funcionario (nome, sobrenome, rg, cpf, ctps, telefone, rua, numero, bairro, cidade, salarioTotal, salarioHoras) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setString(1, this.sobrenome);
			statement.setString(1, this.rg);
			statement.setString(1, this.cpf);
			statement.setString(1, this.ctps);
			statement.setInt(1, this.telefone);
			statement.setString(1, this.rua);
			statement.setInt(1, this.numero);
			statement.setString(1, this.bairro);
			statement.setString(1, this.cidade);
			statement.setDouble(1, this.salarioTotal);
			statement.setDouble(1, this.salarioHoras);

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
			throw new RuntimeException("Um erro ocorreu ao criar o funcionario");
		}
	}

	public static ArrayList<Setor> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Funcionario");

			ArrayList<Funcionario> funcionarios = new ArrayList<Funcionario>();
			while (resultSet.next()) {
				// Cria um funcionario com os dados do BD
				Funcionario f = new Funcionario(resultSet.getInt("idFuncionario"), resultSet.getString("nome"), resultSet.getString("sobrenome"), resultSet.getString("rg"), resultSet.getString("cpf"), resultSet.getString("ctps"), resultSet.getInt("telefone"), resultSet.getInt("numero"), resultSet.getString("rua"), resultSet.getString("bairro"), resultSet.getString("cidade"), resultSet.getDouble("salarioTotal"), resultSet.getDouble("salarioHoras"));

				// Adiciona o funcionario ao retorno
				funcionarios.add(f);
			}

			// Retorna os funcionarios
			return funcionarios;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}

	public static Setor getByID(int id) {
		try{
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();
			
			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM Funcionario WHERE idFuncionario = ?", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setInt(1, id);
			statement.setString(2, nome);
			statement.setString(3, sobrenome);
			statement.setString(4, rg);
			statement.setString(5, cpf);
			statement.setString(6, ctps);
			statement.setInt(7, telefone);
			statement.setString(8, rua);
			statement.setInt(9, numero);
			statement.setString(10, bairro);
			statement.setString(11, cidade);
			statement.setDouble(12, salarioTotal);
			statement.setDouble(13, salarioHoras);
			
			
			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();
			
			// Obtem o primeiro resultado e o retorna
			if(resultSet.next())
				return new Setor(resultSet.getInt("idSetor"), resultSet.getString("nome"));
			
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
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"UPDATE Funcionario SET nome = ? WHERE idSetor = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setInt(2, this.id);

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
			throw new RuntimeException("Um erro ocorreu ao atualizar o Funcionario");
		}
	}

	public boolean delete() {
		try {
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("DELETE FROM Funcionario WHERE idFuncionario = ?");

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
			throw new RuntimeException("Um erro ocorreu ao deletar o Funcionario");
		}
	}

}
