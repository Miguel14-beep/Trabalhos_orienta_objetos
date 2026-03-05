package main.java;

//exemplo de compossição
public class Carro {
	String cor;
	String nome;
	int ano;
	Motor motor;
	
	Carro(String cor, String nome, int ano, String motorTipo){
		this.cor = cor;
		this.nome = nome;
		this.motor = new Motor(motorTipo);
	}
	
	void ligar_carro() {
		this.motor.ligar();
		System.out.println("O " + this.nome + " " + this.cor +" está ligado.");
	}
	
	void buzinar() {
		System.out.println("bi, bi, bi, bi");
	}
	
}
