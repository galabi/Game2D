package Creature;

public class Sheep extends Creature{
	
	
	public Sheep(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreactureImage.getSheepImage();
		damage = 0;
		health = 5;
	}

}
