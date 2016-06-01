package logs;

import java.util.HashMap;

public enum ObjectCode {	
	ABONO("Abono"),
	BONIFICACAO("Bonificação"),
	CARTAO("Cartão"),
	EMPRESA("Empresa"),
	FERIAS("Férias"),
	FUNCAO("Função"),
	FUNCIONARIO("Funcionário"),
	HORARIO("Horário"),
	IMPOSTO("Imposto"),
	SETOR("Setor"),
	USUARIO("Usuário"), 
	BACKUP("Backup"), 
	HOLERITE("Holerite");
	
	private final String value;
	private static HashMap<String, ObjectCode> objectsCodes = new HashMap<String, ObjectCode>();
	
	ObjectCode (String value){
		this.value = value;
	}
	
	public String getValor(){
		return value;
	}
	
	public static ObjectCode getObjectCode(String value) {
		objectsCodes.put("Abono", ObjectCode.ABONO);
		objectsCodes.put("Bonificação", ObjectCode.BONIFICACAO);
		objectsCodes.put("Cartão", ObjectCode.CARTAO);
		objectsCodes.put("Empresa", ObjectCode.EMPRESA);
		objectsCodes.put("Férias", ObjectCode.FERIAS);
		objectsCodes.put("Função", ObjectCode.FUNCAO);
		objectsCodes.put("Funcionário", ObjectCode.FUNCIONARIO);
		objectsCodes.put("Horário", ObjectCode.HORARIO);
		objectsCodes.put("Imposto", ObjectCode.IMPOSTO);
		objectsCodes.put("Setor", ObjectCode.SETOR);
		objectsCodes.put("Usuário", ObjectCode.USUARIO);
		objectsCodes.put("Backup", ObjectCode.BACKUP);
		objectsCodes.put("Holerite", ObjectCode.HOLERITE);
		
		if(objectsCodes.containsKey(value))
			return objectsCodes.get(value);
		
		throw new RuntimeException("Invalid ObjectCode");
	}
}