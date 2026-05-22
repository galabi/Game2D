package creature;

import storage.ItemIds;

public class Chicken extends Creature{


	public Chicken(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreatureImage.getChickenImage();
		damage = 0;
		health = 3;
		lootIds.add(ItemIds.FISH);
	}

}
