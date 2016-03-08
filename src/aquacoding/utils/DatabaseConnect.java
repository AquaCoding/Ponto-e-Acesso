package aquacoding.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public abstract class DatabaseConnect {

	// MySQL Connection
	private static Connection con;

	// Retorna uma conexão com o banco de dados
	public static Connection getInstance() {
		try {
			// Carrega o driver do MySQL
			Class.forName("com.mysql.jdbc.Driver");

			// Cria uma conexão com o MySQL
			con = DriverManager.getConnection("jdbc:mysql://localhost/PontoAcesso",
					"root", "");
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
}
