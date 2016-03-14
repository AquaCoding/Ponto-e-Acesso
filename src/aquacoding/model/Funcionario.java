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
	
	// Setters e Getters
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
		if(nome == null || nome.equals(""))
			throw new RuntimeException("O nome do funcionário não pode estar vazio.");
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		if(sobrenome == null || sobrenome.equals(""))
			throw new RuntimeException("O sobrenome do funcionário não pode estar vazio.");
		this.sobrenome = sobrenome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		if(rg == null || rg.equals(""))
			throw new RuntimeException("O RG do funcionário não pode estar vazio.");
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		if(cpf == null || cpf.equals(""))
			throw new RuntimeException("O CPF do funcionário não pode estar vazio.");
		this.cpf = cpf;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		if(ctps == null || ctps.equals(""))
			throw new RuntimeException("O CTPS do funcionário não pode estar vazio.");
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
		if(rua == null || rua.equals(""))
			throw new RuntimeException("A rua do endereço do funcionário não pode estar vazio.");
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
		if(bairro == null || bairro.equals(""))
			throw new RuntimeException("O bairro do endereço do funcionário não pode estar vazio.");
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		if(cidade == null || cidade.equals(""))
			throw new RuntimeException("A cidade do endereço do funcionário não pode estar vazio.");
		this.cidade = cidade;
	}

	public double getSalarioTotal() {
		return salarioTotal;
	}

	public void setSalarioTotal(double salarioTotal) {
		if(salarioTotal <= 0)
			throw new RuntimeException("O sálario total do funcionário precisa ser maior que 0.");
		this.salarioTotal = salarioTotal;
	}

	public double getSalarioHoras() {
		return salarioHoras;
	}

	public void setSalarioHoras(double salarioHoras) {
		if(salarioHoras <= 0)
			throw new RuntimeException("O sálario por hora do funcionário precisa ser maior que 0.");
		this.salarioHoras = salarioHoras;
	}
	
	// Construtor de Funcionario
	// Só pode ser chamado pelo método build() da classe Builder
	protected Funcionario(Builder builder) {
		setId(builder.id);
		setNome(builder.nome);
		setSobrenome(builder.sobrenome);
		setRg(builder.rg);
		setCpf(builder.cpf);
		setCtps(builder.ctps);
		setTelefone(builder.telefone);
		setRua(builder.rua);
		setNumero(builder.numero);
		setBairro(builder.bairro);
		setCidade(builder.cidade);
		setSalarioTotal(builder.salarioTotal);
		setSalarioHoras(builder.salarioHoras);
	}

	// Builder utilizado para criar instancias de Funcionario
	public static class Builder {
		
		// Atributos
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
		
		// Sets do Builder
		public Builder setId(int id) {
			this.id = id;
			return this;
		}
		
		public Builder setNome(String nome) {
			this.nome = nome;
			return this;
		}
		
		public Builder setSobrenome(String sobrenome) {
			this.sobrenome = sobrenome;
			return this;
		}
		
		public Builder setRg(String rg) {
			this.rg = rg;
			return this;
		}
		
		public Builder setCpf(String cpf) {
			this.cpf = cpf;
			return this;
		}
		
		public Builder setCtps(String ctps) {
			this.ctps = ctps;
			return this;
		}
		
		public Builder setTelefone(int telefone) {
			this.telefone = telefone;
			return this;
		}
		
		public Builder setRua(String rua) {
			this.rua = rua;
			return this;
		}
		
		public Builder setNumero(int numero) {
			this.numero = numero;
			return this;
		}
		
		public Builder setBairro(String bairro) {
			this.bairro = bairro;
			return this;
		}
		
		public Builder setCidade(String cidade) {
			this.cidade = cidade;
			return this;
		}
				
		public Builder setSalarioTotal(double salarioTotal) {
			this.salarioTotal = salarioTotal;
			return this;
		}
		
		public Builder setSalarioHoras(double salarioHoras) {
			this.salarioHoras = salarioHoras;
			return this;
		}
		
		// Cria a instancia de Funcionario
		public Funcionario build() {
			return new Funcionario(this);
		}
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
			statement.setString(2, this.sobrenome);
			statement.setString(3, this.rg);
			statement.setString(4, this.cpf);
			statement.setString(5, this.ctps);
			statement.setInt(6, this.telefone);
			statement.setString(7, this.rua);
			statement.setInt(8, this.numero);
			statement.setString(9, this.bairro);
			statement.setString(10, this.cidade);
			statement.setDouble(11, this.salarioTotal);
			statement.setDouble(12, this.salarioHoras);
			
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
			throw new RuntimeException("Um erro ocorreu ao criar o Funcionario");
		}
	}
	
	public static ArrayList<Funcionario> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Funcionario");

			ArrayList<Funcionario> funcionarios = new ArrayList<>();
			while (resultSet.next()) {
				// Cria um funcionario com os dados do BD
				Funcionario f = new Funcionario.Builder()
						.setId(resultSet.getInt("idFuncionario"))
						.setNome(resultSet.getString("nome"))
						.setSobrenome(resultSet.getString("sobrenome"))
						.setRg(resultSet.getString("rg"))
						.setCpf(resultSet.getString("cpf"))
						.setCtps(resultSet.getString("ctps"))
						.setTelefone(resultSet.getInt("telefone"))
						.setRua(resultSet.getString("rua"))
						.setNumero(resultSet.getInt("numero"))
						.setBairro(resultSet.getString("bairro"))
						.setCidade(resultSet.getString("cidade"))
						.setSalarioTotal(resultSet.getDouble("salarioTotal"))
						.setSalarioHoras(resultSet.getDouble("salarioHoras"))
						.build();

				// Adiciona o funcionario ao retorno
				funcionarios.add(f);
			}

			// Retorna os funcionarios
			return funcionarios;
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
					"UPDATE Funcionario SET nome = ?, sobrenome = ?, rg = ?, cpf = ?, ctps = ?, telefone = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, salarioTotal = ?, salarioHoras = ? WHERE idFuncionario = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setString(2, this.sobrenome);
			statement.setString(3, this.rg);
			statement.setString(4, this.cpf);
			statement.setString(5, this.ctps);
			statement.setInt(6, this.telefone);
			statement.setString(7, this.rua);
			statement.setInt(8, this.numero);
			statement.setString(9, this.bairro);
			statement.setString(10, this.cidade);
			statement.setDouble(11, this.salarioTotal);
			statement.setDouble(12, this.salarioHoras);
			statement.setDouble(13, this.id);

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
