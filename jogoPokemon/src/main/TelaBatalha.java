package main;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import entidade.Agua;
import entidade.Birdmon;
import entidade.Fogo;
import entidade.Habilidade;
import entidade.Madeira;

public class TelaBatalha implements MouseListener {

    Painel gp;

    Birdmon birdmonJogador;
    Birdmon birdmonInimigo;

    // Equipe com no máximo 6 Birdmons
    ArrayList<Birdmon> equipeJogador = new ArrayList<>();

    LogicaBatalha logicaBatalha;

    boolean menuLuta = false;
    boolean menuPokemon = false;
    boolean menuMochila = false;

    private boolean capturaRealizada = false;

    int scrollMochila = 0;

    ArrayList<Item> inventario = new ArrayList<>();

    IACombate iaCombate;

    String mensagemBatalha = "O que você vai fazer?";

    // =========================
    // BOTÕES
    // =========================

    int botaoX = 450;
    int botaoY = 450;
    int botaoLargura = 120;
    int botaoAltura = 40;
    int espacamento = 10;

    public TelaBatalha(Painel gp) {
        this.gp = gp;
    }

    // =========================
    // TURNO DO INIMIGO
    // =========================

    private void executarTurnoInimigo() {

        if (logicaBatalha == null) {
            return;
        }

        if (capturaRealizada) {
            return;
        }

        if (birdmonInimigo == null
                || birdmonInimigo.estaDerrotado()) {
            return;
        }

        if (logicaBatalha.getAtacanteAtual()
                != birdmonInimigo) {
            return;
        }

        mensagemBatalha = iaCombate.executarTurno();

        verificarFimBatalha();

        gp.repaint();
    }

    // =========================
    // CRIAR BIRDMON ALEATÓRIO
    // =========================

    private Birdmon criarBirdmonAleatorio() {

        int sorteio = (int) (Math.random() * 3);

        switch (sorteio) {

            case 0:

                return new Agua(
                        "Aqua",
                        1,
                        100,
                        30,
                        25,
                        10,
                        10
                );

            case 1:

                return new Fogo(
                        "Brasa",
                        1,
                        100,
                        30,
                        25,
                        10,
                        10
                );

            default:

                return new Madeira(
                        "Folha",
                        1,
                        100,
                        30,
                        25,
                        10,
                        10
                );
        }
    }

    // =========================
    // INICIAR BATALHA
    // =========================

    public void iniciarBatalha() {

        capturaRealizada = false;

        // =====================================================
        // CRIA A EQUIPE APENAS NA PRIMEIRA BATALHA
        // =====================================================

        if (equipeJogador.isEmpty()) {

            Birdmon brasa = new Fogo(
                    "Brasa",
                    5,
                    200,
                    30,
                    25,
                    12,
                    15
            );

            Birdmon fagulha = new Fogo(
                    "Fagulha",
                    4,
                    25,
                    20,
                    8,
                    5,
                    18
            );

            equipeJogador.add(brasa);
            equipeJogador.add(fagulha);
        }

        // =====================================================
        // ESCOLHE O BIRDMON ATUAL
        // =====================================================

        if (birdmonJogador == null
                || birdmonJogador.estaDerrotado()) {

            birdmonJogador = encontrarPrimeiroBirdmonDisponivel();
        }

        // =====================================================
        // CRIA O INIMIGO
        // =====================================================

        birdmonInimigo = criarBirdmonAleatorio();
        gp.pokedex.registrarVisto(birdmonInimigo);

        // =====================================================
        // LÓGICA DA BATALHA
        // =====================================================

        logicaBatalha = new LogicaBatalha(
                birdmonJogador,
                birdmonInimigo
        );

        // =====================================================
        // IA
        // =====================================================

        iaCombate = new IACombate(logicaBatalha);

        // =====================================================
        // INVENTÁRIO
        // =====================================================

        // Só cria o inventário na primeira vez.
        // Assim os itens também não são restaurados
        // toda vez que uma nova batalha começa.

        if (inventario.isEmpty()) {

            inventario.add(
                    new Item(
                            "Poção Pequena",
                            5,
                            TipoItem.CURA,
                            20,
                            0
                    )
            );

            inventario.add(
                    new Item(
                            "Suco de Força",
                            2,
                            TipoItem.BUFF_ATAQUE,
                            10,
                            3
                    )
            );

            inventario.add(
                    new Item(
                            "Escudo de Penas",
                            2,
                            TipoItem.BUFF_DEFESA,
                            15,
                            3
                    )
            );

            inventario.add(
                    new Item(
                            "BirdBola Padrão",
                            3,
                            TipoItem.CAPTURA,
                            40,
                            0
                    )
            );

            inventario.add(
                    new Item(
                            "Poção Grande",
                            1,
                            TipoItem.CURA,
                            50,
                            0
                    )
            );

            inventario.add(
                    new Item(
                            "Elixir",
                            2,
                            TipoItem.CURA,
                            100,
                            0
                    )
            );
        }

        // =====================================================
        // ESTADO INICIAL
        // =====================================================

        scrollMochila = 0;

        menuLuta = false;
        menuPokemon = false;
        menuMochila = false;

        mensagemBatalha = "O que você vai fazer?";

        gp.repaint();
    }

    // =========================
    // ENCONTRAR BIRDMON DISPONÍVEL
    // =========================

    private Birdmon encontrarPrimeiroBirdmonDisponivel() {

        for (Birdmon birdmon : equipeJogador) {

            if (birdmon != null
                    && !birdmon.estaDerrotado()) {

                return birdmon;
            }
        }

        return null;
    }

    // =========================
    // DESENHAR BATALHA
    // =========================

    // =========================
    // AUXILIARES VISUAIS
    // =========================

    private Color corPorTipo(String tipo) {

        if (tipo == null) {
            return Color.GRAY;
        }

        switch (tipo) {
            case "Fogo":
                return new Color(255, 99, 71);
            case "Agua":
                return new Color(65, 149, 235);
            case "Planta":
                return new Color(102, 187, 106);
            default:
                return Color.GRAY;
        }
    }

    private void desenharPainel(
            Graphics2D g2,
            int x, int y,
            int largura, int altura) {

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fill(new RoundRectangle2D.Float(
                x + 4, y + 4, largura, altura, 20, 20));

        GradientPaint gradiente = new GradientPaint(
                x, y, Color.WHITE,
                x, y + altura, new Color(230, 230, 230)
        );

        g2.setPaint(gradiente);
        g2.fill(new RoundRectangle2D.Float(
                x, y, largura, altura, 20, 20));
        g2.setPaint(null);

        g2.setColor(new Color(90, 90, 90));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new RoundRectangle2D.Float(
                x, y, largura, altura, 20, 20));
    }

    private void desenharBarra(
            Graphics2D g2,
            int x, int y,
            int largura, int altura,
            int atual, int maximo,
            boolean isVida) {

        g2.setColor(new Color(210, 210, 210));
        g2.fill(new RoundRectangle2D.Float(
                x, y, largura, altura, altura, altura));

        int larguraPreenchida =
                calcularBarra(atual, maximo, largura);

        Color corBarra;

        if (isVida) {

            double proporcao =
                    maximo <= 0 ? 0 : (double) atual / maximo;

            if (proporcao > 0.5) {
                corBarra = new Color(76, 187, 23);
            } else if (proporcao > 0.2) {
                corBarra = new Color(240, 180, 20);
            } else {
                corBarra = new Color(220, 60, 60);
            }

        } else {
            corBarra = new Color(60, 140, 230);
        }

        if (larguraPreenchida > 0) {

            GradientPaint gradienteBarra = new GradientPaint(
                    x, y, corBarra.brighter(),
                    x, y + altura, corBarra
            );

            g2.setPaint(gradienteBarra);
            g2.fill(new RoundRectangle2D.Float(
                    x, y, larguraPreenchida, altura, altura, altura));
            g2.setPaint(null);
        }

        g2.setColor(corBarra.darker());
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(
                x, y, largura, altura, altura, altura));
    }

    private void desenharCriatura(
            Graphics2D g2,
            int x, int y,
            int diametro,
            String tipo) {

        Color corBase = corPorTipo(tipo);
        Color corClara = corBase.brighter();

        RadialGradientPaint gradiente = new RadialGradientPaint(
                x + diametro / 3f, y + diametro / 3f, diametro,
                new float[] {0f, 1f},
                new Color[] {corClara, corBase}
        );

        g2.setPaint(gradiente);
        g2.fillOval(x, y, diametro, diametro);
        g2.setPaint(null);

        g2.setColor(corBase.darker());
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(x, y, diametro, diametro);

        g2.setColor(new Color(255, 255, 255, 120));
        g2.fillOval(
                x + diametro / 5,
                y + diametro / 6,
                diametro / 4,
                diametro / 5
        );
    }

    private void desenharBotao(
            Graphics2D g2,
            int x, int y,
            int largura, int altura,
            String texto,
            Color corBase,
            int tamanhoFonte) {

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fill(new RoundRectangle2D.Float(
                x + 3, y + 3, largura, altura, 16, 16));

        GradientPaint gradiente = new GradientPaint(
                x, y, corBase.brighter(),
                x, y + altura, corBase
        );

        g2.setPaint(gradiente);
        g2.fill(new RoundRectangle2D.Float(
                x, y, largura, altura, 16, 16));
        g2.setPaint(null);

        g2.setColor(corBase.darker());
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new RoundRectangle2D.Float(
                x, y, largura, altura, 16, 16));

        g2.setFont(new Font("SansSerif", Font.BOLD, tamanhoFonte));
        FontMetrics fm = g2.getFontMetrics();

        int textoX = x + (largura - fm.stringWidth(texto)) / 2;
        int textoY = y + (altura + fm.getAscent()) / 2 - 3;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(texto, textoX + 1, textoY + 1);

        g2.setColor(Color.WHITE);
        g2.drawString(texto, textoX, textoY);
    }

    private void desenharBotao(
            Graphics2D g2,
            int x, int y,
            int largura, int altura,
            String texto,
            Color corBase) {

        desenharBotao(g2, x, y, largura, altura, texto, corBase, 14);
    }

    private String obterNomeHabilidade(int indice) {

        if (birdmonJogador.habilidades != null
                && birdmonJogador.habilidades.length > indice
                && birdmonJogador.habilidades[indice] != null) {

            return birdmonJogador.habilidades[indice].nome;
        }

        return "---";
    }
    
    public void draw(Graphics2D g2) {

    	g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        if (birdmonJogador == null
                || birdmonInimigo == null) {
            return;
        }

        desenharFundo(g2);

        desenharCampos(g2);

        desenharBirdmons(g2);

        desenharInformacoesInimigo(g2);

        desenharInformacoesJogador(g2);

        desenharMensagem(g2);

        if (menuLuta) {

            desenharMenuLuta(g2);

        } else if (menuPokemon) {

            desenharMenuPokemon(g2);

        } else if (menuMochila) {

            desenharMenuMochila(g2);

        } else {

            desenharBotoes(g2);
        }
    }

    // =========================
    // MENU POKEMON
    // =========================

    private void desenharMenuPokemon(Graphics2D g2) {

        desenharPainel(g2, 50, 50, 670, 350);

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("EQUIPE", 80, 85);

        int x = 80;
        int y = 100;
        int largura = 280;
        int altura = 60;
        int espacamentoLocal = 10;

        for (int i = 0; i < 6; i++) {

            int posX;
            int posY;

            if (i < 3) {
                posX = x;
                posY = y + i * (altura + espacamentoLocal);
            } else {
                posX = x + 320;
                posY = y + (i - 3) * (altura + espacamentoLocal);
            }

            boolean vazio = i >= equipeJogador.size();

            Color corSlot = vazio
                    ? new Color(225, 225, 225)
                    : new Color(255, 250, 235);

            GradientPaint gradiente = new GradientPaint(
                    posX, posY, corSlot.brighter(),
                    posX, posY + altura, corSlot
            );

            g2.setPaint(gradiente);
            g2.fill(new RoundRectangle2D.Float(
                    posX, posY, largura, altura, 14, 14));
            g2.setPaint(null);

            g2.setColor(new Color(150, 150, 150));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(
                    posX, posY, largura, altura, 14, 14));

            if (!vazio) {

                Birdmon birdmon = equipeJogador.get(i);

                g2.setColor(new Color(40, 40, 40));
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString(
                        birdmon.getNome() + "  Lv." + birdmon.getNivel(),
                        posX + 12, posY + 22
                );

                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.drawString("HP", posX + 12, posY + 38);

                desenharBarra(
                        g2, posX + 35, posY + 30, largura - 50, 10,
                        birdmon.getVida(), birdmon.getVidaMaxima(), true
                );

                if (birdmon == birdmonJogador) {
                    g2.setColor(new Color(70, 140, 220));
                    g2.setFont(new Font("SansSerif", Font.ITALIC, 11));
                    g2.drawString("Em batalha", posX + 12, posY + 55);
                } else if (birdmon.estaDerrotado()) {
                    g2.setColor(new Color(200, 60, 60));
                    g2.setFont(new Font("SansSerif", Font.ITALIC, 11));
                    g2.drawString("Desmaiado", posX + 12, posY + 55);
                }

            } else {

                g2.setColor(new Color(160, 160, 160));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
                g2.drawString("--- vazio ---", posX + 12, posY + 34);
            }
        }

        desenharBotao(g2, 580, 360, 100, 40, "VOLTAR", new Color(120, 120, 120));
    }

    // =========================
    // MENU MOCHILA
    // =========================

    private void desenharMenuMochila(Graphics2D g2) {

        desenharPainel(g2, 50, 50, 670, 350);

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("MOCHILA", 80, 85);

        int x = 80;
        int y = 100;
        int largura = 400;
        int altura = 40;
        int espacamentoLocal = 10;

        int maxItensNaTela = 5;

        for (int i = 0; i < maxItensNaTela; i++) {

            int index = scrollMochila + i;

            if (index < inventario.size()) {

                Item item = inventario.get(index);
                int posY = y + i * (altura + espacamentoLocal);

                GradientPaint gradiente = new GradientPaint(
                        x, posY, new Color(255, 250, 230),
                        x, posY + altura, new Color(240, 225, 190)
                );

                g2.setPaint(gradiente);
                g2.fill(new RoundRectangle2D.Float(
                        x, posY, largura, altura, 12, 12));
                g2.setPaint(null);

                g2.setColor(new Color(170, 140, 90));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(
                        x, posY, largura, altura, 12, 12));

                g2.setColor(new Color(50, 40, 20));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g2.drawString(
                        item.getNome() + " (x" + item.getQuantidade() + ")",
                        x + 14, posY + 25
                );
            }
        }

        desenharBotao(g2, 500, 100, 80, 40, "CIMA", new Color(120, 120, 120), 12);
        desenharBotao(g2, 500, 310, 80, 40, "BAIXO", new Color(120, 120, 120), 12);
        desenharBotao(g2, 580, 360, 100, 40, "VOLTAR", new Color(120, 120, 120));
    }

    // =========================
    // FUNDO
    // =========================

    private void desenharFundo(Graphics2D g2) {

        GradientPaint ceu = new GradientPaint(
                0, 0, new Color(135, 206, 250),
                0, gp.comprimentoTela, new Color(152, 251, 152)
        );

        g2.setPaint(ceu);
        g2.fillRect(0, 0, gp.larguraTela, gp.comprimentoTela);
        g2.setPaint(null);
    }

    // =========================
    // CAMPOS
    // =========================

    private void desenharCampos(Graphics2D g2) {

        desenharSombraCampo(g2, 400, 100, 250, 100);
        desenharSombraCampo(g2, 100, 300, 250, 100);
    }

    private void desenharSombraCampo(
            Graphics2D g2,
            int x, int y,
            int largura, int altura) {

        RadialGradientPaint gradiente = new RadialGradientPaint(
                x + largura / 2f, y + altura / 2f, largura / 2f,
                new float[] {0f, 1f},
                new Color[] {
                        new Color(255, 255, 255, 190),
                        new Color(255, 255, 255, 70)
                }
        );

        g2.setPaint(gradiente);
        g2.fillOval(x, y, largura, altura);
        g2.setPaint(null);

        g2.setColor(new Color(0, 100, 0, 100));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(x, y, largura, altura);
    }

    // =========================
    // BIRDMONS
    // =========================

    private void desenharBirdmons(Graphics2D g2) {

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillOval(485, 155, 70, 18);

        desenharCriatura(
                g2, 480, 80, 80,
                birdmonInimigo != null ? birdmonInimigo.getTipo() : null
        );

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillOval(185, 355, 70, 18);

        desenharCriatura(
                g2, 180, 280, 80,
                birdmonJogador != null ? birdmonJogador.getTipo() : null
        );
    }

    // =========================
    // INFORMAÇÕES DO INIMIGO
    // =========================

    private void desenharInformacoesInimigo(Graphics2D g2) {

        int x = 50;
        int y = 50;
        int largura = 250;
        int altura = 80;

        desenharPainel(g2, x, y, largura, altura);

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString(
                birdmonInimigo.getNome() + "  Lv." + birdmonInimigo.getNivel(),
                x + 12, y + 22
        );

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(new Color(90, 90, 90));
        g2.drawString("HP", x + 12, y + 40);

        desenharBarra(
                g2, x + 35, y + 32, 180, 10,
                birdmonInimigo.getVida(), birdmonInimigo.getVidaMaxima(), true
        );

        g2.setColor(new Color(90, 90, 90));
        g2.drawString("PH", x + 12, y + 62);

        desenharBarra(
                g2, x + 35, y + 54, 180, 10,
                birdmonInimigo.getPh(), birdmonInimigo.getPhMaximo(), false
        );
    }

    // =========================
    // INFORMAÇÕES DO JOGADOR
    // =========================

    private void desenharInformacoesJogador(Graphics2D g2) {

        int x = 400;
        int y = 300;
        int largura = 250;
        int altura = 80;

        desenharPainel(g2, x, y, largura, altura);

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString(
                birdmonJogador.getNome() + "  Lv." + birdmonJogador.getNivel(),
                x + 12, y + 22
        );

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(new Color(90, 90, 90));
        g2.drawString("HP", x + 12, y + 40);

        desenharBarra(
                g2, x + 35, y + 32, 180, 10,
                birdmonJogador.getVida(), birdmonJogador.getVidaMaxima(), true
        );

        g2.setColor(new Color(90, 90, 90));
        g2.drawString("PH", x + 12, y + 62);

        desenharBarra(
                g2, x + 35, y + 54, 180, 10,
                birdmonJogador.getPh(), birdmonJogador.getPhMaximo(), false
        );
    }

    // =========================
    // MENSAGEM
    // =========================

    private void desenharMensagem(Graphics2D g2) {

        int x = 50;
        int y = 420;
        int largura = 360;
        int altura = 100;

        desenharPainel(g2, x, y, largura, altura);

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.drawString(mensagemBatalha, x + 18, y + 35);
    }

    // =========================
    // BOTÕES PRINCIPAIS
    // =========================

    private void desenharBotoes(Graphics2D g2) {

        desenharBotao(
                g2, botaoX, botaoY, botaoLargura, botaoAltura,
                "LUTAR", new Color(220, 90, 70)
        );

        desenharBotao(
                g2, botaoX, botaoY + botaoAltura + espacamento,
                botaoLargura, botaoAltura,
                "POKEMON", new Color(70, 140, 220)
        );

        desenharBotao(
                g2, botaoX + botaoLargura + espacamento, botaoY,
                botaoLargura, botaoAltura,
                "MOCHILA", new Color(200, 160, 60)
        );

        desenharBotao(
                g2, botaoX + botaoLargura + espacamento,
                botaoY + botaoAltura + espacamento,
                botaoLargura, botaoAltura,
                "FUGIR", new Color(120, 120, 120)
        );
    }

    // =========================
    // MENU DE LUTA
    // =========================

    private void desenharMenuLuta(Graphics2D g2) {

        desenharBotao(
                g2, botaoX, botaoY, botaoLargura, botaoAltura,
                "ATAQUE", new Color(220, 90, 70)
        );

        desenharBotao(
                g2, botaoX, botaoY + botaoAltura + espacamento,
                botaoLargura, botaoAltura,
                obterNomeHabilidade(0), new Color(150, 90, 200), 12
        );

        desenharBotao(
                g2, botaoX + botaoLargura + espacamento, botaoY,
                botaoLargura, botaoAltura,
                obterNomeHabilidade(1), new Color(150, 90, 200), 12
        );

        desenharBotao(
                g2, botaoX + botaoLargura + espacamento,
                botaoY + botaoAltura + espacamento,
                botaoLargura, botaoAltura,
                obterNomeHabilidade(2), new Color(150, 90, 200), 12
        );

        desenharBotao(
                g2, botaoX - 100, botaoY, 80, botaoAltura,
                "VOLTAR", new Color(120, 120, 120)
        );
    }

    // =========================
    // BARRAS
    // =========================

    private int calcularBarra(
            int atual,
            int maximo,
            int largura) {

        if (maximo <= 0) {
            return 0;
        }

        if (atual < 0) {
            atual = 0;
        }

        if (atual > maximo) {
            atual = maximo;
        }

        return (int) (
                (double) atual / maximo * largura
        );
    }

    // =========================
    // HABILIDADE
    // =========================

    private void usarHabilidade(int indice) {

        if (capturaRealizada) {
            return;
        }

        if (logicaBatalha.getAtacanteAtual()
                != birdmonJogador) {

            mensagemBatalha =
                    "Não é o seu turno!";

            gp.repaint();

            return;
        }

        if (birdmonJogador.habilidades == null
                || indice < 0
                || indice >= birdmonJogador.habilidades.length
                || birdmonJogador.habilidades[indice] == null) {

            mensagemBatalha =
                    "Habilidade inválida!";

            gp.repaint();

            return;
        }

        Habilidade habilidade =
                birdmonJogador.habilidades[indice];

        boolean usou =
                logicaBatalha.usarHabilidade(
                        birdmonJogador,
                        habilidade
                );

        if (usou) {

            mensagemBatalha =
                    birdmonJogador.getNome()
                            + " usou "
                            + habilidade.nome
                            + "!";

            logicaBatalha.passarTurno();

            menuLuta = false;

            gp.repaint();

            executarTurnoInimigo();

        } else {

            mensagemBatalha =
                    birdmonJogador.getNome()
                            + " não possui PH suficiente!";

            gp.repaint();
        }
    }

    // =========================
    // ATAQUE DO JOGADOR
    // =========================

    private void executarAtaqueJogador() {

        if (capturaRealizada) {
            return;
        }

        if (logicaBatalha.getAtacanteAtual()
                != birdmonJogador) {

            mensagemBatalha =
                    "Não é o seu turno!";

            gp.repaint();

            return;
        }

        boolean acertou =
                logicaBatalha.executarAtaque();

        if (acertou) {

            mensagemBatalha =
                    birdmonJogador.getNome()
                            + " atacou com sucesso!";

        } else {

            mensagemBatalha =
                    birdmonJogador.getNome()
                            + " errou o ataque!";
        }

        menuLuta = false;

        verificarFimBatalha();

        gp.repaint();

        if (!batalhaTerminou()) {

            executarTurnoInimigo();
        }
    }

    // =========================
    // USAR ITEM
    // =========================

    private void usarItem(int index) {

        if (logicaBatalha == null) {
            return;
        }

        if (capturaRealizada) {
            return;
        }

        if (logicaBatalha.getAtacanteAtual()
                != birdmonJogador) {

            mensagemBatalha =
                    "Não é o seu turno!";

            gp.repaint();

            return;
        }

        if (index < 0
                || index >= inventario.size()) {

            return;
        }

        Item item = inventario.get(index);

        if (item.getQuantidade() <= 0) {

            mensagemBatalha =
                    "Você não possui esse item.";

            gp.repaint();

            return;
        }

        // =====================================================
        // CAPTURA
        // =====================================================

        if (item.getTipo() == TipoItem.CAPTURA) {

            mensagemBatalha =
                    logicaBatalha.tentarCapturar(item);

            if (item.getQuantidade() <= 0) {
                inventario.remove(index);
            }

            menuMochila = false;

            // =================================================
            // CAPTURA BEM-SUCEDIDA
            // =================================================

            if (logicaBatalha.foiUltimaCapturaBemSucedida()) {

                Birdmon capturado = logicaBatalha.getAdversario();
                gp.pokedex.registrarCapturado(capturado);

                if (equipeJogador.size() < 6) {

                    equipeJogador.add(capturado);

                    mensagemBatalha =
                            capturado.getNome()
                                    + " entrou para sua equipe!";

                } else {

                    mensagemBatalha =
                            "Sua equipe está cheia! "
                                    + "O Birdmon foi capturado, "
                                    + "mas não pode entrar na equipe.";
                }

                capturaRealizada = true;

                menuLuta = false;
                menuPokemon = false;
                menuMochila = false;

                gp.fugirBatalha();

                return;
            }

            // =================================================
            // CAPTURA FALHOU
            // =================================================

            if (logicaBatalha.getAtacanteAtual()
                    == birdmonInimigo) {

                executarTurnoInimigo();
            }

            gp.repaint();

            return;
        }

        // =====================================================
        // OUTROS ITENS
        // =====================================================

        mensagemBatalha =
                logicaBatalha.usarItem(item);

        if (item.getQuantidade() <= 0) {
            inventario.remove(index);
        }

        menuMochila = false;

        if (!batalhaTerminou()) {
            executarTurnoInimigo();
        }

        gp.repaint();
    }

    // =========================
    // VERIFICAR FIM DA BATALHA
    // =========================

    private boolean batalhaTerminou() {

        if (capturaRealizada) {
            return true;
        }

        if (birdmonInimigo == null) {
            return true;
        }

        if (birdmonInimigo.estaDerrotado()) {
            return true;
        }

        if (birdmonJogador == null) {
            return true;
        }

        return birdmonJogador.estaDerrotado();
    }

    // =========================
    // VERIFICAR FIM DA BATALHA
    // =========================

    private void verificarFimBatalha() {

        if (birdmonInimigo != null
                && birdmonInimigo.estaDerrotado()) {

            mensagemBatalha =
                    birdmonInimigo.getNome()
                            + " foi derrotado!";

            menuLuta = false;
            menuPokemon = false;
            menuMochila = false;

            return;
        }

        if (birdmonJogador != null
                && birdmonJogador.estaDerrotado()) {

            mensagemBatalha =
                    birdmonJogador.getNome()
                            + " foi derrotado!";

            menuLuta = false;
            menuPokemon = false;
            menuMochila = false;
        }
    }

    // =========================
    // CLIQUES
    // =========================

    @Override
    public void mousePressed(MouseEvent e) {

        if (capturaRealizada) {
            return;
        }

        int mouseX = e.getX();
        int mouseY = e.getY();

        // =====================================================
        // MENU DE LUTA
        // =====================================================

        if (menuLuta) {

            // VOLTAR

            if (
                    mouseX >= botaoX - 100
                    && mouseX <= botaoX - 20
                    && mouseY >= botaoY
                    && mouseY <= botaoY + botaoAltura
            ) {

                menuLuta = false;

                mensagemBatalha =
                        "O que você vai fazer?";

                gp.repaint();
            }

            // ATAQUE

            else if (
                    mouseX >= botaoX
                    && mouseX <= botaoX + botaoLargura
                    && mouseY >= botaoY
                    && mouseY <= botaoY + botaoAltura
            ) {

                executarAtaqueJogador();
            }

            // HABILIDADE 1

            else if (
                    mouseX >= botaoX
                    && mouseX <= botaoX + botaoLargura
                    && mouseY >= botaoY + botaoAltura + espacamento
                    && mouseY <= botaoY
                            + botaoAltura * 2
                            + espacamento
            ) {

                usarHabilidade(0);
            }

            // HABILIDADE 2

            else if (
                    mouseX >= botaoX + botaoLargura + espacamento
                    && mouseX <= botaoX
                            + botaoLargura * 2
                            + espacamento
                    && mouseY >= botaoY
                    && mouseY <= botaoY + botaoAltura
            ) {

                usarHabilidade(1);
            }

            // HABILIDADE 3

            else if (
                    mouseX >= botaoX + botaoLargura + espacamento
                    && mouseX <= botaoX
                            + botaoLargura * 2
                            + espacamento
                    && mouseY >= botaoY + botaoAltura + espacamento
                    && mouseY <= botaoY
                            + botaoAltura * 2
                            + espacamento
            ) {

                usarHabilidade(2);
            }

            return;
        }

        // =====================================================
        // MENU POKEMON
        // =====================================================

        if (menuPokemon) {

            // VOLTAR

            if (
                    mouseX >= 580
                    && mouseX <= 680
                    && mouseY >= 360
                    && mouseY <= 400
            ) {

                menuPokemon = false;

                mensagemBatalha =
                        "O que você vai fazer?";

                gp.repaint();

                return;
            }

            // =================================================
            // BIRDMONS
            // =================================================

            for (int i = 0; i < 6; i++) {

                int posX;
                int posY;

                if (i < 3) {

                    posX = 80;
                    posY = 100 + i * 70;

                } else {

                    posX = 400;
                    posY = 100 + (i - 3) * 70;
                }

                if (
                        mouseX >= posX
                        && mouseX <= posX + 280
                        && mouseY >= posY
                        && mouseY <= posY + 60
                ) {

                    // Espaço vazio

                    if (i >= equipeJogador.size()) {
                        return;
                    }

                    Birdmon escolhido =
                            equipeJogador.get(i);

                    // =================================================
                    // JÁ ESTÁ EM BATALHA
                    // =================================================

                    if (escolhido == birdmonJogador) {

                        mensagemBatalha =
                                escolhido.getNome()
                                        + " já está na batalha!";

                        gp.repaint();

                        return;
                    }

                    // =================================================
                    // DESMAIADO
                    // =================================================

                    if (escolhido.estaDerrotado()) {

                        mensagemBatalha =
                                escolhido.getNome()
                                        + " está desmaiado!";

                        gp.repaint();

                        return;
                    }

                    // =================================================
                    // TROCA
                    // =================================================

                    birdmonJogador = escolhido;

                    logicaBatalha.trocarBirdmon(
                            birdmonJogador
                    );

                    mensagemBatalha =
                            "Vai, "
                                    + birdmonJogador.getNome()
                                    + "!";

                    menuPokemon = false;

                    /*
                     * A troca já colocou o inimigo
                     * como próximo atacante.
                     *
                     * NÃO chamamos passarTurno() aqui.
                     */

                    executarTurnoInimigo();

                    gp.repaint();

                    return;
                }
            }

            return;
        }

        // =====================================================
        // MENU MOCHILA
        // =====================================================

        if (menuMochila) {

            // VOLTAR

            if (
                    mouseX >= 580
                    && mouseX <= 680
                    && mouseY >= 360
                    && mouseY <= 400
            ) {

                menuMochila = false;

                mensagemBatalha =
                        "O que você vai fazer?";

                gp.repaint();

                return;
            }

            // CIMA

            if (
                    mouseX >= 500
                    && mouseX <= 580
                    && mouseY >= 100
                    && mouseY <= 140
            ) {

                if (scrollMochila > 0) {

                    scrollMochila--;

                    gp.repaint();
                }

                return;
            }

            // BAIXO

            if (
                    mouseX >= 500
                    && mouseX <= 580
                    && mouseY >= 310
                    && mouseY <= 350
            ) {

                if (
                        scrollMochila + 5
                        < inventario.size()
                ) {

                    scrollMochila++;

                    gp.repaint();
                }

                return;
            }

            // CLIQUE NOS ITENS

            int x = 80;
            int y = 100;
            int altura = 40;
            int espacamento = 10;
            int largura = 400;

            for (int i = 0; i < 5; i++) {

                int index =
                        scrollMochila + i;

                if (index < inventario.size()) {

                    int posY =
                            y + i * (altura + espacamento);

                    if (
                            mouseX >= x
                            && mouseX <= x + largura
                            && mouseY >= posY
                            && mouseY <= posY + altura
                    ) {

                        usarItem(index);

                        return;
                    }
                }
            }

            return;
        }

        // =====================================================
        // MENU PRINCIPAL
        // =====================================================

        // LUTAR

        if (
                mouseX >= botaoX
                && mouseX <= botaoX + botaoLargura
                && mouseY >= botaoY
                && mouseY <= botaoY + botaoAltura
        ) {

            if (
                    logicaBatalha.getAtacanteAtual()
                            == birdmonJogador
            ) {

                menuLuta = true;

                mensagemBatalha =
                        "Escolha seu ataque.";

                gp.repaint();

            } else {

                mensagemBatalha =
                        "Aguarde o turno do inimigo.";

                gp.repaint();
            }
        }

        // POKEMON

        else if (
                mouseX >= botaoX
                && mouseX <= botaoX + botaoLargura
                && mouseY >= botaoY + botaoAltura + espacamento
                && mouseY <= botaoY
                        + botaoAltura * 2
                        + espacamento
        ) {

            if (
                    logicaBatalha.getAtacanteAtual()
                            == birdmonJogador
            ) {

                menuPokemon = true;

                mensagemBatalha =
                        "Escolha um Birdmon.";

                gp.repaint();

            } else {

                mensagemBatalha =
                        "Aguarde o turno do inimigo.";

                gp.repaint();
            }
        }

        // MOCHILA

        else if (
                mouseX >= botaoX + botaoLargura + espacamento
                && mouseX <= botaoX
                        + botaoLargura * 2
                        + espacamento
                && mouseY >= botaoY
                && mouseY <= botaoY + botaoAltura
        ) {

            if (
                    logicaBatalha.getAtacanteAtual()
                            == birdmonJogador
            ) {

                menuMochila = true;

                mensagemBatalha =
                        "Escolha um item.";

                gp.repaint();

            } else {

                mensagemBatalha =
                        "Aguarde o turno do inimigo.";

                gp.repaint();
            }
        }

        // FUGIR

        else if (
                mouseX >= botaoX + botaoLargura + espacamento
                && mouseX <= botaoX
                        + botaoLargura * 2
                        + espacamento
                && mouseY >= botaoY + botaoAltura + espacamento
                && mouseY <= botaoY
                        + botaoAltura * 2
                        + espacamento
        ) {

            gp.fugirBatalha();
        }
    }

    // =========================
    // OUTROS EVENTOS
    // =========================

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}