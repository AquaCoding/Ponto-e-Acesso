package aquacoding.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import aquacoding.model.Acesso;
import aquacoding.model.Ponto;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Serial {

	private String PORT_NUMBER = "COM4";
	private Boolean status = true;
	private String code;
	SerialPort serialPort = new SerialPort(PORT_NUMBER);
	
	// Armazena a instancia da leitura serial
	private static Serial serialInstance = null;
	
	// Bloquea a cria��o de Serial para o getInstance
	protected Serial() {}
	
	// Obtem uma instancia do serial
	public static Serial getInstance() {
		if(serialInstance == null)
			serialInstance = new Serial();
		
		return serialInstance;
	}

	public String getPort() {
		return this.PORT_NUMBER;
	}
	
	public void setPort(String port) {
		this.PORT_NUMBER = port.toUpperCase();				
	}
	
	public Boolean getStatus() {
		return this.status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public String getCode() {		
		return this.code;
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
								System.out.println("Erro Ponto: "+e.getMessage());
							}
						} else {
							try {
								marcaAcesso();
							} catch (SerialPortException e) {
								System.out.println("Erro Acesso: "+e.getMessage());
							}
						}

					}
				}
			});
		} catch (Exception e) {
			System.out.println("Erro na leitura serial " + e.getMessage());
		}
	}

	// Respons�vel por marcar o Ponto
	public void marcaPonto() throws SerialPortException {
		try {
			this.code = serialPort.readString();

			try {
				// Obtem uma conex�o com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um statement
				Statement statement = connect.createStatement();

				// Executa um SQL
				ResultSet resultSet = statement.executeQuery("SELECT * FROM FuncionarioTag");

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

				// encerra conex�o
				connect.close();

			} catch (Exception e) {
				System.out.println("Erro na busca pelo banco: " + e.getMessage());
			}

		} catch (SerialPortException e) {
			System.out.println("Erro na leitura serial " + e.getMessage());
		}
	}

	// Respons�vel por marcar o acesso
	public void marcaAcesso() throws SerialPortException {
		try {
			this.code = serialPort.readString();

			try {
				// Obtem uma conex�o com o banco de dados
				Connection connect = DatabaseConnect.getInstance();

				// Cria um statement
				Statement statement = connect.createStatement();

				// Executa um SQL
				ResultSet resultSet = statement.executeQuery("SELECT * FROM FuncionarioTag");

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
				// encerra conex�o
				connect.close();

			} catch (Exception e) {
				System.out.println("Erro na busca pelo banco: " + e.getMessage());
			}

		} catch (SerialPortException e) {
			System.out.println("Erro na leitura serial " + e.getMessage());
		}
	}

	// Respons�vel por verificar suspens�o
	public boolean verificaSuspensao(int idFuncionario) throws SerialPortException {
		try {
			// Obtem uma conex�o com o banco de dados
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
			};

			// encerra conex�o
			connect.close();

		} catch (Exception e) {
			System.out.println("Erro na busca pelo banco: " + e.getMessage());
		}

		return false;

	}
}
