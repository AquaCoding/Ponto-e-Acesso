package aquacoding.controller;

import java.net.URL;
import java.util.ResourceBundle;

import aquacoding.model.Funcao;
import aquacoding.model.Setor;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class FuncaoNovoController implements Initializable {

	@FXML
	TextField funcaoNome;

	@FXML
	ComboBox<Setor> setoresSelect;

	@FXML
	Button cancelar, cadastrar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Preenche o campo de sele��o de setores
		setoresSelect.setItems(FXCollections.observableArrayList(Setor.getAll()));
		setSetoresSelectFactory();

		// Define evento no bot�o cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});

		// Define o evento do bot�o cadastrar
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Cria uma nova funcao
				Funcao f = new Funcao(funcaoNome.getText(), setoresSelect.getSelectionModel().getSelectedItem());

				// Registra nova fun��o no BD
				if(f.create()) {
					CustomAlert.showAlert("Novo Cargo", "Nova cargo cadastrada com sucesso.", AlertType.WARNING);
					Main.loadListaFuncaoView();
				} else {
					CustomAlert.showAlert("Novo Cargo", "Um erro ocorreu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Novo Cargo", ex.getMessage(), AlertType.WARNING);
			}
		});
	}

	private void setSetoresSelectFactory() {
		// Define um novo cell factory
		setoresSelect.setCellFactory(new Callback<ListView<Setor>, ListCell<Setor>>() {
			// Realiza o override do m�todo padr�o
			@Override
			public ListCell<Setor> call(ListView<Setor> param) {
				// Cria uma nova c�lula da lista
				ListCell<Setor> cell = new ListCell<Setor>() {
					// Realiza o overrida do m�todo padr�o para defini��o do nome de exibi��o na lista
					@Override
					protected void updateItem(Setor item, boolean empty) {
						// Chama o construtor padr�o
						super.updateItem(item, empty);

						// Define o nome customizado
						if (item != null) {
                            setText(item.getNome());
                        } else {
                            setText("");
                        }
					}
				};

				// Retorna a c�lula
				return cell;
			}
		});

		setoresSelect.setConverter(new StringConverter<Setor>() {
			@Override
			public String toString(Setor item) {
				if (item != null) {
					return item.getNome();
                } else {
                    return null;
                }
			}

			@Override
			public Setor fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
      });
	}

}
