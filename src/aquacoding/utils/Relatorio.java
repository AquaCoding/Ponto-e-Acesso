package aquacoding.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
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
		
		// Cria o arquivo do relatorio
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("<html><head><style>* {margin: 0; padding: 0} table {border-collapse: collapse;} td, th {padding: 5px; border: 1px solid #666;} th {background-color: #4CAF50; color: #FFF;}</style></head><body><table><tr><th>Data</th><th>Horas trabalhadas</th></tr>");
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
		lines.add("</table></body></html>");
		
		try {
			// Salva o arquivo
			Files.write(Paths.get("relatorioTrabalho.html"), lines);
			
			// Abre o arquivo
			Main.loadWebView("relatorioTrabalho.html");
		} catch (Exception e1) {
			throw new RuntimeException("Um erro ocorreu ao criar o relatório");
		}
	}
}
