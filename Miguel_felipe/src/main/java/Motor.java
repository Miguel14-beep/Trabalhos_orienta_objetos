package main.java;

//exemplo de compossição
public class Motor {
	String tipo;
	Motor(String tipo){
		this.tipo = tipo;
	}
	
	void ligar() {
		System.out.println("Você ligou o " + this.tipo);
	}
	
}

