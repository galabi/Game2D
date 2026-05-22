package creature;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import MainPackage.Main;
import MainPackage.TilesManager;
import multiplayer.ServerClientHandler;
import playerPackage.Player;

public class CreatureManager {

	private static int tickMovingDelay = 0;
	static final int CreatureSize = 64;
	private static ArrayList<Creature> creatureList = new ArrayList<Creature>();

	public static void createCreature(int x, int y, String type) {
		switch (type) {
		case "slime":   creatureList.add(new Slime(x, y, CreatureSize, CreatureSize));   break;
		case "cow":     creatureList.add(new Cow(x, y, CreatureSize, CreatureSize));     break;
		case "sheep":   creatureList.add(new Sheep(x, y, CreatureSize, CreatureSize));   break;
		case "chicken": creatureList.add(new Chicken(x, y, CreatureSize, CreatureSize)); break;
		}
	}

	public static void render(Graphics2D g2d) {
		for (Creature c : creatureList) {
			c.render(g2d);
		}
	}

	public static void tick() {
		tickMovingDelay = (++tickMovingDelay) % 2;
		if (Main.host) {
			if (tickMovingDelay == 0) {
				for (Creature c : creatureList) {
					c.move();
				}
				sendCreatureSync();
			}
			checkPlayerHit();
			SpawnManager.tick();
		}
	}

	private static void sendCreatureSync() {
		StringBuilder sb = new StringBuilder("creatures:");
		sb.append(" ").append(creatureList.size());
		for (Creature c : creatureList) {
			sb.append(" ").append(c.id)
			  .append(" ").append(c.typeCode)
			  .append(" ").append(c.getX())
			  .append(" ").append(c.getY())
			  .append(" ").append(c.creatureDirection);
		}
		ServerClientHandler.sendDataToServer(sb.toString());
	}

	public static void syncFromNetwork(String[] data) {
		int count = Integer.parseInt(data[1]);
		Set<Integer> receivedIds = new HashSet<>();
		for (int i = 0; i < count; i++) {
			int base = 2 + i * 5;
			int cid  = Integer.parseInt(data[base]);
			int type = Integer.parseInt(data[base + 1]);
			int cx   = Integer.parseInt(data[base + 2]);
			int cy   = Integer.parseInt(data[base + 3]);
			int dir  = Integer.parseInt(data[base + 4]);
			receivedIds.add(cid);
			Creature found = findById(cid);
			if (found != null) {
				found.setX(cx);
				found.setY(cy);
				found.creatureDirection = dir;
			} else {
				Creature nc = createCreatureInternal(type, cx, cy);
				if (nc != null) {
					nc.id = cid;
					creatureList.add(nc);
				}
			}
		}
		creatureList.removeIf(c -> !receivedIds.contains(c.id));
	}

	private static Creature findById(int id) {
		for (Creature c : creatureList) {
			if (c.id == id) return c;
		}
		return null;
	}

	private static Creature createCreatureInternal(int typeCode, int x, int y) {
		switch (typeCode) {
		case 0: return new Slime(x, y, CreatureSize, CreatureSize);
		case 1: return new Cow(x, y, CreatureSize, CreatureSize);
		case 2: return new Sheep(x, y, CreatureSize, CreatureSize);
		case 3: return new Chicken(x, y, CreatureSize, CreatureSize);
		default: return null;
		}
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
		int strength = Main.inventory.getItemInHand().getStrength();
		if (!Main.host) {
			int px = Main.player.getX() + Player.playerCollisionBoxX + Player.playerCollisionBoxWidth  / 2;
			int py = Main.player.getY() + Player.playerCollisionBoxY + Player.playerCollisionBoxHeight / 2;
			Creature nearest = findNearestInRange(
				Main.player.getX() + Player.playerCollisionBoxX,
				Main.player.getY() + Player.playerCollisionBoxY);
			if (nearest == null) return false;
			nearest.applyKnockback(px, py);
			ServerClientHandler.sendDataToServer("attack_creature " + strength);
			return true;
		}
		return attackAt(
			Main.player.getX() + Player.playerCollisionBoxX,
			Main.player.getY() + Player.playerCollisionBoxY,
			strength);
	}

	public static boolean attackCreatureInRange(int playerX, int playerY, int strength) {
		return attackAt(playerX, playerY, strength);
	}

	private static boolean attackAt(int playerX, int playerY, int strength) {
		Creature target = findNearestInRange(playerX, playerY);
		if (target == null) return false;
		target.hitCreature(strength, playerX, playerY);
		if (target.getHealth() <= 0) {
			target.die();
			creatureList.remove(target);
		}
		return true;
	}

	private static Creature findNearestInRange(int playerX, int playerY) {
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
		return target;
	}

	public static ArrayList<Creature> getCreatures() {
		return creatureList;
	}
}
