package logs;

import java.util.HashMap;

public enum ActionsCode {
	CADASTROU("Cadastrou"),
	EDITOU("Editou"),
	REMOVEU("Removou"),
	INVALID_LOGIN("Tentou entrar no sistema e falhou"),
	VALID_LOGIN("Entrou no sistema"),
	LINK("Criou um link"),
	REMOVEU_LINK("Removeu um link"), 
	REALIZOU("Realizou"), 
	RESTAUROU("Restaurou"),
	REVOGOU("Revogou"),
	PERMITIU("Permitiu"), 
	CRIOU_PONTO_MANUAL("Criou ponto manual"), 
	GEROU("Gerou"), 
	SALVOU_MODELO("Salvou modelo");
	
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
		actionsCodes.put("Realizou", ActionsCode.REALIZOU);
		actionsCodes.put("Restaurou", ActionsCode.RESTAUROU);
		actionsCodes.put("Revogou", ActionsCode.REVOGOU);
		actionsCodes.put("Permitiu", ActionsCode.PERMITIU);
		actionsCodes.put("Criou ponto manual", ActionsCode.CRIOU_PONTO_MANUAL);
		actionsCodes.put("Gerou", ActionsCode.GEROU);
		actionsCodes.put("Salvou modelo", SALVOU_MODELO);
		
		if(actionsCodes.containsKey(value))
			return actionsCodes.get(value);
		
		throw new RuntimeException("Invalid ActionCode");
	}
}
