package playerPackage;

import java.util.ArrayList;

import MainPackage.Main;
import MainPackage.TilesManager;
import regeneration.RegenerationManager;
import mapRender.ObjectIds;
import mapRender.ObjectPropertiesManager;
import storage.ChestStorage;

public class PlayerBreakBlock {

	public static void breakBlock() {
	    long timeDelta = System.currentTimeMillis() - PlayerInteraction.timeOfFirstHitToObject;
	    int objId = PlayerInteraction.objectToBreakId;
	    int i = PlayerInteraction.objectI;
	    int j = PlayerInteraction.objectJ;
	    int handItemId = Main.inventory.getItemInHand().getId();
	    ArrayList<Integer> itemWhenBroken = ObjectPropertiesManager.getObject(objId).getItemWhenBroken();

	    if (ObjectIds.isMineableOre(objId)) {
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
	    } else if (objId == ObjectIds.TREE || objId == ObjectIds.TREE_SAPLING) {
	        boolean canBreak = (handItemId == 2 && timeDelta >= PlayerInteraction.BreakTime / 2)
	        		|| timeDelta >= PlayerInteraction.BreakTime;
	        if (canBreak) {
	        	for(Integer k:itemWhenBroken) {
	        		Main.tilesManager.addDrop(i, j, k);
	        		}
	        	 Main.tilesManager.updateBlock(i, j, 0); // single-tile tree/sapling
	        	resetInteraction();
	        }

	    }else if (ObjectPropertiesManager.getObject(objId).isBreakable()) {
	        boolean canBreak = (handItemId == 2 && timeDelta >= PlayerInteraction.BreakTime / 2)
	                         || timeDelta >= PlayerInteraction.BreakTime;
	        if (canBreak) {
	        	// Drop chest contents before removing the object
	        	if (objId == ObjectIds.CHEST) {
	        		storage.Item[][] contents = Main.chestStorage.getChest(i, j);
	        		for (int r = 0; r < ChestStorage.ROWS; r++)
	        			for (int c = 0; c < ChestStorage.COLS; c++)
	        				if (contents[r][c] != null && contents[r][c].getId() != 0)
	        					Main.tilesManager.addDrop(i, j, contents[r][c].getId(), contents[r][c].getQuantity());
	        		Main.chestStorage.removeChest(i, j);
	        	}
	        	Main.tilesManager.updateBlock(i, j, 0);
	            for(Integer k:itemWhenBroken) {
	            	Main.tilesManager.addDrop(i, j, k);
	            }
	            resetInteraction();
	        }
	    }
	}

	private static void resetInteraction() {
	    PlayerInteraction.objectToBreakId = 0;
	    PlayerInteraction.timeOfFirstHitToObject = 0;
	}

}
