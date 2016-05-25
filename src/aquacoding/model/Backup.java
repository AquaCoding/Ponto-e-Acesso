package aquacoding.model;

import com.dropbox.core.*;

import aquacoding.utils.CustomAlert;
import aquacoding.utils.DatabaseConnect;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.util.Locale;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

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
	
	public static void criarZIPFile(File localToSave) {
		if(localToSave != null){
			try {
				// Cria um arquivo temporario e marca ele para ser deletado ao sair
				File temp = File.createTempFile("backup_", ".sql");
				temp.deleteOnExit();
				
				// Cria o backup do banco no arquivo temporario
				DatabaseConnect.makeBackup(temp.getAbsolutePath());

				// Cria uma nova thread para compactação
				Thread t = new Thread(() -> {
					try {
						// Coloca a thread para dormir enquanto o backuo é terminado
						Thread.sleep(2000);
						
						// Inicia um arquivo zip
						ZipFile zipFile = new ZipFile(localToSave.getAbsolutePath());
						
						// Pasta com as informações do usuario
						File folderToAdd = new File("user_data");
						
						// Cria os parametros do arquivo zip
						ZipParameters parameters = new ZipParameters();
						parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
						parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
						
						// Cria o ZIP com o sql de backup e adiciona a pasta do usuario
						zipFile.createZipFile(temp, parameters);
						zipFile.addFolder(folderToAdd, parameters);
						
						Platform.runLater(() -> {
							CustomAlert.showAlert("Backup", "Backup realizado com sucesso.", AlertType.WARNING);
						});
					} catch (ZipException ex) {
						ex.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
				t.start();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
