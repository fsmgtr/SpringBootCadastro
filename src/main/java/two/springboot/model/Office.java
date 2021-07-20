package two.springboot.model;

public enum Office {

	JUNIOR("Júnior"), PLENO("Pleno"), SENIOR("Sênior");

	private String nome;
	@SuppressWarnings("unused")
	private String valor;

	private Office(String nome) {
		this.nome = nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
 
	public String getValor() {
		return valor = this.name();
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
