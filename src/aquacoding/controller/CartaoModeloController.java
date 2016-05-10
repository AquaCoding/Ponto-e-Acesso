package aquacoding.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.LabelStyle;
import aquacoding.utils.MaskField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jfxtras.labs.util.event.MouseControlUtil;


public class CartaoModeloController implements Initializable {
	
	@FXML
	Pane drawPane;
	
	@FXML
	Button addText, addImage, salvar, labelDelete, labelEdit, imageDelete, imageEdit;
	
	@FXML
	CheckBox labelNegrito, labelItalico, imageRatio;
	
	@FXML
	AnchorPane labelOptions, imageOptions;
	
	@FXML
	TextField labelSize, imageWidth, imageHeight;
	
	private ArrayList<Label> labels = new ArrayList<Label>();
	private HashMap<Label, LabelStyle> labelsStyles = new HashMap<Label, LabelStyle>();
	private Label selectedLabel;
	
	private ArrayList<ImageView> images = new ArrayList<ImageView>();
	private ImageView selectedImage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Oculta os menus de opções
		labelOptions.setVisible(false);
		imageOptions.setVisible(false);
		
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
			String name = CustomAlert.showDialogWithInput("Novo label", "Insira o conteudo do label");
			
			if(name == null || name.equals("")) {
				CustomAlert.showAlert("Novo label", "O nome não pode ser vazio", AlertType.WARNING);
			} else {
				Label a = new Label(name);
				labels.add(a);
				
				LabelStyle ls = new LabelStyle();
				labelsStyles.put(a, ls);
				
				setClickActionOnLabel(a);
				MouseControlUtil.makeDraggable(a);
				drawPane.getChildren().add(a);
			}
		});
		
		// Evento de clique para adicionar imagem a area de desenho
		addImage.setOnMouseClicked((MouseEvent e) -> {
			FileChooser fileC = new FileChooser();
			
			fileC.setTitle("Selecione a imagem");
			fileC.getExtensionFilters().add(new ExtensionFilter("Image File (*.png)", "*.png"));
			File image = fileC.showOpenDialog(Main.primaryStage);
			
			if(image != null) {
				ImageView a = new ImageView(image.toURI().toString());
				a.setFitWidth(100);
				a.setFitHeight(100);
				a.setPreserveRatio(true);
				
				images.add(a);
				
				setClickActionOnImage(a);
				MouseControlUtil.makeDraggable(a);
				drawPane.getChildren().add(a);
			}
		});
		
		// Configura os options dos labels
		configureLabelsOptions();
		
		// Configura os options das imagens
		configureImagesOptions();
		
		// Configura o drawPane
		setOnDrawPaneClick();
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
	
	// Remove todas as seleções ao clicar no drawPane
	private void setOnDrawPaneClick() {
		drawPane.setOnMouseClicked((MouseEvent e) -> {
			clearSelection();
		});
	}
	
	// Limpa todas as seleções
	private void clearSelection() {
		if(selectedLabel != null) {
			selectedLabel.getStyleClass().remove("selected");
			selectedLabel = null;
		}
		
		if(selectedImage != null) {
			selectedImage.getStyleClass().remove("selected");
			selectedImage = null;
		}
		
		labelOptions.setVisible(false);
		imageOptions.setVisible(false);
	}
	
	// Configura as opções dos labels
	private void configureLabelsOptions() {
		// Evento das opções dos labels
		labelDelete.setOnMouseClicked((MouseEvent e) -> {
			if(selectedLabel != null) {
				drawPane.getChildren().remove(selectedLabel);
				labels.remove(selectedLabel);
				selectedLabel = null;
				labelOptions.setVisible(false);
			}
		});
		
		labelEdit.setOnMouseClicked((MouseEvent e) -> {
			if(selectedLabel != null) {
				String name = CustomAlert.showDialogWithInput("Editando label", "Insira o conteudo do label");
				
				if(name == null || name.equals("")) {
					CustomAlert.showAlert("Editando label", "O nome não pode ser vazio", AlertType.WARNING);
				} else {
					selectedLabel.setText(name);
				}
			}
		});
		
		MaskField.intMask(labelSize);
		labelSize.setOnAction((ActionEvent e) -> {
			int fontSize = Integer.parseInt(labelSize.getText());
			labelsStyles.get(selectedLabel).setFontSize(fontSize);
			selectedLabel.setStyle(labelsStyles.get(selectedLabel).toStyle());
		});
		
		labelNegrito.setOnAction((ActionEvent e) -> {
			labelsStyles.get(selectedLabel).setBold(labelNegrito.isSelected());
			selectedLabel.setStyle(labelsStyles.get(selectedLabel).toStyle());
		});
		
		labelItalico.setOnAction((ActionEvent e) -> {
			labelsStyles.get(selectedLabel).setItalic(labelItalico.isSelected());
			selectedLabel.setStyle(labelsStyles.get(selectedLabel).toStyle());
		});
	}
	
	// Adiciona o evento de click no label para selecionar
	private void setClickActionOnLabel(Label l) {
		l.setOnMouseClicked((MouseEvent e) -> {
			// Limpa a seleção
			clearSelection();
			
			// Seleciona o label clicado
			l.getStyleClass().add("selected");
			labelOptions.setVisible(true);
			selectedLabel = l;
			
			// Evita a propagação do click
			e.consume();
		});
	}
	
	// Configura as opções das imagens
	private void configureImagesOptions() {
		// Evento das opções dos labels
		imageDelete.setOnMouseClicked((MouseEvent e) -> {
			if(selectedImage != null) {
				drawPane.getChildren().remove(selectedImage);
				images.remove(selectedImage);
				selectedImage = null;
				imageOptions.setVisible(false);
			}
		});
		
		imageEdit.setOnMouseClicked((MouseEvent e) -> {
			if(selectedImage != null) {
				FileChooser fileC = new FileChooser();
				
				fileC.setTitle("Selecione a imagem");
				fileC.getExtensionFilters().add(new ExtensionFilter("Image File (*.png)", "*.png"));
				File image = fileC.showOpenDialog(Main.primaryStage);
				
				if(image != null) {
					selectedImage.setImage(new Image(image.toURI().toString()));
				}
			}
		});
		
		MaskField.intMask(imageWidth);
		MaskField.intMask(imageHeight);
		
		imageRatio.setOnAction((ActionEvent e) -> {
			if(imageRatio.isSelected()) {
				imageHeight.setEditable(false);
				imageHeight.setText("");
			} else {
				imageHeight.setEditable(true);
			}
			
			selectedImage.setPreserveRatio(imageRatio.isSelected());
		});
		
		imageWidth.setOnAction((ActionEvent e) -> {
			int width = Integer.parseInt(imageWidth.getText());
			selectedImage.setFitWidth(width);
			
			if(selectedImage.isPreserveRatio())
				selectedImage.setFitHeight(width);
		});
		
		imageHeight.setOnAction((ActionEvent e) -> {
			int height = Integer.parseInt(imageHeight.getText());
			selectedImage.setFitHeight(height);
		});
	}
	
	// Adiciona o evento de click no label para selecionar
	private void setClickActionOnImage(ImageView i) {
		i.setOnMouseClicked((MouseEvent e) -> {
			// Limpa a seleção
			clearSelection();
			
			// Seleciona o label clicado
			i.getStyleClass().add("selected");
			imageOptions.setVisible(true);
			selectedImage = i;
			
			// Evita a propagação do click
			e.consume();
		});
	}
}