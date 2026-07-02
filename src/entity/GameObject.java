package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import MainPackage.Main;
import mapRender.ObjectPropertiesManager;

public class GameObject extends Tile{

	public GameObject(int id, int x, int y, int size) {
		super(id, x, y, size);
	}

	@Override
	public void render(Graphics2D g2d) {
		int screenX = Main.tilesManager.getCameraX(false), screenY = Main.tilesManager.getCameraY(false);
		var icon = GameTextures.getObjectIcon(id);
		if (icon == null) return;
		// The marching-squares visual tile for data cell (i,j) is centered on the cell's
		// top-left corner, so it sits half a tile up-left of the data grid. Shift the sprite
		// by tileSize/2 up-left so it lands on the rendered terrain instead of the data cell.
		// (getSortY() already assumes this shift.)
		int drawX = x - screenX - icon.getIconWidth();
		int drawY = y - screenY + sizeY / 2 - icon.getIconHeight() * 2;
		g2d.drawImage(icon.getImage(), drawX, drawY, icon.getIconWidth() * 2, icon.getIconHeight() * 2, null);
		if (Main.devmode) {
			// Solid rects come straight from object properties (no per-instance cache — most of
			// the map's objects are empty and don't need one). Drawn in the same shifted frame.
			g2d.setColor(Color.BLACK);
			for (Rectangle r : ObjectPropertiesManager.getObject(id).getSolidInTile()) {
				g2d.drawRect(x - screenX + r.x - sizeX / 2, y - screenY + r.y - sizeY / 2, r.width, r.height);
			}
		}
	}

}
