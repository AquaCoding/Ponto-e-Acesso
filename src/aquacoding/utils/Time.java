package aquacoding.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class Time {
	public static LocalDateTime roundTime(LocalDateTime time) {
		// Arredonda os minutos (Remove os segundos)
		time = time.truncatedTo(ChronoUnit.MINUTES);
		
		// Obtem os minutos
		int minutes = time.get(ChronoField.MINUTE_OF_HOUR);
		
		// Minutos entre 20 e 40 arredonda para 30
		if(minutes >= 20 && minutes <= 40) {
			if(minutes >= 20) {
				minutes = 30 - minutes;
				Duration d = Duration.ofMinutes(minutes);
				time = time.plus(d);
			} else {
				minutes = 40 - minutes;
				Duration d = Duration.ofMinutes(minutes);
				time = time.minus(d);
			}
		// Minutos maior que 50 arredonda pra hora acima
		} else if(minutes >= 50) {
			minutes = 60 - minutes;
			Duration d = Duration.ofMinutes(minutes);
			time = time.plus(d);
		// Minutos menor que 10 arredonda pra hora abaixo
		} else if(minutes <= 10) {
			minutes = 10 - minutes;
			Duration d = Duration.ofMinutes(minutes);
			time = time.minus(d);
		}
		
		return time;
	}
}
