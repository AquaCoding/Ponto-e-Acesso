package aquacoding.utils;

import javafx.scene.control.Alert.AlertType;
import jssc.SerialPort;
import jssc.SerialPortException;

public class DataHora {	
	
	private Serial serial = Serial.getInstance();	
	SerialPort serialPort = new SerialPort(serial.getPort());
	
	public void setar(String data, String hora, String minuto) {		
		try {
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			serialPort.writeString("1 " + data);
			serialPort.writeString("2 " + hora + ":" + minuto);
			CustomAlert.showAlert("Data Hora Ponto - Acesso", "Data e Hora alterado com sucesso", AlertType.INFORMATION);
		} catch (SerialPortException e) {		
			e.printStackTrace();
			CustomAlert.showAlert("Data Hora Ponto - Acesso", "Problemas no serial", AlertType.ERROR);
		}						
	}

}
