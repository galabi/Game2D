	package creature;

public class Cow extends Creature{
	
	
	public Cow(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreatureImage.getCowImage();
		damage = 0;
		health = 5;
		CollisionBoxX = 12;
		CollisionBoxY = 32;
		CollisionBoxWidth = 40;
		CollisionBoxHeight = 32;
	}

}
