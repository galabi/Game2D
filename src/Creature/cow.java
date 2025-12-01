package Creature;

public class cow extends creature{
	
	
	public cow(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreactureImage.getCowImage();
		demage = 0;
		health = 5;
	}

}
