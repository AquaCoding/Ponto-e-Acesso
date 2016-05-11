package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import aquacoding.model.Backup;
import aquacoding.model.Empresa;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.DatabaseConnect;
import aquacoding.utils.Folders;

public class RootLayoutController implements Initializable {

	@FXML
	MenuItem menuBarSistemaDeslogar;
	
	@FXML
	MenuItem menuBarEmpresaNovoEditar, menuBarEmpresaVer;

	@FXML
	MenuItem menuBarSetorNovo, menuBarSetorLista;

	@FXML
	MenuItem menuBarFuncaoNovo, menuBarFuncaoLista;

	@FXML
	MenuItem menuBarFuncionarioNovo, menuBarFuncionarioLista, menuBarFuncionarioAbono;

	@FXML
	MenuItem menuBarHorarioNovo, menuBarHorarioLista;

	@FXML
	MenuItem menuBarUsuarioLista, menuBarUsuarioNovo;

	@FXML
	MenuItem menuBarFeriasLista, menuBarFeriasNova;
	
	@FXML
	MenuItem menuBarCartoesVer, menuBarCartoesModelo;
	
	@FXML
	MenuItem menuBarRelatorioTrabalho, menuBarRelatorioAcesso;
	
	@FXML
	MenuItem menuBarBackupCriar, menuBarBackupSalvar;
	
	@FXML
	MenuItem menuBarImpostoCriar, menuBarImpostoVer;
	
	@FXML
	MenuItem menuBarBonificacaoCriar, menuBarBonificacaoVer;
	
	@FXML
	MenuItem menuBarAuditoriaVer;
	
	private Backup backup;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Eventos de clique no menu do sistema
		menuBarSistemaDeslogar.setOnAction((ActionEvent e) -> {
			Main.loggedUser = null;
			Main.initLoginLayout();
			Main.endRootLayout();
		});
		
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

		// Cria o evento de click no bot�o Usu�rio > Novo
		menuBarUsuarioNovo.setOnAction((ActionEvent e) -> {
			Main.loadUsuarioNovoView();
		});

		// Cria o evento de click no bot�o Usu�rio > Ver todos
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
		
		menuBarBonificacaoCriar.setOnAction((ActionEvent e) -> {
			Main.loadBonificacaoCadastroView();
		});
		
		menuBarBonificacaoVer.setOnAction((ActionEvent e) -> {
			Main.loadBonificacaoListarView();
		});
		
		menuBarCartoesVer.setOnAction((ActionEvent e) -> {
			Main.loadCartoesVerView();
		});
		
		menuBarCartoesModelo.setOnAction((ActionEvent e) -> {
			Main.loadCartoesModeloView();
		});
		
		menuBarRelatorioTrabalho.setOnAction((ActionEvent e) -> {
			Main.loadRelatorioTrabalhoView();
		});
		
		menuBarRelatorioAcesso.setOnAction((ActionEvent e) -> {
			Main.loadRelatorioAcessoView();
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
			fileC.setTitle("Selecione o local para criar o backup");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			fileC.setInitialFileName("PontoAcesso_backup_" + formatter.format(LocalDate.now()));
			fileC.getExtensionFilters().add(new ExtensionFilter("SQL Files (*.sql)", "*.sql"));
			File localToSave = fileC.showSaveDialog(Main.primaryStage);
			
			if(localToSave != null){
				DatabaseConnect.makeBackup(localToSave.getAbsolutePath());
				try {
					String destination = Paths.get(localToSave.toPath().getParent() + "/img").toString();
					Folders.copyFolder(new File("img"), new File(destination));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		menuBarBackupSalvar.setOnAction((ActionEvent e) -> {
			
			try {
				backup.criarSalvarBackup();
			} catch (Exception e1) {
				System.out.println("Erro na cria��o do arquivo" + e1.getMessage());
			}
		});	
		
		// Oculta o ver da empresa, caso nenhuma empresa esteja cadastrada
		if(Empresa.getAll().size() == 0)
			menuBarEmpresaVer.setVisible(false);
	}
}
