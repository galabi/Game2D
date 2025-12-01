package Creature;

public class chicken extends creature{
	
	
	public chicken(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreactureImage.getChickenImage();
		demage = 0;
		health = 3;
	}

}
