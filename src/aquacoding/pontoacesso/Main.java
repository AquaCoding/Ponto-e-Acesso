package aquacoding.pontoacesso;

import java.io.IOException;

import aquacoding.controller.FuncaoEditarController;
import aquacoding.controller.FuncionarioVerController;
import aquacoding.controller.HorarioEditarController;
import aquacoding.controller.SetorEditarController;
import aquacoding.controller.UsuarioEditarController;
import aquacoding.model.Funcao;
import aquacoding.controller.FuncionarioEditarController;
import aquacoding.model.Funcionario;
import aquacoding.model.Horario;
import aquacoding.model.Setor;
import aquacoding.model.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// Extends javafx.application.Application para user JavaFX
public class Main extends Application {

	public static Stage primaryStage;
	private static BorderPane rootLayout;
	private static String pageTitle = "Controle de Ponto e Acesso";

	@Override
	public void start(Stage stage) throws Exception {
		// Inicia a janela principal (Main.fxml)
		primaryStage = stage;
		initRootLayout();
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Realiza a inicialização da janela princpal
	public static void initRootLayout() {
		try {
			// Carrega o root layout do arquivo fxml.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Mostra a scene contendo o root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(""+Main.class.getResource("application.css"));
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
			primaryStage.setTitle(pageTitle + " - Novo Setor");
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

	// Carrega a view de nova função
	public static void loadNovoFuncaoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Nova Função");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncaoNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de listagem de função
	public static void loadListaFuncaoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Funções");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncaoLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de edição de setor
	public static void loadSetorEditarView(Setor setor) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar Setor");
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
			primaryStage.setTitle(pageTitle + " - Novo Horïário");
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
			primaryStage.setTitle(pageTitle + " - Horários");
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
			primaryStage.setTitle(pageTitle + " - Funcionários");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncionarioLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de editar função
	public static void loadFuncaoEditarView(Funcao funcao) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar Função");
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
			primaryStage.setTitle(pageTitle + " - Novo Usuïário");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/UsuarioNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de listagem de usuários
	public static void loadListaUsuarioView() {
		try {
			primaryStage.setTitle(pageTitle + " - Usuários");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/UsuarioLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de edição de usuários
	public static void loadEditarUsuarioView(Usuario usuario) {
		try {
			primaryStage.setTitle(pageTitle + " - Editar Usuário");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/UsuarioEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Obtem o controller da interface e passa o usuï¿½rio a ser editado
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
			primaryStage.setTitle(pageTitle + " - Funcionário");
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
			primaryStage.setTitle(pageTitle + " - Editar Funcionário");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncionarioEditar.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			
			// Obtem o controller da interface e passa o funcionario a ser editado
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
				primaryStage.setTitle(pageTitle + " - Editar Funcionário");
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
				primaryStage.setTitle(pageTitle + " - Editar Horário");
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
				primaryStage.setTitle(pageTitle + " - Nova Empresa");
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
				primaryStage.setTitle(pageTitle + " - Empresa");
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(ClassLoader.getSystemResource("resources/views/EmpresaVer.fxml"));
				AnchorPane personOverview = (AnchorPane) loader.load();
				rootLayout.setCenter(personOverview);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
