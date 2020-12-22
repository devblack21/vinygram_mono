package br.com.app.vinygram.domains;

public enum Authorities {
	
	USER_ROLE("USER_ROLE");
	
	private Authorities(String value) {
		
		this.value = value;
	}
	
	private String value;
	
	public String toString() {
		return value;
	}
}