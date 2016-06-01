package aquacoding.model;

import com.dropbox.core.*;

import aquacoding.pontoacesso.Main;
import aquacoding.utils.CustomAlert;
import aquacoding.utils.DatabaseConnect;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Stream;

import logs.ActionsCode;
import logs.Logs;
import logs.ObjectCode;
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
				DbxEntry.File uploadedFile = client.uploadFile(nome, DbxWriteMode.add(), inputFile.length(),
						inputStream);
				System.out.println("Uploaded: " + uploadedFile.toString());
				CustomAlert.showAlert("Backup", "Backup realizado com sucesso no seu DropBox", AlertType.WARNING);
				
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BACKUP, 0, ActionsCode.REALIZOU);
			} finally {
				inputStream.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			CustomAlert.showAlert("Backup", "Algo deu errado.", AlertType.WARNING);
		}

	}

	public static void criarZIPFile(File localToSave) {
		if (localToSave != null) {
			try {
				// Cria um arquivo temporario e marca ele para ser deletado ao
				// sair
				File temp = File.createTempFile("backup_", ".sql");
				temp.deleteOnExit();

				// Cria o backup do banco no arquivo temporario
				DatabaseConnect.makeBackup(temp.getAbsolutePath());

				// Cria uma nova thread para compactação
				Thread t = new Thread(() -> {
					try {
						// Coloca a thread para dormir enquanto o backuo é
						// terminado
						Thread.sleep(2000);

						// Deleta o arquivo original
						localToSave.delete();

						// Inicia um arquivo zip
						ZipFile zipFile = new ZipFile(localToSave.getAbsolutePath());

						// Pasta com as informações do usuario
						File folderToAdd = new File("user_data");

						// Cria os parametros do arquivo zip
						ZipParameters parameters = new ZipParameters();
						parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
						parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

						// Cria o ZIP com o sql de backup e adiciona a pasta do
						// usuario
						zipFile.createZipFile(temp, parameters);
						zipFile.addFolder(folderToAdd, parameters);

						Platform.runLater(() -> {
							CustomAlert.showAlert("Backup", "Backup realizado com sucesso.", AlertType.WARNING);
							
							// Gera log
							Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BACKUP, 0, ActionsCode.REALIZOU);
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

	public static void restaurar(File fileToOpen) {
		try {
			boolean confirmation = CustomAlert.showConfirmationAlert("Restaurar backup",
					"Isso irá deletar todos os seus dados atuais da aplicação. Caso um arquivo de backup invalido seja informado dados podem ser perdidos. Você tem certeza que deseja continuar?");

			if (confirmation) {
				// Exclui os arquivos existente do usuario no sistema
				deleteDir(new File("user_data"));

				// Inicia o arquivo ZIP
				ZipFile zipFile = new ZipFile(fileToOpen.getAbsolutePath());

				// Extrai o arquivo ZIP
				zipFile.extractAll(new File("").getAbsolutePath());

				// Obtem o arquivo de backup
				Stream<Path> paths = Files.list(Paths.get(""));
				paths.forEach(new Consumer<Path>() {

					@Override
					public void accept(Path t) {
						String fileName = t.getFileName().toString();
						if (fileName.matches("^backup_[\\d]+.sql$")) {
							// Realiza o backup
							DatabaseConnect.restoreBackup(fileName);

							CustomAlert.showAlert("Restaurar Backup", "Backup restaurado com sucesso",
									AlertType.WARNING);
							
							// Gera log
							Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BACKUP, 0, ActionsCode.RESTAUROU);

							// Deleta o arquivo de backup
							Thread t2 = new Thread(() -> {
								try {
									Thread.sleep(5000);
									new File(fileName).delete();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							});
							t2.start();
						}
					}
				});
				paths.close();
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDir(f);
			}
		}
		file.delete();
	}

	public static void restaurarDropBox(String code) {
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
			
			FileOutputStream outputStream = new FileOutputStream("temp.sql");
			try {
				DbxEntry.File downloadedFile = client.getFile("/temp.sql", null, outputStream);
				System.out.println("Metadata: " + downloadedFile.toString());
				
				DatabaseConnect.restoreBackup("temp.sql");
								
				CustomAlert.showAlert("Restaurar Backup", "Backup restaurado com sucesso", AlertType.WARNING);
				
				// Gera log
				Logs.makeLog(Main.loggedUser.getId(), ObjectCode.BACKUP, 0, ActionsCode.RESTAUROU);
			} finally {
				outputStream.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			CustomAlert.showAlert("Backup", "Algo deu errado.", AlertType.WARNING);
		}
	}

}
