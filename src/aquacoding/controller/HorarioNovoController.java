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
	ComboBox<String> horarioInicioHoraTurno1, horarioInicioMinutoTurno1, horarioTerminoHoraTurno1, horarioTerminoMinutoTurno1,
	horarioInicioHoraTurno2, horarioInicioMinutoTurno2, horarioTerminoHoraTurno2, horarioTerminoMinutoTurno2;
	
	@FXML
	ComboBox<String> almocoInicioHora, almocoInicioMinuto, almocoTerminoHora, almocoTerminoMinuto;
	
	@FXML
	Button cancelar, cadastrar;

	@SuppressWarnings("deprecation")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Configura as horas e minutos dos combo box
		defineTime(horarioInicioHoraTurno1, horarioInicioMinutoTurno1);
		defineTime(horarioTerminoHoraTurno1, horarioTerminoMinutoTurno1);
		defineTime(horarioInicioHoraTurno2, horarioInicioMinutoTurno2);
		defineTime(horarioTerminoHoraTurno2, horarioTerminoMinutoTurno2);
		defineTime(almocoInicioHora, almocoInicioMinuto);
		defineTime(almocoTerminoHora, almocoTerminoMinuto);
		
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadMainView();
		});
		
		cadastrar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Horario de inicio e fim do turno
				Time inicioTurno1 = new Time(Integer.parseInt(horarioInicioHoraTurno1.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioInicioMinutoTurno1.getSelectionModel().getSelectedItem()), 0);
				Time terminoTurno1 = new Time(Integer.parseInt(horarioTerminoHoraTurno1.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioTerminoMinutoTurno1.getSelectionModel().getSelectedItem()), 0);
				Time inicioTurno2 = new Time(Integer.parseInt(horarioInicioHoraTurno2.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioInicioMinutoTurno2.getSelectionModel().getSelectedItem()), 0);
				Time terminoTurno2 = new Time(Integer.parseInt(horarioTerminoHoraTurno2.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioTerminoMinutoTurno2.getSelectionModel().getSelectedItem()), 0);
				
				// Horario de inicio e fim do almoço
				Time inicioAlmoco = new Time(Integer.parseInt(almocoInicioHora.getSelectionModel().getSelectedItem()), Integer.parseInt(almocoInicioMinuto.getSelectionModel().getSelectedItem()), 0);
				Time terminoAlmoco = new Time(Integer.parseInt(almocoTerminoHora.getSelectionModel().getSelectedItem()), Integer.parseInt(almocoTerminoMinuto.getSelectionModel().getSelectedItem()), 0);
				
				// Cria novo horario
				Horario h = new Horario(horarioNome.getText(), inicioTurno1 , terminoTurno1, inicioTurno2, terminoTurno2, inicioAlmoco, terminoAlmoco);
				
				// Salva novo horario no BD
				if(h.create()) {
					CustomAlert.showAlert("Novo Horário", "Novo horário cadastrado com sucesso.", AlertType.WARNING);
					Main.loadListaHorarioView();
				} else {
					CustomAlert.showAlert("Novo Horário", "Algo de errado aconteceu.", AlertType.WARNING);
				}
			} catch (RuntimeException ex) {
				CustomAlert.showAlert("Novo Horário", ex.getMessage(), AlertType.WARNING);
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
