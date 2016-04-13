package aquacoding.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
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
		if(dataInicio == null)
			throw new RuntimeException("Uma data de inicio deve ser selecionada.");
		
		if(dataFim == null)
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
			throw new RuntimeException("Um erro ocorreu ao criar o relatório");
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
}

//<html>