package Regeneration;

import java.util.ArrayList;
import java.util.Random;

import Entity.Object;
import MainPackage.Main;
import MainPackage.TilesManager;

public class RegenerationManager {
	
	static ArrayList<RegenerationBlock> regenerationList = new ArrayList<>();
	
	public static void tick() {
		if(regenerationList.isEmpty()) return;
			
		RegenerationBlock temp = regenerationList.get(0);
		
		if(temp.isReadyToGrow()) {
			if(temp.getTargetGrow().equals("Tree")){
				GrowTree(temp.getMapI(), temp.getMapJ(), Main.tilesManager.getObjects());
			}else if(temp.getTargetGrow().equals("Rock Max")) {
				GrowRock(temp.getMapI(), temp.getMapJ(), Main.tilesManager.getObjects());
			}
			
			regenerationList.remove(0);
			
		}
		
	}
	
	//add the object to the growth list;
	public static void insertToGrowthList(Object nextObject,int x,int y) {
		String targetGrow = "";
		
		//tree case
		if(nextObject.getName().equals("Tree sapling")) {
			targetGrow = "Tree";
		}else if(nextObject.getName().equals("Rock")) {
			targetGrow = "Rock Max";
		}
		regenerationList.add(new RegenerationBlock(nextObject,x,y,targetGrow));
	}
	
	public static long getNextGowthTime() {
		if(regenerationList.isEmpty()) {
			return System.currentTimeMillis() + new Random().nextInt(30000,50000);
		}
		return regenerationList.get(regenerationList.size()-1).getGrowthTime()+ new Random().nextInt(10000,30000);
	}
	
	public static void resetList() {
		regenerationList.clear();
	}
	
	
	private static void GrowTree(int rootMapI,int rootMapJ,Object[][] objectsMap) {
		//tree base
    	Main.tilesManager.updateBlock(rootMapI, rootMapJ, 14);
    	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ, 2);
		
		//left tree 
		if(objectsMap[rootMapI][rootMapJ-1].getName().equals("Tree")) {
        	Main.tilesManager.updateBlock(rootMapI, rootMapJ-1, 17);
        	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ-1, 5);

		}else {
        	Main.tilesManager.updateBlock(rootMapI, rootMapJ-1, 13);
        	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ-1, 1);
		}
		
		//right tree
		if(objectsMap[rootMapI][rootMapJ+1].getName().equals("Tree")) {
			System.out.println("sss");
        	Main.tilesManager.updateBlock(rootMapI, rootMapJ+1, 17);
        	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ+1, 5);
		}else {
        	Main.tilesManager.updateBlock(rootMapI, rootMapJ+1, 15);
        	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ+1, 3);
		}
		
	}
	
	private static void GrowRock(int rockMapI,int rockMapJ,Object[][] objectsMap) {
    	Main.tilesManager.updateBlock(rockMapI, rockMapJ, objectsMap[rockMapI][rockMapJ].getId()-1);
    	
		if(objectsMap[rockMapI][rockMapJ].getName().equals("Rock Max") == false) {
			insertToGrowthList(objectsMap[rockMapI][rockMapJ],rockMapJ*TilesManager.tileSize,rockMapI*TilesManager.tileSize);
		}
	
	}
}
