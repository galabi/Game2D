package Creature;

public class slime extends creature{
	
	
	public slime(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreactureImage.getSlimeImage();
		demage = 1;
		health = 5;
	}

}
