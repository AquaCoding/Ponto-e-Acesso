package aquacoding.model;

import java.util.ArrayList;

import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;
import aquacoding.pontoacesso.Main;

public class Holerite {

	private double salario;
	private double bonificacao;
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
	public double getBonificacao() {
		return bonificacao;
	}
	public void setBonificacao(double bonificacao) {
		this.bonificacao = bonificacao;
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

	public void gerarHolerite(){
		double desconto = 0;
		double bonifi = 0;

		if(funcionario.getBonificacoes() != null){
			for(int k = 0; k < funcionario.getBonificacoes().size() ; k++){
				float boni = funcionario.getBonificacoes().get(k).getValor();
				bonifi = bonifi + (salario * boni) /100;
			}
		}

		ArrayList<Imposto> imposts = Imposto.getAll();

		for(int i = 0; i < imposts.size() ; i++){
			double desco = imposts.get(i).getValor();
			desconto = desconto + (salario * desco) /100;
		}

		this.setBruto(salario + bonifi);
		this.setBonificacao(bonifi);
		this.setDescontos(desconto);
		this.setLiquido(bruto - desconto);

		System.out.println("########################");
		System.out.println(funcionario.getNome() + " " + funcionario.getSobrenome());
		System.out.println("Salario: " + this.getSalario());
		System.out.println("Mes: " + this.getMes());
		System.out.println("Bonifica��o: " + this.getBonificacao());
		System.out.println("Descontos: " + this.getDescontos());
		System.out.println("Liquido: " + this.getLiquido());
		System.out.println("Bruto: " + this.getBruto());

		// Gera log
		Logs.makeLog(Main.loggedUser.getId(), ObjectCode.HOLERITE, 0, ActionsCode.GEROU);
	}

	//Construtor
	protected Holerite(Builder builder) {
		setSalario(builder.salario);
		setBonificacao(builder.bonificacao);
		setDescontos(builder.descontos);
		setLiquido(builder.liquido);
		setBruto(builder.bruto);
		setMes(builder.mes);
		setFuncionario(builder.funcionario);
	}



	public static class Builder {

		private double salario;
		private double bonificacao;
		private double descontos;
		private double liquido;
		private double bruto;
		private String mes;
		private Funcionario funcionario;

		public Builder setSalario(double salario){
			this.salario = salario;
			return this;
		}

		public Builder setBonificacao(double bonificacao){
			this.bonificacao = bonificacao;
			return this;
		}

		public Builder setDescontos(double descontos){
			this.descontos = descontos;
			return this;
		}

		public Builder setLiquido(double liquido){
			this.liquido = liquido;
			return this;
		}

		public Builder setBruto(double bruto){
			this.bruto = bruto;
			return this;
		}

		public Builder setMes(String mes){
			this.mes = mes;
			return this;
		}

		public Builder setFuncionario(Funcionario funcionario){
			this.funcionario = funcionario;
			return this;
		}

		public Holerite build(){
			return new Holerite(this);
		}
	}

}
