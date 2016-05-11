package aquacoding.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import aquacoding.pontoacesso.Main;

public class CustomAlert {
	public static Optional<ButtonType> showAlert(String title, String content, AlertType alertType) {
		Alert a = new Alert(alertType);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);        
        
        //Define estilo e icone
        applyAlertStyleAndIcon(a);
        
        // Exibe e retorna o resultado do botão clicado
        return  a.showAndWait();
	}
	
	public static String showDialogWithInput(String title, String message) {
		// Cria um dialog
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(null);
		dialog.setContentText(message);
		
		// Remove a imagem e define o estilo da aplicação
		dialog.setGraphic(null);
		applyDialogStyleAndIcon(dialog);

		//  Obtem o resultado
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    return result.get();
		}
		
        return null;
	}
	
	public static Optional<ButtonType> showAlertWithCustomImage(String title, String content, AlertType alertType, String imagePath) {
		Alert a = new Alert(alertType);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        
        // Define a imagem
        a.setGraphic(new ImageView(Main.class.getResource(imagePath).toString()));
        
        //Define estilo e icone
        applyAlertStyleAndIcon(a);
        
        // Exibe e retorna o resultado do botão clicado
        return  a.showAndWait();
	}
	
	// Exibe um botão para realizar perguntas, retorna true se SIM e false se NÃO
	public static boolean showConfirmationAlert(String title, String content) {
		Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        
        //Define estilo e icone
        applyAlertStyleAndIcon(a);
        
        // Define os botões do alert
        ButtonType yesButton = new ButtonType("Sim", ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Não", ButtonData.CANCEL_CLOSE);
        
        a.getButtonTypes().setAll(yesButton, noButton);
        
        // Exibe e retorna o resultado do botão clicado
        Optional<ButtonType> res = a.showAndWait();
        
        if(res.get() == yesButton)
        	return true;
        
        return false;
	}
	
	// Aplica estilo e icones aos alerts
	private static void applyAlertStyleAndIcon(Alert a) {
		// Define o estilo do alert
        a.getDialogPane().getScene().getStylesheets().add(""+Main.class.getResource("application.css"));
        
        // Define os icons dos alerts
        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new javafx.scene.image.Image("file:img/app_icon.png"));
	}
	
	private static void applyDialogStyleAndIcon(TextInputDialog a) {
		// Define o estilo do alert
        a.getDialogPane().getScene().getStylesheets().add(""+Main.class.getResource("application.css"));
        
        // Define os icons dos alerts
        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new javafx.scene.image.Image("file:img/app_icon.png"));
	}
}
