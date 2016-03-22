package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import aquacoding.model.Funcionario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;

public class FuncionarioNovoController implements Initializable {
	
	// Obtem os elementos FXML
	@FXML
	Button cancelar, cadastrar, btnImage;
	
	@FXML
	TextField funcionarioNome, funcionarioSobrenome, funcionarioRG, funcionarioCPF, funcionarioCTPS, 
	funcionarioTelefone, funcionarioRua, funcionarioNumero, funcionarioBairro, 
	funcionarioCidade, funcionarioEstado, funcionarioSalarioHoras;
	
	@FXML
	Label lblImagePath;
	
	private File selectedFile;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Adiciona mascaras aos campos
		MaskField.phoneMask(funcionarioTelefone);
		MaskField.intMask(funcionarioNumero);
		MaskField.moneyMask(funcionarioSalarioHoras);
		
		// Cancela o cadastro e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Tenta realizar o cadastro
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Cria um novo objeto do Funcionario
				Funcionario f = new Funcionario.Builder()
						.setNome(funcionarioNome.getText())
						.setSobrenome(funcionarioSobrenome.getText())
						.setRg(funcionarioRG.getText())
						.setCpf(funcionarioCPF.getText())
						.setCtps(funcionarioCTPS.getText())
						.setTelefone(funcionarioTelefone.getText())
						.setRua(funcionarioRua.getText())
						.setNumero(Integer.parseInt(funcionarioNumero.getText()))
						.setBairro(funcionarioBairro.getText())
						.setCidade(funcionarioCidade.getText())
						.setEstado(funcionarioEstado.getText())
						.setSalarioHoras(Double.parseDouble(funcionarioSalarioHoras.getText()))
						.build();
				
				// Adiciona a imagem ao objeto do funcionario
				if(selectedFile != null)
					f.setImageURL(selectedFile);
				
				// Tenta registar o Funcionario no BD
				if(f.create()) {
					// Funcionario criado com sucesso
					CustomAlert.showAlert("Novo Funcionario", "Novo Funcionario cadastrado com sucesso", AlertType.WARNING);
					Main.loadListaFuncionarioView();
				} else {
					// Erro ao criar o Funcionario
					CustomAlert.showAlert("Novo Funcionario", "Algo deu errado", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				String message = "";
				if(ex.getMessage() == "empty String") {
					message = "Pagamento hora precisa ser um número";
				} else if(ex.getMessage().matches("^For input string: \"[\\S ]{0,}\"$")) {
					message = "Número precisa ser um número inteiro";
				} else {
					message = ex.getMessage();
				}
					
				CustomAlert.showAlert("Novo Funcionario", message, AlertType.WARNING);
			} catch (Exception ex) {
				
			}
		});
		
		btnImage.setOnMouseClicked((MouseEvent e) -> {
			FileChooser fileC = new FileChooser();
			fileC.setTitle("Selecione a imagem do funcionário");
			fileC.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
			selectedFile = fileC.showOpenDialog(Main.primaryStage);
			
			if(selectedFile != null) {
				lblImagePath.setText(selectedFile.getName());
			}
		});
	}
}
