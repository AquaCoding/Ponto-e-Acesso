package aquacoding.utils;

import java.time.Duration;
import java.time.Instant;

import javafx.application.Platform;
import aquacoding.pontoacesso.Main;

public class Timeout implements Runnable {
	
	private boolean runTimeoutEvent = false;
	private Instant ultimoEvento = Instant.now();
	
	public void setRunTimeoutEvent(boolean run) {
		this.runTimeoutEvent = run;
	}
	
	public void setUltimoEvento() {
		ultimoEvento = Instant.now();
	}
	
	public static void logout() {
		Main.loggedUser = null;
		Main.initLoginLayout();
		Main.endRootLayout();
	}
	
	@Override
	public void run() {
		try {
			while(runTimeoutEvent) {
				// Obtem a duração entre o ultimo evento ocorrido e agora
				long duration = Duration.between(ultimoEvento, Instant.now()).toMillis();
				
				// Verifica se foi maior de 10 minutos
				if(duration >= 10 * 60 * 1000) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Timeout.logout();
						}
					});
				}
				
				// Coloca a thread para dormir por  1 minuto
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
