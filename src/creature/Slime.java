package creature;

public class Slime extends Creature{
	
	
	public Slime(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreatureImage.getSlimeImage();
		damage = 1;
		health = 5;
	}

}
