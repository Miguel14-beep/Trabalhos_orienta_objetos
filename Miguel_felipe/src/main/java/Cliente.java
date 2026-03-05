package main.java;

//exemplo de dependencia.
public class Cliente {

    public void fazerPedido(ServicoEmail servico) {
        System.out.println("Pedido realizado.");
        servico.enviarEmail("Seu pedido foi confirmado!");
    }

}