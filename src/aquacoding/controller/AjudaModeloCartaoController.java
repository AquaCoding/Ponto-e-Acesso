package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.pontoacesso.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class AjudaModeloCartaoController implements Initializable {

	@FXML
	Button cancelar;
	
	@FXML
	TextArea textAjuda;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Evento de click do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		textAjuda.setText("Botão Texto - Adiciona texto ao modelo \n"
				+ "Botão Imagem - Adiciona uma imagem de sua preferência \n"
				+ "Botão Imagem do Perfil - Adiciona a imagem do perfil do funcionário \n"
				+ "Botão Salvar - Salva modelo editável \n"
				+ "Botão Salvar PNG - Salva modelo em formato PNG \n"
				+ "\n"
				+ "Clicando no elemento adicionado é possível fazer sua customização (Elemento Texto): \n"
				+ "				Fonte \n"
				+ "				Tamanho \n"
				+ "				Texto  \n"
				+ "Clicando no elemento adicionado é possível fazer sua customização (Elemento Imagem): \n"
				+ "				Remoção");
		
	}
	
}
