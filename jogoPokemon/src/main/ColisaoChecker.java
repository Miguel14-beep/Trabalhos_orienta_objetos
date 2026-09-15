package main;

import java.awt.Rectangle;

import entidade.Entidade;

public class ColisaoChecker {
	
	Painel gp;
	
	public  ColisaoChecker(Painel gp){
		this.gp = gp;
		
	}
	
	public void checkTile(Entidade entidade) {
		int entiLeftX = entidade.x + entidade.solidArea.x;
		int entiRightX = entidade.x + entidade.solidArea.x + entidade.solidArea.width;
		int entiTopY = entidade.y + entidade.solidArea.y;
		int entiBottomY = entidade.y + entidade.solidArea.y + entidade.solidArea.height;
		
		int entiLeftCol = entiLeftX/gp.tileTamanho;
		int entiRightCol = entiRightX/gp.tileTamanho;
		int entiTopLin = entiTopY/gp.tileTamanho;
		int entiBottomLin = entiBottomY/gp.tileTamanho;
		
		int tileNun1, tileNun2;
		
		switch(entidade.direcao) {
		case "up":
			entiTopLin = (entiTopY - entidade.velocidade)/gp.tileTamanho;
			tileNun1 = gp.tileM.mapaTileNun[entiLeftCol][entiTopLin];
			tileNun2 = gp.tileM.mapaTileNun[entiRightCol][entiTopLin];
			if(gp.tileM.tiles[tileNun1].colisao == true || gp.tileM.tiles[tileNun2].colisao == true) {
				entidade.colisaoOn = true;
			}
			break;
			
		case "down":
			entiBottomLin = (entiBottomY + entidade.velocidade)/gp.tileTamanho;
			tileNun1 = gp.tileM.mapaTileNun[entiLeftCol][entiBottomLin];
			tileNun2 = gp.tileM.mapaTileNun[entiRightCol][entiBottomLin];
			if(gp.tileM.tiles[tileNun1].colisao == true || gp.tileM.tiles[tileNun2].colisao == true) {
				entidade.colisaoOn = true;
			}
			break;
			
		case "left":
			entiLeftCol = (entiLeftX - entidade.velocidade)/gp.tileTamanho;
			tileNun1 = gp.tileM.mapaTileNun[entiLeftCol][entiTopLin];
			tileNun2 = gp.tileM.mapaTileNun[entiLeftCol][entiBottomLin];
			if(gp.tileM.tiles[tileNun1].colisao == true || gp.tileM.tiles[tileNun2].colisao == true) {
				entidade.colisaoOn = true;
			}
			break;
			
		case "right":
			entiRightX = (entiRightX + entidade.velocidade)/gp.tileTamanho;
			tileNun1 = gp.tileM.mapaTileNun[entiRightCol][entiTopLin];
			tileNun2 = gp.tileM.mapaTileNun[entiRightCol][entiBottomLin];
			if(gp.tileM.tiles[tileNun1].colisao == true || gp.tileM.tiles[tileNun2].colisao == true) {
				entidade.colisaoOn = true;
			}
			break;
		}
		
	}
	

	
	
	public boolean checkBatalha(Entidade entidade) {

	    int entiLeftX = entidade.x + entidade.solidArea.x;
	    int entiRightX = entidade.x + entidade.solidArea.x + entidade.solidArea.width;
	    int entiTopY = entidade.y + entidade.solidArea.y;
	    int entiBottomY = entidade.y + entidade.solidArea.y + entidade.solidArea.height;

	    int entiLeftCol = entiLeftX / gp.tileTamanho;
	    int entiRightCol = entiRightX / gp.tileTamanho;
	    int entiTopLin = entiTopY / gp.tileTamanho;
	    int entiBottomLin = entiBottomY / gp.tileTamanho;

	    switch(entidade.direcao) {

	    case "up":

	        entiTopLin = (entiTopY - entidade.velocidade) / gp.tileTamanho;

	        if(gp.tileM.tiles[
	                gp.tileM.mapaTileNun[entiLeftCol][entiTopLin]
	            ].batalha
	            ||
	            gp.tileM.tiles[
	                gp.tileM.mapaTileNun[entiRightCol][entiTopLin]
	            ].batalha) {

	            return true;
	        }

	        break;


	    case "down":

	        entiBottomLin = (entiBottomY + entidade.velocidade) / gp.tileTamanho;

	        if(gp.tileM.tiles[
	                gp.tileM.mapaTileNun[entiLeftCol][entiBottomLin]
	            ].batalha
	            ||
	            gp.tileM.tiles[
	                gp.tileM.mapaTileNun[entiRightCol][entiBottomLin]
	            ].batalha) {

	            return true;
	        }

	        break;


	    case "left":

	        entiLeftCol = (entiLeftX - entidade.velocidade) / gp.tileTamanho;

	        if(gp.tileM.tiles[
	                gp.tileM.mapaTileNun[entiLeftCol][entiTopLin]
	            ].batalha
	            ||
	            gp.tileM.tiles[
	                gp.tileM.mapaTileNun[entiLeftCol][entiBottomLin]
	            ].batalha) {

	            return true;
	        }

	        break;


	    case "right":

	        entiRightCol = (entiRightX + entidade.velocidade) / gp.tileTamanho;

	        if(gp.tileM.tiles[
	                gp.tileM.mapaTileNun[entiRightCol][entiTopLin]
	            ].batalha
	            ||
	            gp.tileM.tiles[
	                gp.tileM.mapaTileNun[entiRightCol][entiBottomLin]
	            ].batalha) {

	            return true;
	        }

	        break;
	    }

	    return false;
	}
}

