package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.Painel;

public class TileManager {
	
	Painel gp;
	public Tile[] tiles;
	public int mapaTileNun[][];
	
	public TileManager(Painel gp) {
		this.gp = gp;
		
		tiles = new Tile[15];
		mapaTileNun = new int[gp.maxColunaTela][gp.maxLacunaTela];
		
		getTileImage();
		loadMapa();
	}
	
	public void getTileImage() {

	    try {

	        tiles[0] = new Tile();
	        tiles[0].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/grass01.png")
	        );


	        tiles[1] = new Tile();
	        tiles[1].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/grama1.png")
	        );
	        tiles[1].colisao = true;


	        tiles[2] = new Tile();
	        tiles[2].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/grama2.png")
	        );


	        // LAVA
	        tiles[3] = new Tile();
	        tiles[3].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/lava1.png")
	        );
	        tiles[3].colisao = true;
	        tiles[3].batalha = true;


	        // ÁGUA
	        tiles[4] = new Tile();
	        tiles[4].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/agua1.png")
	        );
	        tiles[4].colisao = false;
	        tiles[4].batalha = true;


	        // PEDRA
	        tiles[5] = new Tile();
	        tiles[5].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/pedra1.png")
	        );
	        tiles[5].batalha = true;


	        // FLORESTA
	        tiles[6] = new Tile();
	        tiles[6].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/floresta1.png")
	        );
	        tiles[6].batalha = true;


	        // PAREDE 1
	        tiles[7] = new Tile();
	        tiles[7].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/parede1.png")
	        );
	        tiles[7].batalha = true;


	        // PAREDE 2
	        tiles[8] = new Tile();
	        tiles[8].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/parede2.png")
	        );
	        tiles[8].batalha = true;


	        // PORTÃO
	        tiles[9] = new Tile();
	        tiles[9].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/portao1.png")
	        );


	        // TORRE
	        tiles[10] = new Tile();
	        tiles[10].imagem = ImageIO.read(
	            getClass().getResourceAsStream("/tiles/torre1.png")
	        );
	        tiles[10].batalha = true;


	    } catch(IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void draw(Graphics2D g2) {
		
		int col = 0;
		int lin = 0;
		int x =0;
		int y = 0;
		
		while(col < gp.maxColunaTela && lin < gp.maxLacunaTela) {
			
			int tileNun = mapaTileNun[col][lin];
			
			
			g2.drawImage(tiles[tileNun].imagem, x, y, gp.tileTamanho, gp.tileTamanho, null);
			col++;
			x += gp.tileTamanho;
			
			if(col == gp.maxColunaTela) {
				col = 0;
				x = 0;
				lin++;
				y += gp.tileTamanho;
			}
		}
	}
	
	public void loadMapa() {
		try {
			InputStream is = getClass().getResourceAsStream("/mapa/mapa.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int lin = 0;
			
			while(col < gp.maxColunaTela && lin < gp.maxLacunaTela) {
				
				String line = br.readLine();
				while(col < gp.maxColunaTela) {
					String numeros[] = line.split(" ");
					
					int nun = Integer.parseInt(numeros[col]);
					
					mapaTileNun[col][lin] = nun;
					col++;
				}
				if(col == gp.maxColunaTela) {
					col = 0;
					lin++;
				}
			}
			br.close();
			
			
		}catch(Exception e) {
			
		}
	}
}
