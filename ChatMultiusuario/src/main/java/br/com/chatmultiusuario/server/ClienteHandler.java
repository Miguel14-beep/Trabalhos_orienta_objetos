
package br.com.chatmultiusuario.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;

import br.com.chatmultiusuario.model.Mensagem;
import br.com.chatmultiusuario.model.Usuario;
import br.com.chatmultiusuario.persistence.MensagemRepository;
import br.com.chatmultiusuario.persistence.UsuarioRepository;

public class ClienteHandler implements Runnable {

    private static final int QUANTIDADE_HISTORICO = 20;

    private final Socket socket;
    private final Set<ClienteHandler> clientes;
    private final MensagemRepository repositorioMensagens;
    private final UsuarioRepository repositorioUsuarios;

    private PrintWriter saida;
    private Usuario usuario;

    public ClienteHandler(
            Socket socket,
            Set<ClienteHandler> clientes,
            MensagemRepository repositorioMensagens,
            UsuarioRepository repositorioUsuarios) {

        this.socket = socket;
        this.clientes = clientes;
        this.repositorioMensagens = repositorioMensagens;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    @Override
    public void run() {

        boolean registrado = false;

        try (
            Socket conexao = socket;

            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(
                    conexao.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            PrintWriter escritor = new PrintWriter(
                conexao.getOutputStream(),
                true,
                StandardCharsets.UTF_8
            )
        ) {
            saida = escritor;

            String operacao = entrada.readLine();
            String nome = entrada.readLine();
            String senhaTexto = entrada.readLine();

            if (operacao == null || nome == null
                    || senhaTexto == null) {
                return;
            }

            char[] senha = senhaTexto.toCharArray();
            

            try {
                if ("REGISTER".equals(operacao)) {
                    processarCadastro(nome, senha);
                    return;
                }

                if (!"LOGIN".equals(operacao)) {
                    saida.println(
                            "AUTH_FAIL|Operação desconhecida.");
                    return;
                }

                boolean autenticado;

                try {
                    autenticado = repositorioUsuarios.autenticar(
                            nome, senha);
                } catch (SQLException e) {
                    System.err.println(
                            "Erro ao autenticar usuário: "
                                    + e.getMessage());

                    saida.println(
                            "AUTH_FAIL|Erro ao consultar o banco de dados.");
                    return;
                }

                if (!autenticado) {
                    saida.println(
                            "AUTH_FAIL|Nome de usuário ou senha incorretos.");
                    return;
                }

            } finally {
                Arrays.fill(senha, '\0');
            }

            usuario = new Usuario(nome.trim());

            // A partir daqui, o cliente já está autenticado.
            synchronized (clientes) {

                clientes.add(this);
                registrado = true;

                saida.println("AUTH_OK");

                saida.println(
                        "Bem-vindo ao chat, "
                                + usuario.getNome() + "!");

                try {
                    for (Mensagem mensagem :
                            repositorioMensagens.buscarUltimas(
                                    QUANTIDADE_HISTORICO)) {

                        saida.println(mensagem.formatar());
                    }

                } catch (SQLException e) {
                    System.err.println(
                            "Erro ao carregar histórico: "
                                    + e.getMessage());

                    saida.println(
                            "[Sistema] Não foi possível carregar "
                                    + "o histórico.");
                }

                enviarATodos(
                        "[Sistema] " + usuario.getNome()
                                + " entrou no chat.");
            }

            System.out.println(
                    usuario.getNome() + " autenticado e conectado.");

            String texto;

            while ((texto = entrada.readLine()) != null) {

                texto = texto.trim();

                if (texto.isEmpty()) {
                    continue;
                }

                if (texto.equalsIgnoreCase("/sair")) {
                    break;
                }

                Mensagem mensagem = new Mensagem(usuario, texto);

                try {
                    repositorioMensagens.salvar(mensagem);

                } catch (SQLException e) {
                    System.err.println(
                            "Erro ao salvar mensagem: "
                                    + e.getMessage());

                    saida.println(
                            "[Sistema] Erro ao salvar a mensagem. "
                                    + "Ela não foi enviada ao chat.");
                    continue;
                }

                enviarATodos(mensagem.formatar());
                System.out.println(mensagem.formatar());
            }

        } catch (IOException e) {
            System.err.println(
                    "Conexão encerrada: " + e.getMessage());

        } finally {

            if (registrado) {
                synchronized (clientes) {
                    clientes.remove(this);

                    enviarATodos(
                            "[Sistema] " + usuario.getNome()
                                    + " saiu do chat.");
                }
            }

            System.out.println(
                    usuario != null
                            ? usuario.getNome() + " desconectado."
                            : "Conexão temporária encerrada.");
        }
    }


private void processarCadastro(String nome, char[] senha) {
    try {
        System.out.println(
                "Tentando cadastrar usuário: " + nome.trim());

        repositorioUsuarios.cadastrar(nome, senha);

        saida.println("REGISTER_OK");

        if (saida.checkError()) {
            System.err.println(
                    "Não foi possível enviar a confirmação do cadastro.");
        }

        System.out.println(
                "Conta cadastrada com sucesso: " + nome.trim());

    } catch (SQLException e) {
        System.err.println(
                "Erro SQL durante o cadastro:");
        e.printStackTrace();

        String mensagem = e.getMessage();

        if (mensagem != null
                && mensagem.toLowerCase().contains("unique")) {
            mensagem = "Esse nome de usuário já está cadastrado.";
        } else {
            mensagem = "Erro no banco de dados: " + mensagem;
        }

        saida.println("REGISTER_FAIL|" + mensagem);

    } catch (Exception e) {
        System.err.println(
                "Erro inesperado durante o cadastro:");
        e.printStackTrace();

        saida.println(
                "REGISTER_FAIL|Erro inesperado no servidor.");

    } finally {
        Arrays.fill(senha, '\0');
    }
}


    private void enviarATodos(String mensagem) {
        synchronized (clientes) {
            for (ClienteHandler cliente : clientes) {
                if (cliente.saida != null) {
                    cliente.saida.println(mensagem);
                }
            }
        }
    }
}
