package aquacoding.pontoacesso;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;

import aquacoding.controller.BonificacaoCadastroController;
import aquacoding.controller.BonificacaoLinkController;
import aquacoding.controller.CartaoModeloController;
import aquacoding.controller.EmpresaEditarController;
import aquacoding.controller.EmpresaVerController;
import aquacoding.controller.FeriasEditarController;
import aquacoding.controller.FuncaoEditarController;
import aquacoding.controller.FuncionarioEditarController;
import aquacoding.controller.FuncionarioFeriasVerController;
import aquacoding.controller.FuncionarioVerController;
import aquacoding.controller.HorarioEditarController;
import aquacoding.controller.ImpostoNovoController;
import aquacoding.controller.SetorEditarController;
import aquacoding.controller.UsuarioEditarController;
import aquacoding.controller.UsuarioNovoController;
import aquacoding.controller.WebViewController;
import aquacoding.model.Bonificacao;
import aquacoding.model.Empresa;
import aquacoding.model.Ferias;
import aquacoding.model.Funcao;
import aquacoding.model.Funcionario;
import aquacoding.model.Horario;
import aquacoding.model.Imposto;
import aquacoding.model.Setor;
import aquacoding.model.Usuario;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.Image;
import aquacoding.utils.Serial;

// Extends javafx.application.Application para user JavaFX
public class Main extends Application {

	public static Stage primaryStage;
	private static BorderPane rootLayout;
	private static Stage loginStage = new Stage();
	private static String pageTitle = "Controle de Ponto e Acesso";
	public static Usuario loggedUser = null;

	private static boolean firstTime = true;
    private static TrayIcon trayIcon = null;

    private static Thread serialThread;

	@Override
	public void start(Stage stage) throws Exception {
		// Inicia a janela principal (Main.fxml)
		primaryStage = stage;
		Platform.setImplicitExit(false);
		initLoginLayout();

		// Serial
		Serial serial = new Serial();
		serialThread = new Thread(() -> {
			try {
				serial.SerialLeitura();
			} catch (Exception e) {
				System.out.println("Erro Serial: " + e.getMessage());
			}
		});
		serialThread.start();

		primaryStage.getIcons().add(new javafx.scene.image.Image("file:img/app_icon.png"));	
		loginStage.getIcons().add(new javafx.scene.image.Image("file:img/app_icon.png"));
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Cria o icone do relogio
    public static void createTrayIcon(final Stage stage) {
        // Verifica se existe suporte para icones no relogio
    	if (SystemTray.isSupported()) {
            // Obtem a instancia dos icones do relogio
            SystemTray tray = SystemTray.getSystemTray();

            // Remove o icone existente caso houver
            if(trayIcon != null)
            	tray.remove(trayIcon);

            // Carrega uma image para o icone
            java.awt.Image image = null;
            try {
                image = ImageIO.read(new File(Image.APP_ICON_PATH));
            } catch (IOException ex) {
                System.out.println(ex);
            }

            // Subescreve o evento de fechar da janela
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                	// Oculta a janela
                    hide(stage);
                }
            });

            // A��es a serem executadas no icone do relogio
            // Fecha o programa
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {

                    System.exit(0);
                }
            };

            // Reabre a janela
            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };

            // Cria os menus do icone
            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Exibir");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Fechar");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);

            // Cria o TrayIcon
            trayIcon = new TrayIcon(image, pageTitle, popup);

            // Configura os Listeners do icone
            trayIcon.addActionListener(showListener);

            // Define o icone para se redimensionar sozinho
            trayIcon.setImageAutoSize(true);

            // Adiciona o icone
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        }
    }

    // Exibe uma mensagem quando o programa � minimizado pela primeira vez
    public static void onMinimize() {
        if (firstTime) {
        	trayIcon.displayMessage(pageTitle, "O programa esta minimizado no rel�gio", TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }

    // Oculta a janela
    private static void hide(final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    onMinimize();
                } else {
                    System.exit(0);
                }
            }
        });
    }

	// Realiza a inicializa��o da janela de login
	public static void initLoginLayout() {
		try {
			if (loginStage == null) {
				loginStage.setResizable(false);
				loginStage.initModality(Modality.APPLICATION_MODAL);
				loginStage.setAlwaysOnTop(true);
			}

			if (!Usuario.haveUsuario()) {
				CustomAlert.showAlert("Primeiro acesso", "� preciso criar um usu�rio", AlertType.INFORMATION);
				initPrimeiroAcesso();
			} else {
				createTrayIcon(loginStage);

				// Inicia a tela de login
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(ClassLoader.getSystemResource("resources/views/Login.fxml"));
				Parent root = (AnchorPane) loader.load();

				// Configura a tela de login
				Scene scene = new Scene(root);
				scene.getStylesheets().add("" + Main.class.getResource("application.css"));
				loginStage.setResizable(false);
				loginStage.setTitle(pageTitle + " - Entrar");
				loginStage.setScene(scene);
				loginStage.show();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Fecha a janela de login
	public static void endLoginLayout() {
		loginStage.close();
	}

	public static void endRootLayout() {
		primaryStage.close();
	}

	// Realiza a inicializa��o da janela princpal
	public static void initPrimeiroAcesso() {
		try {
			// Carrega o layout de cadastro de usuario
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/UsuarioNovo.fxml"));
			Parent root = (AnchorPane) loader.load();

			// Obtem o controller da view e informa para fechar e abrir o login apos o criar
			UsuarioNovoController controller = loader.getController();
			controller.setCloseAfterCreate(true);

			// Inicia o layout
			Scene scene = new Scene(root);
			scene.getStylesheets().add("" + Main.class.getResource("application.css"));
			loginStage.setResizable(false);
			loginStage.setTitle(pageTitle + " - Criando primeiro usuario");
			loginStage.setScene(scene);
			loginStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Realiza a inicializa��o da janela princpal
	public static void initRootLayout() {
		try {
			createTrayIcon(primaryStage);

			// Carrega o root layout do arquivo fxml.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Mostra a scene contendo o root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add("" + Main.class.getResource("application.css"));
			primaryStage.setScene(scene);
			primaryStage.show();

			loadMainView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view principal
	public static void loadMainView() {
		try {
			primaryStage.setTitle(pageTitle);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/Main.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de novo setor dentro da janela principal
	public static void loadNovoSetorView() {
		try {
			primaryStage.setTitle(pageTitle + " - Novo setor");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/SetorNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de listagem de setor
	public static void loadListaSetorView() {
		try {
			primaryStage.setTitle(pageTitle + " - Setor");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/SetorLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de nova fun��o
	public static void loadNovoFuncaoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Nova fun��o");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncaoNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de listagem de fun��o
	public static void loadListaFuncaoView() {
		try {
			primaryStage.setTitle(pageTitle + " - fun��es");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncaoLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de edi��o de setor
	public static void loadSetorEditarView(Setor setor) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar setor");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/SetorEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o setor a ser editado
			SetorEditarController controller = loader.getController();
			controller.setSetor(setor);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de novo horario
	public static void loadNovoHorarioView() {
		try {
			primaryStage.setTitle(pageTitle + " - Novo hor�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/HorarioNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de listagem de horarios
	public static void loadListaHorarioView() {
		try {
			primaryStage.setTitle(pageTitle + " - Hor�rios");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/HorarioLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de lista de Funcionario
	public static void loadListaFuncionarioView() {
		try {
			primaryStage.setTitle(pageTitle + " - Funcion�rios");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncionarioLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar fun��o
	public static void loadFuncaoEditarView(Funcao funcao) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar fun��o");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncaoEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o setor a ser editado
			FuncaoEditarController controller = loader.getController();
			controller.setFuncao(funcao);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de novo usuario
	public static void loadUsuarioNovoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Novo usu��rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/UsuarioNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de listagem de usu�rios
	public static void loadListaUsuarioView() {
		try {
			primaryStage.setTitle(pageTitle + " - Usu�rios");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/UsuarioLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de edi��o de usu�rios
	public static void loadEditarUsuarioView(Usuario usuario) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar usu�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/UsuarioEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o usu�rio a ser editado
			UsuarioEditarController controller = loader.getController();
			controller.setUsuario(usuario);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de novo Funcionario
	public static void loadNovoFuncionarioView() {
		try {
			primaryStage.setTitle(pageTitle + " - Novo funcion�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncionarioNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar funcionpario
	public static void loadFuncionarioEditarView(Funcionario funcionario) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar funcion�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncionarioEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o funcionario a ser
			// editado
			FuncionarioEditarController controller = loader.getController();
			controller.setFuncionario(funcionario);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de visualizar funcionario
	public static void loadFuncionarioVerView(Funcionario funcionario) {
		try {
			primaryStage.setTitle(pageTitle + " - Ver funcion�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncionarioVer.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface
			FuncionarioVerController controller = loader.getController();
			controller.setFuncionario(funcionario);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar um horario
	public static void loadHorarioEditarView(Horario horario) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar hor�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/HorarioEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface
			HorarioEditarController controller = loader.getController();
			controller.setHorario(horario);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar um horario
	public static void loadEmpresaNovoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Nova empresa");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/EmpresaNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar um horario
	public static void loadEmpresaVer() {
		try {
			primaryStage.setTitle(pageTitle + " - Ver empresa");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/EmpresaVer.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface
			EmpresaVerController controller = loader.getController();
			controller.loadEmpresaInfo();

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de listagem de ferias
	public static void loadListaFeriasView() {
		try {
			primaryStage.setTitle(pageTitle + " - F�rias");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FeriasLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de na F�rias
	public static void loadFeriasNovoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Nova f�rias");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FeriasNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de edi��o de empresa
	public static void loadEmpresaEditarView(Empresa empresa) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar empresa");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/EmpresaEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa a empresa a ser editada
			EmpresaEditarController controller = loader.getController();
			controller.setEmpresa(empresa);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de na Abono de Falta
	public static void loadFuncionarioAbonoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Cadastro de abono de falta");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncionarioAbono.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar ferias
	public static void loadFeriasEditarView(Ferias ferias) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar f�rias");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FeriasEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o setor a ser editado
			FeriasEditarController controller = loader.getController();
			controller.setFerias(ferias);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadBonificacaoCadastroView() {
		try {
			primaryStage.setTitle(pageTitle + " - Cadastrar bonifica��o");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/BonificacaoCadastro.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de gerar relatorio de trabalho
	public static void loadRelatorioTrabalhoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Relat�rio de trabalho");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/RelatorioPonto.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Inicia a webview e abre o arquivo especificado
	public static void loadWebView(String fileToOpen) {
		try {
			primaryStage.setTitle(pageTitle + " - Relat�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/WebView.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface
			WebViewController controller = loader.getController();
			controller.openPage(fileToOpen);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar bonifica��o
	public static void loadBonificacaoEditarView(Bonificacao bonificacao) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar Bonifica��o");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/BonificacaoCadastro.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o setor a ser editado
			BonificacaoCadastroController controller = loader.getController();
			controller.setBonificacao(bonificacao);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de exibi��o dos cart�es
	public static void loadCartoesVerView() {
		try {
			primaryStage.setTitle(pageTitle + " - Cart�es");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/CartoesVer.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de cadastro de imposto
	public static void loadImpostoCriarView() {
		try {
			primaryStage.setTitle(pageTitle + " - Cadastrar imposto");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/ImpostoNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de visualiza��o de imposto
	public static void loadImpostoVerView() {
		try {
			primaryStage.setTitle(pageTitle + " - Impostos");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/ImpostoVer.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de auditoria dos logs
	public static void loadAuditoriaVerView() {
		try {
			primaryStage.setTitle(pageTitle + " - Auditoria");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/AuditoriaVer.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar imposto
	public static void loadImpostoEditarView(Imposto imposto) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar Imposto");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/ImpostoNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o funcionario a ser
			// editado
			ImpostoNovoController controller = loader.getController();
			controller.setImposto(imposto);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de listagem de bonifica��es
	public static void loadBonificacaoListarView() {
		try {
			primaryStage.setTitle(pageTitle + " - Bonifica��es");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/BonificacaoLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadBonificacaoLink(Bonificacao bonificacao) {
		try {
			primaryStage.setTitle(pageTitle + " - Linkando bonifica��o");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/BonificacaoLink.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa a bonifica��o a ser linkada
			BonificacaoLinkController controller = loader.getController();
			controller.setBonificacao(bonificacao);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadRelatorioAcessoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Relat�rio de acessos");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/RelatorioAcesso.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadCartoesModeloView() {
		try {
			primaryStage.setTitle(pageTitle + " - Modelo de cart�o");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/CartaoModeloCriar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar funcionpario
	public static void loadFuncionarioFeriasView(Funcionario funcionario) {
		try {
			primaryStage.setTitle(pageTitle + " - F�rias do " + funcionario.getNome());
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncionarioFeriasVer.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o funcionario a ser
			// editado
			FuncionarioFeriasVerController controller = loader.getController();
			controller.setFuncionario(funcionario);

			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadParsedModeloCartao(Funcionario funcionario) {
		try {
			primaryStage.setTitle(pageTitle + " - Modelo de cart�o");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/CartaoModeloCriar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			
			// Obtem o controller da interface e passa o funcionario
			CartaoModeloController controller = loader.getController();
			controller.setFuncionario(funcionario);
						
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
