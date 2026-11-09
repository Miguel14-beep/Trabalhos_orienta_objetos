
package br.com.chatmultiusuario.persistence;

import br.com.chatmultiusuario.model.Mensagem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MensagemRepository {

    private static final String URL = "jdbc:sqlite:chat.db";

    public MensagemRepository() throws SQLException {
        criarTabela();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void criarTabela() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS mensagens (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    remetente TEXT NOT NULL,
                    conteudo TEXT NOT NULL,
                    horario TEXT NOT NULL
                )
                """;

        try (Connection conexao = conectar();
             Statement comando = conexao.createStatement()) {

            comando.execute(sql);
        }
    }

    public void salvar(Mensagem mensagem) throws SQLException {
        String sql = """
                INSERT INTO mensagens (remetente, conteudo, horario)
                VALUES (?, ?, ?)
                """;

        try (Connection conexao = conectar();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(
                    1, mensagem.getRemetente().getNome());

            comando.setString(
                    2, mensagem.getConteudo());

            comando.setString(
                    3, mensagem.getHorario().toString());

            comando.executeUpdate();
        }
    }

    public List<Mensagem> buscarUltimas(int quantidade)
            throws SQLException {

        List<Mensagem> mensagens = new ArrayList<>();

        if (quantidade <= 0) {
            return mensagens;
        }

        String sql = """
                SELECT remetente, conteudo, horario
                FROM (
                    SELECT id, remetente, conteudo, horario
                    FROM mensagens
                    ORDER BY id DESC
                    LIMIT ?
                )
                ORDER BY id ASC
                """;

        try (Connection conexao = conectar();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setInt(1, quantidade);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    String nome = resultado.getString("remetente");
                    String conteudo = resultado.getString("conteudo");
                    LocalDateTime horario = LocalDateTime.parse(
                            resultado.getString("horario"));

                    Mensagem mensagem = new Mensagem(
                            new br.com.chatmultiusuario.model.Usuario(nome),
                            conteudo,
                            horario);

                    mensagens.add(mensagem);
                }
            }
        }

        return mensagens;
    }
}
