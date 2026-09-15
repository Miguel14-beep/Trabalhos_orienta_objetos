package entidade;

public class EfeitoAtivo {

    public Habilidade habilidade;
    public int turnosRestantes;

    public EfeitoAtivo(Habilidade habilidade) {

        this.habilidade = habilidade;

        if (habilidade != null) {
            this.turnosRestantes = habilidade.duracao;
        } else {
            this.turnosRestantes = 0;
        }
    }

    public void resetarDuracao() {

        if (habilidade != null) {
            turnosRestantes = habilidade.duracao;
        }
    }

    public void diminuirTurno() {

        if (turnosRestantes > 0) {
            turnosRestantes--;
        }
    }

    public boolean terminou() {

        return turnosRestantes <= 0;
    }
}