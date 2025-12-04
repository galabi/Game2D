package Creature;

public class cow extends creature{
	
	
	public cow(int x, int y, int sizeX, int sizeY) {
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
