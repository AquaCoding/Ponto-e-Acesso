package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Empresa;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class EmpresaNovoController implements Initializable {
	
	@FXML
	TextField empresaNome, empresaRazaoSocial, empresaCNPJ, empresaRua, empresaNumero, empresaBairro, empresaEstado, empresaCidade;

	@FXML
	Button cancelar, cadastrar, btnImage;
	
	@FXML
	Label lblImagePath;
	
	private File selectedFile;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Define o evento padrão do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				Empresa emp = new Empresa.Builder().setNome(empresaNome.getText())
						.setRazaoSocial(empresaRazaoSocial.getText())
						.setCNPJ(empresaCNPJ.getText())
						.setRua(empresaRua.getText())
						.setNumero(Integer.parseInt(empresaNumero.getText()))
						.setBairro(empresaBairro.getText())
						.setCidade(empresaCidade.getText())
						.setEstado(empresaEstado.getText())
						.build();
				
				// Adiciona a imagem ao objeto da empresa
				if (selectedFile != null)
					emp.setImageURL(selectedFile);
				
				// Tenta registar a empresa no BD
				if (emp.create()) {
					// Empresa criado com sucesso
					CustomAlert.showAlert("Cadastro da empresa", "Empresa cadastrada com sucesso",
							AlertType.WARNING);
					Main.initRootLayout();
					Main.loadEmpresaVer();
				} else {
					// Erro ao criar o empresa
					CustomAlert.showAlert("Cadastro da empresa", "Algo deu errado", AlertType.WARNING);
				}
			} catch(RuntimeException ex) {
				CustomAlert.showAlert("Cadastro da empresa", ex.getMessage(), AlertType.WARNING);
			}
		});
		
		btnImage.setOnMouseClicked((MouseEvent e) -> {
			FileChooser fileC = new FileChooser();
			fileC.setTitle("Selecione a imagem do funcionário");
			fileC.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
			selectedFile = fileC.showOpenDialog(Main.primaryStage);

			if (selectedFile != null) {
				lblImagePath.setText(selectedFile.getName());
			}
		});
		
	}

}
