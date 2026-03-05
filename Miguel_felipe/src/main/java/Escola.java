package main.java;

//exemplo de agregação.
public class Escola {
	String nome;
	int ano;
	Professor[] professores;
	public Escola(String nome, int ano, Professor[] professores) {
		this.nome = nome;
		this.ano = ano;
		this.professores = professores;
	}
	
	void mostraInfo() {
		System.out.println("A " + this.nome + ", desde " + this.ano);
		System.out.println("Professores: ");
		for(Professor professor : professores) {
			System.out.println(professor.mostraInfo());
		}
	}
}
