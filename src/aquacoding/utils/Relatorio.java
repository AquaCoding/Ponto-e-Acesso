package aquacoding.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.DatePicker;
import aquacoding.model.Funcionario;
import aquacoding.model.Horario;
import aquacoding.pontoacesso.Main;

public class Relatorio {

	public static void gerarRelatorioTrabalho(DatePicker dataInicio, DatePicker dataFim, Funcionario selecionado) {
		if(dataInicio.getValue() == null)
			throw new RuntimeException("Uma data de inicio deve ser selecionada.");
		
		if(dataFim.getValue() == null)
			throw new RuntimeException("Uma data de fim deve ser selecionada.");
		
		if(selecionado == null)
			throw new RuntimeException("Um funcionário deve ser selecionado.");
		
		// Gera o relatório de horas trabalhadas
		ArrayList<String> horasTrabalhadas = gerarRelatorioHorasTrabalhadas(dataInicio, dataFim, selecionado);
		
		// Gera o relatório dos pontos batidos
		ArrayList<String> pontos = gerarRelatorioPontos(dataInicio, dataFim, selecionado);
		
		ArrayList<String> relatorioFinal = new ArrayList<String>();
		relatorioFinal.add("<!DOCTYPE html><html>");
		relatorioFinal.add("<head><meta charset=\"UTF-8\"><style>* {margin: 0; padding: 0} p {font-size: 10px;} table {border-collapse: collapse;} .left {float: left} .right {float: right} td, th {padding: 5px; border: 1px solid #666;} th {background-color: #4CAF50; color: #FFF;}</style></head>");
		relatorioFinal.add("<body>");
		relatorioFinal.addAll(horasTrabalhadas);
		relatorioFinal.addAll(pontos);
		relatorioFinal.add("</body></html>");
		
		try {
			// Salva o arquivo
			Files.write(Paths.get("relatorioTrabalho.html"), relatorioFinal);
			
			// Abre o arquivo
			Main.loadWebView("relatorioTrabalho.html");
		} catch (Exception e1) {
			throw new RuntimeException("Um erro ocorreu ao criar o relatório.");
		}
	}
	
	private static ArrayList<String> gerarRelatorioHorasTrabalhadas(DatePicker dataInicio, DatePicker dataFim, Funcionario selecionado) {
		// Obtem as datas do formulario
		LocalDate LocalDateInicio = dataInicio.getValue();
		LocalDate LocalDateFim = dataFim.getValue();
		
		// Percorre por todas as datas no intervalo e procura por horas trabalhadas
		HashMap<LocalDate, Duration> horasTrabalhadas = new HashMap<LocalDate, Duration>();
		do {
			horasTrabalhadas.put(LocalDateInicio, Horario.getHorasTrabalhadasByDateAndFuncionario(selecionado, LocalDateInicio.toString()));
			
			// Incrementa um dia a data
			LocalDateInicio = LocalDateInicio.plusDays(1);
		} while(LocalDateInicio.isBefore(LocalDateFim) || LocalDateInicio.isEqual(LocalDateFim));
		
		// Percorre por todas as horas trabalhas resetando o horario de inicio
		LocalDateInicio = dataInicio.getValue();
		
		// Cria o arquivo do relatorio de horas trabalhadas
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("<p>*Dias com número de pontos impares são ignorados</p><table class=\"left\"><tr><th colspan=\"2\">Horas trabalhadas por dia*</th></tr><tr><th>Data</th><th>Horas trabalhadas</th></tr>");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		do {
			if(horasTrabalhadas.containsKey(LocalDateInicio)) {
				if(horasTrabalhadas.get(LocalDateInicio) != null) {
					lines.add("<tr><td>"+LocalDateInicio.format(formatter)+"</td><td>"+horasTrabalhadas.get(LocalDateInicio).toString().substring(2)+"</td></tr>");
				} else {
					lines.add("<tr><td>"+LocalDateInicio.format(formatter)+"</td><td>Nenhum registro</td></tr>");
				}
			} else {
				lines.add("<tr><td>"+LocalDateInicio.format(formatter)+"</td><td>Nenhum registro</td></tr>");
			}
			
			// Incrementa um dia a data
			LocalDateInicio = LocalDateInicio.plusDays(1);
		} while(LocalDateInicio.isBefore(LocalDateFim) || LocalDateInicio.isEqual(LocalDateFim));
		lines.add("</table>");
		
		return lines;
	}
	
	private static ArrayList<String> gerarRelatorioPontos(DatePicker dataInicio, DatePicker dataFim, Funcionario selecionado) {
		// Obtem as datas do formulario
		LocalDate LocalDateInicio = dataInicio.getValue();
		LocalDate LocalDateFim = dataFim.getValue();
		
		// Percorre por todas as datas no intervalo e procura por horas trabalhadas
		ArrayList<ArrayList<Instant>> pontos = new ArrayList<ArrayList<Instant>>();
		do {
			// Obtem todos os pontos de um dia
			pontos.add(Horario.getPontoByDateAndFuncionario(selecionado, LocalDateInicio.toString()));
			
			// Incrementa um dia a data
			LocalDateInicio = LocalDateInicio.plusDays(1);
		} while(LocalDateInicio.isBefore(LocalDateFim) || LocalDateInicio.isEqual(LocalDateFim));
		
		// Percorre por todas as horas trabalhas resetando o horario de inicio
		LocalDateInicio = dataInicio.getValue();
		
		// Cria o arquivo do relatorio de horas trabalhadas
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("<table class=\"right\"><tr><th>Horário dos pontos</th></tr><tr><th>Data</th></tr>");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());
		
		for(ArrayList<Instant> in: pontos) {
			for(Instant i: in) {
				lines.add("<tr><td>"+ formatter.format(i) +"</tr></td>");
			}
		}
		lines.add("</table>");
		
		// Retorna o relatorio
		return lines;
	}
	
	public static ArrayList<LocalDate> contaFaltas(LocalDate LocalDateInicio, LocalDate LocalDateFim, Funcionario selecionado) {
		// Obtem um arraylist dos dias das semanas trabalhados pelo funcionário
		ArrayList<Horario> horarios = selecionado.getHorario();
		ArrayList<DayOfWeek> diasTrabalhados = new ArrayList<DayOfWeek>();
		for(Horario h: horarios) {
			if(h.getSegunda())
				diasTrabalhados.add(DayOfWeek.MONDAY);
			if(h.getTerca())
				diasTrabalhados.add(DayOfWeek.TUESDAY);
			if(h.getQuarta())
				diasTrabalhados.add(DayOfWeek.WEDNESDAY);
			if(h.getQuinta())
				diasTrabalhados.add(DayOfWeek.THURSDAY);
			if(h.getSexta())
				diasTrabalhados.add(DayOfWeek.FRIDAY);
			if(h.getSabado())
				diasTrabalhados.add(DayOfWeek.SATURDAY);
			if(h.getDomingo())
				diasTrabalhados.add(DayOfWeek.SUNDAY);
		}
					
		// Percorre por todas as datas no intervalo e procura por horas trabalhadas
		ArrayList<LocalDate> faltas = new ArrayList<LocalDate>();
		do {
			
			// Verifica se é um dia de trabalho válido
			if(diasTrabalhados.contains(LocalDateInicio.getDayOfWeek())) {
				// Verifica se o dia não possui nenhuma hora trabalhada
				if(Horario.getHorasTrabalhadasByDateAndFuncionario(selecionado, LocalDateInicio.toString()) == null)
					//adiciona ele ao arraylist de faltas
					faltas.add(LocalDateInicio);
			}
			
			// Incrementa um dia a data
			LocalDateInicio = LocalDateInicio.plusDays(1);
		} while(LocalDateInicio.isBefore(LocalDateFim) || LocalDateInicio.isEqual(LocalDateFim));
		
		// Retorna as faltas
		return faltas;
	}
	
	private static ArrayList<String> gerarRelatorioAcessosHoras(DatePicker dataInicio, DatePicker dataFim) {		
		// Select de todos os acessos
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();
			
			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement("SELECT * FROM AcessoRelatorio  WHERE dia >= ? AND dia <= ?");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			// Realiza o bind dos valores
			statement.setString(1, formatter.format(dataInicio.getValue()));
			statement.setString(2, formatter.format(dataFim.getValue()));

			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();

			// Cria o arquivo do relatorio de horas trabalhadas
			ArrayList<String> lines = new ArrayList<String>();
			lines.add("<table class=\"left\"><tr><th colspan=\"3\">Acessos por hora</th></tr><tr><th>Data</th><th>Hora</th><th>Acessos</th></tr>");
			
			// Enquanto tiver resultado, adiciona a tabela do relatorio
			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			while (resultSet.next()) {
				lines.add("<tr><td>"+formatter.format(resultSet.getDate("dia").toLocalDate())+"</td><td>"+resultSet.getString("hora")+"</td><td>"+resultSet.getInt("numAcesso")+"</td></tr>");
			}
			
			// Encerra a tabela do relatório
			lines.add("</table>");
			
			return lines;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Um erro ocorreu ao obter os acessos.");
		}
	}
	
	public static void gerarRelatorioAcesso(DatePicker dataInicio, DatePicker dataFim) {
		if(dataInicio.getValue() == null)
			throw new RuntimeException("Uma data de inicio deve ser selecionada.");
		
		if(dataFim.getValue() == null)
			throw new RuntimeException("Uma data de fim deve ser selecionada.");
		
		// Gera o relatório de horas trabalhadas
		ArrayList<String> acessosHoras = gerarRelatorioAcessosHoras(dataInicio, dataFim);
		
		ArrayList<String> relatorioFinal = new ArrayList<String>();
		relatorioFinal.add("<!DOCTYPE html><html>");
		relatorioFinal.add("<head><meta charset=\"UTF-8\"><style>* {margin: 0; padding: 0} p {font-size: 10px;} table {border-collapse: collapse;} .left {float: left} .right {float: right} td, th {padding: 5px; border: 1px solid #666;} th {background-color: #4CAF50; color: #FFF;}</style></head>");
		relatorioFinal.add("<body>");
		relatorioFinal.addAll(acessosHoras);
		relatorioFinal.add("</body></html>");
		
		try {
			// Salva o arquivo
			Files.write(Paths.get("relatorioAcesso.html"), relatorioFinal);
			
			// Abre o arquivo
			Main.loadWebView("relatorioAcesso.html");
		} catch (Exception e1) {
			throw new RuntimeException("Um erro ocorreu ao criar o relatório.");
		}
	}
}