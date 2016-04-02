package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Empresa;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class EmpresaNovoController implements Initializable {
	
	@FXML
	TextField empresaNome, empresaRazaoSocial, empresaCNPJ, empresaRua, empresaNumero, empresaBairro, empresaEstado, empresaCidade;

	@FXML
	Button cancelar, cadastrar;
	
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
				
				// Tenta registar a empresa no BD
				if (emp.create()) {
					// Empresa criado com sucesso
					CustomAlert.showAlert("Cadastro da empresa", "Empresa cadastrada com sucesso",
							AlertType.WARNING);
					Main.loadEmpresaVer();
				} else {
					// Erro ao criar o empresa
					CustomAlert.showAlert("Cadastro da empresa", "Algo deu errado", AlertType.WARNING);
				}
			} catch(RuntimeException ex) {
				CustomAlert.showAlert("Cadastro da empresa", ex.getMessage(), AlertType.WARNING);
			}
		});
	}

}
