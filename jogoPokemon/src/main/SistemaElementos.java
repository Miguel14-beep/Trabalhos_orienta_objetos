package main;

public class SistemaElementos {

    public static double multiplicador(
            String atacante,
            String defensor) {

        // =========================
        // VANTAGENS
        // =========================

        // Fogo é forte contra Planta
        if (atacante.equals("Fogo")
                && defensor.equals("Planta")) {

            return 1.5;
        }

        // Planta é forte contra Água
        if (atacante.equals("Planta")
                && defensor.equals("Agua")) {

            return 1.5;
        }

        // Água é forte contra Fogo
        if (atacante.equals("Agua")
                && defensor.equals("Fogo")) {

            return 1.5;
        }

        // =========================
        // DESVANTAGENS
        // =========================

        // Fogo é fraco contra Água
        if (atacante.equals("Fogo")
                && defensor.equals("Agua")) {

            return 0.75;
        }

        // Água é fraca contra Planta
        if (atacante.equals("Agua")
                && defensor.equals("Planta")) {

            return 0.75;
        }

        // Planta é fraca contra Fogo
        if (atacante.equals("Planta")
                && defensor.equals("Fogo")) {

            return 0.75;
        }

        // =========================
        // NEUTRO
        // =========================

        return 1.0;
    }
}