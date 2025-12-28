package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import MainPackage.Main;
import mapRender.ObjectPropertiesManager;

public class GameObject extends Tile{

	public GameObject(int id, int x, int y, int size) {
		super(id, x, y, size);
	}
	
	@Override
	public void render(Graphics2D g2d) {
		int screenX = Main.tilesManager.getCameraX(false),screenY = Main.tilesManager.getCameraY(false);
		g2d.drawImage(GameTextures.getObjectIcon(id).getImage(), x-screenX,y-screenY, sizeX,sizeY,null);
		if(Main.devmode) {
			g2d.setColor(Color.white);
			ArrayList<Rectangle> solidInTile = ObjectPropertiesManager.getObject(id).getSolidInTile();
			g2d.drawRect(x-screenX, y-screenY, sizeX,sizeY);
			for(Rectangle i: solidInTile) {
				g2d.drawRect(x+i.x - screenX,y+i.y- screenY, i.width, i.height);

			}
		}
	}
	
}
