package aquacoding.model;

public class Holerite {

	private double salario;
	private double bonificacaoo;
	private double descontos;
	private double liquido;
	private double bruto;
	private String mes;
	private Funcionario funcionario;

	//Gets e Sets
	public double getSalario() {
		return salario;
	}
	public void setSalario(double salario) {
		this.salario = salario;
	}
	public double getBonificacaoo() {
		return bonificacaoo;
	}
	public void setBonificacaoo(double bonificacaoo) {
		this.bonificacaoo = bonificacaoo;
	}
	public double getDescontos() {
		return descontos;
	}
	public void setDescontos(double descontos) {
		this.descontos = descontos;
	}
	public double getLiquido() {
		return liquido;
	}
	public void setLiquido(double liquido) {
		this.liquido = liquido;
	}
	public double getBruto() {
		return bruto;
	}
	public void setBruto(double bruto) {
		this.bruto = bruto;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	//Construtor
	public Holerite(double salario, double bonificacaoo, double descontos, double liquido, double bruto, String mes,
			Funcionario funcionario) {
		this.salario = salario;
		this.bonificacaoo = bonificacaoo;
		this.descontos = descontos;
		this.liquido = liquido;
		this.bruto = bruto;
		this.mes = mes;
		this.funcionario = funcionario;
	}





}
