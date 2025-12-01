package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import MainPackage.Main;

public class Object extends Tile{
	ImageIcon frontImg;
	boolean gate, breakable;
	ArrayList<Integer> itemWhenBroken;
	
	public Object(int id, int x, int y, int size) {
		super(id, x, y, size);
	}
	
	public void drawFront(Graphics2D g2d) {
		int screenX = Main.tilesManager.getCameraX(false),screenY = Main.tilesManager.getCameraY(false);
		try {
			g2d.drawImage(frontImg.getImage(), x-screenX,y-screenY, sizeX,sizeY,null);
		} catch (Exception e) {
			System.out.println(x/sizeX +" "+y/sizeY+" "+id);
		}
	}
	
	
	public void setType(int id) {
		this.id  = id;
		image = GameTextures.getObjectIcon(id);
		frontImg = GameTextures.getFrontObjectIcon(id);
		solidInTile.clear();
		gate = false;
		breakable = false;
		itemWhenBroken = new ArrayList<Integer>();
		itemWhenBroken.clear();
		
		switch (id) {
		case 0: 
			solid = false;
			water = false;
			break;
		case 1: 
			solid = true;
			water = false;
			breakable = true;
			itemWhenBroken.add(1);
			itemWhenBroken.add(7);
			solidInTile.add(new Rectangle(26 ,40 ,14 ,22));
			break;
		case 2: 
			water = false;
			solid = false;
			itemWhenBroken.add(7);
			breakable = true;
			break;
		case 3:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(20 ,60 ,21 ,4));
			break;
		case 4:
			water = false;
			solid = true;
			breakable = true;
			itemWhenBroken.add(6);
			solidInTile.add(new Rectangle(20 ,20 ,27 ,27));
			break;
		case 5:
			water = false;
			solid = true;
			breakable = true;
			itemWhenBroken.add(6);
			solidInTile.add(new Rectangle(20 ,20 ,27 ,27));
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
			solid = true;
			solidInTile.add(new Rectangle(16 ,0 ,49 ,64));
			break;
		case 13:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 14:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,49 ,64));
			break;
		case 15:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(16 ,0 ,49 ,64));
			break;
		case 16:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 17:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,49 ,64));
			break;
		case 18:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(16 ,0 ,49 ,34));
			break;
		case 19:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,34));
			break;
		case 20:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,49 ,34));
			break;
		case 21:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(16 ,0 ,49 ,34));
			break;
		case 22:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,34));
			break;
		case 23:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,49 ,34));
			break;
		case 24:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(26 ,42 ,13 ,10));
			break;
		case 25:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,6));
			solidInTile.add(new Rectangle(23 ,49 ,42 ,15));
			solidInTile.add(new Rectangle(10 ,48 ,18 ,12));
			break;
		case 26:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,6));
			solidInTile.add(new Rectangle(0 ,49 ,64 ,15));
			break;
		case 27:
			water = true;
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,6));
			solidInTile.add(new Rectangle(0 ,49 ,42 ,15));
			solidInTile.add(new Rectangle(43 ,48 ,15 ,12));
			break;
		case 28:
			water = false;
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(20 ,20 ,27 ,27));
			break;
		case 29:
			water = false;
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(26 ,52 ,14 ,12));
			break;
		case 30:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(24 ,24 ,17 ,15));
			break;
		case 31:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(21 ,2 ,20 ,25));
			itemWhenBroken.add(9);
			breakable = true;
			break;
		case 32:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(21 ,1 ,20 ,25));
			breakable = true;
			itemWhenBroken.add(9);
			break;
		case 33:
			water = false;
			solid = true;
			solidInTile.add(new Rectangle(18 ,1 ,38 ,41));
			breakable = true;
			itemWhenBroken.add(9);
			break;
		}
	}
	public boolean isBreakable(){
		return breakable;
	}
	
	public ArrayList<Integer> getItemWhenBroken(){
		return itemWhenBroken;
	}
	
	public boolean isGate() {
	return gate;	
	}
	
}
