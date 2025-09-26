package model;

public enum UserGender {
	M("Masculino"),
	F("Feminino"),
	T("Não binário");
	
	private String value;
	
	UserGender(String value){
		this.value = value;
	}
	
	public String value() {
		return value;
	}
}
