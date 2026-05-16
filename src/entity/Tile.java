package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;


import MainPackage.Main;
import mapRender.MapObject;
import mapRender.ObjectPropertiesManager;
import mapRender.TilePropertiesManager;

public class Tile extends Entity{
	
	int id;
	
	public Tile(int id,int x, int y, int size) {
		super(x, y, size,size);
		this.id = id;
	}
	
	@Override
	public void render(Graphics2D g2d) {
		int screenX = Main.tilesManager.getCameraX(false),screenY = Main.tilesManager.getCameraY(false);
		g2d.drawImage(GameTextures.getTileIcon(id).getImage(), x-screenX,y-screenY, sizeX,sizeY,null);
		if(Main.devmode) {
			g2d.setColor(Color.white);
			ArrayList<Rectangle> solidInTile = TilePropertiesManager.getTile(id).getSolidInTile();
			g2d.drawRect(x-screenX, y-screenY, sizeX,sizeY);
			for(Rectangle i: solidInTile) {
				g2d.drawRect(x+i.x - screenX,y+i.y- screenY, i.width, i.height);
			}
		}
	}
	
	public boolean isSolid(int playerXInTile, int playerYInTile,int width,int height) {
		Rectangle r = new Rectangle(playerXInTile,playerYInTile,width,height);
		MapObject topObj = ObjectPropertiesManager.getObject(Main.tilesManager.getObjects(y/sizeY,x/sizeX).id);
		ArrayList<Rectangle> solidInTile = TilePropertiesManager.getTile(id).getSolidInTile();

		if(!topObj.IsSolid()) {
			for(Rectangle i: solidInTile) {
				if(i.intersects(r))return true;
			}
		}
		for(Rectangle i : topObj.getSolidInTile()) {
			if(i.intersects(r)) {
				return true;
			}
		}
		return false;
	}

	public void triggerGate() {
		MapObject topObj = ObjectPropertiesManager.getObject(Main.tilesManager.getObjects(y/sizeY, x/sizeX).id);
		if(topObj.isGate()) {
			Main.tilesManager.readFile();
		}
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

}
