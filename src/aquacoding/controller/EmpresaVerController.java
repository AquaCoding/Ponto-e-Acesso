package aquacoding.controller;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class EmpresaVerController implements Initializable{

	@FXML
	Label empresaNome, empresaRazaoSocial, empresaCNPJ, empresaRua, empresaNumero, empresaBairro, empresaCidade, empresaEstado;
	
	@FXML
	Button cancelar, editar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Evento do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		// Evento do botão de editar
		editar.setOnMouseClicked((MouseEvent e) -> {
			
		});
		
		loadEmpresaInfo();
	}
	
	private void loadEmpresaInfo() {
		// Obtem a empresa
		ArrayList<Empresa> empresas = Empresa.getAll();
		
		if(empresas.size() > 0) {
			// Obtem a empresa principal
			Empresa e = empresas.get(0);
			
			// Define os labels
			empresaNome.setText(e.getNome());
			empresaRazaoSocial.setText(e.getRazaoSocial());
			empresaCNPJ.setText(e.getCNPJ());
			empresaRua.setText(e.getRua());
			empresaNumero.setText(""+e.getNumero());
			empresaBairro.setText(e.getBairro());
			empresaCidade.setText(e.getCidade());
			empresaEstado.setText(e.getEstado());
		} else {
			CustomAlert.showAlert("Empresa não cadastrada", "É preciso cadastrar uma empresa antes.", AlertType.WARNING);
			Main.loadEmpresaNovoView();
		}		
	}

}
