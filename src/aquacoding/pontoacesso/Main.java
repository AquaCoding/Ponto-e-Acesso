package aquacoding.pontoacesso;

import java.io.IOException;

import aquacoding.controller.FuncaoEditarController;
import aquacoding.controller.SetorEditarController;
import aquacoding.model.Funcao;
import aquacoding.model.Setor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// Extends javafx.application.Application para user JavaFX
public class Main extends Application {

	private static Stage primaryStage;
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

	// Realiza a inicializa��o da janela princpal
	public static void initRootLayout() {
		try {
			// Carrega o root layout do arquivo fxml.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Mostra a scene contendo o root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();

			loadMainView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de novo setor dentro da janela principal
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

	// Carrega a view de novo setor dentro da janela principal
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

	// Carrega a view de novo setor dentro da janela principal
	public static void loadNovoFuncaoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Nova Fun��o");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncaoNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de novo setor dentro da janela principal
	public static void loadListaFuncaoView() {
		try {
			primaryStage.setTitle(pageTitle + " - Fun��es");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/FuncaoLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de edi��o do setor dentro da janela principal
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

	// Carrega a view de novo setor dentro da janela principal
	public static void loadNovoHorarioView() {
		try {
			primaryStage.setTitle(pageTitle + " - Novo Hor�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/HorarioNovo.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de novo setor dentro da janela principal
	public static void loadListaHorarioView() {
		try {
			primaryStage.setTitle(pageTitle + " - Novo Hor�rio");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClassLoader.getSystemResource("resources/views/HorarioLista.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carrega a view de edi��o da fun��o dentro da janela principal
		public static void loadFuncaoEditarView(Funcao funcao) {
			try {
				primaryStage.setTitle(pageTitle + " - Editar Setor");
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

		// Carrega a view de novo usuario dentro da janela principal
		public static void loadUsuarioNovoView() {
			try {
				primaryStage.setTitle(pageTitle + " - Novo Usu�rio");
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(ClassLoader.getSystemResource("resources/views/UsuarioNovo.fxml"));
				AnchorPane personOverview = (AnchorPane) loader.load();
				rootLayout.setCenter(personOverview);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
