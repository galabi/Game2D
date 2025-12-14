package creature;

public class Slime extends Creature{
	
	
	public Slime(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreactureImage.getSlimeImage();
		damage = 1;
		health = 5;
	}

}
