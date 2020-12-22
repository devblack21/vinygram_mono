package br.com.app.vinygram.domains;

public enum Operation {
	
	LIKE("Curtiu seu post."), FOLLOW("começou a te seguir.");
	
	private String value;
	
	
	private Operation(String value) {
		this.value = value;
	}

	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.value;
	}
}
