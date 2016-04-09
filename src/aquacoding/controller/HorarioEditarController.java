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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class HorarioEditarController implements Initializable {
	
	@FXML
	TextField horarioNome;
	
	@FXML
	ComboBox<String> horarioInicioHora, horarioInicioMinuto, horarioTerminoHora, horarioTerminoMinuto;
	
	@FXML
	ComboBox<String> almocoInicioHora, almocoInicioMinuto, almocoTerminoHora, almocoTerminoMinuto;
	
	@FXML
	CheckBox segundaD, tercaD, quartaD, quintaD, sextaD, sabadoD, domingoD;
	
	@FXML
	Button cancelar, editar;
	
	private Horario h;

	@SuppressWarnings("deprecation")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Configura as horas e minutos dos combo box
		defineTime(horarioInicioHora, horarioInicioMinuto);
		defineTime(horarioTerminoHora, horarioTerminoMinuto);
		defineTime(almocoInicioHora, almocoInicioMinuto);
		defineTime(almocoTerminoHora, almocoTerminoMinuto);
				
		// Evento do botão cancelar
		cancelar.setOnMouseClicked((MouseEvent e) -> {
			Main.loadListaHorarioView();
		});
		
		editar.setOnMouseClicked((MouseEvent e) -> {
			try {
				// Horario de inicio e fim do turno
				Time inicio = new Time(Integer.parseInt(horarioInicioHora.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioInicioMinuto.getSelectionModel().getSelectedItem()), 0);
				Time termino = new Time(Integer.parseInt(horarioTerminoHora.getSelectionModel().getSelectedItem()), Integer.parseInt(horarioTerminoMinuto.getSelectionModel().getSelectedItem()), 0);
				
				// Horario de inicio e fim do almoço
				Time inicioAlmoco = new Time(Integer.parseInt(almocoInicioHora.getSelectionModel().getSelectedItem()), Integer.parseInt(almocoInicioMinuto.getSelectionModel().getSelectedItem()), 0);
				Time terminoAlmoco = new Time(Integer.parseInt(almocoTerminoHora.getSelectionModel().getSelectedItem()), Integer.parseInt(almocoTerminoMinuto.getSelectionModel().getSelectedItem()), 0);
				
				// Define os novos horarios
				h.setInicio(inicio);
				h.setTermino(termino);
				h.setInicioAlmoco(inicioAlmoco);
				h.setTerminoAlmoco(terminoAlmoco);
				
				// Dias
				h.setSegunda(segundaD.isSelected());
				h.setTerca(tercaD.isSelected());
				h.setQuarta(quartaD.isSelected());
				h.setQuinta(quintaD.isSelected());
				h.setSexta(sextaD.isSelected());
				h.setSabado(sabadoD.isSelected());
				h.setDomingo(domingoD.isSelected());
				
				// Define o novo nome
				h.setNome(horarioNome.getText());
				
				if(h.update()){
					CustomAlert.showAlert("Editar Turno", "Turno atualizado com sucesso.", AlertType.WARNING);
					Main.loadListaHorarioView();
				} else {
					CustomAlert.showAlert("Editar Turno", "Algo de errado aconteceu.", AlertType.WARNING);
				}
			} catch(RuntimeException ex) {
				CustomAlert.showAlert("Editar Turno", ex.getMessage(), AlertType.WARNING);
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void setHorario(Horario h) {
		this.h = h;
		
		// Horario nome
		horarioNome.setText(h.getNome());
		
		// Horario inicio e termino
		horarioInicioHora.getSelectionModel().select(h.getInicio().getHours());
		horarioInicioMinuto.getSelectionModel().select(h.getInicio().getMinutes());
		horarioTerminoHora.getSelectionModel().select(h.getTermino().getHours());
		horarioTerminoMinuto.getSelectionModel().select(h.getTermino().getMinutes());
		
		// Horario almoço
		almocoInicioHora.getSelectionModel().select(h.getInicioAlmoco().getHours());
		almocoInicioMinuto.getSelectionModel().select(h.getInicioAlmoco().getMinutes());
		almocoTerminoHora.getSelectionModel().select(h.getTerminoAlmoco().getHours());
		almocoTerminoMinuto.getSelectionModel().select(h.getTerminoAlmoco().getMinutes());
		
		// Dias
		segundaD.setSelected(h.getSegunda());
		tercaD.setSelected(h.getTerca());
		quartaD.setSelected(h.getQuarta());
		quintaD.setSelected(h.getQuinta());
		sextaD.setSelected(h.getSexta());
		sabadoD.setSelected(h.getSabado());
		domingoD.setSelected(h.getDomingo());
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
