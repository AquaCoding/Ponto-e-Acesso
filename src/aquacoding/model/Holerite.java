package aquacoding.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.tablegenerator.Table;
import aquacoding.utils.tablegenerator.TableData;
import aquacoding.utils.tablegenerator.TableRow;

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
		System.out.println("Bonificação: " + this.getBonificacao());
		System.out.println("Descontos: " + this.getDescontos());
		System.out.println("Liquido: " + this.getLiquido());
		System.out.println("Bruto: " + this.getBruto());

		// Gera log
		Logs.makeLog(Main.loggedUser.getId(), ObjectCode.HOLERITE, -1, ActionsCode.GEROU);
		
		Table table = generateTable();
		System.out.println(table.getAsHTML());
	}
	
	public static double getSalario(int i, LocalDate inicio, Funcionario funcionario) {
		double salario = 0;
		for(int j = 0; j <= i+1; j++ ){
			 LocalDate amanhas = inicio.plusDays(j);
			 int horas;

			 Duration aux = Horario.getHorasTrabalhadasByDateAndFuncionario(funcionario, String.valueOf(amanhas));
			 if(Horario.getHorasTrabalhadasByDateAndFuncionario(funcionario, String.valueOf(amanhas)) != null){
				  horas = Integer.valueOf(""+aux.toHours());
			 }else{
				  horas = 0;
			 }

			 double dinheiroHora = funcionario.getSalarioHoras();
			 salario = salario + horas * dinheiroHora;
		}
		return salario;
	}
	
	private Table generateTable() {
		Table table = new Table();
		
		// Cria as linhas
		TableRow tr1 = new TableRow();
		TableRow tr2 = new TableRow();
		TableRow tr3 = new TableRow();
		TableRow tr4 = new TableRow();
		TableRow tr5 = new TableRow();
		TableRow tr6 = new TableRow();
		TableRow tr7 = new TableRow();
		TableRow tr8 = new TableRow();
		TableRow tr9 = new TableRow();
		TableRow tr10 = new TableRow();
		TableRow tr11 = new TableRow();
		TableRow tr12 = new TableRow();
		TableRow tr13 = new TableRow();
		TableRow tr14 = new TableRow();
		TableRow tr15 = new TableRow();
		TableRow tr16 = new TableRow();
		TableRow tr17 = new TableRow();
		
		// Primeira linha
		TableData l1c1 = new TableData("Dados da empresa");
		l1c1.setIsTH(true);
		l1c1.setColSpan(6);
		tr1.add(l1c1);
		
		// Segunda linha
		TableData l2c1 = new TableData("Nome da empresa");
		l2c1.setIsTH(true);
		l2c1.setColSpan(3);
		tr2.add(l2c1);
		
		TableData l2c2 = new TableData("CNPJ");
		l2c2.setIsTH(true);
		l2c2.setColSpan(3);
		tr2.add(l2c2);
		
		ArrayList<Empresa> e = Empresa.getAll();
		if(e != null && e.size() == 1) {
			// Terceira linha
			TableData l3c1 = new TableData(e.get(0).getNome());
			l3c1.setColSpan(3);
			tr3.add(l3c1);
			
			TableData l3c2 = new TableData(e.get(0).getCNPJ());
			l3c2.setColSpan(3);
			tr3.add(l3c2);

			// Quarta linha
			TableData l4c1 = new TableData("Razão Social");
			l4c1.setIsTH(true);
			l4c1.setColSpan(6);
			tr4.add(l4c1);	
			
			// Quinta linha
			TableData l5c1 = new TableData(e.get(0).getRazaoSocial());
			l5c1.setColSpan(6);
			tr5.add(l5c1);
		}
		
		// Sexta linha
		TableData l6c1 = new TableData("Dados do funcionário");
		l6c1.setIsTH(true);
		l6c1.setColSpan(6);
		tr6.add(l6c1);
		
		// Setima linha
		TableData l7c1 = new TableData("Nome");
		l7c1.setIsTH(true);
		l7c1.setColSpan(3);
		tr7.add(l7c1);
		
		TableData l7c2 = new TableData("CPF");
		l7c2.setIsTH(true);
		l7c2.setColSpan(2);
		tr7.add(l7c2);
		
		TableData l7c3 = new TableData("RG");
		l7c3.setIsTH(true);
		tr7.add(l7c3);
		
		// Oitava linha
		TableData l8c1 = new TableData(funcionario.getNome() + " " + funcionario.getSobrenome());
		l8c1.setColSpan(3);
		tr8.add(l8c1);
		
		TableData l8c2 = new TableData(funcionario.getCpf());
		l8c2.setColSpan(2);
		tr8.add(l8c2);
		
		TableData l8c3 = new TableData(funcionario.getRg());
		tr8.add(l8c3);
		
		// Nona linha
		TableData l9c1 = new TableData("Setor");
		l9c1.setIsTH(true);
		l9c1.setColSpan(2);
		tr9.add(l9c1);
		
		TableData l9c2 = new TableData("Cargo");
		l9c2.setIsTH(true);
		l9c2.setColSpan(2);
		tr9.add(l9c2);
		
		TableData l9c3 = new TableData("Data de admissão");
		l9c3.setIsTH(true);
		l9c3.setColSpan(2);
		tr9.add(l9c3);
		
		// Decima linha
		TableData l10c1 = new TableData(funcionario.getFuncao().getSetor().getNome());
		l10c1.setColSpan(2);
		tr10.add(l10c1);
		
		TableData l10c2 = new TableData(funcionario.getFuncao().getNome());
		l10c2.setColSpan(2);
		tr10.add(l10c2);
		
		TableData l10c3 = new TableData(funcionario.getAdmissaoString());
		l10c3.setColSpan(2);
		tr10.add(l10c3);
		
		// Decima primeira linha
		TableData l11c1 = new TableData("Data");
		l11c1.setIsTH(true);
		tr11.add(l11c1);
		
		TableData l11c2 = new TableData(getMes());
		l11c2.setColSpan(5);
		tr11.add(l11c2);
		
		// Decima segunda linha
		TableData l12c1 = new TableData("Descrição");
		l12c1.setIsTH(true);
		l12c1.setColSpan(5);
		tr12.add(l12c1);
		
		TableData l12c2 = new TableData("Valor");
		l12c2.setIsTH(true);
		l12c2.setColSpan(5);
		tr12.add(l12c2);
		
		// Decima terceira linha
		TableData l13c1 = new TableData("Salário");
		l13c1.setColSpan(5);
		tr13.add(l13c1);
		
		TableData l13c2 = new TableData(getSalario());
		l13c2.setColSpan(5);
		tr13.add(l13c2);
		
		// Decima quarta linha
		TableData l14c1 = new TableData("Bonificações");
		l14c1.setColSpan(5);
		tr14.add(l14c1);
		
		TableData l14c2 = new TableData(getBonificacao());
		l14c2.setColSpan(5);
		tr14.add(l14c2);
		
		// Decima quinta linha
		TableData l15c1 = new TableData("Descontos");
		l15c1.setColSpan(5);
		tr15.add(l15c1);
		
		TableData l15c2 = new TableData(getDescontos());
		l15c2.setColSpan(5);
		tr15.add(l15c2);
		
		// Decima sexta linha
		TableData l16c1 = new TableData("Liquido");
		l16c1.setIsTH(true);
		l16c1.setColSpan(3);
		tr16.add(l16c1);
		
		TableData l16c2 = new TableData("Bruto");
		l16c2.setIsTH(true);
		l16c2.setColSpan(3);
		tr16.add(l16c2);
		
		// Decima setima linha
		TableData l17c1 = new TableData(getLiquido());
		l17c1.setColSpan(3);
		tr17.add(l17c1);
		
		TableData l17c2 = new TableData(getBruto());
		l17c2.setColSpan(3);
		tr17.add(l17c2);
				
		// Adiciona a tabela
		table.add(tr1);
		table.add(tr2);
		table.add(tr3);
		table.add(tr4);
		table.add(tr5);
		table.add(tr6);
		table.add(tr7);
		table.add(tr8);
		table.add(tr9);
		table.add(tr10);
		table.add(tr11);
		table.add(tr12);
		table.add(tr13);
		table.add(tr14);
		table.add(tr15);
		table.add(tr16);
		table.add(tr17);
		
		return table;
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
