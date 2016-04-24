package aquacoding.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import aquacoding.model.Acesso;
import aquacoding.model.Ponto;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Serial {

	private static final String PORT_NUMBER = "COM6";
	Boolean status;

	SerialPort serialPort = new SerialPort(PORT_NUMBER);

	public void SerialLeitura() throws SerialPortException {
		try {

			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			serialPort.addEventListener(new SerialPortEventListener() {

				@Override
				public void serialEvent(SerialPortEvent event) {
					if (event.isRXCHAR()) {

						if (status) {
							try {
								marcaPonto();
							} catch (SerialPortException e) {
								e.printStackTrace();
							}
						} else {
							try {
								marcaAcesso();
							} catch (SerialPortException e) {
								e.printStackTrace();
							}
						}

					}
				}
			});
		} catch (Exception e) {
			System.out.println("Erro na leitura serial " + e.getMessage());
		}
	}

	// Responsável por marcar o Ponto
	public void marcaPonto() throws SerialPortException {
		try {
			String buffer = serialPort.readString();

			try {
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um statement
				Statement statement = connect.createStatement();

				// Executa um SQL
				ResultSet resultSet = statement.executeQuery("SELECT * FROM FuncionarioTag");

				while (resultSet.next()) {
					if (buffer.equals(resultSet.getString("codigo"))) {
						System.out.println("Achou");
						Ponto ponto = new Ponto(resultSet.getInt("idFuncionario"),
								resultSet.getInt("idFuncionarioTag"));
						System.out.println("Objeto");
						if (ponto.create()) {
							serialPort.writeString("Ponto registrado");
							System.out.println("criou");
						} else {
							serialPort.writeString("Erro, contate o Suporte");
							System.out.println("errou");
						}
					}
				}
				;

				// encerra conexão
				connect.close();

			} catch (Exception e) {
				System.out.println("Erro na busca pelo banco: " + e.getMessage());
			}

		} catch (SerialPortException e) {
			System.out.println("Erro na leitura serial " + e.getMessage());
		}
	}

	// Responsável por marcar o acesso
	public void marcaAcesso() throws SerialPortException {
		try {
			String buffer = serialPort.readString();

			try {
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um statement
				Statement statement = connect.createStatement();

				// Executa um SQL
				ResultSet resultSet = statement.executeQuery("SELECT * FROM FuncionarioTag");

				while (resultSet.next()) {
					if (buffer.equals(resultSet.getString("codigo"))) {
						System.out.println("Achou");
						Acesso acesso = new Acesso(resultSet.getInt("idFuncionario"),
								resultSet.getInt("idFuncionarioTag"));
						System.out.println("Objeto");
						if (acesso.create()) {
							serialPort.writeString("Acesso registrado");
							System.out.println("criou");
						} else {
							serialPort.writeString("Erro, contate o Suporte");
							System.out.println("errou");
						}
					}
				};

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
