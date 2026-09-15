package main;

import java.util.ArrayList;

import entidade.Birdmon;

public class Pokedex {

    // =========================
    // ENTRADA DA POKÉDEX
    // =========================

    public static class EntradaPokedex {

        private String nome;
        private String tipo;
        private boolean capturado;

        public EntradaPokedex(String nome, String tipo, boolean capturado) {
            this.nome = nome;
            this.tipo = tipo;
            this.capturado = capturado;
        }

        public String getNome() {
            return nome;
        }

        public String getTipo() {
            return tipo;
        }

        public boolean isCapturado() {
            return capturado;
        }

        public void marcarComoCapturado() {
            this.capturado = true;
        }
    }

    private ArrayList<EntradaPokedex> registros = new ArrayList<>();

    // =========================
    // REGISTRAR COMO VISTO
    // =========================

    public void registrarVisto(Birdmon birdmon) {

        if (birdmon == null) {
            return;
        }

        EntradaPokedex existente =
                buscar(birdmon.getNome(), birdmon.getTipo());

        if (existente == null) {

            registros.add(
                    new EntradaPokedex(
                            birdmon.getNome(),
                            birdmon.getTipo(),
                            false
                    )
            );

            System.out.println(
                    "Pokedex: "
                            + birdmon.getNome()
                            + " registrado como visto."
            );
        }
    }

    // =========================
    // REGISTRAR COMO CAPTURADO
    // =========================

    public void registrarCapturado(Birdmon birdmon) {

        if (birdmon == null) {
            return;
        }

        EntradaPokedex existente =
                buscar(birdmon.getNome(), birdmon.getTipo());

        if (existente == null) {

            registros.add(
                    new EntradaPokedex(
                            birdmon.getNome(),
                            birdmon.getTipo(),
                            true
                    )
            );

        } else if (!existente.isCapturado()) {

            existente.marcarComoCapturado();
        }

        System.out.println(
                "Pokedex: "
                        + birdmon.getNome()
                        + " registrado como capturado."
        );
    }

    // =========================
    // BUSCA (evita duplicar entradas)
    // =========================

    private EntradaPokedex buscar(String nome, String tipo) {

        for (EntradaPokedex entrada : registros) {

            if (entrada.getNome().equals(nome)
                    && entrada.getTipo().equals(tipo)) {

                return entrada;
            }
        }

        return null;
    }

    public boolean jaRegistrado(String nome, String tipo) {
        return buscar(nome, tipo) != null;
    }

    // =========================
    // CONSULTAS
    // =========================

    public ArrayList<EntradaPokedex> getRegistros() {
        return registros;
    }

    public int totalRegistrados() {
        return registros.size();
    }

    public int totalCapturados() {

        int contador = 0;

        for (EntradaPokedex entrada : registros) {

            if (entrada.isCapturado()) {
                contador++;
            }
        }

        return contador;
    }
}
