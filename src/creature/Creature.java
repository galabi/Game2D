package creature;

import java.awt.Color;
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

	private static int idCounter = 0;
	int id = idCounter++;
	int typeCode = 0;

	protected ArrayList<Integer> lootIds = new ArrayList<>();
	protected long nextMoveTime = 0;
	private static final int KNOCKBACK_DISTANCE = 40;
	private static final int KNOCKBACK_SPEED    = 4;
	private boolean knockbackActive = false;

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

		//debug
		if(Main.devmode) {
			g2d.setColor(Color.white);
			g2d.drawRect(x+CollisionBoxX-Main.tilesManager.getCameraX(false), y+CollisionBoxY-Main.tilesManager.getCameraY(false), CollisionBoxWidth, CollisionBoxHeight);
		}
	}
	
	public void move() {
		collision(creatureDirection);
		if (x != targetX) {
			if (Math.abs(targetX - x) <= Math.abs(speed)) {
				x = targetX;
				if (knockbackActive) endKnockback();
				return;
			}
			x += speed;
		} else if (y != targetY) {
			if (Math.abs(targetY - y) <= Math.abs(speed)) {
				y = targetY;
				if (knockbackActive) endKnockback();
				return;
			}
			y += speed;
		} else {
			if (knockbackActive) endKnockback();
			long now = System.currentTimeMillis();
			if (now >= nextMoveTime) {
				setNextLocation();
				nextMoveTime = now + 2000 + RANDOM.nextInt(3000);
			}
		}
	}

	private void endKnockback() {
		knockbackActive = false;
		speed = 1;
		nextMoveTime = 0;
	}
	
	public void hitCreature(int damage, int attackerX, int attackerY) {
		health -= damage;
		applyKnockback(attackerX, attackerY);
	}

	public void applyKnockback(int attackerX, int attackerY) {
		int myCX = x + CollisionBoxX + CollisionBoxWidth  / 2;
		int myCY = y + CollisionBoxY + CollisionBoxHeight / 2;
		int dx = myCX - attackerX;
		int dy = myCY - attackerY;
		if (Math.abs(dx) >= Math.abs(dy)) {
			targetX = x + (dx >= 0 ? KNOCKBACK_DISTANCE : -KNOCKBACK_DISTANCE);
			targetY = y;
			speed = dx >= 0 ? KNOCKBACK_SPEED : -KNOCKBACK_SPEED;
			creatureDirection = dx >= 0 ? 2 : 1;
		} else {
			targetX = x;
			targetY = y + (dy >= 0 ? KNOCKBACK_DISTANCE : -KNOCKBACK_DISTANCE);
			speed = dy >= 0 ? KNOCKBACK_SPEED : -KNOCKBACK_SPEED;
			creatureDirection = dy >= 0 ? 0 : 3;
		}
		knockbackActive = true;
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

	public void setHealth(int h) {
		health = h;
	}

	public int getCreatureDirection() {
		return creatureDirection;
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
			if (isWaterTile((y + CollisionBoxY + CollisionBoxHeight + speed) / ts, (x + CollisionBoxX) / ts)) targetY = y;
			if (isWaterTile((y + CollisionBoxY + CollisionBoxHeight + speed) / ts, (x + CollisionBoxX + CollisionBoxWidth) / ts)) targetY = y;
			break;

		case 1: // left — check left edge
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY) / ts]
					[(x + CollisionBoxX + speed) / ts]
					.isSolid(xLeftSpeed, yTop, 1, CollisionBoxHeight)) targetX = x;
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + CollisionBoxHeight) / ts]
					[(x + CollisionBoxX + speed) / ts]
					.isSolid(xLeftSpeed, yBottom - CollisionBoxHeight, 1, CollisionBoxHeight)) targetX = x;
			if (isWaterTile((y + CollisionBoxY) / ts, (x + CollisionBoxX + speed) / ts)) targetX = x;
			if (isWaterTile((y + CollisionBoxY + CollisionBoxHeight) / ts, (x + CollisionBoxX + speed) / ts)) targetX = x;
			break;

		case 2: // right — check right edge
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY) / ts]
					[(x + CollisionBoxX + CollisionBoxWidth + speed) / ts]
					.isSolid(xRightSpeed, yTop, 1, CollisionBoxHeight)) targetX = x;
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + CollisionBoxHeight) / ts]
					[(x + CollisionBoxX + CollisionBoxWidth + speed) / ts]
					.isSolid(xRightSpeed, yBottom - CollisionBoxHeight, 1, CollisionBoxHeight)) targetX = x;
			if (isWaterTile((y + CollisionBoxY) / ts, (x + CollisionBoxX + CollisionBoxWidth + speed) / ts)) targetX = x;
			if (isWaterTile((y + CollisionBoxY + CollisionBoxHeight) / ts, (x + CollisionBoxX + CollisionBoxWidth + speed) / ts)) targetX = x;
			break;

		case 3: // up — check top edge
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + speed) / ts]
					[(x + CollisionBoxX) / ts]
					.isSolid(xLeft, yTopSpeed, CollisionBoxWidth, 1)) targetY = y;
			if (Main.tilesManager.getTiles()[(y + CollisionBoxY + speed) / ts]
					[(x + CollisionBoxX + CollisionBoxWidth) / ts]
					.isSolid(xRight - CollisionBoxWidth, yTopSpeed, CollisionBoxWidth, 1)) targetY = y;
			if (isWaterTile((y + CollisionBoxY + speed) / ts, (x + CollisionBoxX) / ts)) targetY = y;
			if (isWaterTile((y + CollisionBoxY + speed) / ts, (x + CollisionBoxX + CollisionBoxWidth) / ts)) targetY = y;
			break;
		}
	}

	private boolean isWaterTile(int i, int j) {
		if (i < 0 || i >= Main.tilesManager.getmaxScreenCol() || j < 0 || j >= Main.tilesManager.getmaxScreenRow()) return false;
		return Main.tilesManager.getTiles(i, j).isWater();
	}
	
	public boolean isInTarget() {
		return (x == targetX && y == targetY);
	}
}
