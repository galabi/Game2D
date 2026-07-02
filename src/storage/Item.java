package storage;

import java.awt.Image;
import javax.swing.ImageIcon;

import entity.GameTextures;

public class Item {
	
	int id;
	int quantity;
	int strength;
	int idToPlace;
	
	String name;
	
	boolean stackable;
	boolean solid;
	boolean placeable;
	
	ImageIcon logo;
	
	public Item(int id) {
		this.id = id;
		setItem(id);
	}
	
	public Item() {
		setItem(0);
	}
	
	public void setItem(int id) {
		this.id = id;
		quantity = 1;
		logo = GameTextures.getItemIcon(id);
		if (logo == null) logo = GameTextures.getItemIcon(0); // blank fallback (e.g. icon not yet in items.png)
		
		switch(id) {
		case 1:
			name = "Wood";
			stackable = true;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		case 2:
			name = "Axe";
			stackable = false;
			strength = 2;
			placeable = false;
			idToPlace = 0;
			break;
		case 3:
			name = "Fishing Rod";
			stackable = false;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		case 4:
			name = "Fish";
			stackable = true;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		case 5:
			name = "Baked Fish";
			stackable = true;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		case 6:
			name = "Campfire";
			stackable = false;
			strength = 1;
			placeable = true;
			idToPlace = 6; // CAMPFIRE object
			break;
		case 7:
			name = "Tree sapling";
			stackable = true;
			strength = 1;
			placeable = true;
			idToPlace = 2; // TREE_SAPLING object
			break;
		case 8:
			name = "Stick";
			stackable = true;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		case 9:
			name = "Stone";
			stackable = true;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		case 10:
			name = "Pickaxe";
			stackable = false;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		case 11:
			name = "Sword";
			stackable = false;
			strength = 2;
			placeable = false;
			idToPlace = 0;
			break;
		case 12:
			name = "Chest";
			stackable = false;
			strength = 1;
			placeable = true;
			idToPlace = 3; // CHEST object
			break;
		case 13:
			name = "Workbench";
			stackable = false;
			strength = 1;
			placeable = true;
			idToPlace = 4; // WORKBENCH object
			break;
		case 14:
			name = "Strong Axe";
			stackable = false;
			strength = 3;
			placeable = false;
			idToPlace = 0;
			break;
		case 15:
			name = "Broad Sword";
			stackable = false;
			strength = 3;
			placeable = false;
			idToPlace = 0;
			break;
		case 16:
			name = "Iron Ore";
			stackable = true;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		case 17:
			name = "Gold Ore";
			stackable = true;
			strength = 1;
			placeable = false;
			idToPlace = 0;
			break;
		default:
			name = " ";
			id = 0;
			quantity = 0;
			strength = 1;
			placeable = false;
			break;
		}

	}
	
	public Item Clone() {
		Item clone = new Item(id);
		clone.quantity = quantity;
		return clone;
	}
	
	public Image getImage() {
		return logo.getImage();
	}
	public void setBlank() {
		setItem(0);
	}
	public boolean isSolid(){
		return solid;
	}
	public boolean isPlaceable(){
		return placeable;
	}
	public int getId() {
		return id;
	}
	public int getStrength() {
		return strength;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getName() {
		return name;
	}
	public boolean isStackable() {
		return stackable;
	}
	public int getIdToPlace() {
		return idToPlace;
	}

	
}
