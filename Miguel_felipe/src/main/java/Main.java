package main.java;

public class Main {

	public static void main(String[] args) {

		Carro carro1 = new Carro("azul", "fusca", 1960, "in-line 4");
		carro1.ligar_carro();
		carro1.buzinar();
		Carro carro2 = new Carro("amarelo", "camaro", 2000, "V8");
		carro2.ligar_carro();
		carro2.buzinar();
	
	
		Trator trator1 = new Trator("vermelho", "1400", 2014, "V6 Disel", "agricultura", "Massey Ferguson");
		trator1.ligar_carro();
		trator1.buzinar();
		Trator trator2 = new Trator("laranja", "L3301", 2020, "V6 Disel", "construção", "kubota");
		trator2.ligar_carro();
		trator2.buzinar();
		
	
		Professor professor1 = new Professor("Carlos", 4000.57);
		Professor professor2 = new Professor("João", 5000.00);
		Professor professor3 = new Professor("Ana", 7522.87);
		
		Professor[] professores1 = {professor1, professor2, professor3};
		
		Escola livraria1 = new Escola("Escola Estadual Nova Brasilia", 1995, professores1);
		livraria1.mostraInfo();
		
		
		Cliente cliente = new Cliente();
        ServicoEmail servico = new ServicoEmail();

        cliente.fazerPedido(servico);
	}
	
}
