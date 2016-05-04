package aquacoding.model;

import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;

import javax.swing.JOptionPane;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;


public class Backup {

	public void criarSalvarBackup() throws IOException, DbxException {
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI(
					"https://www.dropbox.com/1/oauth2/authorize?locale=pt_BR&client_id=nmhlfbvih0g20dl&response_type=code"));
		} catch (IOException e) {
			System.out.println(e);
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
		// Get your app key and secret from the Dropbox developers website.
		final String APP_KEY = "nmhlfbvih0g20dl";
		final String APP_SECRET = "y5wjp7wazntf43z";

		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

		String code = JOptionPane.showInputDialog("Cole o código gerado na página");
		
		// This will fail if the user enters an invalid authorization code.
		DbxAuthFinish authFinish = webAuth.finish(code);
		String accessToken = authFinish.accessToken;

		DbxClient client = new DbxClient(config, accessToken);

		File inputFile = new File("nomeArquivo");
		FileInputStream inputStream = new FileInputStream(inputFile);
		try {
		    DbxEntry.File uploadedFile = client.uploadFile("nomeArquivo",
		        DbxWriteMode.add(), inputFile.length(), inputStream);
		    System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
		    inputStream.close();
		}

	}
	
}
