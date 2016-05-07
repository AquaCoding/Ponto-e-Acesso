package aquacoding.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import aquacoding.pontoacesso.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jfxtras.labs.util.event.MouseControlUtil;


public class CartaoModeloController implements Initializable {
	
	@FXML
	Pane drawPane;
	
	@FXML
	Button addText, addImage, salvar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Evento de clique para salvar a imagem
		salvar.setOnMouseClicked((MouseEvent e) -> {
			FileChooser fileC = new FileChooser();
			fileC.setTitle("Selecione o local para criar o modelo do cartão");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			fileC.setInitialFileName("Modelo_Cartão_" + formatter.format(LocalDate.now()));
			fileC.getExtensionFilters().add(new ExtensionFilter("Image File (*.png)", "*.png"));
			File localToSave = fileC.showSaveDialog(Main.primaryStage);
			
			if(localToSave != null){
				WritableImage snapshot = drawPane.snapshot(new SnapshotParameters(), null);
	            saveImage(snapshot, localToSave);
			}
		});	 
		
		// Evento de clique para adicionar texto a area de desenho
		addText.setOnMouseClicked((MouseEvent e) -> {
			Label a = new Label("Novo texto");
			MouseControlUtil.makeDraggable(a);
			drawPane.getChildren().add(a);
		});
		
		// Evento de clique para adicionar imagem a area de desenho
		addImage.setOnMouseClicked((MouseEvent e) -> {
			System.out.println(new File("img/app_icon.png").toURI().toString());
			ImageView a = new ImageView(new File("img/app_icon.png").toURI().toString());
			a.setStyle("-fx-background-color: #4CAF50;");
			MouseControlUtil.makeDraggable(a);
			drawPane.getChildren().add(a);
		});
	}
	
	// Salva uma snapshot no formato png em disco
	private void saveImage(WritableImage snapshot, File file) {
	    BufferedImage image;
	    BufferedImage bufferedImage = new BufferedImage(550, 400, BufferedImage.TYPE_INT_ARGB);
	    image = javafx.embed.swing.SwingFXUtils.fromFXImage(snapshot, bufferedImage);
	    
	    try {
	        Graphics2D gd = (Graphics2D) image.getGraphics();
	        gd.translate(drawPane.getWidth(), drawPane.getHeight());
	        ImageIO.write(image, "png", file);
	    } catch (IOException ex) {
	    	System.out.println(ex.getMessage());
	    };
	}
}