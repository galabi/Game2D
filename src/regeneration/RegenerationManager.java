package regeneration;

import java.util.PriorityQueue;
import java.util.Random;

import MainPackage.Main;
import MainPackage.TilesManager;
import entity.GameObject;
import mapRender.ObjectIds;
import mapRender.ObjectPropertiesManager;

public class RegenerationManager {
	
	static PriorityQueue<RegenerationBlock> regenerationList = new PriorityQueue<>();

	public static void tick() {
		while(!regenerationList.isEmpty() && regenerationList.peek().isReadyToGrow()) {
				
			RegenerationBlock temp = regenerationList.poll();
			
			if(temp.getGrowthType() == GrowthType.TREE){
				GrowTree(temp.getMapI(), temp.getMapJ(), Main.tilesManager.getObjects());
			}else if(temp.getGrowthType() == GrowthType.ROCK) {
				GrowRock(temp.getMapI(), temp.getMapJ(), Main.tilesManager.getObjects());
			}
		}
	}
	
	//add the object to the growth list;
	public static void insertToGrowthList(GameObject nextObject, int x, int y) {
		int id = nextObject.getId();

		GrowthType growthType = null;
		if (id == ObjectIds.TREE_SAPLING) {
			growthType = GrowthType.TREE;
		} else if (ObjectIds.isOre(id) && !ObjectIds.isOreMax(id)) {
			growthType = GrowthType.ROCK;
		} else {
			return; // nothing to regrow
		}
		regenerationList.add(new RegenerationBlock(nextObject, x, y, growthType));
	}
	
	public static long getNextGrowthTime() {
		return System.currentTimeMillis() + new Random().nextInt(30000,50000);
	}
	
	public static void resetList() {
		regenerationList.clear();
	}
	
	
	private static void GrowTree(int rootMapI,int rootMapJ,GameObject[][] objectsMap) {
    	// single-tile tree: sapling matures into a tree in place
    	Main.tilesManager.updateBlock(rootMapI, rootMapJ, ObjectIds.TREE);
	}

	private static void GrowRock(int rockMapI,int rockMapJ,GameObject[][] objectsMap) {
    	// grow the ore back one stage toward its MAX (mining advanced it with +1)
    	Main.tilesManager.updateBlock(rockMapI, rockMapJ, objectsMap[rockMapI][rockMapJ].getId()-1);

		if(!ObjectIds.isOreMax(objectsMap[rockMapI][rockMapJ].getId())) {
			insertToGrowthList(objectsMap[rockMapI][rockMapJ],rockMapJ*TilesManager.tileSize,rockMapI*TilesManager.tileSize);
		}

	}
}
