package aquacoding.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import aquacoding.model.Acesso;
import aquacoding.model.Ponto;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Serial {

	private static String PORT_NUMBER;
	private static Boolean status;
	private String code;
	SerialPort serialPort = new SerialPort(PORT_NUMBER);	
	
	public String getPort() {
		return PORT_NUMBER;
	}

	public void setPort(String port) {
		PORT_NUMBER = port.toUpperCase();
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean ponto) {
		status = ponto;
	}

	public String getCode() {
		return this.code;
	}
	
	// Armazena a instancia da leitura serial
	private static Serial serialInstance = null;

	// Bloquea a criação de Serial para o getInstance
	protected Serial() {
	}

	// Obtem uma instancia do serial
	public static Serial getInstance() {		
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um statement
			Statement statement = connect.createStatement();

			// Executa um SQL
			ResultSet resultSet = statement.executeQuery("SELECT * FROM config");
			
			resultSet.next();
			
			PORT_NUMBER = resultSet.getString("com");
			
			status = resultSet.getBoolean("ponto");
			
			connect.close();
		} catch (SQLException e) {
			throw new RuntimeException("Um erro ocorreu ao obter as configurações do sistema");
		}
		
		if (serialInstance == null)
			serialInstance = new Serial();

		return serialInstance;
	}

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
								System.out.println("Erro Ponto: " + e.getMessage());
							}
						} else {
							try {
								marcaAcesso();
							} catch (SerialPortException e) {
								System.out.println("Erro Acesso: " + e.getMessage());
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
			this.code = serialPort.readString();

			try {
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um statement
				Statement statement = connect.createStatement();

				// Executa um SQL
				ResultSet resultSet = statement.executeQuery("SELECT * FROM FuncionarioTag");

				resultSet.next();

					if (verificaSuspensao(resultSet.getInt("idFuncionario")) == true) {
						serialPort.writeString("Funcionario Suspenso");
					} else {
						while (resultSet.next()) {
							if (this.code.equals(resultSet.getString("codigo"))) {
								Ponto ponto = new Ponto(resultSet.getInt("idFuncionario"),
										resultSet.getInt("idFuncionarioTag"));
								if (ponto.create()) {
									serialPort.writeString("Ponto registrado");
								} else {
									serialPort.writeString("Erro, contate o Suporte");
								}
							}
						};
					}				
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
			this.code = serialPort.readString();

			try {
				// Obtem uma conexão com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um statement
				Statement statement = connect.createStatement();

				// Executa um SQL
				ResultSet resultSet = statement.executeQuery("SELECT * FROM FuncionarioTag");

				resultSet.next();			
					if (verificaSuspensao(resultSet.getInt("idFuncionario")) == true) {
						serialPort.writeString("Funcionario Suspenso");
					} else {
						while (resultSet.next()) {
							if (this.code.equals(resultSet.getString("codigo"))) {
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
					}				
				// encerra conexão
				connect.close();

			} catch (Exception e) {
				System.out.println("Erro na busca pelo banco: " + e.getMessage());
			}

		} catch (SerialPortException e) {
			System.out.println("Erro na leitura serial " + e.getMessage());
		}
	}

	// Responsável por verificar suspensão
	public boolean verificaSuspensao(int idFuncionario) throws SerialPortException {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Executa um SQL
			PreparedStatement statement = (PreparedStatement) connect.prepareStatement(
					"SELECT * FROM Funcionario WHERE idFuncionario = ?", Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, idFuncionario);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (resultSet.getBoolean("suspensao") == true) {
					return true;
				} else {
					return false;
				}
			}
			;

			// encerra conexão
			connect.close();

		} catch (Exception e) {
			System.out.println("Erro na busca pelo banco: " + e.getMessage());
		}

		return false;

	}
}
