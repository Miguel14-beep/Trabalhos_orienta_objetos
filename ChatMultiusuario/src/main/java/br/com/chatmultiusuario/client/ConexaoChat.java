
package br.com.chatmultiusuario.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ConexaoChat implements AutoCloseable {

    private final Socket socket;
    private final BufferedReader entrada;
    private final PrintWriter saida;

    public ConexaoChat(
            String servidor,
            int porta,
            String nomeUsuario,
            char[] senha) throws IOException {

        Socket novoSocket = new Socket(servidor, porta);

        BufferedReader novaEntrada = null;
        PrintWriter novaSaida = null;

        try {
            novaEntrada = new BufferedReader(
                    new InputStreamReader(
                            novoSocket.getInputStream(),
                            StandardCharsets.UTF_8));

            novaSaida = new PrintWriter(
                    novoSocket.getOutputStream(),
                    true,
                    StandardCharsets.UTF_8);

            novaSaida.println("LOGIN");
            novaSaida.println(nomeUsuario);
            novaSaida.println(senha);

            if (novaSaida.checkError()) {
                throw new IOException(
                        "Não foi possível enviar as credenciais.");
            }

            String resposta = novaEntrada.readLine();

            if (resposta == null || !resposta.equals("AUTH_OK")) {
                String motivo = resposta == null
                        ? "O servidor encerrou a conexão."
                        : resposta.startsWith("AUTH_FAIL|")
                                ? resposta.substring("AUTH_FAIL|".length())
                                : resposta;

                throw new IOException(motivo);
            }

            this.socket = novoSocket;
            this.entrada = novaEntrada;
            this.saida = novaSaida;

        } catch (IOException | RuntimeException e) {
            try {
                novoSocket.close();
            } catch (IOException erroFechamento) {
                e.addSuppressed(erroFechamento);
            }

            throw e;

        } finally {
            Arrays.fill(senha, '\0');
        }
    }

    public static String cadastrar(
            String servidor,
            int porta,
            String nomeUsuario,
            char[] senha) {

        try (Socket socket = new Socket(servidor, porta);
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(
                             socket.getInputStream(),
                             StandardCharsets.UTF_8));
             PrintWriter saida = new PrintWriter(
                     socket.getOutputStream(),
                     true,
                     StandardCharsets.UTF_8)) {

            saida.println("REGISTER");
            saida.println(nomeUsuario);
            saida.println(senha);

            if (saida.checkError()) {
                return "Não foi possível enviar os dados ao servidor.";
            }

            String resposta = entrada.readLine();

            if ("REGISTER_OK".equals(resposta)) {
                return null;
            }

            if (resposta != null
                    && resposta.startsWith("REGISTER_FAIL|")) {
                return resposta.substring("REGISTER_FAIL|".length());
            }

            return "Resposta inesperada do servidor.";

        } catch (IOException e) {
            return "Não foi possível conectar ao servidor: "
                    + e.getMessage();

        } finally {
            Arrays.fill(senha, '\0');
        }
    }

    public void enviarMensagem(String mensagem) throws IOException {
        if (socket.isClosed()) {
            throw new IOException("A conexão está fechada.");
        }

        saida.println(mensagem);

        if (saida.checkError()) {
            throw new IOException(
                    "Falha ao enviar mensagem ao servidor.");
        }
    }

    public String receberMensagem() throws IOException {
        return entrada.readLine();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
