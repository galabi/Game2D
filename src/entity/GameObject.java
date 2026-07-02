package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import MainPackage.Main;
import mapRender.ObjectPropertiesManager;

public class GameObject extends Tile{

	private final ArrayList<Rectangle> cachedObjectSolidAreas = new ArrayList<>();

	public GameObject(int id, int x, int y, int size) {
		super(id, x, y, size);
		refreshSolidCache(id);
	}

	// Called by MapGenerator (and any other code) that changes the object id after construction.
	@Override
	public void setId(int id) {
		super.setId(id);
		refreshSolidCache(id);
	}

	private void refreshSolidCache(int id) {
		cachedObjectSolidAreas.clear();
		var props = ObjectPropertiesManager.getObject(id);
		if (props != null) cachedObjectSolidAreas.addAll(props.getSolidInTile());
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
			g2d.setColor(Color.BLACK);
			for (Rectangle r : cachedObjectSolidAreas) {
				g2d.drawRect(x - screenX + r.x - sizeX / 2, y - screenY + r.y - sizeY / 2, r.width, r.height);
			}
		}
	}

}
