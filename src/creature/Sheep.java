package creature;

import storage.ItemIds;

public class Sheep extends Creature{


	public Sheep(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreatureImage.getSheepImage();
		damage = 0;
		health = 5;
		lootIds.add(ItemIds.STICK);
	}

}
