package main; // ou package entidade; dependendo de onde você colocar

public class Item {
    private String nome;
    private int quantidade;
    private TipoItem tipo;
    private int valorEfeito; 
    private int duracaoTurnos; // NOVA VARIÁVEL: Quantos turnos o efeito dura

    // Construtor atualizado
    public Item(String nome, int quantidade, TipoItem tipo, int valorEfeito, int duracaoTurnos) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.valorEfeito = valorEfeito;
        this.duracaoTurnos = duracaoTurnos;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public TipoItem getTipo() { return tipo; }
    public int getValorEfeito() { return valorEfeito; }
    public int getDuracaoTurnos() { return duracaoTurnos; } // NOVO GETTER
    
    public void reduzirQuantidade() {
        if (quantidade > 0) {
            quantidade--;
        }
    }
}