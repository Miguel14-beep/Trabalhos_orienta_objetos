package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entidade.Jogador;
import tile.TileManager;

public class Painel extends JPanel implements Runnable {

    final int tamanhoOriginal = 16;
    final int escala = 3;

    public final int tileTamanho = tamanhoOriginal * escala;
    public final int maxColunaTela = 16;
    public final int maxLacunaTela = 12;

    public final int larguraTela = tileTamanho * maxColunaTela;
    public final int comprimentoTela = tileTamanho * maxLacunaTela;

    int fps = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    public ColisaoChecker cChecker = new ColisaoChecker(this);

    Jogador jogador = new Jogador(this, keyH);

    TelaBatalha telaBatalha;

    public boolean emBatalha = false;
    int xAntesDaBatalha;
    int yAntesDaBatalha;

    public Painel() {

        this.setPreferredSize(
            new Dimension(larguraTela, comprimentoTela)
        );

        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);

        telaBatalha = new TelaBatalha(this);
        this.addMouseListener(telaBatalha);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / fps;
        long timer = 0;
        int drawCount = 0;

        long tempoAtual;
        long ultimaVez = System.nanoTime();

        double delta = 0;

        while (gameThread != null) {

            tempoAtual = System.nanoTime();

            delta += (tempoAtual - ultimaVez) / drawInterval;
            timer += (tempoAtual - ultimaVez);

            ultimaVez = tempoAtual;

            if (delta >= 1) {

                update();
                repaint();

                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {

                System.out.println("fps: " + drawCount);

                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {

        if (!emBatalha) {
            jogador.update();
        }
    }

    public void iniciarBatalha() {

        xAntesDaBatalha = jogador.x;
        yAntesDaBatalha = jogador.y;

        emBatalha = true;
        telaBatalha.iniciarBatalha();

        System.out.println("BATALHA INICIADA!");
        repaint();
    }
    
    public void fugirBatalha() {

    	jogador.voltarUmTile();
    	
        emBatalha = false;

        repaint();

        System.out.println("BATALHA ENCERRADA!");
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (emBatalha) {

            telaBatalha.draw(g2);

        } else {

            tileM.draw(g2);
            jogador.draw(g2);
        }

        g2.dispose();
    }
}