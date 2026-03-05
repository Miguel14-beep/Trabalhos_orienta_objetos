package main.java;

//Exemplo de herança e polimorfismo
public class Trator extends Carro {
	String funcao;
	String fabricante;
	
	
	public Trator(String cor, String nome, int ano, String motorTipo, String funcao, String fabricante) {
		super(cor, nome, ano, motorTipo);
		this.funcao = funcao;
		this.fabricante = fabricante;
	}
	
	@Override
	void buzinar() {
		System.out.println("BEEEE, BEEEEE");
	}
	
	@Override
	void ligar_carro() {
		this.motor.ligar();
		System.out.println("O " + this.fabricante + " " +this.nome + " " + this.cor + " para "+ this.funcao + " está ligado.");
	}
}


