package entidade;

import main.SistemaElementos;

public class Fogo extends Birdmon {

    public Fogo(
            String nome,
            int nivel,
            int vidaMaxima,
            int phMaximo,
            int ataque,
            int defesa,
            int velocidade) {

        super(
                nome,
                "Fogo",
                nivel,
                vidaMaxima,
                phMaximo,
                ataque,
                defesa,
                velocidade
        );
    }

    @Override
    public int calcularDano(Birdmon defensor) {

        if (defensor == null) {
            return 0;
        }

        int danoBase = getAtaqueComEfeitos();

        double multiplicador = SistemaElementos.multiplicador(
                getTipo(),
                defensor.getTipo()
        );

        int dano = (int) Math.round(danoBase * multiplicador);

        dano -= defensor.getDefesaComEfeitos();

        if (dano < 1) {
            dano = 1;
        }

        return dano;
    }
}