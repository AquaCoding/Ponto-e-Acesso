package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;



import aquacoding.pontoacesso.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class RootLayoutController implements Initializable {

	@FXML
	MenuItem menuBarSetorNovo, menuBarSetorLista;

	@FXML
	MenuItem menuBarFuncaoNovo, menuBarFuncaoLista;

	@FXML
	MenuItem menuBarHorarioNovo, menuBarHorarioLista;

	@FXML
	MenuItem menuBarUsuarioLista, menuBarUsuarioNovo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Cria o evento de click no botão Setor > Novo
		menuBarSetorNovo.setOnAction((ActionEvent e) -> {
			Main.loadNovoSetorView();
		});

		menuBarSetorLista.setOnAction((ActionEvent e) -> {
			Main.loadListaSetorView();
		});

		menuBarFuncaoNovo.setOnAction((ActionEvent e) -> {
			Main.loadNovoFuncaoView();
		});

		menuBarFuncaoLista.setOnAction((ActionEvent e) -> {
			Main.loadListaFuncaoView();
		});

		menuBarHorarioNovo.setOnAction((ActionEvent e) -> {
			Main.loadNovoHorarioView();
		});

		menuBarHorarioLista.setOnAction((ActionEvent e) -> {
			Main.loadListaHorarioView();
		});

		// Cria o evento de click no botão Usuário > Novo
		menuBarUsuarioNovo.setOnAction((ActionEvent e) -> {
			Main.loadUsuarioNovoView();
		});
	}

}
