
package br.com.chatmultiusuario.persistence;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UsuarioRepository {

    private static final String URL = "jdbc:sqlite:chat.db";

    private static final int ITERACOES = 210_000;
    private static final int TAMANHO_SALT = 16;
    private static final int TAMANHO_HASH = 256;

    private final SecureRandom random = new SecureRandom();

    public UsuarioRepository() throws SQLException {
        criarTabela();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void criarTabela() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome_usuario TEXT NOT NULL COLLATE NOCASE UNIQUE,
                salt TEXT NOT NULL,
                hash_senha TEXT NOT NULL,
                criado_em TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (Connection conexao = conectar();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.executeUpdate();
        }
    }

    public synchronized void cadastrar(
            String nomeUsuario,
            char[] senha) throws SQLException {

        validarNome(nomeUsuario);
        validarSenha(senha);

        byte[] salt = new byte[TAMANHO_SALT];
        random.nextBytes(salt);

        byte[] hash;

        try {
            hash = gerarHash(senha, salt);
        } catch (GeneralSecurityException e) {
            throw new SQLException(
                    "Não foi possível proteger a senha.", e);
        }

        String sql = """
            INSERT INTO usuarios
                (nome_usuario, salt, hash_senha)
            VALUES (?, ?, ?)
            """;

        try (Connection conexao = conectar();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(1, nomeUsuario.trim());
            comando.setString(
                    2, Base64.getEncoder().encodeToString(salt));
            comando.setString(
                    3, Base64.getEncoder().encodeToString(hash));

            comando.executeUpdate();

        } finally {
            Arrays.fill(salt, (byte) 0);
            Arrays.fill(hash, (byte) 0);
        }
    }

    public boolean autenticar(
            String nomeUsuario,
            char[] senha) throws SQLException {

        if (nomeUsuario == null || nomeUsuario.isBlank()
                || senha == null || senha.length == 0) {
            return false;
        }

        String sql = """
            SELECT salt, hash_senha
            FROM usuarios
            WHERE nome_usuario = ?
            """;

        try (Connection conexao = conectar();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(1, nomeUsuario.trim());

            try (ResultSet resultado = comando.executeQuery()) {

                if (!resultado.next()) {
                    return false;
                }

                byte[] salt = Base64.getDecoder().decode(
                        resultado.getString("salt"));

                byte[] hashArmazenado =
                        Base64.getDecoder().decode(
                                resultado.getString("hash_senha"));

                byte[] hashInformado = null;

                try {
                    hashInformado = gerarHash(senha, salt);

                    return MessageDigest.isEqual(
                            hashArmazenado, hashInformado);

                } catch (GeneralSecurityException e) {
                    throw new SQLException(
                            "Não foi possível verificar a senha.", e);

                } finally {
                    Arrays.fill(salt, (byte) 0);
                    Arrays.fill(hashArmazenado, (byte) 0);

                    if (hashInformado != null) {
                        Arrays.fill(hashInformado, (byte) 0);
                    }
                }
            }
        }
    }

    private byte[] gerarHash(
            char[] senha,
            byte[] salt) throws GeneralSecurityException {

        PBEKeySpec especificacao = new PBEKeySpec(
                senha, salt, ITERACOES, TAMANHO_HASH);

        try {
            SecretKeyFactory fabrica =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");

            return fabrica.generateSecret(especificacao).getEncoded();

        } finally {
            especificacao.clearPassword();
        }
    }

    private void validarNome(String nome) throws SQLException {
        if (nome == null) {
            throw new SQLException("Informe um nome de usuário.");
        }

        String nomeLimpo = nome.trim();

        if (nomeLimpo.length() < 3 || nomeLimpo.length() > 30) {
            throw new SQLException(
                    "O nome de usuário deve ter entre 3 e 30 caracteres.");
        }

        if (!nomeLimpo.matches("[a-zA-Z0-9_.-]+")) {
            throw new SQLException(
                    "Use somente letras sem acento, números, ponto, "
                    + "hífen ou sublinhado no nome de usuário.");
        }
    }

    private void validarSenha(char[] senha) throws SQLException {
        if (senha == null || senha.length < 8) {
            throw new SQLException(
                    "A senha deve ter pelo menos 8 caracteres.");
        }

        if (senha.length > 128) {
            throw new SQLException(
                    "A senha não pode ultrapassar 128 caracteres.");
        }
    }
}
