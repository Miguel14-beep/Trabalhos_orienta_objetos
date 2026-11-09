
package br.com.chatmultiusuario.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.chatmultiusuario.persistence.MensagemRepository;
import br.com.chatmultiusuario.persistence.UsuarioRepository;

public class Servidor {

    private static final int PORTA = 5000;

    private static final Set<ClienteHandler> clientes =
            ConcurrentHashMap.newKeySet();

    private static final ExecutorService executor =
            Executors.newCachedThreadPool();

    public static void main(String[] args) {

        System.out.println("=== SERVIDOR DE CHAT ===");

        final MensagemRepository repositorioMensagens;
        final UsuarioRepository repositorioUsuarios;

        try {
            repositorioMensagens = new MensagemRepository();
            repositorioUsuarios = new UsuarioRepository();

            System.out.println(
                    "Banco de dados inicializado com sucesso.");

        } catch (SQLException e) {
            System.err.println(
                    "Erro ao inicializar o banco: "
                            + e.getMessage());

            executor.shutdown();
            return;
        }

        try (ServerSocket servidor = new ServerSocket(PORTA)) {

            System.out.println(
                    "Servidor iniciado na porta " + PORTA);

            while (true) {
                Socket socketCliente = servidor.accept();

                System.out.println(
                        "Nova conexão: "
                                + socketCliente.getInetAddress());

                ClienteHandler cliente = new ClienteHandler(
                        socketCliente,
                        clientes,
                        repositorioMensagens,
                        repositorioUsuarios);

                executor.execute(cliente);
            }

        } catch (IOException e) {
            System.err.println(
                    "Erro no servidor: " + e.getMessage());

        } finally {
            executor.shutdown();
            System.out.println("Servidor encerrado.");
        }
    }
}
