package regeneration;

import java.util.PriorityQueue;
import java.util.Random;

import MainPackage.Main;
import mapRender.ObjectIds;

public class RegenerationManager {

	static PriorityQueue<RegenerationBlock> regenerationList = new PriorityQueue<>();

	public static void tick() {
		while(!regenerationList.isEmpty() && regenerationList.peek().isReadyToGrow()) {

			RegenerationBlock temp = regenerationList.poll();

			if(temp.getGrowthType() == GrowthType.TREE){
				GrowTree(temp.getMapI(), temp.getMapJ());
			}else if(temp.getGrowthType() == GrowthType.ROCK) {
				GrowRock(temp.getMapI(), temp.getMapJ());
			}
		}
	}

	//add the object at (mapI,mapJ) to the growth list, keyed by its id
	public static void insertToGrowthList(int id, int mapI, int mapJ) {
		GrowthType growthType;
		if (id == ObjectIds.TREE_SAPLING) {
			growthType = GrowthType.TREE;
		} else if (ObjectIds.isOre(id) && !ObjectIds.isOreMax(id)) {
			growthType = GrowthType.ROCK;
		} else {
			return; // nothing to regrow
		}
		regenerationList.add(new RegenerationBlock(mapI, mapJ, growthType));
	}

	public static long getNextGrowthTime() {
		return System.currentTimeMillis() + new Random().nextInt(30000,50000);
	}

	public static void resetList() {
		regenerationList.clear();
	}


	private static void GrowTree(int rootMapI,int rootMapJ) {
    	// single-tile tree: sapling matures into a tree in place
    	Main.tilesManager.updateBlock(rootMapI, rootMapJ, ObjectIds.TREE);
	}

	private static void GrowRock(int rockMapI,int rockMapJ) {
    	// grow the ore back one stage toward its MAX (mining advanced it with +1)
    	int newId = Main.tilesManager.getObjectId(rockMapI, rockMapJ) - 1;
    	Main.tilesManager.updateBlock(rockMapI, rockMapJ, newId);
    	// keep regrowing until it reaches MAX (insert self-filters maxed ores)
    	insertToGrowthList(newId, rockMapI, rockMapJ);
	}
}
