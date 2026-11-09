
package br.com.chatmultiusuario.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class TelaLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String SERVIDOR = "localhost";
    private static final int PORTA = 5000;

    private final BiConsumer<String, ConexaoChat> aoEntrar;

    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JButton botaoCriarConta;

    public TelaLogin(BiConsumer<String, ConexaoChat> aoEntrar) {
        this.aoEntrar = aoEntrar;

        configurarJanela();
    }

    private void configurarJanela() {
        setTitle("Chat Multiusuário - Login");
        setSize(390, 350);
        setMinimumSize(new Dimension(350, 330));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel();
        principal.setBackground(Color.WHITE);
        principal.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30));
        principal.setLayout(
                new BoxLayout(principal, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel(
                "Chat Multiusuário", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 23));
        titulo.setForeground(new Color(39, 103, 181));
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Entre na sua conta");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(100, 110, 125));
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);

        campoUsuario = new JTextField();
        campoSenha = new JPasswordField();

        campoUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoSenha.setFont(new Font("SansSerif", Font.PLAIN, 14));

        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBackground(new Color(39, 103, 181));
        botaoEntrar.setForeground(Color.WHITE);
        botaoEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        botaoEntrar.setFocusPainted(false);
        botaoEntrar.setAlignmentX(CENTER_ALIGNMENT);
        botaoEntrar.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 38));

        botaoCriarConta = new JButton("Criar conta");
        botaoCriarConta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        botaoCriarConta.setForeground(new Color(39, 103, 181));
        botaoCriarConta.setFocusPainted(false);
        botaoCriarConta.setBorderPainted(false);
        botaoCriarConta.setContentAreaFilled(false);
        botaoCriarConta.setAlignmentX(CENTER_ALIGNMENT);

        principal.add(titulo);
        principal.add(Box.createVerticalStrut(5));
        principal.add(subtitulo);
        principal.add(Box.createVerticalStrut(22));

        principal.add(new JLabel("Nome de usuário"));
        principal.add(Box.createVerticalStrut(5));
        principal.add(campoUsuario);
        campoUsuario.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 34));

        principal.add(Box.createVerticalStrut(12));
        principal.add(new JLabel("Senha"));
        principal.add(Box.createVerticalStrut(5));
        principal.add(campoSenha);
        campoSenha.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 34));

        principal.add(Box.createVerticalStrut(20));
        principal.add(botaoEntrar);
        principal.add(Box.createVerticalStrut(5));
        principal.add(botaoCriarConta);

        setContentPane(principal);

        botaoEntrar.addActionListener(e -> entrar());
        campoSenha.addActionListener(e -> entrar());
        campoUsuario.addActionListener(e -> campoSenha.requestFocusInWindow());
        botaoCriarConta.addActionListener(e -> abrirCadastro());

        setVisible(true);
    }

    private void entrar() {
        String nome = campoUsuario.getText().trim();
        char[] senha = campoSenha.getPassword();

        if (nome.isEmpty() || senha.length == 0) {
            Arrays.fill(senha, '\0');

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha o nome de usuário e a senha.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        definirBotoesAtivos(false);

        try {
            ConexaoChat conexao = new ConexaoChat(
                    SERVIDOR, PORTA, nome, senha);

            // A senha é apagada pelo construtor de ConexaoChat.
            aoEntrar.accept(nome, conexao);
            dispose();

        } catch (Exception e) {
            Arrays.fill(senha, '\0');
            campoSenha.setText("");

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage() == null
                            ? "Não foi possível entrar."
                            : e.getMessage(),
                    "Falha no login",
                    JOptionPane.ERROR_MESSAGE);

        } finally {
            if (isDisplayable()) {
                definirBotoesAtivos(true);
            }
        }
    }

    private void abrirCadastro() {
        JTextField nome = new JTextField();
        JPasswordField senha = new JPasswordField();
        JPasswordField confirmarSenha = new JPasswordField();

        JPanel formulario = new JPanel(new GridLayout(0, 1, 5, 5));
        formulario.setPreferredSize(new Dimension(300, 190));

        formulario.add(new JLabel("Escolha um nome de usuário:"));
        formulario.add(nome);
        formulario.add(new JLabel("Senha (mínimo 8 caracteres):"));
        formulario.add(senha);
        formulario.add(new JLabel("Confirme a senha:"));
        formulario.add(confirmarSenha);

        int opcao = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "Criar conta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (opcao != JOptionPane.OK_OPTION) {
            return;
        }

        String nomeUsuario = nome.getText().trim();
        char[] senhaNova = senha.getPassword();
        char[] confirmacao = confirmarSenha.getPassword();

        try {
            if (nomeUsuario.isEmpty()) {
                mostrarErroCadastro("Informe um nome de usuário.");
                return;
            }

            if (!Arrays.equals(senhaNova, confirmacao)) {
                mostrarErroCadastro("As senhas não coincidem.");
                return;
            }

            String erro = ConexaoChat.cadastrar(
                    SERVIDOR, PORTA, nomeUsuario, senhaNova);

            if (erro == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Conta criada com sucesso! Agora você pode entrar.",
                        "Cadastro concluído",
                        JOptionPane.INFORMATION_MESSAGE);

                campoUsuario.setText(nomeUsuario);
                campoSenha.setText("");
                campoSenha.requestFocusInWindow();

            } else {
                mostrarErroCadastro(erro);
            }

        } finally {
            Arrays.fill(senhaNova, '\0');
            Arrays.fill(confirmacao, '\0');
        }
    }

    private void mostrarErroCadastro(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Não foi possível criar a conta",
                JOptionPane.WARNING_MESSAGE);
    }

    private void definirBotoesAtivos(boolean ativo) {
        botaoEntrar.setEnabled(ativo);
        botaoCriarConta.setEnabled(ativo);
    }
}
