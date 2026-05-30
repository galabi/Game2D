package creature;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

import MainPackage.Main;
import storage.ItemIds;

public class Cow extends Creature {

	private final ImageIcon[][] animFrames;
	private int animFrame = 0;
	private long lastAnimTime = System.currentTimeMillis();
	private static final long ANIM_INTERVAL = 400;

	public Cow(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreatureImage.getCowImage();
		animFrames = CreatureImage.getCowAnimImage();
		damage = 0;
		health = 5;
		CollisionBoxX = 12;
		CollisionBoxY = 32;
		CollisionBoxWidth = 40;
		CollisionBoxHeight = 32;
		typeCode = 1;
		lootIds.add(ItemIds.FISH);
	}

	@Override
	public void render(Graphics2D g2d) {
		long now = System.currentTimeMillis();
		if (x != targetX || y != targetY) {
			if (now - lastAnimTime >= ANIM_INTERVAL) {
				animFrame = (animFrame + 1) % 4;
				lastAnimTime = now;
			}
		} else {
			animFrame = 0;
		}

		g2d.setColor(shadowColor);
		g2d.fillOval(x - Main.tilesManager.getCameraX(false) + (sizeX - 44) / 2,
				y - Main.tilesManager.getCameraY(false) + sizeY - 10, 44, 20);
		g2d.drawImage(animFrames[creatureDirection][animFrame].getImage(),
				x - Main.tilesManager.getCameraX(false),
				y - Main.tilesManager.getCameraY(false), sizeX, sizeY, null);

		if (Main.devmode) {
			g2d.setColor(Color.white);
			g2d.drawRect(x + CollisionBoxX - Main.tilesManager.getCameraX(false),
					y + CollisionBoxY - Main.tilesManager.getCameraY(false),
					CollisionBoxWidth, CollisionBoxHeight);
		}
	}

}
