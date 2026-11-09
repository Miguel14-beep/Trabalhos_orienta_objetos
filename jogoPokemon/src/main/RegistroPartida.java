package main;

public class RegistroPartida {

    public String resultado;   // "VITORIA" ou "DERROTA"
    public String data;        // data/hora formatada
    public int especiesCapturadas;
    public long duracaoSegundos;

    public RegistroPartida(
            String resultado,
            String data,
            int especiesCapturadas,
            long duracaoSegundos) {

        this.resultado = resultado;
        this.data = data;
        this.especiesCapturadas = especiesCapturadas;
        this.duracaoSegundos = duracaoSegundos;
    }

    // =========================
    // SERIALIZAÇÃO SIMPLES EM TEXTO
    // =========================

    public String toLinhaArquivo() {

        return resultado
                + ";" + data
                + ";" + especiesCapturadas
                + ";" + duracaoSegundos;
    }

    public static RegistroPartida fromLinhaArquivo(String linha) {

        if (linha == null || linha.isBlank()) {
            return null;
        }

        try {

            String[] partes = linha.split(";");

            if (partes.length < 4) {
                return null;
            }

            return new RegistroPartida(
                    partes[0],
                    partes[1],
                    Integer.parseInt(partes[2]),
                    Long.parseLong(partes[3])
            );

        } catch (Exception e) {

            return null;
        }
    }

    // =========================
    // FORMATAÇÃO
    // =========================

    public String getDuracaoFormatada() {

        long minutos = duracaoSegundos / 60;
        long segundos = duracaoSegundos % 60;

        return minutos + "m " + segundos + "s";
    }

    public boolean isVitoria() {
        return "VITORIA".equals(resultado);
    }
}