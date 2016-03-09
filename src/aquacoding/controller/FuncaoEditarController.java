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

public class FuncaoEditarController implements Initializable {

	@FXML
	TextField funcaoNome;
	
	@FXML
	ComboBox<Setor> setoresSelect;
	
	@FXML
	Button cancelar, editar;
	
	private Funcao funcao;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Preenche o campo de seleção de setores
		setoresSelect.setItems(FXCollections.observableArrayList(Setor.getAll()));
		setSetoresSelectFactory();
		
		// Retorna para o menu de listagem ao clicar em cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaFuncaoView();
		});
		
		// Realiza a edição da função
		editar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Salva as alterações no objeto
				funcao.setNome(funcaoNome.getText());
				funcao.setSetor(setoresSelect.getSelectionModel().getSelectedItem());
				
				// Tenta atualizar
				if(funcao.update()) {
					CustomAlert.showAlert("Editar Função", "Função editada com sucesso.", AlertType.WARNING);
					Main.loadListaFuncaoView();
				} else {
					CustomAlert.showAlert("Editar Função", "Algo errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Editar Função", ex.getMessage(), AlertType.WARNING);
			}
		});
	}

	// Define a função que esta sendo usada para a edição
	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
		funcaoNome.setText(funcao.getNome());
		setoresSelect.getSelectionModel().select(funcao.getSetor());
	}
	
	// Define o comportamento do combobox
	private void setSetoresSelectFactory() {
		// Define um novo cell factory
		setoresSelect.setCellFactory(new Callback<ListView<Setor>, ListCell<Setor>>() {
			// Realiza o override do método padrão
			@Override
			public ListCell<Setor> call(ListView<Setor> param) {
				// Cria uma nova célula da lista
				ListCell<Setor> cell = new ListCell<Setor>() {
					// Realiza o overrida do método padrão para definição do nome de exibição na lista
					@Override
					protected void updateItem(Setor item, boolean empty) {
						// Chama o construtor padrão
						super.updateItem(item, empty);
						
						// Define o nome customizado
						if (item != null) {
                            setText(item.getNome());
                        } else {
                            setText("");
                        }
					}
				};
				
				// Retorna a célula
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
