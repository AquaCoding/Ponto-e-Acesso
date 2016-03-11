package aquacoding.model;

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
	private String estado;
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
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		if(estado == null || estado.equals(""))
			throw new RuntimeException("O estado do endereço do funcionário não pode estar vazio.");
		this.estado = estado;
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
		setEstado(builder.estado);
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
		private String estado;
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
		
		public Builder setEstado(String estado) {
			this.estado = estado;
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
}
