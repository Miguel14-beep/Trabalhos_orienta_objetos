package entidade;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entidade {

	public int x, y;
	public int velocidade;
	
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String direcao;
	
	public int spriteCounter = 0;
	public int spriteNun = 1;
	
	public Rectangle solidArea;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean colisaoOn = false;

}
