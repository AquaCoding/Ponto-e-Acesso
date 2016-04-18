package aquacoding.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import aquacoding.model.Ponto;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Serial {

	private static final String PORT_NUMBER = "COM6";

	public void SerialLeitura() throws SerialPortException {
		try {
			SerialPort serialPort = new SerialPort(PORT_NUMBER);
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			serialPort.addEventListener(new SerialPortEventListener() {
				String tag, preview;

				@Override
				public void serialEvent(SerialPortEvent event) {
					if (event.isRXCHAR()) {
						try {
							byte buffer[] = serialPort.readBytes();
							for (int i = 0; i < buffer.length; i++) {
								char l = (char) buffer[i];
								preview += l;
								if(!preview.equals(null)) {
									tag += l;
								}
								
							}

							System.out.println(tag);
							
							try {
								// Obtem uma conexão com o banco de dados
								Connection connect = DatabaseConnect.getInstance();

								// Cria um statement
								Statement statement = connect.createStatement();

								// Executa um SQL
								ResultSet resultSet = statement.executeQuery("SELECT * FROM FuncionarioTag");

								boolean estado = false;
								
								while (resultSet.next()) {
									if (tag.equals(resultSet.getString("codigo"))) {
										Ponto ponto = new Ponto(resultSet.getInt("idFuncionario"), resultSet.getInt("idFuncionarioTag"));

										if (ponto.create()) {
											serialPort.writeString("Ponto registrado");
										} else {
											serialPort.writeString("Erro, contate o Suporte");
										}
										estado = true;
									}
								};

								if(estado == false)
									serialPort.writeString("Usuário não encontrado");
								
								// encerra conexão
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
	}

}
