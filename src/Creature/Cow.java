package Creature;

public class Cow extends Creature{
	
	
	public Cow(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreactureImage.getCowImage();
		damage = 0;
		health = 5;
		CollisionBoxX = 11;
		CollisionBoxY = 2;
		CollisionBoxWidth = 13;
		CollisionBoxHeight = 30;
	}

}
