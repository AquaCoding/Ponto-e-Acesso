package logs;

import java.util.HashMap;

public enum ActionsCode {
	CADASTROU("Cadastrou"),
	EDITOU("Editou"),
	REMOVEU("Removou"),
	INVALID_LOGIN("Tentou entrar no sistema e falhou"),
	VALID_LOGIN("Entrou no sistema"),
	LINK("Criou um link"),
	REMOVEU_LINK("Removeu um link");
	
	private final String value;
	private static HashMap<String, ActionsCode> actionsCodes = new HashMap<String, ActionsCode>();
	
	ActionsCode (String value){
		this.value = value;
	}
	
	public String getValor(){
		return value;
	}

	public static ActionsCode getActionsCode(String value) {
		actionsCodes.put("Cadastrou", ActionsCode.CADASTROU);
		actionsCodes.put("Editou", ActionsCode.EDITOU);
		actionsCodes.put("Removou", ActionsCode.REMOVEU);
		actionsCodes.put("Tentou entrar no sistema e falhou", ActionsCode.INVALID_LOGIN);
		actionsCodes.put("Entrou no sistema", ActionsCode.VALID_LOGIN);
		actionsCodes.put("Criou um link", ActionsCode.LINK);
		actionsCodes.put("Removeu um link", ActionsCode.REMOVEU_LINK);
		
		if(actionsCodes.containsKey(value))
			return actionsCodes.get(value);
		
		throw new RuntimeException("Invalid ActionCode");
	}
}
