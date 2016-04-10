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

public class Empresa {
	
	private int idEmpresa;
	private String nome;
	private String razaoSocial;
	private String CNPJ;
	private String rua;
	private int numero;
	private String bairro;
	private String cidade;
	private String estado;
	private File profileImage;
	
	// GETTERS and SETTERS
	public int getIdEmpresa() {
		return idEmpresa;
	}
	
	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		if(nome == null || nome.equals(""))
			throw new RuntimeException("O nome da empresa não pode estar vazio.");
		this.nome = nome;
	}
	
	public String getRazaoSocial() {
		return razaoSocial;
	}
	
	public void setRazaoSocial(String razaoSocial) {
		if(razaoSocial == null || razaoSocial.equals(""))
			throw new RuntimeException("A Razao Social da empresa não pode estar vazio.");
		this.razaoSocial = razaoSocial;
	}
	
	public String getCNPJ() {
		return CNPJ;
	}
	
	public void setCNPJ(String CNPJ) {
		if(CNPJ == null || CNPJ.equals(""))
			throw new RuntimeException("O CNPJ da empresa não pode estar vazio.");
		this.CNPJ = CNPJ;
	}
	
	public String getRua() {
		return rua;
	}
	
	public void setRua(String rua) {
		if(rua == null || rua.equals(""))
			throw new RuntimeException("A rua da empresa não pode estar vazio.");
		this.rua = rua;
	}
	
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(int numero) {
		if(numero < 0)
			throw new RuntimeException("O número precisa ser maior que zero.");
		this.numero = numero;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		if(bairro == null || bairro.equals(""))
			throw new RuntimeException("O bairro da empresa não pode estar vazio.");
		this.bairro = bairro;
	}
	
	public String getCidade() {
		return cidade;
	}
	
	public void setCidade(String cidade) {
		if(cidade == null || cidade.equals(""))
			throw new RuntimeException("A cidade da empresa não pode estar vazio.");
		this.cidade = cidade;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		if(estado == null || estado.equals(""))
			throw new RuntimeException("O estado da empresa não pode estar vazio.");
		this.estado = estado;
	}
	
	public File getProfileImage() {
		return profileImage;
	}

	public void setImageURL(File profileImage) {
		if(profileImage == null)
			throw new RuntimeException("URL inválida");
		this.profileImage = profileImage;
	}
	
	// CONSTRUCTOR
	protected Empresa(Builder build) {
		this.idEmpresa 		= build.idEmpresa;
		this.nome 			= build.nome;
		this.razaoSocial 	= build.razaoSocial;
		this.CNPJ 			= build.CNPJ;
		this.rua 			= build.rua;
		this.numero 		= build.numero;
		this.bairro 		= build.bairro;
		this.cidade 		= build.cidade;
		this.estado 		= build.estado;
	}
	
	// BUILDER
	public static class Builder {
		
		private int idEmpresa;
		private String nome;
		private String razaoSocial;
		private String CNPJ;
		private String rua;
		private int numero;
		private String bairro;
		private String cidade;
		private String estado;
		
		public Builder setIdEmpresa(int idEmpresa) {
			this.idEmpresa = idEmpresa;
			return this;
		}
		
		public Builder setNome(String nome) {
			this.nome = nome;
			return this;
		}
		
		public Builder setRazaoSocial(String razaoSocial) {
			this.razaoSocial = razaoSocial;
			return this;
		}
		
		public Builder setCNPJ(String cNPJ) {
			CNPJ = cNPJ;
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
		
		public Empresa build() {
			return new Empresa(this);
		}
	}
	
	public boolean create() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO Empresa (nome, razaoSocial, cnpj, rua, numero, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setString(2, this.razaoSocial);
			statement.setString(3, this.CNPJ);
			statement.setString(4, this.rua);
			statement.setInt(5, this.numero);
			statement.setString(6, this.bairro);
			statement.setString(7, this.cidade);
			statement.setString(8, this.estado);

			// Executa o SQL
			int ret = statement.executeUpdate();

			// Retorna resultado
			if (ret == 1) {
				// Define o id a classe
				ResultSet id = statement.getGeneratedKeys();
				while (id.next())
					setIdEmpresa(id.getInt(1));

				// Encerra conexao
				connect.close();
				
				// Salva a imagem
				if(profileImage != null) {
					Image.copyImage(profileImage, Image.EMPRESA_IMAGE_PATH+this.idEmpresa);
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
	
	public boolean update() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"UPDATE Empresa SET nome = ?, razaoSocial = ?, cnpj = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ? WHERE idEmpresa = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.nome);
			statement.setString(2, this.razaoSocial);
			statement.setString(3, this.CNPJ);
			statement.setString(4, this.rua);
			statement.setInt(5, this.numero);
			statement.setString(6, this.bairro);
			statement.setString(7, this.cidade);
			statement.setString(8, this.estado);
			statement.setInt(9, this.idEmpresa);
			
			// Executa o SQL
			int ret = statement.executeUpdate();

			// Encerra conexao
			connect.close();

			// Salva a imagem
			if (profileImage != null) {
				Image.copyImage(profileImage, "img/empresa/" + this.idEmpresa);
			}

			// Retorna resultado
			if (ret == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Um erro ocorreu ao atualizar a Empresa");
		}
	}
	
	public static ArrayList<Empresa> getAll() {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Empresa");

			ArrayList<Empresa> empresa = new ArrayList<>();
			while (resultSet.next()) {
				// Cria um funcionario com os dados do BD
				Empresa e = new Empresa.Builder()
						.setIdEmpresa(resultSet.getInt("idEmpresa"))
						.setNome(resultSet.getString("nome"))
						.setRazaoSocial(resultSet.getString("razaoSocial"))
						.setCNPJ(resultSet.getString("cnpj"))
						.setRua(resultSet.getString("rua"))
						.setNumero(resultSet.getInt("numero"))
						.setBairro(resultSet.getString("bairro"))
						.setCidade(resultSet.getString("cidade"))
						.setEstado(resultSet.getString("estado"))
						.build();

				// Obtem a imagem do perfil
				e.setImageURL(new File(Image.EMPRESA_IMAGE_PATH + e.getIdEmpresa() + Image.EMPRESA_IMAGE_EXTENSION));
				
				// Adiciona o funcionario ao retorno
				empresa.add(e);
			}

			// Retorna os funcionarios
			return empresa;
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu");
		}
	}
}
