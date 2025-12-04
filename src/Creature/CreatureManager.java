package Creature;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class CreatureManager {
	
	
	private static int tickMovingDelay = 0;
	
	static ArrayList<Creature> creatureList = new ArrayList<Creature>();
	
	public static void CreateCreature(int x,int y, String type) {
		switch (type) {
		case "slime": 
			creatureList.add(new Slime(x, y, 32, 32));
			break;
		case "cow": 
			creatureList.add(new Cow(x, y, 32, 32));
			break;
		case "sheep": 
			creatureList.add(new Sheep(x, y, 32, 32));
			break;
		case "chicken": 
			creatureList.add(new Chicken(x, y, 32, 32));
			break;
		default:
			return;
		}
	}
	
	//render all the creatures
	public static void render(Graphics2D g2d) {
		for(Creature i :creatureList) {
			i.render(g2d);
		}
	}
	
	public static void tick() {
		tickMovingDelay = (++tickMovingDelay)%2;
		if(tickMovingDelay == 0) {
			for(Creature i :creatureList) {
				i.move();
			}
		}
	}
	
	
	
}
