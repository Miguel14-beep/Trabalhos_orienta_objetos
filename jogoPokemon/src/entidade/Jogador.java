package entidade;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import main.Item;
import main.KeyHandler;
import main.Painel;
import main.TipoItem;

public class Jogador extends Entidade{
	Painel gp;
	KeyHandler keyH;
	List inventario = new ArrayList<>();
	
	public Jogador(Painel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		
		setDefaultValues();
		getImgJogador();
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
		// Exemplo de como vai ficar a criação na sua lista:
		inventario.add(new Item("Poção Pequena", 5, TipoItem.CURA, 20, 0));
		inventario.add(new Item("Suco de Força", 2, TipoItem.BUFF_ATAQUE, 10, 3)); // Dura 3 turnos
		inventario.add(new Item("Escudo de Penas", 2, TipoItem.BUFF_DEFESA, 15, 3)); // Dura 3 turnos
		inventario.add(new Item("BirdBola Padrão", 3, TipoItem.CAPTURA, 40, 0));
	}
	public void setDefaultValues() {
		x = 100;
		y = 100;
		velocidade = 4;
		direcao = "down";
	}
	
	
	public void getImgJogador() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/jogador/boy_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/jogador/boy_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/jogador/boy_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/jogador/boy_down_2.png"));
			
			left1 = ImageIO.read(getClass().getResourceAsStream("/jogador/boy_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/jogador/boy_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/jogador/boy_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/jogador/boy_right_2.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void voltarUmTile() {

	    switch (direcao) {

	        case "up":
	            y += gp.tileTamanho;
	            break;

	        case "down":
	            y -= gp.tileTamanho;
	            break;

	        case "left":
	            x += gp.tileTamanho;
	            break;

	        case "right":
	            x -= gp.tileTamanho;
	            break;
	    }
	}
	
	public void update() {
		if(keyH.upPressed == true) {
			
			direcao = "up";
		}
		else if(keyH.downPressed == true) {
			
			direcao = "down";
		}
		else if(keyH.leftPressed == true) {
			
			direcao = "left";
		}
		else if(keyH.rightPressed == true) {
			
			direcao = "right";
		}
		
		colisaoOn = false;
		gp.cChecker.checkTile(this);
		
		boolean batalha = gp.cChecker.checkBatalha(this);

		if(batalha) {
		    gp.iniciarBatalha();
		    return;
		}
		
		
		
		if(colisaoOn == false) {
			if(keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.upPressed) {
				switch(direcao) {
				case "up":
					y -= velocidade;
					break;
					
				case "down":
					y += velocidade;
					break;
					
				case "left":
					x -= velocidade;
					break;
					
				case "right":
					x += velocidade;
					break;
				}
			}
		}
		
		spriteCounter++;
		if(spriteCounter > 15) {
			if(spriteNun == 1) {
				spriteNun = 2;
				
			}else if(spriteNun == 2 ){
				spriteNun = 1;
			}
			spriteCounter = 0;
		}

	}
	
	public void draw(Graphics g2) {
		
		//se quiser sum quadrado que se move como jogador, descomente os dois comentarios abaixo, depois comente tudo dentro de draw
		
		//g2.setColor(Color.white);
		//g2.fillRect(x, y, gp.tileTamanho, gp.tileTamanho);
		
		
		//comente daqui pra baixo
		BufferedImage imagem = null;
		
		switch(direcao) {
		case "up":
			if(spriteNun == 1) {
				imagem = up1;
			}
			if(spriteNun == 2) {
				imagem = up2;
			}
			break;
		case "down":
			if(spriteNun == 1) {
				imagem = down1;
			}
			if(spriteNun == 2) {
				imagem = down2;
			}
			break;
		case "left":
			if(spriteNun == 1) {
				imagem = left1;
			}
			if(spriteNun == 2) {
				imagem = left2;
			}
			break;
		case "right":
			if(spriteNun == 1) {
				imagem = right1;
			}
			if(spriteNun == 2) {
				imagem = right2;
			}
			break;
		}
		
		g2.drawImage(imagem, x, y, gp.tileTamanho, gp.tileTamanho, null);
		
	}
}
