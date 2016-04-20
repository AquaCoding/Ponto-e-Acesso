package logs;

import aquacoding.model.Log;

public class Logs {
	
	public static void makeLog(int idUsuario, ObjectCode objeto, int objectoId, ActionsCode acao) {
		Log l = new Log(idUsuario, objeto, objectoId, acao);
		l.create();
	}
}

