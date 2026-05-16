package creature;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import MainPackage.Main;
import MainPackage.TilesManager;
import playerPackage.Player;

public class CreatureManager {
	
	
	private static int tickMovingDelay = 0;
	
	final static int CreatureSize = 64;
	
	
	static ArrayList<Creature> creatureList = new ArrayList<Creature>();
	
	public static void createCreature(int x,int y, String type) {
		switch (type) {
		case "slime": 
			creatureList.add(new Slime(x, y, CreatureSize, CreatureSize));
			break;
		case "cow": 
			creatureList.add(new Cow(x, y, CreatureSize, CreatureSize));
			break;
		case "sheep": 
			creatureList.add(new Sheep(x, y, CreatureSize, CreatureSize));
			break;
		case "chicken": 
			creatureList.add(new Chicken(x, y, CreatureSize, CreatureSize));
			break;
		default:
			return;
		}
	}
	
	//render all the creatures
	public static void render(Graphics2D g2d) {
		for(Creature i :creatureList) {
			i.render(g2d);
		}
	}
	
	public static void tick() {
		tickMovingDelay = (++tickMovingDelay)%2;
		if(tickMovingDelay == 0) {
			for(Creature i :creatureList) {
				i.move();
			}
		}
		checkPlayerHit();
	}

	private static void checkPlayerHit() {
		Player player = Main.player;
		if (player == null) return;
		Rectangle playerRect = new Rectangle(
			player.getX() + Player.playerCollisionBoxX,
			player.getY() + Player.playerCollisionBoxY,
			Player.playerCollisionBoxWidth,
			Player.playerCollisionBoxHeight
		);
		for (Creature c : creatureList) {
			if (c.getDamage() > 0 && c.getCollisionRect().intersects(playerRect)) {
				player.takeDamage(c.getDamage());
			}
		}
	}
	
	public static boolean attackCreatureInRange() {
		if (Main.player == null) return false;
		int playerX = Main.player.getX() + Player.playerCollisionBoxX;
		int playerY = Main.player.getY() + Player.playerCollisionBoxY;
		int range = TilesManager.tileSize * 2;

		Creature target = null;
		int minDist = Integer.MAX_VALUE;
		for (Creature c : creatureList) {
			Rectangle rect = c.getCollisionRect();
			int cx = rect.x + rect.width / 2;
			int cy = rect.y + rect.height / 2;
			int dist = Math.abs(cx - playerX) + Math.abs(cy - playerY);
			if (dist <= range && dist < minDist) {
				minDist = dist;
				target = c;
			}
		}
		if (target == null) return false;

		int strength = Main.inventory.getItemInHand().getStrength();
		target.hitCreature(strength);
		if (target.getHealth() <= 0) {
			creatureList.remove(target);
		}
		return true;
	}

	public static ArrayList<Creature> getCreatures(){
		return creatureList;
	}
	
	
	
}
