package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoricoPartidas {

    private static final String ARQUIVO = "historico_partidas.txt";

    private List<RegistroPartida> registros = new ArrayList<>();

    public HistoricoPartidas() {
        carregar();
    }

    // =========================
    // REGISTRAR
    // =========================

    public void registrarVitoria(
            int especiesCapturadas,
            long duracaoSegundos) {

        adicionar("VITORIA", especiesCapturadas, duracaoSegundos);
    }

    public void registrarDerrota(
            int especiesCapturadas,
            long duracaoSegundos) {

        adicionar("DERROTA", especiesCapturadas, duracaoSegundos);
    }

    private void adicionar(
            String resultado,
            int especiesCapturadas,
            long duracaoSegundos) {

        String data = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm"
        ).format(new Date());

        RegistroPartida registro = new RegistroPartida(
                resultado,
                data,
                especiesCapturadas,
                duracaoSegundos
        );

        // Mais recente primeiro
        registros.add(0, registro);

        salvar();
    }

    // =========================
    // PERSISTÊNCIA EM ARQUIVO
    // =========================

    private void carregar() {

        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader br =
                new BufferedReader(new FileReader(arquivo))) {

            String linha;

            while ((linha = br.readLine()) != null) {

                RegistroPartida registro =
                        RegistroPartida.fromLinhaArquivo(linha);

                if (registro != null) {
                    registros.add(registro);
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void salvar() {

        try (PrintWriter pw =
                new PrintWriter(new FileWriter(ARQUIVO))) {

            for (RegistroPartida registro : registros) {
                pw.println(registro.toLinhaArquivo());
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // =========================
    // CONSULTAS
    // =========================

    public List<RegistroPartida> getRegistros() {
        return registros;
    }

    public int totalVitorias() {

        int contador = 0;

        for (RegistroPartida registro : registros) {

            if (registro.isVitoria()) {
                contador++;
            }
        }

        return contador;
    }

    public int totalDerrotas() {
        return registros.size() - totalVitorias();
    }
}