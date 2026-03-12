package exercico1;

public class Exerci_D {

	 public static void main(String[] args) {

	        Cliente c = new Cliente("Fernanda");

	        ContaBancaria conta = new ContaBancaria(1234, c);

	        conta.mostrarConta();
	    }

}
