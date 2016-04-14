package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Funcionario;
import aquacoding.model.Horario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FuncionarioEditarController implements Initializable {

	// Obtem os elementos FXML
	@FXML
	Button cancelar, alterar, btnImage;

	@FXML
	Label lblImagePath;

	@FXML
	TextField funcionarioNome, funcionarioSobrenome, funcionarioRG, funcionarioCPF, funcionarioCTPS,
	funcionarioTelefone, funcionarioRua, funcionarioNumero, funcionarioBairro,
	funcionarioCidade, funcionarioEstado, funcionarioSalarioHoras;

	@FXML
	ComboBox<Horario> horarioSelect, horarioSelect2;

	private Funcionario funcionario;
	private File selectedFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Adiciona mascaras aos campos
		MaskField.phoneMask(funcionarioTelefone);
		MaskField.intMask(funcionarioNumero);
		MaskField.moneyMask(funcionarioSalarioHoras);
		MaskField.cpfMaks(funcionarioCPF);
		MaskField.rgMaks(funcionarioRG);

		// Cancela a edição e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaFuncionarioView();
		});

		// Preenche o campo de seleção do horario
				horarioSelect.setItems(FXCollections.observableArrayList(Horario.getAll()));
				horarioSelect2.setItems(FXCollections.observableArrayList(Horario.getAll()));
				
		// Tenta realizar a edição
		alterar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Atualiza a info do funcionario
				funcionario.setNome(funcionarioNome.getText());
				funcionario.setNome(funcionarioNome.getText());
				funcionario.setSobrenome(funcionarioSobrenome.getText());
				funcionario.setRg(funcionarioRG.getText());
				funcionario.setCpf(funcionarioCPF.getText());
				funcionario.setCtps(funcionarioCTPS.getText());
				funcionario.setTelefone(funcionarioTelefone.getText());
				funcionario.setRua(funcionarioRua.getText());
				funcionario.setNumero(Integer.parseInt(funcionarioNumero.getText()));
				funcionario.setBairro(funcionarioBairro.getText());
				funcionario.setCidade(funcionarioCidade.getText());
				funcionario.setEstado(funcionarioEstado.getText());
				funcionario.setSalarioHoras(Double.parseDouble(funcionarioSalarioHoras.getText()));

				if(horarioSelect != null){
					funcionario.setHorario(horarioSelect.getSelectionModel().getSelectedItem());
				}
				
				if(horarioSelect2 != null){
					funcionario.setHorario(horarioSelect2.getSelectionModel().getSelectedItem());
				}

				// Adiciona a imagem ao objeto do funcionario
				if (selectedFile != null)
					funcionario.setImageURL(selectedFile);

				// Tenta alterar o funcionario no BD
				if (funcionario.update()) {
					// funcionario alterado com sucesso
					CustomAlert.showAlert("Editar Funcionário", "Funcionário alterado com sucesso.", AlertType.WARNING);
					Main.loadListaFuncionarioView();
				} else {
					// Erro ao cria o funcionario
					CustomAlert.showAlert("Editar Funcionário", "Algo deu errado.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				String message = "";
				if(ex.getMessage() == "empty String") {
					message = "Pagamento hora precisa ser um número.";
				} else if(ex.getMessage().matches("^For input string: \"[\\S ]{0,}\"$")) {
					message = "Número precisa ser um número inteiro.";
				} else {
					message = ex.getMessage();
				}

				CustomAlert.showAlert("Novo Funcionário", message, AlertType.WARNING);
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

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
		funcionarioNome.setText(funcionario.getNome());
		funcionarioSobrenome.setText(funcionario.getSobrenome());
		funcionarioRG.setText(funcionario.getRg());
		funcionarioCPF.setText(funcionario.getCpf());
		funcionarioCTPS.setText(funcionario.getCtps());
		funcionarioTelefone.setText(funcionario.getTelefone());
		funcionarioRua.setText(funcionario.getRua());
		funcionarioNumero.setText(""+funcionario.getNumero());
		funcionarioBairro.setText(funcionario.getBairro());
		funcionarioCidade.setText(funcionario.getCidade());
		funcionarioEstado.setText(funcionario.getEstado());
		funcionarioSalarioHoras.setText(""+funcionario.getSalarioHoras());
	}

}
