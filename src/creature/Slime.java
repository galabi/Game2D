package creature;

public class Slime extends Creature{
	
	
	public Slime(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreatureImage.getSlimeImage();
		damage = 1;
		health = 5;
		CollisionBoxX = 12;
		CollisionBoxY = 32;
		CollisionBoxWidth = 40;
		CollisionBoxHeight = 32;
	}

}
