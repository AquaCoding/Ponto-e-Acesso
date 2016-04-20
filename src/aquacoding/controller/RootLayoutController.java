package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import aquacoding.model.Empresa;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.DatabaseConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class RootLayoutController implements Initializable {

	@FXML
	MenuItem menuBarEmpresaNovoEditar, menuBarEmpresaVer;

	@FXML
	MenuItem menuBarSetorNovo, menuBarSetorLista;

	@FXML
	MenuItem menuBarFuncaoNovo, menuBarFuncaoLista;

	@FXML
	MenuItem menuBarFuncionarioNovo, menuBarFuncionarioLista, menuBarFuncionarioAbono, menuBarFuncionarioBonificacao;

	@FXML
	MenuItem menuBarHorarioNovo, menuBarHorarioLista;

	@FXML
	MenuItem menuBarUsuarioLista, menuBarUsuarioNovo;

	@FXML
	MenuItem menuBarFeriasLista, menuBarFeriasNova;
	
	@FXML
	MenuItem menuBarCartoesVer;
	
	@FXML
	MenuItem menuBarRelatorioTrabalho;
	
	@FXML
	MenuItem menuBarBackupCriar;
	
	@FXML
	MenuItem menuBarImpostoCriar, menuBarImpostoVer;
	
	@FXML
	MenuItem menuBarAuditoriaVer;

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
				Main.loadEmpresaEditarView(Empresa.getAll().get(0));
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
		
		menuBarFuncionarioAbono.setOnAction((ActionEvent e) -> {
			Main.loadFuncionarioAbonoView();
		});
		
		menuBarFuncionarioBonificacao.setOnAction((ActionEvent e) -> {
			Main.loadBonificacaoCadastroView();
		});
		
		menuBarCartoesVer.setOnAction((ActionEvent e) -> {
			Main.loadCartoesVerView();
		});
		
		menuBarRelatorioTrabalho.setOnAction((ActionEvent e) -> {
			Main.loadRelatorioTrabalhoView();
		});
		
		menuBarImpostoCriar.setOnAction((ActionEvent e) -> {
			Main.loadImpostoCriarView();
		});
		
		menuBarImpostoVer.setOnAction((ActionEvent e) -> {
			Main.loadImpostoVerView();
		});	
		
		menuBarAuditoriaVer.setOnAction((ActionEvent e) -> {
			Main.loadAuditoriaVerView();
		});	
		
		menuBarBackupCriar.setOnAction((ActionEvent e) -> {
			FileChooser fileC = new FileChooser();
			fileC.setTitle("Selecione a imagem do funcionário");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			fileC.setInitialFileName("PontoAcesso_backup_" + formatter.format(LocalDate.now()));
			fileC.getExtensionFilters().add(new ExtensionFilter("SQL Files (*.sql)", "*.sql"));
			File localToSave = fileC.showSaveDialog(Main.primaryStage);
			
			DatabaseConnect.makeBackup(localToSave.getAbsolutePath());
		});
		
		// Oculta o ver da empresa, caso nenhuma empresa esteja cadastrada
		if(Empresa.getAll().size() == 0)
			menuBarEmpresaVer.setVisible(false);
	}
}
