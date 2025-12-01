package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import MainPackage.Main;

public class Tile extends entity{
	
	int id=0;
	boolean solid,water = false;
	ArrayList<Rectangle> solidInTile;
	
	public Tile(int id,int x, int y, int size) {
		super(x, y, size,size);
		solidInTile = new ArrayList<Rectangle>();
		setType(id);
	}
	
	@Override
	public void render(Graphics2D g2d) {
		super.render(g2d);
		int screenX = Main.tilesManager.getCameraX(false),screenY = Main.tilesManager.getCameraY(false);
		if(Main.devmode) {
			g2d.setColor(Color.white);
			for(Rectangle i: solidInTile) {
				g2d.drawRect(x+i.x - screenX,y+i.y- screenY, i.width, i.height);

			}
		}
	}
	
	public void setType(int id) {
		this.id  = id;
		image = GameTextures.getTileIcon(id);
		solidInTile.clear();
		switch (id) {
		case 0: 
			solid = false;
			water = false;
			break;
		case 1: 
			water = false;
			solid = false;
			break;
		case 2: 
			water = false;
			solid = false;
			break;
		case 3:
			water = false;
			solid = false;
			break;
		case 4:
			water = false;
			solid = false;
			break;
		case 5:
			water = false;
			solid = false;
			break;
		case 6:
			water = false;
			solid = false;
			break;
		case 7:
			water = false;
			solid = false;
			break;
		case 8:
			water = false;
			solid = false;
			break;
		case 9:
			water = false;
			solid = false;
			break;
		case 10:
			water = false;
			solid = false;
			break;
		case 11:
			water = false;
			solid = false;
			break;
		case 12:
			water = false;
			solid = false;
			break;
		case 13:
			water = false;
			solid = false;
			break;
		case 14:
			water = false;
			solid = false;
			break;
		case 15:
			water = false;
			solid = false;
			break;
		case 16:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 17:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 18:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(20 ,20 ,44 ,44));
			break;
		case 19:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,20 ,64 ,44));
			break;
		case 20:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,20 ,44 ,44));
			break;
		case 21:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(20 ,0 ,44 ,64));
			break;
		case 22:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,44 ,64));
			break;
		case 23:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(20 ,0 ,44 ,44));
			break;
		case 24:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,44));
			break;
		case 25:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,44 ,44));
			break;
		case 26:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,44));
			solidInTile.add(new Rectangle(0 ,0 ,44 ,64));
			break;
		case 27:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,44));
			solidInTile.add(new Rectangle(20 ,0 ,44 ,64));
			break;
		case 28:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,44 ,64));
			solidInTile.add(new Rectangle(0 ,20 ,64 ,44));
			break;
		case 29:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(20 ,0 ,44 ,64));
			solidInTile.add(new Rectangle(0 ,20 ,64 ,44));
			break;
		case 30:
			water = false;
			solid = false;
			break;
		case 31:
			water = false;
			solid = false;
			
			break;
		case 32:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,30 ,64));
			solidInTile.add(new Rectangle(0 ,0 ,64 ,56));
			break;
		case 33:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,56));
			break;
		case 34:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,56));
			solidInTile.add(new Rectangle(34 ,0 ,30 ,64));
			break;
		case 35:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,47 ,30 ,17));
			break;
		case 36:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,30 ,64));
			break;
		case 37:
			water = false;
			solid = false;
			break;
		case 38:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(34 ,0 ,30 ,64));
			break;
		case 39:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(34 ,47 ,30 ,17));
			break;
		case 40:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,30 ,64));
			solidInTile.add(new Rectangle(0 ,46 ,64 ,18));
			break;
		case 41:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,46 ,64 ,18));
			break;
		case 42:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,46 ,64 ,18));
			solidInTile.add(new Rectangle(34 ,0 ,30 ,64));
			break;
		case 43:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(36 ,0 ,30 ,56));
			break;
		case 44:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,30 ,56));
			break;
		case 45:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 46:
			water = false;
			solid = false;
			break;
		case 47:
			water = false;
			solid = false;
			break;
		case 48:
			water = false;
			solid = false;
			break;
		case 49:
			water = false;
			solid = false;
			break;
		case 50:
			water = false;
			solid = false;
			break;
		case 51:
			water = false;
			solid = false;
			break;
		case 52:
			water = false;
			solid = false;
			break;
		case 53:
			water = false;
			solid = false;
			break;
		case 54:
			water = false;
			solid = false;
			break;
		case 55:
			water = false;
			solid = false;
			break;
		case 56:
			water = false;
			solid = false;
			break;
		case 57:
			water = false;
			solid = false;
			break;
		case 58:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(23 ,0 ,42 ,11));
			solidInTile.add(new Rectangle(23 ,59 ,42 ,6));
			solidInTile.add(new Rectangle(9 ,7 ,17 ,7));
			solidInTile.add(new Rectangle(9 ,48 ,18 ,12));
			break;
		case 59:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,11));
			solidInTile.add(new Rectangle(0 ,59 ,64 ,6));
			break;
		case 60:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,43 ,11));
			solidInTile.add(new Rectangle(0 ,59 ,42 ,6));
			solidInTile.add(new Rectangle(43 ,47 ,15 ,14));
			solidInTile.add(new Rectangle(43 ,7 ,15 ,7));
			break;
		}
	}
	
	public boolean isSolid(int playerXInTile, int playerYInTile,int width,int height) {
		Rectangle r = new Rectangle(playerXInTile,playerYInTile,width,height);
		
		if(!Main.tilesManager.getObjects()[y/sizeY][x/sizeX].solid) {
			for(Rectangle i: solidInTile) {
				if(i.intersects(r))return true;
			}
		}
		for(Rectangle i : ((Object) Main.tilesManager.getObjects()[y/sizeY][x/sizeX]).getRectangles()) {
			if(i.intersects(r)) {
				if(((Object) Main.tilesManager.getObjects()[y/sizeY][x/sizeX]).isGate()) {
					Main.tilesManager.readFile();
				}
				
				return true;
			}
		}
		return false;
	}
	
	public int getId(){
		return id;
	}
	public boolean IsSolid() {
		return solid;
	}
	public ArrayList<Rectangle> getRectangles(){
		return solidInTile;
	}
	
	public boolean isWater() {
		return water;
	}
	public boolean isWater(int mouseX,int mouseY) {
		if(!water) return false;
		
		int mouseXInTile = (Main.tilesManager.getCameraX(false) + mouseX)%sizeX;
		int mouseYInTile = (Main.tilesManager.getCameraY(false) + mouseY)%sizeY;
				
		for(Rectangle i : solidInTile) {
			if(i.contains(mouseXInTile, mouseYInTile)) {
				return true;
			}
		}
		return false;
	}

}
