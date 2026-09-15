package entidade;

public class Habilidade {

    public String nome;
    public String atributo;
    public int efeito;
    public int custoPH;
    public int duracao;

    public Habilidade(
            String nome,
            String atributo,
            int efeito,
            int custoPH) {

        this.nome = nome;
        this.atributo = atributo;
        this.efeito = efeito;
        this.custoPH = custoPH;
        this.duracao = 3;
    }

    public void aplicarEfeito(Birdmon usuario, Habilidade habilidade) {

        if (usuario == null || habilidade == null) {
            return;
        }

        /*
         * O efeito não altera diretamente ataque, defesa ou velocidade.
         *
         * Ele será colocado em EfeitoAtivo e calculado pelos métodos:
         *
         * getAtaqueComEfeitos()
         * getDefesaComEfeitos()
         * getVelocidadeComEfeitos()
         *
         * Dessa forma, o bônus desaparece quando o efeito termina.
         */
    }
}