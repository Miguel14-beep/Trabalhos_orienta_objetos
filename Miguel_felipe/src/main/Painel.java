package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;


import javax.swing.JPanel;

public class Painel extends JPanel{
	
	LogicaSemaforo semaforo;
	
	public Painel(LogicaSemaforo semaforo) {
		
		this.semaforo = semaforo;
		this.setPreferredSize(new Dimension(1000, 1000));
		this.setSize(getPreferredSize());
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		semaforo.mudarCor(g2);
        
        

		
		g2.dispose();
	}
}
