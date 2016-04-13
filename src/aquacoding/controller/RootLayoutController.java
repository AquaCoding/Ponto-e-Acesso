package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;




import aquacoding.model.Empresa;
import aquacoding.pontoacesso.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class RootLayoutController implements Initializable {

	@FXML
	MenuItem menuBarEmpresaNovoEditar, menuBarEmpresaVer;

	@FXML
	MenuItem menuBarSetorNovo, menuBarSetorLista;

	@FXML
	MenuItem menuBarFuncaoNovo, menuBarFuncaoLista;

	@FXML
	MenuItem menuBarFuncionarioNovo, menuBarFuncionarioLista;

	@FXML
	MenuItem menuBarHorarioNovo, menuBarHorarioLista;

	@FXML
	MenuItem menuBarUsuarioLista, menuBarUsuarioNovo;

	@FXML
	MenuItem menuBarFeriasLista, menuBarFeriasNova;

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

		// Cria o evento de click no botão Usuário > Ver todos
		menuBarUsuarioLista.setOnAction((ActionEvent e) -> {
			Main.loadListaUsuarioView();
		});

		menuBarFuncionarioNovo.setOnAction((ActionEvent e) -> {
			Main.loadNovoFuncionarioView();
		});

		menuBarFuncionarioLista.setOnAction((ActionEvent e) -> {
			Main.loadListaFuncionarioView();
		});

		menuBarEmpresaNovoEditar.setOnAction((ActionEvent e) -> {
			if(Empresa.getAll().size() == 0) {
				// Abre cadastro
				Main.loadEmpresaNovoView();
			} else {
				// Abre editar
				
			}
		});

		menuBarEmpresaVer.setOnAction((ActionEvent e) -> {
			Main.loadEmpresaVer();
		});

		menuBarFeriasLista.setOnAction((ActionEvent e) -> {
			Main.loadListaFeriasView();
		});

		menuBarFeriasNova.setOnAction((ActionEvent e) -> {
			Main.loadFeriasNovoView();
		});
		
		// Oculta o ver da empresa, caso nenhuma empresa esteja cadastrada
		if(Empresa.getAll().size() == 0)
			menuBarEmpresaVer.setVisible(false);

	}
}
