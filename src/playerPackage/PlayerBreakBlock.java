package playerPackage;

import java.util.ArrayList;

import MainPackage.Main;
import MainPackage.TilesManager;
import regeneration.RegenerationManager;
import mapRender.ObjectIds;
import mapRender.ObjectPropertiesManager;

public class PlayerBreakBlock {
	
	public static void breakBlock() {
	    long timeDelta = System.currentTimeMillis() - PlayerInteraction.timeOfFirstHitToObject;
	    int objId = PlayerInteraction.objectToBreakId;
	    int i = PlayerInteraction.objectI;
	    int j = PlayerInteraction.objectJ;
	    int handItemId = Main.inventory.getItemInHand().getId();
	    ArrayList<Integer> itemWhenBroken = ObjectPropertiesManager.getObject(objId).getItemWhenBroken();
	    
	    if (objId == ObjectIds.ROCK || objId == ObjectIds.ROCK_MAX) {
	        boolean canBreak = (handItemId == 10 && timeDelta >= PlayerInteraction.BreakTime / 2)
	                         || timeDelta >= PlayerInteraction.rockBreakTime;
	        if (canBreak) {
	            Main.tilesManager.updateBlock(i, j, objId + 1);
	            RegenerationManager.insertToGrowthList(Main.tilesManager.getObjects()[i][j], j * TilesManager.tileSize, i * TilesManager.tileSize);
	            
	            for(Integer k:itemWhenBroken) {
	            	Main.tilesManager.addDrop(i, j, k);
	            }
	            resetInteraction();
	        }
	    } else if (objId == ObjectIds.TREE_TRUNK) {
	        boolean canBreak = (handItemId == 2 && timeDelta >= PlayerInteraction.BreakTime / 2)
	        		|| timeDelta >= PlayerInteraction.BreakTime;
            if (canBreak) {
            	for(Integer k:itemWhenBroken) {
            		Main.tilesManager.addDrop(i, j, k);
            		}
            	cutTree(i,j);
            	resetInteraction();
            	}
	    	
	    } else if (objId == ObjectIds.TREE_SAPLING) {
	        boolean canBreak = (handItemId == 2 && timeDelta >= PlayerInteraction.BreakTime / 2)
	        		|| timeDelta >= PlayerInteraction.BreakTime;
            if (canBreak) {
            	for(Integer k:itemWhenBroken) {
            		Main.tilesManager.addDrop(i, j, k);
            		}
            	 Main.tilesManager.updateBlock(i, j, 0);
            	 Main.tilesManager.updateBlock(i-1, j, 0);
            	resetInteraction();
            }
            
	    }else if (ObjectPropertiesManager.getObject(objId).isBreakable()) {
	        boolean canBreak = (handItemId == 2 && timeDelta >= PlayerInteraction.BreakTime / 2)
	                         || timeDelta >= PlayerInteraction.BreakTime;
	        if (canBreak) {
	        	 Main.tilesManager.updateBlock(i, j, 0);
	            for(Integer k:itemWhenBroken) {
	            	 Main.tilesManager.addDrop(i, j, k);
	            }
	            resetInteraction();
	        }
	    }
	}
	
	private static void cutTree(int rootMapI,int rootMapJ) {
		//tree base
    	Main.tilesManager.updateBlock(rootMapI, rootMapJ, 0);
    	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ, 0);
		
		//left tree 
		if(ObjectPropertiesManager.getObject(Main.tilesManager.getObjects(rootMapI, rootMapJ-2).getId()).isTree()) {
        	Main.tilesManager.updateBlock(rootMapI, rootMapJ-1, 15);
        	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ-1, 3);

		}else {
        	Main.tilesManager.updateBlock(rootMapI, rootMapJ-1, 0);
        	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ-1, 0);
		}

		//right tree
		if(ObjectPropertiesManager.getObject(Main.tilesManager.getObjects(rootMapI, rootMapJ+2).getId()).isTree()) {
        	Main.tilesManager.updateBlock(rootMapI, rootMapJ+1, 13);
        	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ+1, 1);
		}else {
        	Main.tilesManager.updateBlock(rootMapI, rootMapJ+1, 0);
        	Main.tilesManager.updateBlock(rootMapI-1, rootMapJ+1, 0);
		}
	}
	
	
	private static void resetInteraction() {
	    PlayerInteraction.objectToBreakId = 0;
	    PlayerInteraction.timeOfFirstHitToObject = 0;
	}
	
}
