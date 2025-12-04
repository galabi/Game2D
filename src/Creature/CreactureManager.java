package Creature;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class CreactureManager {
	
	
	private static int tickMovingDelay = 0;
	
	static ArrayList<creature> creatureList = new ArrayList<creature>();
	
	public static void CreateCreature(int x,int y, String type) {
		switch (type) {
		case "slime": 
			creatureList.add(new slime(x, y, 32, 32));
			break;
		case "cow": 
			creatureList.add(new cow(x, y, 32, 32));
			break;
		case "sheep": 
			creatureList.add(new sheep(x, y, 32, 32));
			break;
		case "chicken": 
			creatureList.add(new chicken(x, y, 32, 32));
			break;
		default:
			return;
		}
	}
	
	//render all the creatures
	public static void render(Graphics2D g2d) {
		for(creature i :creatureList) {
			i.render(g2d);
		}
	}
	
	public static void tick() {
		tickMovingDelay = (++tickMovingDelay)%2;
		if(tickMovingDelay == 0) {
			for(creature i :creatureList) {
				i.move();
			}
		}
	}
	
	
	
}
