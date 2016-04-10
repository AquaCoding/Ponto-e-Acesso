package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class EmpresaEditarController implements Initializable {

	@FXML
	TextField empresaNome, empresaRazaoSocial, empresaCNPJ, empresaRua, empresaNumero, empresaBairro, empresaEstado,
			empresaCidade;

	@FXML
	Button cancelar, editar, btnImage;

	@FXML
	Label lblImagePath;

	private File selectedFile;

	private Empresa empresa;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Retorna para o menu de listagem ao clicar em cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Realiza a edição da empresa
		editar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Salva as alterações no objeto
				empresa.setNome(empresaNome.getText());
				empresa.setRazaoSocial(empresaRazaoSocial.getText());
				empresa.setCNPJ(empresaCNPJ.getText());
				empresa.setRua(empresaRua.getText());
				empresa.setNumero(Integer.parseInt(empresaNumero.getText()));
				empresa.setBairro(empresaBairro.getText());
				empresa.setEstado(empresaEstado.getText());
				empresa.setCidade(empresaCidade.getText());

				// Adiciona a imagem ao objeto da empresa
				if (selectedFile != null)
					empresa.setImageURL(selectedFile);
				
				// Tenta atualizar
				if (empresa.update()) {
					CustomAlert.showAlert("Editar Empresa", "Empresa editada com sucesso.", AlertType.WARNING);
					Main.loadEmpresaVer();
				} else {
					CustomAlert.showAlert("Editar Empresa", "Algo errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Editar Empresa", ex.getMessage(), AlertType.WARNING);
			}
		});
		
		btnImage.setOnMouseClicked((MouseEvent e) -> {
			FileChooser fileC = new FileChooser();
			fileC.setTitle("Selecione o logo da Empresa");
			fileC.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
			selectedFile = fileC.showOpenDialog(Main.primaryStage);

			if (selectedFile != null) {
				lblImagePath.setText(selectedFile.getName());
			}
		});

	}
	
	public void setEmpresa(Empresa empresa) {
		ArrayList<Empresa> empresas = Empresa.getAll();
		
		Empresa e = empresas.get(0);
		
		empresaNome.setText(e.getNome());
		empresaRazaoSocial.setText(e.getRazaoSocial());
		empresaCNPJ.setText(e.getCNPJ());
		empresaRua.setText(e.getRua());
		empresaNumero.setText(""+e.getNumero());
		empresaBairro.setText(e.getBairro());
		empresaCidade.setText(e.getCidade());
		empresaEstado.setText(e.getEstado());
	}

}
