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

public class HorarioEditarController implements Initializable {
	
	@FXML
	TextField horarioNome;
	
	@FXML
	ComboBox<String> horarioInicioHoraTurno1, horarioInicioMinutoTurno1, horarioTerminoHoraTurno1, horarioTerminoMinutoTurno1,
	horarioInicioHoraTurno2, horarioInicioMinutoTurno2, horarioTerminoHoraTurno2, horarioTerminoMinutoTurno2;
	
	@FXML
	ComboBox<String> almocoInicioHora, almocoInicioMinuto, almocoTerminoHora, almocoTerminoMinuto;
	
	@FXML
	Button cancelar, editar;
	
	private Horario h;

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
				
		// Evento do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaHorarioView();
		});
		
		editar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Horario de inicio e fim do turno
				Time inicioTurno1 = new Time(Integer.parseInt(horarioInicioHoraTurno1.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioInicioMinutoTurno1.getSelectionModel().getSelectedItem()), 0);
				Time terminoTurno1 = new Time(Integer.parseInt(horarioTerminoHoraTurno1.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioTerminoMinutoTurno1.getSelectionModel().getSelectedItem()), 0);
				Time inicioTurno2 = new Time(Integer.parseInt(horarioInicioHoraTurno2.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioInicioMinutoTurno2.getSelectionModel().getSelectedItem()), 0);
				Time terminoTurno2 = new Time(Integer.parseInt(horarioTerminoHoraTurno2.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioTerminoMinutoTurno2.getSelectionModel().getSelectedItem()), 0);
				
				// Horario de inicio e fim do almoço
				Time inicioAlmoco = new Time(Integer.parseInt(almocoInicioHora.getSelectionModel().getSelectedItem()), Integer.parseInt(almocoInicioMinuto.getSelectionModel().getSelectedItem()), 0);
				Time terminoAlmoco = new Time(Integer.parseInt(almocoTerminoHora.getSelectionModel().getSelectedItem()), Integer.parseInt(almocoTerminoMinuto.getSelectionModel().getSelectedItem()), 0);
				
				// Define os novos horarios
				h.setInicioTurno1(inicioTurno1);
				h.setTerminoTurno1(terminoTurno1);
				h.setInicioTurno2(inicioTurno2);
				h.setTerminoTurno2(terminoTurno2);
				h.setInicioAlmoco(inicioAlmoco);
				h.setTerminoAlmoco(terminoAlmoco);
				
				// Define o novo nome
				h.setNome(horarioNome.getText());
				
				if(h.update()){
					CustomAlert.showAlert("Editar Horário", "Horário atualizado com sucesso.", AlertType.WARNING);
					Main.loadListaHorarioView();
				} else {
					CustomAlert.showAlert("Editar Horário", "Algo de errado aconteceu.", AlertType.WARNING);
				}
			} catch(RuntimeException ex) {
				CustomAlert.showAlert("Editar Horário", ex.getMessage(), AlertType.WARNING);
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void setHorario(Horario h) {
		this.h = h;
		
		// Horario nome
		horarioNome.setText(h.getNome());
		
		// Horario inicio e termino
		horarioInicioHoraTurno1.getSelectionModel().select(h.getInicioTurno1().getHours());
		horarioInicioMinutoTurno1.getSelectionModel().select(h.getInicioTurno1().getMinutes());
		horarioTerminoHoraTurno1.getSelectionModel().select(h.getTerminoTurno1().getHours());
		horarioTerminoMinutoTurno1.getSelectionModel().select(h.getTerminoTurno1().getMinutes());
		
		horarioInicioHoraTurno2.getSelectionModel().select(h.getInicioTurno2().getHours());
		horarioInicioMinutoTurno2.getSelectionModel().select(h.getInicioTurno2().getMinutes());
		horarioTerminoHoraTurno2.getSelectionModel().select(h.getTerminoTurno2().getHours());
		horarioTerminoMinutoTurno2.getSelectionModel().select(h.getTerminoTurno2().getMinutes());
		
		// Horario almoço
		almocoInicioHora.getSelectionModel().select(h.getInicioAlmoco().getHours());
		almocoInicioMinuto.getSelectionModel().select(h.getInicioAlmoco().getMinutes());
		almocoTerminoHora.getSelectionModel().select(h.getTerminoAlmoco().getHours());
		almocoTerminoMinuto.getSelectionModel().select(h.getTerminoAlmoco().getMinutes());
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
