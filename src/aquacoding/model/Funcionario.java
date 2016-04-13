package aquacoding.model;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import aquacoding.utils.DatabaseConnect;
import aquacoding.utils.Image;

public class Funcionario {

	private int id;
	private String nome;
	private String sobrenome;
	private String rg;
	private String cpf;
	private String ctps;
	private String telefone;
	private String rua;
	private int numero;
	private String bairro;
	private String cidade;
	private String estado;
	private double salarioHoras;
	private File profileImage;
	private ArrayList<Horario> horario = new ArrayList<Horario>();

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
		if (nome == null || nome.equals(""))
			throw new RuntimeException("O nome do funcionário não pode estar vazio.");
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		if (sobrenome == null || sobrenome.equals(""))
			throw new RuntimeException("O sobrenome do funcionário não pode estar vazio.");
		this.sobrenome = sobrenome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		if (rg == null || rg.equals(""))
			throw new RuntimeException("O RG do funcionário não pode estar vazio.");
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		if (cpf == null || cpf.equals(""))
			throw new RuntimeException("O CPF do funcionário não pode estar vazio.");
		this.cpf = cpf;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		if (ctps == null || ctps.equals(""))
			throw new RuntimeException("O CTPS do funcionário não pode estar vazio.");
		this.ctps = ctps;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		if (telefone == null || telefone.equals(""))
			throw new RuntimeException("O telefone do funcionário não pode estar vazio.");
		this.telefone = telefone;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		if (rua == null || rua.equals(""))
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
		if (bairro == null || bairro.equals(""))
			throw new RuntimeException("O bairro do endereço do funcionário não pode estar vazio.");
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		if (cidade == null || cidade.equals(""))
			throw new RuntimeException("A cidade do endereço do funcionário não pode estar vazio.");
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		if (estado == null || estado.equals(""))
			throw new RuntimeException("O estado do endereço do funcionário não pode estar vazio.");
		this.estado = estado;
	}

	public double getSalarioHoras() {
		return salarioHoras;
	}

	public void setSalarioHoras(double salarioHoras) {
		if (salarioHoras <= 0)
			throw new RuntimeException("O sálario por hora do funcionário precisa ser maior que 0.");
		this.salarioHoras = salarioHoras;
	}

	public File getProfileImage() {
		return profileImage;
	}

	public void setImageURL(File profileImage) {
		if (profileImage == null)
			throw new RuntimeException("URL inválida");
		this.profileImage = profileImage;
	}

	public ArrayList<Horario> getHorario() {
	     return horario;
	}

	public void setHorario(Horario horario) {
		this.horario.clear();
		this.horario.add(horario);

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
		setEstado(builder.estado);
		setSalarioHoras(builder.salarioHoras);
		this.horario = builder.horario;



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
		private String telefone;
		private String rua;
		private int numero;
		private String bairro;
		private String cidade;
		private String estado;
		private double salarioHoras;


		private ArrayList<Horario> horario = new ArrayList<Horario>();

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

		public Builder setTelefone(String telefone) {
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

		public Builder setEstado(String estado) {
			this.estado = estado;
			return this;
		}

		public Builder setSalarioHoras(double salarioHoras) {
			this.salarioHoras = salarioHoras;
			return this;
		}

		public Builder setHorario(Horario horario) {
			this.horario.add(horario);
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
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"INSERT INTO Funcionario (nome, sobrenome, rg, cpf, ctps, telefone, rua, numero, bairro, cidade, estado, salarioHoras) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setString(2, this.sobrenome);
			statement.setString(3, this.rg);
			statement.setString(4, this.cpf);
			statement.setString(5, this.ctps);
			statement.setString(6, this.telefone);
			statement.setString(7, this.rua);
			statement.setInt(8, this.numero);
			statement.setString(9, this.bairro);
			statement.setString(10, this.cidade);
			statement.setString(11, this.estado);
			statement.setDouble(12, this.salarioHoras);

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Retorna resultado
			if (ret == 1) {
				// Define o id a classe
				ResultSet id = statement.getGeneratedKeys();
				while (id.next())
					setId(id.getInt(1));

				// Percorre cada um dos horarios
				for(int i = 0; i < horario.size(); i++) {
					// Cria um prepared statement
					PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement("INSERT INTO HorarioFuncionario (idHorario, idFuncionario) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

					// Define os binds
					statement2.setInt(1, horario.get(i).getId());
					statement2.setInt(2, this.id);

					// Executa o SQL
					statement2.executeUpdate();
				}

				// Encerra conexao
				connect.close();

				// Salva a imagem
				if (profileImage != null) {
					Image.copyImage(profileImage, Image.PROFILE_IMAGE_PATH + this.id);
				}

				return true;
			} else {
				// Encerra conexao
				connect.close();
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
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
				Funcionario f = new Funcionario.Builder().setId(resultSet.getInt("idFuncionario"))
						.setNome(resultSet.getString("nome")).setSobrenome(resultSet.getString("sobrenome"))
						.setRg(resultSet.getString("rg")).setCpf(resultSet.getString("cpf"))
						.setCtps(resultSet.getString("ctps")).setTelefone(resultSet.getString("telefone"))
						.setRua(resultSet.getString("rua")).setNumero(resultSet.getInt("numero"))
						.setBairro(resultSet.getString("bairro")).setCidade(resultSet.getString("cidade"))
						.setEstado(resultSet.getString("estado")).setSalarioHoras(resultSet.getDouble("salarioHoras"))
						.build();

				// Obtem os horarios desse funcionario
				PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement("SELECT * FROM HorarioFuncionario WHERE idFuncionario = ?");
				statement2.setInt(1, f.getId());

				// Executa o SQL
				ResultSet resultSet2 = statement2.executeQuery();

				// Se exitir horario, adiciona ao funcionario
				while (resultSet2.next())
					f.setHorario(Horario.getByID(resultSet2.getInt("idHorario")));


				// Obtem a imagem do perfil
				f.setImageURL(new File(Image.PROFILE_IMAGE_PATH + f.getId() + Image.PROFILE_IMAGE_EXTENSION));

				// Adiciona o funcionario ao retorno
				funcionarios.add(f);
			}

			// Retorna os funcionarios
			return funcionarios;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}

	public static ArrayList<Funcionario> getAll(String filter) {
		try {
			// Define o filtro pra buscar no meio das strings
			filter = "%" + filter + "%";

			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement("SELECT * FROM Funcionario WHERE nome LIKE ? OR sobrenome LIKE ? OR cpf LIKE ?");
			statement.setString(1, filter);
			statement.setString(2, filter);
			statement.setString(3, filter);

			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();
			//ResultSet resultSet = statement.executeQuery("SELECT * FROM Funcionario WHERE nome = ? OR cpf = ?");

			ArrayList<Funcionario> funcionarios = new ArrayList<>();
			while (resultSet.next()) {
				// Cria um funcionario com os dados do BD
				Funcionario f = new Funcionario.Builder().setId(resultSet.getInt("idFuncionario"))
						.setNome(resultSet.getString("nome")).setSobrenome(resultSet.getString("sobrenome"))
						.setRg(resultSet.getString("rg")).setCpf(resultSet.getString("cpf"))
						.setCtps(resultSet.getString("ctps")).setTelefone(resultSet.getString("telefone"))
						.setRua(resultSet.getString("rua")).setNumero(resultSet.getInt("numero"))
						.setBairro(resultSet.getString("bairro")).setCidade(resultSet.getString("cidade"))
						.setEstado(resultSet.getString("estado")).setSalarioHoras(resultSet.getDouble("salarioHoras"))
						.build();

				// Obtem os horarios desse funcionario
				PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement("SELECT * FROM HorarioFuncionario WHERE idFuncionario = ?");
				statement2.setInt(1, f.getId());

				// Executa o SQL
				ResultSet resultSet2 = statement2.executeQuery();

				// Se exitir horario, adiciona ao funcionario
				while (resultSet2.next())
					f.setHorario(Horario.getByID(resultSet2.getInt("idHorario")));

				// Obtem a imagem do perfil
				f.setImageURL(new File(Image.PROFILE_IMAGE_PATH + f.getId() + Image.PROFILE_IMAGE_EXTENSION));

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
					"UPDATE Funcionario SET nome = ?, sobrenome = ?, rg = ?, cpf = ?, ctps = ?, telefone = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, salarioHoras = ? WHERE idFuncionario = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setString(2, this.sobrenome);
			statement.setString(3, this.rg);
			statement.setString(4, this.cpf);
			statement.setString(5, this.ctps);
			statement.setString(6, this.telefone);
			statement.setString(7, this.rua);
			statement.setInt(8, this.numero);
			statement.setString(9, this.bairro);
			statement.setString(10, this.cidade);
			statement.setString(11, this.estado);
			statement.setDouble(12, this.salarioHoras);
			statement.setDouble(13, this.id);


			// Executa o SQL
			int ret = statement.executeUpdate();

			if(horario.size() == 1){
				// Cria um prepared statement
				PreparedStatement statement2 = (PreparedStatement) connect
						.prepareStatement("DELETE FROM horariofuncionario WHERE idHorario = ? AND idFuncionario = ?");
				statement2.setInt(1, this.horario.get(0).getId());
				statement2.setInt(2, this.id);

				// Executa o SQL
				statement2.executeUpdate();
			}


			// Encerra conexao
			connect.close();

			// Salva a imagem
			if (profileImage != null) {
				Image.copyImage(profileImage, "img/profile/" + this.id);
			}

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

			if (resp == 1) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao deletar o Funcionario");
		}
	}

	public static Funcionario getByID(int id) {
		try{
			// Obtem uma conex�o com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM Setor WHERE idSetor = ?", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setInt(1, id);

			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();

			// Obtem o primeiro resultado e o retorna
			if(resultSet.next())
				return new Funcionario.Builder().setId(resultSet.getInt("idFuncionario"))
						.setNome(resultSet.getString("nome")).setSobrenome(resultSet.getString("sobrenome"))
						.setRg(resultSet.getString("rg")).setCpf(resultSet.getString("cpf"))
						.setCtps(resultSet.getString("ctps")).setTelefone(resultSet.getString("telefone"))
						.setRua(resultSet.getString("rua")).setNumero(resultSet.getInt("numero"))
						.setBairro(resultSet.getString("bairro")).setCidade(resultSet.getString("cidade"))
						.setEstado(resultSet.getString("estado")).setSalarioHoras(resultSet.getDouble("salarioHoras"))
						.build();

			// Se nada for achado, retorna nulo
			return null;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}

}
