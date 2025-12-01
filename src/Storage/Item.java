package Storage;

import java.awt.Image;
import javax.swing.ImageIcon;

import Entity.GameTextures;

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
			name = "Fire pit";
			stackable = false;
			strength = 1;
			placeable = true;
			idToPlace = 5;
			break;
		case 7:
			name = "Tree seed";
			stackable = true;
			strength = 1;
			placeable = true;
			idToPlace = 2;
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
	public boolean IsSolid(){
		return solid;
	}
	public boolean IsPlaceable(){
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
