package main;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
        
		
		SwingUtilities.invokeLater(() -> {
			LogicaSemaforo semaforo = new LogicaSemaforo();
	        Painel painel = new Painel(semaforo);
	        painel.setLayout(null);
	        
	        JButton botao = new JButton("Clique aqui");
	        botao.setBounds(110, 160, 200, 100);
	        botao.addActionListener(e -> {
	        	semaforo.avancar();
	        	painel.repaint();
	        	
	        });
	        
	        painel.add(botao);
	        
	        JFrame janela = new JFrame();
	        janela.setTitle("Minha Primeira Janela");
	        janela.setSize(400, 300);
	        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        janela.add(painel);
	        
	        janela.setVisible(true);
    
		});
   
    }

}
