package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

    public void draw(Graphics2D g2) {

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

        g2.setColor(Color.WHITE);

        g2.fillRect(
                50,
                50,
                670,
                350
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "EQUIPE",
                80,
                80
        );

        int x = 80;
        int y = 100;
        int largura = 280;
        int altura = 60;
        int espacamento = 10;

        // Mostra sempre as 6 posições possíveis.
        for (int i = 0; i < 6; i++) {

            int posX;
            int posY;

            if (i < 3) {

                posX = x;
                posY = y + i * (altura + espacamento);

            } else {

                posX = x + 320;
                posY = y + (i - 3) * (altura + espacamento);
            }

            g2.setColor(Color.LIGHT_GRAY);

            g2.fillRect(
                    posX,
                    posY,
                    largura,
                    altura
            );

            g2.setColor(Color.BLACK);

            if (i < equipeJogador.size()) {

                Birdmon birdmon =
                        equipeJogador.get(i);

                g2.drawString(
                        birdmon.getNome()
                                + "  Lv."
                                + birdmon.getNivel(),
                        posX + 10,
                        posY + 20
                );

                g2.drawString(
                        "HP "
                                + birdmon.getVida()
                                + "/"
                                + birdmon.getVidaMaxima(),
                        posX + 10,
                        posY + 40
                );

            } else {

                g2.drawString(
                        "---",
                        posX + 10,
                        posY + 30
                );
            }
        }

        // BOTÃO VOLTAR

        g2.setColor(Color.WHITE);

        g2.fillRect(
                580,
                360,
                100,
                40
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "VOLTAR",
                600,
                385
        );
    }

    // =========================
    // MENU MOCHILA
    // =========================

    private void desenharMenuMochila(Graphics2D g2) {

        g2.setColor(Color.WHITE);

        g2.fillRect(
                50,
                50,
                670,
                350
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "MOCHILA",
                80,
                80
        );

        int x = 80;
        int y = 100;
        int largura = 400;
        int altura = 40;
        int espacamento = 10;

        int maxItensNaTela = 5;

        for (int i = 0; i < maxItensNaTela; i++) {

            int index = scrollMochila + i;

            if (index < inventario.size()) {

                Item item =
                        inventario.get(index);

                int posY =
                        y + i * (altura + espacamento);

                g2.setColor(Color.LIGHT_GRAY);

                g2.fillRect(
                        x,
                        posY,
                        largura,
                        altura
                );

                g2.setColor(Color.BLACK);

                g2.drawString(
                        item.getNome()
                                + " (x"
                                + item.getQuantidade()
                                + ")",
                        x + 10,
                        posY + 25
                );
            }
        }

        // CIMA

        g2.setColor(Color.LIGHT_GRAY);

        g2.fillRect(
                500,
                100,
                80,
                40
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "CIMA",
                520,
                125
        );

        // BAIXO

        g2.setColor(Color.LIGHT_GRAY);

        g2.fillRect(
                500,
                310,
                80,
                40
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "BAIXO",
                515,
                335
        );

        // VOLTAR

        g2.setColor(Color.WHITE);

        g2.fillRect(
                580,
                360,
                100,
                40
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "VOLTAR",
                600,
                385
        );
    }

    // =========================
    // FUNDO
    // =========================

    private void desenharFundo(Graphics2D g2) {

        g2.setColor(Color.GREEN);

        g2.fillRect(
                0,
                0,
                gp.larguraTela,
                gp.comprimentoTela
        );
    }

    // =========================
    // CAMPOS
    // =========================

    private void desenharCampos(Graphics2D g2) {

        g2.setColor(Color.WHITE);

        g2.fillOval(
                400,
                100,
                250,
                100
        );

        g2.fillOval(
                100,
                300,
                250,
                100
        );
    }

    // =========================
    // BIRDMONS
    // =========================

    private void desenharBirdmons(Graphics2D g2) {

        // Inimigo

        g2.setColor(Color.RED);

        g2.fillOval(
                480,
                80,
                80,
                80
        );

        // Jogador

        g2.setColor(Color.BLUE);

        g2.fillOval(
                180,
                280,
                80,
                80
        );
    }

    // =========================
    // INFORMAÇÕES DO INIMIGO
    // =========================

    private void desenharInformacoesInimigo(
            Graphics2D g2) {

        int x = 50;
        int y = 50;
        int largura = 250;
        int altura = 80;

        g2.setColor(Color.WHITE);

        g2.fillRect(
                x,
                y,
                largura,
                altura
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                birdmonInimigo.getNome()
                        + "  Lv."
                        + birdmonInimigo.getNivel(),
                x + 10,
                y + 20
        );

        int larguraHP =
                calcularBarra(
                        birdmonInimigo.getVida(),
                        birdmonInimigo.getVidaMaxima(),
                        180
                );

        g2.setColor(Color.RED);

        g2.fillRect(
                x + 10,
                y + 35,
                larguraHP,
                10
        );

        int larguraPH =
                calcularBarra(
                        birdmonInimigo.getPh(),
                        birdmonInimigo.getPhMaximo(),
                        180
                );

        g2.setColor(Color.BLUE);

        g2.fillRect(
                x + 10,
                y + 55,
                larguraPH,
                10
        );
    }

    // =========================
    // INFORMAÇÕES DO JOGADOR
    // =========================

    private void desenharInformacoesJogador(
            Graphics2D g2) {

        int x = 400;
        int y = 300;
        int largura = 250;
        int altura = 80;

        g2.setColor(Color.WHITE);

        g2.fillRect(
                x,
                y,
                largura,
                altura
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                birdmonJogador.getNome()
                        + "  Lv."
                        + birdmonJogador.getNivel(),
                x + 10,
                y + 20
        );

        int larguraHP =
                calcularBarra(
                        birdmonJogador.getVida(),
                        birdmonJogador.getVidaMaxima(),
                        180
                );

        g2.setColor(Color.RED);

        g2.fillRect(
                x + 10,
                y + 35,
                larguraHP,
                10
        );

        int larguraPH =
                calcularBarra(
                        birdmonJogador.getPh(),
                        birdmonJogador.getPhMaximo(),
                        180
                );

        g2.setColor(Color.BLUE);

        g2.fillRect(
                x + 10,
                y + 55,
                larguraPH,
                10
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

        g2.setColor(Color.WHITE);

        g2.fillRect(
                x,
                y,
                largura,
                altura
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                mensagemBatalha,
                x + 15,
                y + 30
        );
    }

    // =========================
    // BOTÕES PRINCIPAIS
    // =========================

    private void desenharBotoes(Graphics2D g2) {

        g2.setColor(Color.WHITE);

        // LUTAR

        g2.fillRect(
                botaoX,
                botaoY,
                botaoLargura,
                botaoAltura
        );

        // POKEMON

        g2.fillRect(
                botaoX,
                botaoY + botaoAltura + espacamento,
                botaoLargura,
                botaoAltura
        );

        // MOCHILA

        g2.fillRect(
                botaoX + botaoLargura + espacamento,
                botaoY,
                botaoLargura,
                botaoAltura
        );

        // FUGIR

        g2.fillRect(
                botaoX + botaoLargura + espacamento,
                botaoY + botaoAltura + espacamento,
                botaoLargura,
                botaoAltura
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "LUTAR",
                botaoX + 35,
                botaoY + 25
        );

        g2.drawString(
                "POKEMON",
                botaoX + 25,
                botaoY
                        + botaoAltura
                        + espacamento
                        + 25
        );

        g2.drawString(
                "MOCHILA",
                botaoX
                        + botaoLargura
                        + espacamento
                        + 25,
                botaoY + 25
        );

        g2.drawString(
                "FUGIR",
                botaoX
                        + botaoLargura
                        + espacamento
                        + 35,
                botaoY
                        + botaoAltura
                        + espacamento
                        + 25
        );
    }

    // =========================
    // MENU DE LUTA
    // =========================

    private void desenharMenuLuta(Graphics2D g2) {

        g2.setColor(Color.WHITE);

        // ATAQUE

        g2.fillRect(
                botaoX,
                botaoY,
                botaoLargura,
                botaoAltura
        );

        // HABILIDADE 1

        g2.fillRect(
                botaoX,
                botaoY + botaoAltura + espacamento,
                botaoLargura,
                botaoAltura
        );

        // HABILIDADE 2

        g2.fillRect(
                botaoX + botaoLargura + espacamento,
                botaoY,
                botaoLargura,
                botaoAltura
        );

        // HABILIDADE 3

        g2.fillRect(
                botaoX + botaoLargura + espacamento,
                botaoY + botaoAltura + espacamento,
                botaoLargura,
                botaoAltura
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "ATAQUE",
                botaoX + 30,
                botaoY + 25
        );

        // HABILIDADE 1

        if (birdmonJogador.habilidades != null
                && birdmonJogador.habilidades.length > 0
                && birdmonJogador.habilidades[0] != null) {

            g2.drawString(
                    birdmonJogador.habilidades[0].nome,
                    botaoX + 15,
                    botaoY
                            + botaoAltura
                            + espacamento
                            + 25
            );
        }

        // HABILIDADE 2

        if (birdmonJogador.habilidades != null
                && birdmonJogador.habilidades.length > 1
                && birdmonJogador.habilidades[1] != null) {

            g2.drawString(
                    birdmonJogador.habilidades[1].nome,
                    botaoX
                            + botaoLargura
                            + espacamento
                            + 15,
                    botaoY + 25
            );
        }

        // HABILIDADE 3

        if (birdmonJogador.habilidades != null
                && birdmonJogador.habilidades.length > 2
                && birdmonJogador.habilidades[2] != null) {

            g2.drawString(
                    birdmonJogador.habilidades[2].nome,
                    botaoX
                            + botaoLargura
                            + espacamento
                            + 15,
                    botaoY
                            + botaoAltura
                            + espacamento
                            + 25
            );
        }

        // VOLTAR

        g2.setColor(Color.WHITE);

        g2.fillRect(
                botaoX - 100,
                botaoY,
                80,
                botaoAltura
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                "VOLTAR",
                botaoX - 85,
                botaoY + 25
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

            if (logicaBatalha
                    .foiUltimaCapturaBemSucedida()) {

                Birdmon capturado =
                        logicaBatalha.getAdversario();

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