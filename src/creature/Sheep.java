package creature;

import storage.ItemIds;

public class Sheep extends Creature{


	public Sheep(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreatureImage.getSheepImage();
		damage = 0;
		health = 5;
		typeCode = 2;
		CollisionBoxX = 12;
		CollisionBoxY = 32;
		CollisionBoxWidth = 40;
		CollisionBoxHeight = 32;
		lootIds.add(ItemIds.STICK);
	}

}
