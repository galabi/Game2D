package Creature;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

import Entity.entity;
import Entity.gameColors;
import MainPackage.Main;

public class creature extends entity{
	
	ImageIcon[] image;
	int creatureDirection = 0;
	int maxHealth;
	int health;
	int demage;
	
	final static Color SadowColor = gameColors.playerSadowColor;
	
	public creature(int x, int y ,int sizeX ,int sizeY) {
		super(x, y, sizeX, sizeY);
	}
	
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(SadowColor);
		g2d.fillOval(x - Main.tilesManager.getCameraX(false)+(sizeX-23)/2, y - Main.tilesManager.getCameraY(false)+sizeY-7, 25,15);
		g2d.drawImage(image[creatureDirection].getImage(), x - Main.tilesManager.getCameraX(false) ,
				y - Main.tilesManager.getCameraY(false), sizeX, sizeY,null);
		
	}
	
	//use this to lower the creature health points
	public void hitCreature(int demage) {
		health -= demage;
	}
}
