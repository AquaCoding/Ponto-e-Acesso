package aquacoding.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import aquacoding.pontoacesso.Main;

public class CustomAlert {
	public static Optional<ButtonType> showAlert(String title, String content, AlertType alertType) {
		Alert a = new Alert(alertType);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        
        // Define o estilo do alert
        //a.getDialogPane().getScene().getStylesheets().add(""+Main.class.getResource("application.css"));
        
        // Define os icons dos alerts
        //Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        //stage.getIcons().add(new Image(""+Main.class.getResource("icon.png")));
        
        // Exibe e retorna o resultado do botão clicado
        return  a.showAndWait();
	}
	
	public static Optional<ButtonType> showAlertWithCustomImage(String title, String content, AlertType alertType, String imagePath) {
		Alert a = new Alert(alertType);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        
        // Define a imagem
        a.setGraphic(new ImageView(Main.class.getResource(imagePath).toString()));
        
        // Define o estilo do alert
        a.getDialogPane().getScene().getStylesheets().add(""+Main.class.getResource("application.css"));
        
        // Define os icons dos alerts
        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(""+Main.class.getResource("icon.png")));
        
        // Exibe e retorna o resultado do botão clicado
        return  a.showAndWait();
	}
}
