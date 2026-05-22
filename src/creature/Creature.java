package creature;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

import MainPackage.Main;
import MainPackage.TilesManager;
import entity.Entity;
import entity.GameColors;

public class Creature extends Entity{
	
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

	protected ArrayList<Integer> lootIds = new ArrayList<>();
	protected long nextMoveTime = 0;
	private long hitFlashTime = 0;
	private static final long HIT_FLASH_DURATION_MS = 200;

	final static Color shadowColor = GameColors.playerShadowColor;
	private static final Random RANDOM = new Random();
	
	public Creature(int x, int y ,int sizeX ,int sizeY) {
		super(x, y, sizeX, sizeY);
		targetX = x;
		targetY = y;
	}
	
	public void render(Graphics2D g2d) {
		g2d.setColor(shadowColor);
		g2d.fillOval(x - Main.tilesManager.getCameraX(false) + (sizeX - 44) / 2, y - Main.tilesManager.getCameraY(false) + sizeY - 10, 44, 20);
		g2d.drawImage(image[creatureDirection].getImage(), x - Main.tilesManager.getCameraX(false) ,
				y - Main.tilesManager.getCameraY(false), sizeX, sizeY,null);

		if (System.currentTimeMillis() - hitFlashTime < HIT_FLASH_DURATION_MS) {
			Composite original = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
			g2d.setColor(Color.RED);
			g2d.fillRect(x - Main.tilesManager.getCameraX(false),
						 y - Main.tilesManager.getCameraY(false), sizeX, sizeY);
			g2d.setComposite(original);
		}

		//debug
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
			long now = System.currentTimeMillis();
			if (now >= nextMoveTime) {
				setNextLocation();
				nextMoveTime = now + 2000 + RANDOM.nextInt(3000);
			}
		}
	}
	
	//use this to lower the creature health points
	public void hitCreature(int damage) {
		health -= damage;
		hitFlashTime = System.currentTimeMillis();
	}

	public void die() {
		int mapI = (y + CollisionBoxY + CollisionBoxHeight / 2) / TilesManager.tileSize;
		int mapJ = (x + CollisionBoxX + CollisionBoxWidth / 2) / TilesManager.tileSize;
		for (int itemId : lootIds) {
			Main.tilesManager.addDrop(mapI, mapJ, itemId);
		}
	}

	public Rectangle getCollisionRect() {
		return new Rectangle(x + CollisionBoxX, y + CollisionBoxY, CollisionBoxWidth, CollisionBoxHeight);
	}

	public int getDamage() {
		return damage;
	}

	public int getHealth() {
		return health;
	}
	
	
	public void setNextLocation() {
		int Direction;
		int target;
		speed = Math.abs(speed);

		Direction = RANDOM.nextInt(4);
		target = (1 + RANDOM.nextInt(3)) * TilesManager.tileSize;
		
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
	
	private void collision(int direction) {
		int ts = TilesManager.tileSize;
		// X positions within tile — no speed offset for vertical moves
		int xLeft       = (x + CollisionBoxX) % ts;
		int xRight      = (x + CollisionBoxX + CollisionBoxWidth) % ts;
		// X positions with speed — for horizontal moves
		int xLeftSpeed  = (x + CollisionBoxX + speed) % ts;
		int xRightSpeed = (x + CollisionBoxX + CollisionBoxWidth + speed) % ts;
		// Y positions within tile — no speed offset for horizontal moves
		int yTop        = (y + CollisionBoxY) % ts;
		int yBottom     = (y + CollisionBoxY + CollisionBoxHeight) % ts;
		// Y positions with speed — for vertical moves
		int yTopSpeed    = (y + CollisionBoxY + speed) % ts;
		int yBottomSpeed = (y + CollisionBoxY + CollisionBoxHeight + speed) % ts;

		switch (direction) {
		case 0: // down — check bottom edge
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + CollisionBoxHeight + speed) / ts]
					[(x + CollisionBoxX) / ts]
					.isSolid(xLeft, yBottomSpeed, CollisionBoxWidth, 1)) targetY = y;
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + CollisionBoxHeight + speed) / ts]
					[(x + CollisionBoxX + CollisionBoxWidth) / ts]
					.isSolid(xRight - CollisionBoxWidth, yBottomSpeed, CollisionBoxWidth, 1)) targetY = y;
			break;

		case 1: // left — check left edge
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY) / ts]
					[(x + CollisionBoxX + speed) / ts]
					.isSolid(xLeftSpeed, yTop, 1, CollisionBoxHeight)) targetX = x;
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + CollisionBoxHeight) / ts]
					[(x + CollisionBoxX + speed) / ts]
					.isSolid(xLeftSpeed, yBottom - CollisionBoxHeight, 1, CollisionBoxHeight)) targetX = x;
			break;

		case 2: // right — check right edge
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY) / ts]
					[(x + CollisionBoxX + CollisionBoxWidth + speed) / ts]
					.isSolid(xRightSpeed, yTop, 1, CollisionBoxHeight)) targetX = x;
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + CollisionBoxHeight) / ts]
					[(x + CollisionBoxX + CollisionBoxWidth + speed) / ts]
					.isSolid(xRightSpeed, yBottom - CollisionBoxHeight, 1, CollisionBoxHeight)) targetX = x;
			break;

		case 3: // up — check top edge
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + speed) / ts]
					[(x + CollisionBoxX) / ts]
					.isSolid(xLeft, yTopSpeed, CollisionBoxWidth, 1)) targetY = y;
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + speed) / ts]
					[(x + CollisionBoxX + CollisionBoxWidth) / ts]
					.isSolid(xRight - CollisionBoxWidth, yTopSpeed, CollisionBoxWidth, 1)) targetY = y;
			break;
		}
	}
	
	public boolean isInTarget() {
		return (x == targetX && y == targetY);
	}
}
