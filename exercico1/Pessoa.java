package exercico1;

public class Pessoa {
	String nome;
	Pessoa amigo;
	public Pessoa(String nome) {
		this.nome = nome;
	}
	
	public void set_amigo(Pessoa amigo) {
		this.amigo = amigo;
	}
	
    public void mostrarAmigo() {
        if (amigo != null) {
            System.out.println(nome + " é amigo de " + amigo.nome);
        } else {
            System.out.println(nome + " não tem amigo definido.");
        }
    }
}
