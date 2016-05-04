package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.Relatorio;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class RelatorioAcessoController implements Initializable {

	@FXML
	DatePicker dataInicio, dataFim;
	
	@FXML
	Button cancelar, gerar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cancela a exibição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		gerar.setOnMouseClicked((MouseEvent e) -> {
			try {
				Relatorio.gerarRelatorioAcesso(dataInicio, dataFim);
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Relatório Trabalho", ex.getMessage(), AlertType.WARNING);
			}
		});
	}

}
