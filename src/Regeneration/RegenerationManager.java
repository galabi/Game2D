package Regeneration;

import java.util.ArrayList;
import java.util.Random;

import Entity.Object;
import MainPackage.Main;
import MainPackage.TilesManager;
import mutiplayer.ServerClientHandler;

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
		objectsMap[rootMapI][rootMapJ].setType(14); 
		objectsMap[rootMapI-1][rootMapJ].setType(2); 
		
		//left tree 
		if(objectsMap[rootMapI][rootMapJ-1].getName().equals("Tree")) {
			objectsMap[rootMapI][rootMapJ-1].setType(17);
			objectsMap[rootMapI-1][rootMapJ-1].setType(5);
		}else {
			objectsMap[rootMapI][rootMapJ-1].setType(13);
			objectsMap[rootMapI-1][rootMapJ-1].setType(1);
		}
		
		//right tree
		if(objectsMap[rootMapI][rootMapJ+1].getName().equals("Tree")) {
			objectsMap[rootMapI][rootMapJ+1].setType(17);
			objectsMap[rootMapI-1][rootMapJ+1].setType(5);
		}else {
			objectsMap[rootMapI][rootMapJ+1].setType(15);
			objectsMap[rootMapI-1][rootMapJ+1].setType(3);
		}
		
		ServerClientHandler.sendDataToServer("update_block " + rootMapJ*TilesManager.tileSize + " " + rootMapI*TilesManager.tileSize + " " + objectsMap[rootMapI][rootMapJ].getId());
		ServerClientHandler.sendDataToServer("update_block " + rootMapJ*TilesManager.tileSize + " " + (rootMapI-1)*TilesManager.tileSize + " " + objectsMap[rootMapI-1][rootMapJ].getId());
		ServerClientHandler.sendDataToServer("update_block " + (rootMapJ+1)*TilesManager.tileSize + " " + rootMapI*TilesManager.tileSize + " " + objectsMap[rootMapI][rootMapJ+1].getId());
		ServerClientHandler.sendDataToServer("update_block " + (rootMapJ+1)*TilesManager.tileSize + " " + (rootMapI-1)*TilesManager.tileSize + " " + objectsMap[rootMapI-1][rootMapJ+1].getId());
		ServerClientHandler.sendDataToServer("update_block " + (rootMapJ-1)*TilesManager.tileSize + " " + rootMapI*TilesManager.tileSize + " " + objectsMap[rootMapI][rootMapJ-1].getId());
		ServerClientHandler.sendDataToServer("update_block " + (rootMapJ-1)*TilesManager.tileSize + " " + (rootMapI-1)*TilesManager.tileSize + " " + objectsMap[rootMapI-1][rootMapJ-1].getId());
		
	}
	
	private static void GrowRock(int rockMapI,int rockMapJ,Object[][] objectsMap) {
		objectsMap[rockMapI][rockMapJ].setType(objectsMap[rockMapI][rockMapJ].getId()-1);
		if(objectsMap[rockMapI][rockMapJ].getName().equals("Rock Max") == false) {
			insertToGrowthList(objectsMap[rockMapI][rockMapJ],rockMapJ*TilesManager.tileSize,rockMapI*TilesManager.tileSize);
		}
		ServerClientHandler.sendDataToServer("update_block " + rockMapJ*TilesManager.tileSize + " " + rockMapI*TilesManager.tileSize + " " + objectsMap[rockMapI][rockMapJ].getId());
	}
	
}
