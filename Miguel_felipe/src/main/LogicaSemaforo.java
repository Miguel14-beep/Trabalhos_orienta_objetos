package main;

import java.awt.Color;
import java.awt.Graphics;

public class LogicaSemaforo {
    private String[] estados = {"VERMELHO", "VERDE", "AMARELO"};
    private int indiceAtual = 0;

    public String estadoAtual() {
        return estados[indiceAtual];
    }

    public void avancar() {
        
    	indiceAtual++;
    	if(indiceAtual == 3) {
    		indiceAtual = 0;
    	}
    }
    
    public void mudarCor(Graphics g2) {
    	String atual = estadoAtual();
    	
    	if(atual.equals("VERMELHO")) {
    		g2.setColor(Color.red);
            g2.fillOval(30, 50, 100, 100);
            
            g2.setColor(Color.DARK_GRAY);
            g2.fillOval(150, 50, 100, 100);
            
            g2.setColor(Color.DARK_GRAY);
            g2.fillOval(270, 50, 100, 100);
            
    	}else if(atual.equals("VERDE")) {
    		g2.setColor(Color.DARK_GRAY);
            g2.fillOval(30, 50, 100, 100);
            
            g2.setColor(Color.DARK_GRAY);
            g2.fillOval(150, 50, 100, 100);
            
            g2.setColor(Color.green);
            g2.fillOval(270, 50, 100, 100);
            
    	}else if(atual.equals("AMARELO")) {
    		g2.setColor(Color.DARK_GRAY);
            g2.fillOval(30, 50, 100, 100);
            
            g2.setColor(Color.yellow);
            g2.fillOval(150, 50, 100, 100);
            
            g2.setColor(Color.DARK_GRAY);
            g2.fillOval(270, 50, 100, 100);
    	}
    }
}

