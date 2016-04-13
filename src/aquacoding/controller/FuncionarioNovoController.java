package aquacoding.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import aquacoding.model.Funcionario;
import aquacoding.model.Horario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.MaskField;

public class FuncionarioNovoController implements Initializable {

	// Obtem os elementos FXML
	@FXML
	Button cancelar, cadastrar, btnImage;

	@FXML
	TextField funcionarioNome, funcionarioSobrenome, funcionarioRG, funcionarioCPF, funcionarioCTPS,
			funcionarioTelefone, funcionarioRua, funcionarioNumero, funcionarioBairro, funcionarioCidade,
			funcionarioEstado, funcionarioSalarioHoras;

	@FXML
	Label lblImagePath;

	@FXML
	ComboBox<Horario> horarioSelect, horarioSelect2;

	private File selectedFile;

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Adiciona mascaras aos campos
		MaskField.phoneMask(funcionarioTelefone);
		MaskField.intMask(funcionarioNumero);
		MaskField.moneyMask(funcionarioSalarioHoras);
		MaskField.cpfMaks(funcionarioCPF);
		MaskField.rgMaks(funcionarioRG);

		// Cancela o cadastro e retorna pra tela inicial
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Preenche o campo de seleção do horario
		horarioSelect.setItems(FXCollections.observableArrayList(Horario.getAll()));
		horarioSelect2.setItems(FXCollections.observableArrayList(Horario.getAll()));

		setHorarioSelectFactory();

		// Tenta realizar o cadastro
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				Funcionario f;
				// Cria um novo objeto do Funcionario
				if( (horarioSelect.getSelectionModel().getSelectedItem() != null) && (horarioSelect2.getSelectionModel().getSelectedItem() != null)){
					f = new Funcionario.Builder().setNome(funcionarioNome.getText())
										.setSobrenome(funcionarioSobrenome.getText()).setRg(funcionarioRG.getText())
										.setCpf(funcionarioCPF.getText()).setCtps(funcionarioCTPS.getText())
										.setTelefone(funcionarioTelefone.getText()).setRua(funcionarioRua.getText())
										.setNumero(Integer.parseInt(funcionarioNumero.getText())).setBairro(funcionarioBairro.getText())
										.setCidade(funcionarioCidade.getText()).setEstado(funcionarioEstado.getText())
										.setSalarioHoras(Double.parseDouble(funcionarioSalarioHoras.getText()))
										.setHorario(horarioSelect.getSelectionModel().getSelectedItem())
										.setHorario(horarioSelect2.getSelectionModel().getSelectedItem())
										.build();

				} else {
					 f = new Funcionario.Builder().setNome(funcionarioNome.getText())
										.setSobrenome(funcionarioSobrenome.getText()).setRg(funcionarioRG.getText())
										.setCpf(funcionarioCPF.getText()).setCtps(funcionarioCTPS.getText())
										.setTelefone(funcionarioTelefone.getText()).setRua(funcionarioRua.getText())
										.setNumero(Integer.parseInt(funcionarioNumero.getText())).setBairro(funcionarioBairro.getText())
										.setCidade(funcionarioCidade.getText()).setEstado(funcionarioEstado.getText())
										.setSalarioHoras(Double.parseDouble(funcionarioSalarioHoras.getText()))
										.setHorario(horarioSelect.getSelectionModel().getSelectedItem())
										.build();
				}


				// Adiciona a imagem ao objeto do funcionario
				if (selectedFile != null)
					f.setImageURL(selectedFile);

				// Tenta registar o Funcionario no BD
				if (f.create()) {
					// Funcionario criado com sucesso
					CustomAlert.showAlert("Novo Funcionário", "Novo Funcionário cadastrado com sucesso",
							AlertType.WARNING);
					Main.loadListaFuncionarioView();
				} else {
					// Erro ao criar o Funcionario
					CustomAlert.showAlert("Novo Funcionario", "Algo deu errado", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				String message = "";
				if (ex.getMessage() == "empty String") {
					message = "Pagamento hora precisa ser um número";
				} else if (ex.getMessage().matches("^For input string: \"[\\S ]{0,}\"$")) {
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

			if (selectedFile != null) {
				lblImagePath.setText(selectedFile.getName());
			}
		});
	}

	private void setHorarioSelectFactory() {
		// Define um novo cell factory
		horarioSelect.setCellFactory(new Callback<ListView<Horario>, ListCell<Horario>>() {
			// Realiza o override do método padrão
			@Override
			public ListCell<Horario> call(ListView<Horario> param) {
				// Cria uma nova célula da lista
				ListCell<Horario> cell = new ListCell<Horario>() {
					// Realiza o overrida do método padrão para definição do
					// nome de exibição na lista
					@Override
					protected void updateItem(Horario item, boolean empty) {
						// Chama o construtor padrão
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
							setText(item.toString());
						} else {
							setText("");
						}
					}
				};
				// Retorna a célula
				return cell;
			}
		});
	}
}
