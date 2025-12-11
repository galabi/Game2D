package Creature;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.ImageIcon;

import Entity.entity;
import Entity.GameColors;
import MainPackage.Main;
import MainPackage.TilesManager;

public class Creature extends entity{
	
	ImageIcon[] image;
	int creatureDirection = 0;
	int maxHealth;
	int health;
	int damage;
	int targetX,targetY;
	int speed = 1;
	
	int CollisionBoxX;
	int CollisionBoxY;
	int CollisionBoxWidth;
	int CollisionBoxHeight;
	
	final static Color SadowColor = GameColors.playerShadowColor;
	
	public Creature(int x, int y ,int sizeX ,int sizeY) {
		super(x, y, sizeX, sizeY);
		targetX = x;
		targetY = y;
	}
	
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(SadowColor);
		g2d.fillOval(x - Main.tilesManager.getCameraX(false) + (sizeX - 44) / 2, y - Main.tilesManager.getCameraY(false) + sizeY - 10, 44, 20);
		g2d.drawImage(image[creatureDirection].getImage(), x - Main.tilesManager.getCameraX(false) ,
				y - Main.tilesManager.getCameraY(false), sizeX, sizeY,null);
		
		//debag
		if(Main.devmode) {
			g2d.setColor(Color.white);
			g2d.drawRect(x+CollisionBoxX-Main.tilesManager.getCameraX(false), y+CollisionBoxY-Main.tilesManager.getCameraY(false), CollisionBoxWidth, CollisionBoxHeight);
		}
	}
	
	public void move() {
		collision(creatureDirection);
		if(x != targetX){
			if(Math.abs(targetX-x) <= Math.abs(speed)) {
				x = targetX;
				return;
			}
			
			x += speed;
		}else if(y != targetY) {
			if(Math.abs(targetY-y) <= Math.abs(speed)) {
				y = targetY;
				return;
			}
			y += speed;
		}else {
			setNextLocation();
		}
	}
	
	//use this to lower the creature health points
	public void hitCreature(int damage) {
		health -= damage;
	}
	
	
	public void setNextLocation() {
		Random r = new Random();
		int Direction;
		int target;
		speed = Math.abs(speed);

		
		Direction = r.nextInt(4);
		target = r.nextInt(0,64);
		
		/**
		 * 0 = down
		 * 1 = left
		 * 2 = right
		 * 3 = up
			*/
			
			switch (Direction) {
			case 0: 
				targetX = x;
				targetY = y+target;
				speed = Math.abs(speed);
				break;
			case 1: 
				targetX = x-target;
				targetY = y;
				speed = -Math.abs(speed);
				break;
			case 2: 
				targetX = x+target;
				targetY = y;
				speed = Math.abs(speed);
				break;
			case 3: 
				targetX = x;
				targetY = y-target;
				speed = -Math.abs(speed);
				break;
			}
			
			creatureDirection = Direction;
			
		
		
	}
	
	private void collision(int Direction){
		int XInTile = (x + CollisionBoxX + speed) % TilesManager.tileSize;;
		int XInTileAndWidth = (x + CollisionBoxX + CollisionBoxWidth + speed) % TilesManager.tileSize;;
		int YInTile = (y + CollisionBoxY + speed) % TilesManager.tileSize;
		int YInTileAndHeight = (y + CollisionBoxY + CollisionBoxHeight + speed) % TilesManager.tileSize;;
		
		switch(Direction){
		case 0:
			if(Main.tilesManager.getTiles()[(int)((y + CollisionBoxY + speed)/TilesManager.tileSize)]
					[(int)((x + CollisionBoxX + speed )/TilesManager.tileSize)].isSolid(XInTile,YInTile,CollisionBoxWidth,1)) targetY = y;
			
			if(Main.tilesManager.getTiles()[(int)((y + CollisionBoxY + speed)/TilesManager.tileSize)]
					[(int)((x + CollisionBoxX + CollisionBoxWidth + speed )/TilesManager.tileSize)].isSolid(XInTileAndWidth-CollisionBoxWidth,YInTile,CollisionBoxWidth,1))targetY = y;
			break;
			
		case 1:
			if(Main.tilesManager.getTiles()[(int)((y + CollisionBoxY + speed )/TilesManager.tileSize)]
					[(int)((x + CollisionBoxX + speed)/TilesManager.tileSize)].isSolid(XInTile,YInTile,1,CollisionBoxHeight)) targetX = x;
			
			if(Main.tilesManager.getTiles()[(int)((y + CollisionBoxY + CollisionBoxHeight + speed )/TilesManager.tileSize)]
					[(int)((x + CollisionBoxX + speed)/TilesManager.tileSize)].isSolid(XInTile,YInTileAndHeight-CollisionBoxHeight,1,CollisionBoxHeight)) targetX = x;
			break;
			
		case 2:
			if(Main.tilesManager.getTiles()[(int)((y + CollisionBoxY + speed)/TilesManager.tileSize)]
					[(int)((x + CollisionBoxX + CollisionBoxWidth + speed)/TilesManager.tileSize)].isSolid(XInTileAndWidth,YInTile,1,CollisionBoxHeight)) targetX = x;			
			
			if(Main.tilesManager.getTiles()[(int)((y + CollisionBoxY + CollisionBoxHeight + speed )/TilesManager.tileSize)]
					[(int)((x + CollisionBoxX + CollisionBoxWidth + speed)/TilesManager.tileSize)].isSolid(XInTileAndWidth,YInTileAndHeight-CollisionBoxHeight,1,CollisionBoxHeight)) targetX = x;
			break;
		
		case 3:
			if(Main.tilesManager.getTiles()[(int)((y + CollisionBoxY + CollisionBoxHeight + speed)/TilesManager.tileSize)]
					[(int)((x + CollisionBoxX + speed)/TilesManager.tileSize)].isSolid(XInTile,YInTileAndHeight,CollisionBoxWidth,1)) targetY = y;
			
			if(Main.tilesManager.getTiles()[(int)((y + CollisionBoxY + CollisionBoxHeight + speed)/TilesManager.tileSize)]
					[(int)((x + CollisionBoxX + CollisionBoxWidth + speed)/TilesManager.tileSize)].isSolid(XInTileAndWidth-CollisionBoxWidth,YInTileAndHeight,CollisionBoxWidth,1)) targetY = y;
		break;
		

		}
		
	}
	
	public boolean IsInTarget() {
		return (x == targetX && y == targetY);
	}
}
