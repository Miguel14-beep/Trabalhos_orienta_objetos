package main;

import entidade.Birdmon;
import entidade.Habilidade;

public class IACombate {

    private LogicaBatalha logica;

    public IACombate(LogicaBatalha logica) {
        this.logica = logica;
    }

    public String executarTurno() {

        Birdmon inimigo =
                logica.getAtacanteAtual();

        Birdmon jogador =
                logica.getDefensorAtual();

        if (inimigo == null || jogador == null) {
            return "";
        }

        if (inimigo != logica.getAdversario()) {
            return "";
        }

        if (inimigo.estaDerrotado()) {
            return inimigo.getNome()
                    + " está derrotado!";
        }

        if (jogador.estaDerrotado()) {
            return jogador.getNome()
                    + " está derrotado!";
        }

        Habilidade habilidade =
                escolherHabilidade(inimigo);

        if (habilidade != null) {

            boolean usou =
                    logica.usarHabilidade(
                            inimigo,
                            habilidade
                    );

            if (usou) {

                String mensagem =
                        inimigo.getNome()
                        + " usou "
                        + habilidade.nome
                        + "!";

                logica.passarTurno();

                return mensagem;
            }
        }

        int vidaAntes =
                jogador.getVida();

        boolean acertou =
                logica.atacar(
                        inimigo,
                        jogador
                );

        String mensagem;

        if (acertou) {

            int dano =
                    vidaAntes
                    - jogador.getVida();

            if (dano < 0) {
                dano = 0;
            }

            mensagem =
                    inimigo.getNome()
                    + " causou "
                    + dano
                    + " de dano!";

        } else {

            mensagem =
                    inimigo.getNome()
                    + " errou o ataque!";
        }

        logica.passarTurno();

        return mensagem;
    }

    private Habilidade escolherHabilidade(
            Birdmon inimigo) {

        if (inimigo.habilidades == null
                || inimigo.habilidades.length == 0) {

            return null;
        }

        // 30% de chance de tentar usar habilidade
        if (Math.random() >= 0.30) {
            return null;
        }

        for (Habilidade habilidade
                : inimigo.habilidades) {

            if (habilidade != null
                    && inimigo.getPh()
                    >= habilidade.custoPH) {

                return habilidade;
            }
        }

        return null;
    }
}