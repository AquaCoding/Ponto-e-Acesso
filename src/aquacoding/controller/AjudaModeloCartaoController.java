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
		// Evento de click do bot�o cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		textAjuda.setText("Bot�o Texto - Adiciona texto ao modelo \n"
				+ "Bot�o Imagem - Adiciona uma imagem de sua prefer�ncia \n"
				+ "Bot�o Imagem do Perfil - Adiciona a imagem do perfil do funcion�rio \n"
				+ "Bot�o Salvar - Salva modelo edit�vel \n"
				+ "Bot�o Salvar PNG - Salva modelo em formato PNG \n"
				+ "\n"
				+ "Clicando no elemento adicionado � poss�vel fazer sua customiza��o (Elemento Texto): \n"
				+ "				Fonte \n"
				+ "				Tamanho \n"
				+ "				Texto  \n"
				+ "Clicando no elemento adicionado � poss�vel fazer sua customiza��o (Elemento Imagem): \n"
				+ "				Remo��o");
		
	}
	
}
