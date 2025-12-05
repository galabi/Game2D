package Entity;

import java.awt.Graphics2D;

import MainPackage.Main;
import MainPackage.TilesManager;
import Storage.Item;

public class ItemOnFloor {
	static int tileSize = TilesManager.tileSize;
	int tileX,tileY;
	Item item;
	
	public ItemOnFloor(Item item,int tileX,int tileY) {
		this.item = item;
		this.tileX = tileX;
		this.tileY = tileY;
	}
	
	public void render(Graphics2D g2d) {
		g2d.drawImage(item.getImage(), (tileX*tileSize)-Main.tilesManager.getCameraX(false),(tileY*tileSize)-Main.tilesManager.getCameraY(false),tileSize,tileSize, null);
		
	}
	
	public int getTileX() {
		return tileX;
	}
	
	public int getTileY() {
		return tileY;
	}
	public Item getItem() {
		return item;
	}
}
