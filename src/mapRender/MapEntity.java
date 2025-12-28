package mapRender;


import java.awt.Rectangle;
import java.util.ArrayList;

import MainPackage.TilesManager;

public class MapEntity {
	
	int id;
	boolean solid;
	ArrayList<Rectangle> solidInTile = new ArrayList<Rectangle>();
	final static int size = TilesManager.tileSize;
	
	public MapEntity() {
		
	}
	
	public MapEntity(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean IsSolid() {
		return solid;
	}
	
	public ArrayList<Rectangle> getSolidInTile(){
		return solidInTile;
	}
}
