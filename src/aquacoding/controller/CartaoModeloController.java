package aquacoding.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;







import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;







import javax.imageio.ImageIO;







import jfxtras.labs.util.event.MouseControlUtil;
import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.LabelStyle;
import aquacoding.utils.MaskField;

@SuppressWarnings("deprecation")
// Utilizando metodo descontinuado .impl_getUrl() pois a classe Image não possui um método para se obter
// A URI da imagem utilizada
public class CartaoModeloController implements Initializable {
	
	private final String SAVED_LABEL_PATH = "data/cartao/labels.data";
	private final String SAVED_IMAGE_PATH = "data/cartao/images.data";
	
	@FXML
	Pane drawPane;
	
	@FXML
	Button cancelar, addText, addImage, addImagePerfil, salvarPNG, salvar, labelDelete, labelEdit, imageDelete, imageEdit;
	
	@FXML
	CheckBox labelNegrito, labelItalico, imageRatio;
	
	@FXML
	AnchorPane labelOptions, imageOptions;
	
	@FXML
	TextField labelSize, imageWidth, imageHeight;
	
	// Armazena informações do label
	private ArrayList<Label> labels = new ArrayList<Label>();
	private HashMap<Label, LabelStyle> labelsStyles = new HashMap<Label, LabelStyle>();
	private Label selectedLabel;
	
	// Armazena informações das imagens
	private ArrayList<ImageView> images = new ArrayList<ImageView>();
	private ImageView selectedImage;
	private ImageView imageProfile;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Oculta os menus de opções
		labelOptions.setVisible(false);
		imageOptions.setVisible(false);
		
		// Carrega os labels e images salvos anteriormente
		loadLabelsAndImage();
		
		// Evento do botão de cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Salva os modelos para carregamentos posteriores
		salvar.setOnMouseClicked((MouseEvent e) -> {
			ArrayList<String> savedLabels = new ArrayList<String>();
			for(Label l: labels) {
				LabelStyle ls = labelsStyles.get(l);
				String labelInfo = l.getText() + "#_" + ls.getFontSize()+ "#_" + ls.isBold() + "#_" + ls.isItalic() + "#_" + l.getLayoutX() + "#_" + l.getLayoutY();
				savedLabels.add(labelInfo);
			}
			
			ArrayList<String> savedImage = new ArrayList<String>();
			for(ImageView i: images) {
				String imageInfo = i.getImage().impl_getUrl() + "#_" + i.getFitWidth() + "#_" + i.getFitHeight() + "#_" + i.isPreserveRatio() + "#_" + i.getLayoutX() + "#_" + i.getLayoutY();
				if(i == imageProfile) {
					imageInfo += "#_true";
				}
				savedImage.add(imageInfo);
			}
			
			// Salva as images no disco
			try {
				Files.write(Paths.get(SAVED_LABEL_PATH), savedLabels);
				Files.write(Paths.get(SAVED_IMAGE_PATH), savedImage);
				
				CustomAlert.showAlert("Modelo do cartão", "Modelo do cartão salvo com sucesso", AlertType.WARNING);
				
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.CARTAO, 0, ActionsCode.SALVOU_MODELO);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		// Evento de clique para salvar a imagem
		salvarPNG.setOnMouseClicked((MouseEvent e) -> {
			FileChooser fileC = new FileChooser();
			fileC.setTitle("Selecione o local para criar o modelo do cartão");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			fileC.setInitialFileName("Modelo_Cartão_" + formatter.format(LocalDate.now()));
			fileC.getExtensionFilters().add(new ExtensionFilter("Image File (*.png)", "*.png"));
			File localToSave = fileC.showSaveDialog(Main.primaryStage);
			
			if(localToSave != null){
				WritableImage snapshot = drawPane.snapshot(new SnapshotParameters(), null);
	            saveImage(snapshot, localToSave);
	            CustomAlert.showAlert("Modelo do cartão", "Modelo do cartão em PNG salvo com sucesso", AlertType.WARNING);
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
				try {
					Path newFile = Files.copy(Paths.get(image.toPath().toString()), Paths.get("data/cartao/images/" + image.getName()), StandardCopyOption.REPLACE_EXISTING);
					
					ImageView a = new ImageView(newFile.toUri().toString());
					a.setFitWidth(100);
					a.setFitHeight(100);
					a.setPreserveRatio(true);
					
					images.add(a);
					
					setClickActionOnImage(a);
					MouseControlUtil.makeDraggable(a);
					drawPane.getChildren().add(a);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		addImagePerfil.setOnMouseClicked((MouseEvent e) -> {
			File i = new File(aquacoding.utils.Image.PROFILE_IMAGE_DEFAULT);
			ImageView a = new ImageView(i.toURI().toString());
			a.setFitWidth(100);
			a.setFitHeight(100);
			a.setPreserveRatio(true);
			
			images.add(a);
			imageProfile = a;
			
			setClickActionOnImage(a);
			MouseControlUtil.makeDraggable(a);
			drawPane.getChildren().add(a);
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
				try {
					drawPane.getChildren().remove(selectedImage);
					images.remove(selectedImage);
					
					// Somente deleta a imagem se não for uma imagem de perfil padrão
					if(selectedImage != imageProfile) {
						String url = selectedImage.getImage().impl_getUrl().substring(6, selectedImage.getImage().impl_getUrl().length());
						Files.delete(Paths.get(url.replaceAll("%20", " ")));
					}
					
					selectedImage = null;
					imageOptions.setVisible(false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
	
	private void loadLabelsAndImage() {
		try {
			// Carrega os labels
			if(Files.isReadable(Paths.get(SAVED_LABEL_PATH))) {
				List<String> savedLabels = Files.readAllLines(Paths.get(SAVED_LABEL_PATH));
				
				for(String l: savedLabels) {
					// Separa as infos do labels
					String infos[] = l.split("#_");
					
					// Cria o label
					Label a = new Label(infos[0]);
					MouseControlUtil.makeDraggable(a);
					setClickActionOnLabel(a);
					
					// Cria os styles
					LabelStyle ls = new LabelStyle();
					ls.setFontSize(Integer.parseInt(infos[1]));
					ls.setBold(Boolean.valueOf(infos[2]));
					ls.setItalic(Boolean.valueOf(infos[3]));
					
					// Aplica o style
					a.setStyle(ls.toStyle());
					
					// Adiciona o label ao panel
					drawPane.getChildren().add(a);
					
					// Posiciona o elemento no local correto
					a.setLayoutX(Double.parseDouble(infos[4]));
					a.setLayoutY(Double.parseDouble(infos[5]));
					
					// Adiciona o label ao arraylist e o style ao hashmap
					labels.add(a);
					labelsStyles.put(a, ls);
				}
			}
			
			// Carrega as imagens
			if(Files.isReadable(Paths.get(SAVED_IMAGE_PATH))) {
				List<String> savedImage = Files.readAllLines(Paths.get(SAVED_IMAGE_PATH));
				
				for(String l: savedImage) {
					// Separa as infos do labels
					String infos[] = l.split("#_");
					
					// Cria a ImageView
					ImageView a = new ImageView(infos[0]);
					MouseControlUtil.makeDraggable(a);
					setClickActionOnImage(a);
					
					// Define informações da image
					a.setFitWidth(Double.parseDouble(infos[1]));
					a.setFitHeight(Double.parseDouble(infos[2]));
					a.setPreserveRatio(Boolean.valueOf(infos[3]));
					a.setLayoutX(Double.parseDouble(infos[4]));
					a.setLayoutY(Double.parseDouble(infos[5]));
					
					// Coloca image no painel
					drawPane.getChildren().add(a);
					
					// Registra no ArrayList
					images.add(a);
					
					// Verifica se é a imagem do perfil
					if(infos.length == 7) {
						if(Boolean.valueOf(infos[6])) {
							imageProfile = a;
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFuncionario(Funcionario funcionario) {
		for(Label l: labels) {
			if(l.getText().equals("#nome")) {
				l.setText(funcionario.getNome() + " " + funcionario.getSobrenome());
			} else if (l.getText().equals("#cpf")) {
				l.setText(funcionario.getCpf());
			} else if(l.getText().equals("#cargo")) {
				l.setText(funcionario.getFuncao().getNome());
			}
			
			if(imageProfile != null) {				
				File f = funcionario.getProfileImage();
				if (f.exists()) {
					Image i = new Image(f.toURI().toString());
					imageProfile.setImage(i);
				}					
			}
			// Oculta o botão de salvar
			salvar.setVisible(false);
		}
	}
}