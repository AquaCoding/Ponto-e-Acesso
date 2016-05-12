package aquacoding.model;

import com.dropbox.core.*;

import aquacoding.utils.CustomAlert;
import aquacoding.utils.DatabaseConnect;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import java.util.Locale;

public class Backup {

	public static void criarSalvarBackup(String code) {	
		try {
			// Get your app key and secret from the Dropbox developers website.
			final String APP_KEY = "nmhlfbvih0g20dl";
			final String APP_SECRET = "y5wjp7wazntf43z";

			DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

			DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
			DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
			
			// This will fail if the user enters an invalid authorization code.
			DbxAuthFinish authFinish = webAuth.finish(code);
			String accessToken = authFinish.accessToken;

			DbxClient client = new DbxClient(config, accessToken);
			
			DatabaseConnect.makeBackup("temp.sql");

			Thread.sleep(2000);
			
			File inputFile = new File("temp.sql");
			FileInputStream inputStream = new FileInputStream(inputFile);
			try {
				String nome = "/temp.sql";
			    DbxEntry.File uploadedFile = client.uploadFile(nome,
			        DbxWriteMode.add(), inputFile.length(), inputStream);
			    System.out.println("Uploaded: " + uploadedFile.toString());
			    CustomAlert.showAlert("Backup", "Backup realizado com sucesso no seu DropBox", AlertType.WARNING);
			} finally {
			    inputStream.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			CustomAlert.showAlert("Backup", "Algo deu errado.", AlertType.WARNING);
		}
		
	}
	
}
