package mapRender;


import java.awt.Rectangle;
import java.util.ArrayList;

import MainPackage.Main;
import MainPackage.TilesManager;

public class MapTile extends MapEntity{
	boolean water;
	
	public MapTile() {

	}
	
	public MapTile(int id) {
		super(id);
	}
	
	
	public ArrayList<Rectangle> getRectangles(){
		return solidInTile;
	}
	
	public boolean isWater() {
		return water;
	}
	public boolean isWater(int mouseX,int mouseY) {
		if(!water) return false;
		
		int mouseXInTile = (Main.tilesManager.getCameraX(false) + mouseX)%TilesManager.tileSize;
		int mouseYInTile = (Main.tilesManager.getCameraY(false) + mouseY)%TilesManager.tileSize;
				
		for(Rectangle i : solidInTile) {
			if(i.contains(mouseXInTile, mouseYInTile)) {
				return true;
			}
		}
		return false;
	}
	

}
