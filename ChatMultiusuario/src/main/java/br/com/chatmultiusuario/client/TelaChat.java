
package br.com.chatmultiusuario.client;

import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TelaChat extends JFrame {

    private static final long serialVersionUID = 1L;

    

    private static final Color AZUL = new Color(39, 103, 181);
    private static final Color AZUL_ESCURO = new Color(29, 78, 143);
    private static final Color FUNDO = new Color(245, 247, 250);
    private static final Color BORDA = new Color(207, 220, 238);
    private static final Color TEXTO = new Color(35, 47, 65);
    private static final Color ROXO = new Color(147, 51, 191);

    private static final Pattern PADRAO_MENSAGEM =
            Pattern.compile("^\\[(.*?)\\]\\s+([^:]+):\\s*(.*)$");

    private ConexaoChat conexao;
    private volatile boolean conectado = false;

    private String nomeUsuario;

    private JPanel painelMensagens;
    private JScrollPane rolagemMensagens;
    private JTextField campoMensagem;
    private JButton botaoEnviar;
    private JLabel statusConexao;
    private JLabel tituloGrupo;
    private JLabel quantidadeParticipantes;
    private JPanel listaParticipantes;
    

	public TelaChat(String nomeUsuario, ConexaoChat conexao) {
	    this.nomeUsuario = nomeUsuario;
	    this.conexao = conexao;
	
	    configurarJanela();
	
	    conectado = true;
	
	    campoMensagem.setEnabled(true);
	    botaoEnviar.setEnabled(true);
	
	    statusConexao.setText("Conectado · Online");
	
	    adicionarParticipante(nomeUsuario, true);
	    adicionarAviso("Conectado ao servidor.");
	
	    campoMensagem.requestFocusInWindow();
	
	    iniciarRecebimento();
	}


    private void configurarJanela() {
        setTitle("Chat Multiusuário");
        setSize(820, 580);
        setMinimumSize(new Dimension(650, 450));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Mantém o visual padrão do Swing.
        }

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(Color.WHITE);

        principal.add(criarCabecalho(), BorderLayout.NORTH);
        principal.add(criarAreaCentral(), BorderLayout.CENTER);

        setContentPane(principal);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                desconectar();
                dispose();
            }
        });

        setVisible(true);
    }

    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout(12, 0));
        cabecalho.setBackground(AZUL);
        cabecalho.setBorder(
                BorderFactory.createEmptyBorder(8, 14, 8, 14));
        cabecalho.setPreferredSize(new Dimension(820, 66));

        JPanel grupo = new JPanel(new FlowLayout(
                FlowLayout.LEFT, 10, 0));
        grupo.setOpaque(false);

        JLabel avatarGrupo = new JLabel("GD", SwingConstants.CENTER);
        avatarGrupo.setOpaque(true);
        avatarGrupo.setBackground(new Color(190, 153, 43));
        avatarGrupo.setForeground(Color.WHITE);
        avatarGrupo.setFont(new Font("SansSerif", Font.BOLD, 17));
        avatarGrupo.setPreferredSize(new Dimension(44, 44));
        avatarGrupo.setBorder(BorderFactory.createLineBorder(
                new Color(255, 218, 75), 3));

        JPanel informacoes = new JPanel();
        informacoes.setOpaque(false);
        informacoes.setLayout(
        	    new BoxLayout(informacoes, BoxLayout.Y_AXIS));

        JLabel nomeGrupo = new JLabel("Grupo do Trabalho");
        nomeGrupo.setForeground(Color.WHITE);
        nomeGrupo.setFont(
                new Font("SansSerif", Font.BOLD, 15));

        statusConexao = new JLabel("Conectando...");
        statusConexao.setForeground(new Color(225, 237, 255));
        statusConexao.setFont(
                new Font("SansSerif", Font.PLAIN, 11));

        informacoes.add(Box.createVerticalStrut(4));
        informacoes.add(nomeGrupo);
        informacoes.add(Box.createVerticalStrut(5));
        informacoes.add(statusConexao);

        grupo.add(avatarGrupo);
        grupo.add(informacoes);

        JButton botaoConvidar = new JButton("+ Convidar");
        botaoConvidar.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        botaoConvidar.setForeground(AZUL_ESCURO);
        botaoConvidar.setBackground(Color.WHITE);
        botaoConvidar.setFocusPainted(false);
        botaoConvidar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(205, 220, 241)),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));

        botaoConvidar.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "O recurso de convite ainda não foi implementado.",
                        "Convidar participantes",
                        JOptionPane.INFORMATION_MESSAGE));

        cabecalho.add(grupo, BorderLayout.CENTER);
        cabecalho.add(botaoConvidar, BorderLayout.EAST);

        return cabecalho;
    }

    private JPanel criarAreaCentral() {
        JPanel areaCentral = new JPanel(new BorderLayout());
        areaCentral.setBackground(Color.WHITE);

        JPanel conversa = criarPainelConversa();
        JPanel participantes = criarPainelParticipantes();

        JSplitPane divisao = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                conversa,
                participantes);

        divisao.setResizeWeight(0.76);
        divisao.setDividerLocation(0.76);
        divisao.setDividerSize(5);
        divisao.setBorder(BorderFactory.createEmptyBorder());
        divisao.setContinuousLayout(true);
        divisao.setBackground(Color.WHITE);

        areaCentral.add(divisao, BorderLayout.CENTER);

        return areaCentral;
    }

    private JPanel criarPainelConversa() {
        JPanel conversa = new JPanel(new BorderLayout(0, 8));
        conversa.setBackground(Color.WHITE);
        conversa.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 8));

        painelMensagens = new JPanel();
        painelMensagens.setLayout(
                new BoxLayout(painelMensagens, BoxLayout.Y_AXIS));
        painelMensagens.setBackground(Color.WHITE);
        painelMensagens.setBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4));

        rolagemMensagens = new JScrollPane(painelMensagens);
        rolagemMensagens.setBorder(
                BorderFactory.createLineBorder(BORDA));
        rolagemMensagens.getVerticalScrollBar().setUnitIncrement(16);
        rolagemMensagens.getViewport().setBackground(Color.WHITE);

        conversa.add(rolagemMensagens, BorderLayout.CENTER);
        conversa.add(criarCampoEnvio(), BorderLayout.SOUTH);

        return conversa;
    }

    private JPanel criarCampoEnvio() {
        JPanel painelEnvio = new JPanel(new BorderLayout(8, 0));
        painelEnvio.setBackground(Color.WHITE);
        painelEnvio.setPreferredSize(new Dimension(0, 65));

        campoMensagem = new JTextField();
        campoMensagem.setFont(
                new Font("SansSerif", Font.PLAIN, 14));
        campoMensagem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        campoMensagem.setEnabled(false);

        botaoEnviar = new JButton("Enviar");
        botaoEnviar.setFont(
                new Font("SansSerif", Font.BOLD, 13));
        botaoEnviar.setForeground(Color.WHITE);
        botaoEnviar.setBackground(AZUL);
        botaoEnviar.setFocusPainted(false);
        botaoEnviar.setBorder(BorderFactory.createEmptyBorder(
                10, 22, 10, 22));
        botaoEnviar.setEnabled(false);

        botaoEnviar.addActionListener(e -> enviarMensagem());
        campoMensagem.addActionListener(e -> enviarMensagem());

        painelEnvio.add(campoMensagem, BorderLayout.CENTER);
        painelEnvio.add(botaoEnviar, BorderLayout.EAST);

        return painelEnvio;
    }

    private JPanel criarPainelParticipantes() {
        JPanel participantes = new JPanel(new BorderLayout());
        participantes.setBackground(Color.WHITE);
        participantes.setBorder(BorderFactory.createEmptyBorder(
                10, 5, 10, 10));

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        conteudo.setBorder(BorderFactory.createLineBorder(BORDA));

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBackground(Color.WHITE);
        topo.setBorder(BorderFactory.createEmptyBorder(
                8, 10, 8, 10));

        JLabel titulo = new JLabel("Participantes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(AZUL_ESCURO);

        quantidadeParticipantes = new JLabel("0 participantes");
        quantidadeParticipantes.setFont(
                new Font("SansSerif", Font.PLAIN, 11));
        quantidadeParticipantes.setForeground(
                new Color(105, 119, 139));

        topo.add(titulo);
        topo.add(Box.createVerticalStrut(5));
        topo.add(quantidadeParticipantes);

        listaParticipantes = new JPanel();
        listaParticipantes.setLayout(
                new BoxLayout(listaParticipantes, BoxLayout.Y_AXIS));
        listaParticipantes.setBackground(Color.WHITE);
        listaParticipantes.setBorder(
                BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JScrollPane rolagemParticipantes =
                new JScrollPane(listaParticipantes);

        rolagemParticipantes.setBorder(BorderFactory.createEmptyBorder());
        rolagemParticipantes.getVerticalScrollBar()
                .setUnitIncrement(12);
        rolagemParticipantes.getViewport().setBackground(Color.WHITE);

        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(rolagemParticipantes, BorderLayout.CENTER);

        participantes.add(conteudo, BorderLayout.CENTER);

        return participantes;
    }

    

   

    private void iniciarRecebimento() {
        Thread thread = new Thread(() -> {
            try {
                String mensagem;

                while (conectado
                        && (mensagem = conexao.receberMensagem()) != null) {

                    final String recebida = mensagem;

                    SwingUtilities.invokeLater(
                            () -> processarMensagem(recebida));
                }

            } catch (IOException e) {
                if (conectado) {
                    SwingUtilities.invokeLater(
                            () -> adicionarAviso(
                                    "A conexão com o servidor foi encerrada."));
                }

            } finally {
                conectado = false;

                SwingUtilities.invokeLater(() -> {
                    campoMensagem.setEnabled(false);
                    botaoEnviar.setEnabled(false);
                    statusConexao.setText("Desconectado");
                });
            }
        }, "Thread-Recebimento-Chat");

        thread.setDaemon(true);
        thread.start();
    }

    private void processarMensagem(String mensagem) {
        Matcher matcher = PADRAO_MENSAGEM.matcher(mensagem);

        if (matcher.matches()) {
            String horario = matcher.group(1);
            String remetente = matcher.group(2).trim();
            String conteudo = matcher.group(3);

            boolean minhaMensagem =
                    remetente.equalsIgnoreCase(nomeUsuario);

            adicionarBalao(
                    remetente,
                    conteudo,
                    horario,
                    minhaMensagem);

        } else {
            adicionarAviso(mensagem);
        }
    }

    private void adicionarBalao(
            String remetente,
            String conteudo,
            String horario,
            boolean minhaMensagem) {

        JPanel linha = new JPanel(new BorderLayout());
        linha.setBackground(Color.WHITE);
        linha.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel balao = new JPanel();
        balao.setLayout(new BoxLayout(balao, BoxLayout.Y_AXIS));
        balao.setOpaque(true);
        balao.setBackground(
                minhaMensagem
                        ? new Color(222, 234, 251)
                        : new Color(247, 249, 252));

        balao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        minhaMensagem
                                ? new Color(157, 190, 242)
                                : BORDA),
                BorderFactory.createEmptyBorder(7, 10, 5, 10)));

        JLabel nome = new JLabel(remetente);
        nome.setFont(new Font("SansSerif", Font.BOLD, 12));
        nome.setForeground(minhaMensagem ? AZUL_ESCURO : ROXO);

        JTextArea texto = new JTextArea(conteudo);
        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setOpaque(false);
        texto.setForeground(TEXTO);
        texto.setFont(new Font("SansSerif", Font.PLAIN, 13));
        texto.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        Dimension tamanhoTexto = texto.getPreferredSize();
        int larguraMaxima = Math.max(
                150,
                Math.min(340, rolagemMensagens.getViewport().getWidth() - 90));

        texto.setSize(new Dimension(larguraMaxima, Short.MAX_VALUE));
        texto.setPreferredSize(new Dimension(
                Math.min(larguraMaxima, Math.max(100, tamanhoTexto.width)),
                texto.getPreferredSize().height));

        JLabel hora = new JLabel(horario);
        hora.setFont(new Font("SansSerif", Font.PLAIN, 10));
        hora.setForeground(new Color(105, 119, 139));
        hora.setAlignmentX(Component.LEFT_ALIGNMENT);

        nome.setAlignmentX(Component.LEFT_ALIGNMENT);
        texto.setAlignmentX(Component.LEFT_ALIGNMENT);

        balao.add(nome);
        balao.add(texto);
        balao.add(Box.createVerticalStrut(3));
        balao.add(hora);

        balao.setMaximumSize(new Dimension(larguraMaxima + 30, 1000));

        if (minhaMensagem) {
            linha.add(balao, BorderLayout.EAST);
        } else {
            linha.add(balao, BorderLayout.WEST);
        }

        painelMensagens.add(linha);
        painelMensagens.add(Box.createVerticalStrut(4));

        painelMensagens.revalidate();
        painelMensagens.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar barra =
                    rolagemMensagens.getVerticalScrollBar();
            barra.setValue(barra.getMaximum());
        });
    }

    private void adicionarAviso(String mensagem) {
        JLabel aviso = new JLabel(mensagem, SwingConstants.CENTER);
        aviso.setFont(new Font("SansSerif", Font.ITALIC, 11));
        aviso.setForeground(new Color(105, 119, 139));
        aviso.setAlignmentX(Component.CENTER_ALIGNMENT);
        aviso.setBorder(BorderFactory.createEmptyBorder(5, 4, 7, 4));

        painelMensagens.add(aviso);
        painelMensagens.add(Box.createVerticalStrut(3));

        painelMensagens.revalidate();
        painelMensagens.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar barra =
                    rolagemMensagens.getVerticalScrollBar();
            barra.setValue(barra.getMaximum());
        });
    }

    private void adicionarParticipante(String nome, boolean online) {
        JPanel participante = new JPanel(new BorderLayout(8, 0));
        participante.setBackground(Color.WHITE);
        participante.setBorder(BorderFactory.createEmptyBorder(
                5, 3, 5, 3));
        participante.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 55));

        JLabel avatar = new JLabel(
                iniciais(nome), SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(220, 235, 255));
        avatar.setForeground(AZUL_ESCURO);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 12));
        avatar.setPreferredSize(new Dimension(37, 37));
        avatar.setBorder(BorderFactory.createLineBorder(
                new Color(120, 170, 240), 2));

        JPanel dados = new JPanel();
        dados.setOpaque(false);
        dados.setLayout(new BoxLayout(dados, BoxLayout.Y_AXIS));

        JLabel nomeLabel = new JLabel(nome);
        nomeLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        nomeLabel.setForeground(TEXTO);

        JLabel status = new JLabel(online ? "● Online" : "● Offline");
        status.setFont(new Font("SansSerif", Font.PLAIN, 10));
        status.setForeground(
                online
                        ? new Color(36, 150, 73)
                        : new Color(130, 130, 130));

        dados.add(Box.createVerticalStrut(5));
        dados.add(nomeLabel);
        dados.add(Box.createVerticalStrut(3));
        dados.add(status);

        participante.add(avatar, BorderLayout.WEST);
        participante.add(dados, BorderLayout.CENTER);

        listaParticipantes.add(participante);
        listaParticipantes.add(Box.createVerticalStrut(3));

        quantidadeParticipantes.setText(
                "1 participante · " + (online ? "1 online" : "0 online"));

        listaParticipantes.revalidate();
        listaParticipantes.repaint();
    }

    private String iniciais(String nome) {
        String[] partes = nome.trim().split("\\s+");

        if (partes.length == 1) {
            return partes[0].substring(
                    0, Math.min(2, partes[0].length())).toUpperCase();
        }

        return (partes[0].substring(0, 1)
                + partes[partes.length - 1].substring(0, 1))
                .toUpperCase();
    }

    private void enviarMensagem() {
        if (!conectado || conexao == null) {
            return;
        }

        String conteudo = campoMensagem.getText().trim();

        if (conteudo.isEmpty()) {
            return;
        }

        try {
            conexao.enviarMensagem(conteudo);
            campoMensagem.setText("");
            campoMensagem.requestFocusInWindow();

            if (conteudo.equalsIgnoreCase("/sair")) {
                desconectar();
                dispose();
            }

        } catch (IOException e) {
            adicionarAviso(
                    "Erro ao enviar mensagem: " + e.getMessage());
            desconectar();
        }
    }

    private void desconectar() {
        conectado = false;

        if (conexao != null) {
            try {
                conexao.close();
            } catch (IOException e) {
                System.err.println(
                        "Erro ao fechar conexão: " + e.getMessage());
            }

            conexao = null;
        }

        if (campoMensagem != null) {
            campoMensagem.setEnabled(false);
            botaoEnviar.setEnabled(false);
        }
    }
}
