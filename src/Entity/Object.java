package Entity;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Object extends Tile{
	boolean gate, breakable,topPart = false;
	ArrayList<Integer> itemWhenBroken;
	String objectName = "";
	
	public Object(int id, int x, int y, int size) {
		super(id, x, y, size);
		setType(id);
	}
	
	
	public void setType(int id) {
		this.id  = id;
		image = GameTextures.getObjectIcon(id);
		solidInTile.clear();
		gate = false;
		water = false;
		breakable = false;
		itemWhenBroken = new ArrayList<Integer>();
		itemWhenBroken.clear();
		
		switch (id) {
		case 0: 
			solid = false;
			break;
		case 1: 
			objectName = "Tree";
			solid = false;
			topPart = true;
			break;
		case 2: 
			objectName = "Tree";
			water = false;
			topPart = true;
			break;
		case 3:
			objectName = "Tree";
			solid = false;
			topPart = true;
			break;
		case 4:
			objectName = "Tree sapling head";
			solid = false;
			break;
		case 5:
			objectName = "Tree";
			solid = false;
			topPart = true;
			break;
		case 6:
			objectName = "Street lamp";
			solid = false;
			topPart = true;
			break;
		case 7:
			objectName = "Campfire on";
			solid = true;
			breakable = true;
			solidInTile.add(new Rectangle(6 ,6 ,48 ,48));
			itemWhenBroken.add(6);
			break;
		case 8:
			objectName = "Well";
			solid = true;
			solidInTile.add(new Rectangle(10 ,46 ,46 ,20));
			break;
		case 9:
			objectName = "Up ladder";
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(0 ,60 ,64 ,4));
			break;
		case 10:
			objectName = "Down ladder";
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(0 ,24 ,40 ,40));
			break;
		case 11:
			objectName = "Down ladder";
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(24 ,24 ,40 ,40));
			break;
		case 12:
			objectName = "Garbage container";
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(0 ,23 ,40 ,40));
			break;
		case 13:
			objectName = "Tree";
			solid = false;
			break;
		case 14:
			objectName = "Tree";
			solid = true;
			itemWhenBroken.add(1);
			itemWhenBroken.add(7);
			breakable = true;
			solidInTile.add(new Rectangle(12 ,20 ,40 ,42));
			break;
		case 15:
			objectName = "Tree";
			solid = false;
			break;
		case 16:
			objectName = "Tree sapling";
			solid = false;
			breakable = true;
			itemWhenBroken.add(7);
			break;
		case 17:
			objectName = "Tree";
			solid = false;
			break;
		case 18:
			objectName = "Street lamp";
			solid = true;
			solidInTile.add(new Rectangle(16 ,52 ,36 ,12));
			break;
		case 19:
			objectName = "Campfire";
			solid = true;
			breakable = true;
			solidInTile.add(new Rectangle(6 ,6 ,58 ,58));
			itemWhenBroken.add(6);
			break;
		case 20:
			objectName = "Well";
			solid = true;
			solidInTile.add(new Rectangle(10 ,0 ,46 ,30));
			break;
		case 21:
			objectName = "Up ladder";
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(12 ,0 ,44 ,56));
			break;
		case 22:
			objectName = "Down ladder";
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(26 ,0 ,40 ,40));
			break;
		case 23:
			objectName = "Down ladder";
			solid = true;
			gate = true;
			solidInTile.add(new Rectangle(0 ,0 ,40 ,40));
			break;
		case 24:
			objectName = "House 1";
			solid = false;
			break;
		case 25:
			objectName = "House 1";
			solid = false;
			break;
		case 26:
			objectName = "House 1";
			solid = false;
			break;
		case 27:
			objectName = "House 1";
			solid = false;
			break;
		case 28:
			objectName = "House 1";
			solid = false;
			break;
		case 29:
			objectName = "House 1";
			solid = false;
			break;
		case 30:
			objectName = "House 2";
			solid = false;
			break;
		case 31:
			objectName = "House 2";
			solid = false;
			break;
		case 32:
			objectName = "House 2";
			solid = false;
			break;
		case 33:
			objectName = "House 2";
			solid = false;
			break;
		case 34:
			objectName = "House 2";
			solid = false;
			break;
		case 35:
			objectName = "House 2";
			solid = false;
			break;
		case 36:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(30 ,16 ,34 ,48));
			solidInTile.add(new Rectangle(38 ,0 ,26 ,16));
			topPart = true;
			break;
		case 37:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 38:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 39:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 40:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 41:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,26 ,18));
			solidInTile.add(new Rectangle(0 ,18 ,34 ,48));
			topPart = true;
			break;
		case 42:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(30 ,16 ,34 ,48));
			solidInTile.add(new Rectangle(38 ,0 ,26 ,16));
			topPart = true;
			break;
		case 43:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 44:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 45:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 46:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 47:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,26 ,18));
			solidInTile.add(new Rectangle(0 ,18 ,34 ,48));
			topPart = true;
			break;
		case 48:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(30 ,0 ,34 ,64));
			break;
		case 49:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 50:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 51:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 52:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 53:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,34 ,64));
			break;
		case 54:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(32 ,0 ,34 ,64));
			break;
		case 55:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 56:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 57:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 58:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,64));
			break;
		case 59:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,34 ,64));
			break;
		case 60:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(30 ,0 ,34 ,50));
			break;
		case 61:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,50));
			solidInTile.add(new Rectangle(42 ,50 ,18 ,10));
			break;
		case 62:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,50));
			solidInTile.add(new Rectangle(0 ,50 ,20 ,10));
			break;
		case 63:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,50));
			break;
		case 64:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,50));
			break;
		case 65:
			objectName = "House 1";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,34 ,50));
			break;
		case 66:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(32 ,0 ,34 ,50));
			break;
		case 67:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,50));
			solidInTile.add(new Rectangle(62 ,50 ,4 ,10));
			break;
		case 68:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,50));
			solidInTile.add(new Rectangle(0 ,50 ,6 ,10));
			break;
		case 69:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,50));
			break;
		case 70:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,64 ,50));
			break;
		case 71:
			objectName = "House 2";
			solid = true;
			solidInTile.add(new Rectangle(0 ,0 ,34 ,50));
			break;
		case 72:
			objectName = "Rock Max";
			solid = true;
			breakable = true;
			solidInTile.add(new Rectangle(16 ,20 ,36 ,30));
			itemWhenBroken.add(9);
			break;
		case 73:
			objectName = "Rock";
			solid = true;
			breakable = true;
			solidInTile.add(new Rectangle(16 ,20 ,36 ,30));
			itemWhenBroken.add(9);
			break;
		case 74:
			objectName = "Rock Min";
			solid = true;
			breakable = true;
			solidInTile.add(new Rectangle(16 ,20 ,36 ,30));
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
	
	public String getName() {
		return objectName;
	}
	
	public boolean isTopPart() {
		return topPart;
	}
}
