package aquacoding.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Serial {

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void SerialLeitura() throws SerialPortException {

		
		
		try {
			SerialPort serialPort = new SerialPort("COM4");
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			serialPort.addEventListener(new SerialPortEventListener() {
				String tag;
				@Override
				public void serialEvent(SerialPortEvent event) {
					if (event.isRXCHAR()) {
						try {
							byte buffer[] = serialPort.readBytes();
							for (int i = 0; i < buffer.length; i++) {
								char l = (char) buffer[i];
								System.out.print(l);
								tag += l;
							}
							
							try {
								// Obtem uma conexão com o banco de dados
								Connection connect = DatabaseConnect.getInstance();

								// Cria um statement
								Statement statement = connect.createStatement();

								// Executa um SQL
								ResultSet resultSet = statement.executeQuery("SELECT * FROM FuncionarioTag");																
								
								resultSet.next();
								
								String codigo = resultSet.getString("codigo"); 
								
								System.out.println();
								System.out.println(codigo);
								
								if(tag.equals(codigo)) {
									serialPort.writeString("Ponto registrado");								
								}else {
									serialPort.writeString("Bloqueado");
								}
								
								//encerra conexão
								connect.close();
								
							} catch (Exception e) {
								System.out.println("Erro na busca pelo banco: " + e.getMessage());
							}
							
							
						} catch (SerialPortException e) {
							System.out.println("Erro na leitura serial " + e.getMessage());
						}
					}
				}
			});
		} catch (Exception e) {
			System.out.println("Erro na leitura serial " + e.getMessage());
		}

		// serialPort.writeString();
	}

}
