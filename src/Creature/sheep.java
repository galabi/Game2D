package Creature;

public class sheep extends creature{
	
	
	public sheep(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreactureImage.getSheepImage();
		damage = 0;
		health = 5;
	}

}
