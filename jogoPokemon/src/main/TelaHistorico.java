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
import java.util.List;

public class TelaHistorico implements MouseListener {

    Painel gp;

    int scroll = 0;

    int panelX = 50;
    int panelY = 40;
    int panelLargura = 670;
    int panelAltura = 490;

    int itemX = 80;
    int itemYInicio = 130;
    int itemLargura = 550;
    int itemAltura = 50;
    int espacamento = 10;
    int maxItensNaTela = 6;

    int botaoCimaX = 650;
    int botaoCimaY = 130;
    int botaoBaixoX = 650;
    int botaoBaixoY = 430;
    int botaoLarguraScroll = 60;
    int botaoAlturaScroll = 40;

    int voltarLargura = 140;
    int voltarAltura = 36;

    public TelaHistorico(Painel gp) {
        this.gp = gp;
    }

    private int voltarX() {
        return (gp.larguraTela - voltarLargura) / 2;
    }

    private int voltarY() {
        return panelY + panelAltura + 4;
    }

    // =========================
    // DESENHO
    // =========================

    public void draw(Graphics2D g2) {

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        GradientPaint fundo = new GradientPaint(
                0, 0, new Color(40, 40, 60),
                0, gp.comprimentoTela, new Color(15, 15, 25)
        );

        g2.setPaint(fundo);
        g2.fillRect(0, 0, gp.larguraTela, gp.comprimentoTela);
        g2.setPaint(null);

        desenharPainel(g2);
        desenharTitulo(g2);
        desenharLista(g2);
        desenharBotoesScroll(g2);
        desenharVoltar(g2);
    }

    private void desenharPainel(Graphics2D g2) {

        g2.setColor(new Color(0, 0, 0, 90));
        g2.fill(new RoundRectangle2D.Float(
                panelX + 4, panelY + 4,
                panelLargura, panelAltura, 22, 22));

        GradientPaint gradiente = new GradientPaint(
                panelX, panelY, Color.WHITE,
                panelX, panelY + panelAltura, new Color(225, 225, 235)
        );

        g2.setPaint(gradiente);
        g2.fill(new RoundRectangle2D.Float(
                panelX, panelY,
                panelLargura, panelAltura, 22, 22));
        g2.setPaint(null);

        g2.setColor(new Color(80, 80, 90));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new RoundRectangle2D.Float(
                panelX, panelY,
                panelLargura, panelAltura, 22, 22));
    }

    private void desenharTitulo(Graphics2D g2) {

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("HISTÓRICO DE PARTIDAS", panelX + 30, panelY + 40);

        int vitorias = gp.historico.totalVitorias();
        int derrotas = gp.historico.totalDerrotas();

        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));

        g2.setColor(new Color(60, 150, 80));
        g2.drawString("Vitórias: " + vitorias, panelX + 30, panelY + 65);

        g2.setColor(new Color(190, 60, 60));
        g2.drawString(
                "Derrotas: " + derrotas,
                panelX + 30 + 110,
                panelY + 65
        );
    }

    private void desenharLista(Graphics2D g2) {

        List<RegistroPartida> registros = gp.historico.getRegistros();

        if (registros.isEmpty()) {

            g2.setColor(new Color(110, 110, 110));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 15));
            g2.drawString(
                    "Nenhuma partida registrada ainda.",
                    itemX, itemYInicio + 30
            );

            return;
        }

        for (int i = 0; i < maxItensNaTela; i++) {

            int index = scroll + i;

            if (index >= registros.size()) {
                break;
            }

            RegistroPartida registro = registros.get(index);

            int posY = itemYInicio + i * (itemAltura + espacamento);

            Color corFundo = registro.isVitoria()
                    ? new Color(225, 245, 230)
                    : new Color(250, 225, 225);

            Color corBorda = registro.isVitoria()
                    ? new Color(90, 170, 110)
                    : new Color(190, 100, 100);

            g2.setColor(corFundo);
            g2.fill(new RoundRectangle2D.Float(
                    itemX, posY, itemLargura, itemAltura, 14, 14));

            g2.setColor(corBorda);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(
                    itemX, posY, itemLargura, itemAltura, 14, 14));

            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.setColor(corBorda.darker());
            g2.drawString(
                    registro.isVitoria() ? "VITÓRIA" : "DERROTA",
                    itemX + 15, posY + 22
            );

            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.setColor(new Color(60, 60, 60));
            g2.drawString(registro.data, itemX + 150, posY + 22);

            g2.drawString(
                    "Capturas: " + registro.especiesCapturadas
                            + "   Duração: " + registro.getDuracaoFormatada(),
                    itemX + 15, posY + 40
            );
        }
    }

    private void desenharBotaoScroll(
            Graphics2D g2,
            int x, int y,
            String texto,
            boolean habilitado) {

        Color corBase = habilitado
                ? new Color(120, 120, 140)
                : new Color(190, 190, 190);

        GradientPaint gradiente = new GradientPaint(
                x, y, corBase.brighter(),
                x, y + botaoAlturaScroll, corBase
        );

        g2.setPaint(gradiente);
        g2.fill(new RoundRectangle2D.Float(
                x, y, botaoLarguraScroll, botaoAlturaScroll, 12, 12));
        g2.setPaint(null);

        g2.setColor(corBase.darker());
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Float(
                x, y, botaoLarguraScroll, botaoAlturaScroll, 12, 12));

        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();

        int textoX = x + (botaoLarguraScroll - fm.stringWidth(texto)) / 2;
        int textoY = y + (botaoAlturaScroll + fm.getAscent()) / 2 - 3;

        g2.setColor(Color.WHITE);
        g2.drawString(texto, textoX, textoY);
    }

    private void desenharBotoesScroll(Graphics2D g2) {

        boolean podeSubir = scroll > 0;

        boolean podeDescer =
                scroll + maxItensNaTela < gp.historico.getRegistros().size();

        desenharBotaoScroll(
                g2, botaoCimaX, botaoCimaY, "CIMA", podeSubir);

        desenharBotaoScroll(
                g2, botaoBaixoX, botaoBaixoY, "BAIXO", podeDescer);
    }

    private void desenharVoltar(Graphics2D g2) {

        int x = voltarX();
        int y = voltarY();

        g2.setColor(new Color(0, 0, 0, 80));
        g2.fill(new RoundRectangle2D.Float(
                x + 2, y + 2, voltarLargura, voltarAltura, 14, 14));

        GradientPaint gradiente = new GradientPaint(
                x, y, new Color(140, 140, 150),
                x, y + voltarAltura, new Color(90, 90, 100)
        );

        g2.setPaint(gradiente);
        g2.fill(new RoundRectangle2D.Float(
                x, y, voltarLargura, voltarAltura, 14, 14));
        g2.setPaint(null);

        g2.setColor(new Color(60, 60, 60));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Float(
                x, y, voltarLargura, voltarAltura, 14, 14));

        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();

        String texto = "VOLTAR";

        int textoX = x + (voltarLargura - fm.stringWidth(texto)) / 2;
        int textoY = y + (voltarAltura + fm.getAscent()) / 2 - 3;

        g2.setColor(Color.WHITE);
        g2.drawString(texto, textoX, textoY);
    }

    // =========================
    // CLIQUES
    // =========================

    @Override
    public void mousePressed(MouseEvent e) {

        if (!gp.emHistorico) {
            return;
        }

        int mouseX = e.getX();
        int mouseY = e.getY();

        // VOLTAR

        if (mouseX >= voltarX()
                && mouseX <= voltarX() + voltarLargura
                && mouseY >= voltarY()
                && mouseY <= voltarY() + voltarAltura) {

            gp.emHistorico = false;
            gp.repaint();

            return;
        }

        // CIMA

        if (mouseX >= botaoCimaX
                && mouseX <= botaoCimaX + botaoLarguraScroll
                && mouseY >= botaoCimaY
                && mouseY <= botaoCimaY + botaoAlturaScroll) {

            if (scroll > 0) {

                scroll--;
                gp.repaint();
            }

            return;
        }

        // BAIXO

        if (mouseX >= botaoBaixoX
                && mouseX <= botaoBaixoX + botaoLarguraScroll
                && mouseY >= botaoBaixoY
                && mouseY <= botaoBaixoY + botaoAlturaScroll) {

            if (scroll + maxItensNaTela
                    < gp.historico.getRegistros().size()) {

                scroll++;
                gp.repaint();
            }

            return;
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