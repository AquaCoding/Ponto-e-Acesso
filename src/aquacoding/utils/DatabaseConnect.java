package aquacoding.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.Alert.AlertType;

public abstract class DatabaseConnect {
	
	private final static String DB_HOST = "localhost";
	private final static String DB_NAME = "PontoAcesso";
	private static String DB_USER = "root";
	private static String DB_PASS = "";

	// MySQL Connection
	private static Connection con;

	// Retorna uma conexão com o banco de dados
	public static Connection getInstance() {
		try {
			// Carrega nome de usuario e senha
			ArrayList<String> bd = (ArrayList<String>) Files.readAllLines(Paths.get("app_data/db.data"));
			DB_USER = bd.get(0);
			DB_PASS = bd.get(1);
			
			// Carrega o driver do MySQL
			Class.forName("com.mysql.jdbc.Driver");

			// Cria uma conexão com o MySQL
			con = DriverManager.getConnection("jdbc:mysql://"+DB_HOST+"/"+DB_NAME, DB_USER, DB_PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Retorna a conexão
		return con;
	}

	public static void importSQL(Connection conn, InputStream in) throws SQLException {
		Scanner s = new Scanner(in);
		s.useDelimiter("(;(\r)?\n)|(--\n)");
		Statement st = null;
		try {
			st = conn.createStatement();
			while (s.hasNext()) {
				String line = s.next();
				if (line.startsWith("/*!") && line.endsWith("*/")) {
					int i = line.indexOf(' ');
					line = line
							.substring(i + 1, line.length() - " */".length());
				}

				if (line.trim().length() > 0) {
					st.execute(line);
				}
			}
		} finally {
			if (st != null)
				st.close();
			
			if(s != null)
				s.close();
		}
	}
	
	public static void makeBackup(String localToSave) {
		try {
		   String command = "cmd.exe /C mysqldump --user="+DB_USER+" --password="+DB_PASS+" "+DB_NAME+" > "+localToSave;	   
		   Runtime.getRuntime().exec(command);
		} catch (IOException ex) {
		   System.out.println(ex.getMessage());
		}
	}
	
	public static void restoreBackup(String fileToExecute) {
		try {
		   String command = "cmd.exe /C mysql -u "+DB_USER+" "+DB_NAME+" < "+fileToExecute;	   
		   Runtime.getRuntime().exec(command);
		} catch (IOException ex) {
		   System.out.println(ex.getMessage());
		}
	}
	
	public static void restoreBackup(String fileToExecute, String user, String pass) {
		try {
		   String command = "cmd.exe /C mysql -u "+user+" "+pass+" < \""+fileToExecute+"\"";
		   Runtime.getRuntime().exec(command);
		} catch (IOException ex) {
		   System.out.println(ex.getMessage());
		}
	}

	public static void firstAccess() {
		try {
			ArrayList<String> file = (ArrayList<String>) Files.readAllLines(Paths.get("app_data/firstAccess.data"));
			
			// Precisa configurar o primeiro acesso
			if(Boolean.valueOf(file.get(0))) {
				// Define que o primeiro accesso ocorreu
				file.clear();
				file.add("false");
				Files.write(Paths.get("app_data/firstAccess.data"), file);
				
				// Solicita nome de usuario do banco e senha
				String user = CustomAlert.showDialogWithInput("Configuração do banco", "Qual o nome do usuário do banco de dados?");
				String pass = CustomAlert.showDialogWithInput("Configuração do banco", "Qual a senha do usuário do banco de dados?");
				
				// Cria o banco
				File sql = new File("DB/SQL.sql");
				System.out.println(sql.getAbsolutePath());
				DatabaseConnect.restoreBackup(sql.getAbsolutePath(), user, pass);
				
				// Salva as credencias
				file.clear();
				file.add(user);
				file.add(pass);
				Files.write(Paths.get("app_data/db.data"), file);
				
				Thread.sleep(1000);
				
				CustomAlert.showAlert("Configuração do banco", "Banco configurado.", AlertType.WARNING);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
