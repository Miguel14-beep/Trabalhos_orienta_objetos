package exercico1;

public class ContaBancaria {

    int numero;
    Cliente cliente;

    public ContaBancaria(int numero, Cliente cliente) {
        this.numero = numero;
        this.cliente = cliente;
    }

    public void mostrarConta() {
        System.out.println("Conta: " + numero);
        System.out.println("Cliente: " + cliente.nome);
    }
}
