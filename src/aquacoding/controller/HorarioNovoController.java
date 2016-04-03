package aquacoding.controller;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;




import aquacoding.model.Horario;
import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class HorarioNovoController implements Initializable {
	
	@FXML
	TextField horarioNome;
	
	@FXML
	ComboBox<String> horarioInicioHora, horarioInicioMinuto, horarioTerminoHora, horarioTerminoMinuto;
	
	@FXML
	ComboBox<String> almocoInicioHora, almocoInicioMinuto, almocoTerminoHora, almocoTerminoMinuto;
	
	@FXML
	Button cancelar, cadastrar;

	@SuppressWarnings("deprecation")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Configura as horas e minutos dos combo box
		defineTime(horarioInicioHora, horarioInicioMinuto);
		defineTime(horarioTerminoHora, horarioTerminoMinuto);
		defineTime(almocoInicioHora, almocoInicioMinuto);
		defineTime(almocoTerminoHora, almocoTerminoMinuto);
		
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Horario de inicio e fim do turno
				Time inicio = new Time(Integer.parseInt(horarioInicioHora.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioInicioMinuto.getSelectionModel().getSelectedItem()), 0);
				Time termino = new Time(Integer.parseInt(horarioTerminoHora.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioTerminoMinuto.getSelectionModel().getSelectedItem()), 0);
				
				// Horario de inicio e fim do almo�o
				Time inicioAlmoco = new Time(Integer.parseInt(almocoInicioHora.getSelectionModel().getSelectedItem()), Integer.parseInt(almocoInicioMinuto.getSelectionModel().getSelectedItem()), 0);
				Time terminoAlmoco = new Time(Integer.parseInt(almocoTerminoHora.getSelectionModel().getSelectedItem()), Integer.parseInt(almocoTerminoMinuto.getSelectionModel().getSelectedItem()), 0);
				
				// Cria novo horario
				Horario h = new Horario(horarioNome.getText(), inicio , termino, inicioAlmoco, terminoAlmoco);
				
				// Salva novo horario no BD
				if(h.create()) {
					CustomAlert.showAlert("Novo Hor�rio", "Novo hor�rio cadastrado com sucesso.", AlertType.WARNING);
					Main.loadListaHorarioView();
				} else {
					CustomAlert.showAlert("Novo Hor�rio", "Algo de errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Novo Hor�rio", ex.getMessage(), AlertType.WARNING);
			}
		});
	}
	
	// Configura as horas e os minutos nos combo box
	private void defineTime(ComboBox<String> hora, ComboBox<String> minuto) {
		ArrayList<String> horas = new ArrayList<String>();
		ArrayList<String> minutos = new ArrayList<String>();
		
		for(int i = 0; i <= 23; i++) {
			if(i < 10) {
				horas.add("0"+i);
			} else {
				horas.add(""+i);
			}
		}
		
		for(int i = 0; i <= 59; i++) {
			if(i < 10) {
				minutos.add("0"+i);
			} else {
				minutos.add(""+i);
			}
		}
		
		hora.setItems(FXCollections.observableArrayList(horas));
		minuto.setItems(FXCollections.observableArrayList(minutos));
	}

}
