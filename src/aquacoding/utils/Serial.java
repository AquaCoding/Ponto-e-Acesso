package aquacoding.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aquacoding.model.Acesso;
import aquacoding.model.Ponto;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Serial {

	private static String PORT_NUMBER;
	private static Boolean status;
	private Boolean canRegisterPontoOrAcesso = true;
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
						try {
							// Obtem o codigo do cartão e limpa removendo
							// espaços e quebras de linhas
							if (serialPort.getInputBufferBytesCount() == 14) {
								code = serialPort.readString();
								Pattern codePattern = Pattern.compile("[\\w]{2} [\\w]{2} [\\w]{2} [\\w]{2}");
								Matcher m = codePattern.matcher(code);

								while (m.find())
									code = m.group(0);
							}

						} catch (SerialPortException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						if (canRegisterPontoOrAcesso) {
							if (status) {
								try {
									marcaPonto();
									System.out.println("Cadastrando ponto...");
								} catch (SerialPortException e) {
									System.out.println("Erro Ponto: " + e.getMessage());
								}
							} else {
								try {
									marcaAcesso();
									System.out.println("Cadastrando Acesso...");
								} catch (SerialPortException e) {
									System.out.println("Erro Acesso: " + e.getMessage());
								}
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
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM FuncionarioTag WHERE codigo = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.code);

			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (verificaSuspensao(resultSet.getInt("idFuncionario")) == true) {
					serialPort.writeString("Funcionario Suspenso");
				} else {
					// Verifica a data do ultimo ponto
					statement = (PreparedStatement) connect.prepareStatement(
							"SELECT * FROM Ponto WHERE horario >= NOW() - INTERVAL 10 MINUTE AND idFuncionario = ?;");
					statement.setInt(1, resultSet.getInt("idFuncionario"));
					ResultSet resultSet2 = statement.executeQuery();

					// Se não houver nenhum ponto nos ultimos 10 minutos do
					// mesmo idFuncionario, registra o ponto
					if (!resultSet2.next()) {
						Ponto ponto = new Ponto(resultSet.getInt("idFuncionario"),
								resultSet.getInt("idFuncionarioTag"));
						if (ponto.create()) {
							serialPort.writeString("Ponto registrado");
						} else {
							serialPort.writeString("Erro, contate o Suporte");
						}
					}
				}
			}

			// encerra conexão
			connect.close();

		} catch (Exception e) {
			System.out.println("Erro na busca pelo banco: " + e.getMessage());
		}
	}

	// Responsável por marcar o acesso
	public void marcaAcesso() throws SerialPortException {
		try {
			// Obtem uma conexão com o banco de dados
			Connection connect = DatabaseConnect.getInstance();

			// Cria um prepared statement
			PreparedStatement statement = (PreparedStatement) connect
					.prepareStatement("SELECT * FROM FuncionarioTag WHERE codigo = ?");

			// Realiza o bind dos valores
			statement.setString(1, this.code);

			// Executa o SQL
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (verificaSuspensao(resultSet.getInt("idFuncionario")) == true) {
					serialPort.writeString("Funcionario Suspenso");
				} else {
					// Verifica a data do ultimo acesso
					statement = (PreparedStatement) connect.prepareStatement(
							"SELECT * FROM Acesso WHERE horario >= NOW() - INTERVAL 1 MINUTE AND idFuncionario = ?;");
					statement.setInt(1, resultSet.getInt("idFuncionario"));
					ResultSet resultSet2 = statement.executeQuery();

					// Se não houver nenhum ponto nos ultimos 10 minutos do
					// mesmo idFuncionario, registra o ponto
					if (!resultSet2.next()) {
						Acesso acesso = new Acesso(resultSet.getInt("idFuncionario"),
								resultSet.getInt("idFuncionarioTag"));
						if (acesso.create()) {
							serialPort.writeString("Ponto registrado");
						} else {
							serialPort.writeString("Erro, contate o Suporte");
						}
					}
				}
			}

			// encerra conexão
			connect.close();

		} catch (Exception e) {
			System.out.println("Erro na busca pelo banco: " + e.getMessage());
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

			resultSet.next();
			if (resultSet.getBoolean("suspensao") == true) {
				// encerra conexão
				connect.close();
				return true;
			} else {
				// encerra conexão
				connect.close();
				return false;
			}

		} catch (Exception e) {
			System.out.println("Erro na busca pelo banco: " + e.getMessage());
		}

		return false;

	}

	public void stopLogic(int millis) {
		canRegisterPontoOrAcesso = false;
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(millis);
				Serial.getInstance().startLogic();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Retornando leitura...");
		});
		t.start();
	}

	public void startLogic() {
		canRegisterPontoOrAcesso = true;
	}
}
