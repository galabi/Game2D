package creature;

public class Chicken extends Creature{
	
	
	public Chicken(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreactureImage.getChickenImage();
		damage = 0;
		health = 3;
	}

}
