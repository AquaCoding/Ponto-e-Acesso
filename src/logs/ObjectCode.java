package logs;

import java.util.HashMap;

public enum ObjectCode {	
	ABONO("Abono"),
	BONIFICACAO("Bonifica��o"),
	CARTAO("Cart�o"),
	EMPRESA("Empresa"),
	FERIAS("F�rias"),
	FUNCAO("Fun��o"),
	FUNCIONARIO("Funcion�rio"),
	HORARIO("Hor�rio"),
	IMPOSTO("Imposto"),
	SETOR("Setor"),
	USUARIO("Usu�rio"), 
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
		objectsCodes.put("Bonifica��o", ObjectCode.BONIFICACAO);
		objectsCodes.put("Cart�o", ObjectCode.CARTAO);
		objectsCodes.put("Empresa", ObjectCode.EMPRESA);
		objectsCodes.put("F�rias", ObjectCode.FERIAS);
		objectsCodes.put("Fun��o", ObjectCode.FUNCAO);
		objectsCodes.put("Funcion�rio", ObjectCode.FUNCIONARIO);
		objectsCodes.put("Hor�rio", ObjectCode.HORARIO);
		objectsCodes.put("Imposto", ObjectCode.IMPOSTO);
		objectsCodes.put("Setor", ObjectCode.SETOR);
		objectsCodes.put("Usu�rio", ObjectCode.USUARIO);
		objectsCodes.put("Backup", ObjectCode.BACKUP);
		objectsCodes.put("Holerite", ObjectCode.HOLERITE);
		
		if(objectsCodes.containsKey(value))
			return objectsCodes.get(value);
		
		throw new RuntimeException("Invalid ObjectCode");
	}
}