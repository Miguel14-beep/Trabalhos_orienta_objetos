package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

public class TelaFim implements MouseListener {

    Painel gp;

    int botaoLargura = 220;
    int botaoAltura = 50;
    
    int botaoHistoricoAltura = 44;

    public TelaFim(Painel gp) {
        this.gp = gp;
    }

    private int botaoX() {
        return (gp.larguraTela - botaoLargura) / 2;
    }

    private int botaoY() {
        return gp.comprimentoTela - 120;
    }
    private int botaoHistoricoY() {
        return gp.comprimentoTela - 60;
    }

    // =========================
    // TELA DE GAME OVER
    // =========================

    public void drawGameOver(Graphics2D g2) {

        desenharFundo(g2, new Color(70, 15, 15), new Color(20, 0, 0));
        desenharTitulo(g2, "GAME OVER", new Color(230, 70, 70));
        desenharSubtitulo(g2, "Todos os seus Birdmons foram derrotados.");
        desenharBotaoReiniciar(g2, new Color(200, 70, 60));
        desenharBotaoHistorico(g2);
    }

    // =========================
    // TELA DE VITÓRIA
    // =========================

    public void drawVitoria(Graphics2D g2) {

        desenharFundo(g2, new Color(20, 65, 25), new Color(5, 25, 5));
        desenharTitulo(g2, "VOCÊ VENCEU!", new Color(255, 215, 0));
        desenharSubtitulo(g2, "Você capturou todos os Birdmons selvagens!");
        desenharBotaoReiniciar(g2, new Color(60, 160, 90));
        desenharBotaoHistorico(g2);
    }

    // =========================
    // ELEMENTOS VISUAIS
    // =========================

    private void desenharFundo(Graphics2D g2, Color topo, Color base) {

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        GradientPaint gradiente = new GradientPaint(
                0, 0, topo,
                0, gp.comprimentoTela, base
        );

        g2.setPaint(gradiente);
        g2.fillRect(0, 0, gp.larguraTela, gp.comprimentoTela);
        g2.setPaint(null);
    }

    private void desenharTitulo(Graphics2D g2, String texto, Color cor) {

        g2.setFont(new Font("SansSerif", Font.BOLD, 44));
        FontMetrics fm = g2.getFontMetrics();

        int x = (gp.larguraTela - fm.stringWidth(texto)) / 2;
        int y = gp.comprimentoTela / 2 - 40;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawString(texto, x + 3, y + 3);

        g2.setColor(cor);
        g2.drawString(texto, x, y);
    }

    private void desenharSubtitulo(Graphics2D g2, String texto) {

        g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
        FontMetrics fm = g2.getFontMetrics();

        int x = (gp.larguraTela - fm.stringWidth(texto)) / 2;
        int y = gp.comprimentoTela / 2;

        g2.setColor(Color.WHITE);
        g2.drawString(texto, x, y);
    }

    private void desenharBotaoReiniciar(Graphics2D g2, Color corBase) {

        int x = botaoX();
        int y = botaoY();

        g2.setColor(new Color(0, 0, 0, 90));
        g2.fill(new RoundRectangle2D.Float(
                x + 3, y + 3, botaoLargura, botaoAltura, 18, 18));

        GradientPaint gradiente = new GradientPaint(
                x, y, corBase.brighter(),
                x, y + botaoAltura, corBase
        );

        g2.setPaint(gradiente);
        g2.fill(new RoundRectangle2D.Float(
                x, y, botaoLargura, botaoAltura, 18, 18));
        g2.setPaint(null);

        g2.setColor(corBase.darker());
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new RoundRectangle2D.Float(
                x, y, botaoLargura, botaoAltura, 18, 18));

        String texto = "JOGAR NOVAMENTE";

        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        FontMetrics fm = g2.getFontMetrics();

        int textoX = x + (botaoLargura - fm.stringWidth(texto)) / 2;
        int textoY = y + (botaoAltura + fm.getAscent()) / 2 - 4;

        g2.setColor(Color.WHITE);
        g2.drawString(texto, textoX, textoY);
    }
    
    private void desenharBotaoHistorico(Graphics2D g2) {

        int x = botaoX();
        int y = botaoHistoricoY();

        Color corBase = new Color(90, 90, 110);

        g2.setColor(new Color(0, 0, 0, 80));
        g2.fill(new RoundRectangle2D.Float(
                x + 3, y + 3, botaoLargura, botaoHistoricoAltura, 16, 16));

        GradientPaint gradiente = new GradientPaint(
                x, y, corBase.brighter(),
                x, y + botaoHistoricoAltura, corBase
        );

        g2.setPaint(gradiente);
        g2.fill(new RoundRectangle2D.Float(
                x, y, botaoLargura, botaoHistoricoAltura, 16, 16));
        g2.setPaint(null);

        g2.setColor(corBase.darker());
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new RoundRectangle2D.Float(
                x, y, botaoLargura, botaoHistoricoAltura, 16, 16));

        String texto = "HISTÓRICO";

        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();

        int textoX = x + (botaoLargura - fm.stringWidth(texto)) / 2;
        int textoY = y + (botaoHistoricoAltura + fm.getAscent()) / 2 - 4;

        g2.setColor(Color.WHITE);
        g2.drawString(texto, textoX, textoY);
    }

    // =========================
    // CLIQUE NO BOTÃO
    // =========================

    @Override
    public void mousePressed(MouseEvent e) {

        if (!gp.emGameOver && !gp.emVitoria) {
            return;
        }

        if (gp.emHistorico) {
            return;
        }

        int mouseX = e.getX();
        int mouseY = e.getY();

        int x = botaoX();
        int y = botaoY();

        if (mouseX >= x && mouseX <= x + botaoLargura
                && mouseY >= y && mouseY <= y + botaoAltura) {

            gp.reiniciarJogo();
            return;
        }

        int yHist = botaoHistoricoY();

        if (mouseX >= x && mouseX <= x + botaoLargura
                && mouseY >= yHist && mouseY <= yHist + botaoHistoricoAltura) {

            gp.emHistorico = true;
            gp.repaint();
        }
    }

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