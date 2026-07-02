package playerPackage;

import MainPackage.Main;
import MainPackage.TilesManager;
import creature.CreatureManager;
import storage.Item;
import storage.ItemIds;
import mapRender.ObjectIds;

public class PlayerInteraction {
	
	static int objectToBreakId = 0;
	static long timeOfFirstHitToObject = 0;
	private final static int tileSize = TilesManager.tileSize;
	final static int BreakTime = 4000;
	final static int rockBreakTime = 10000;
	static boolean breaking = false; 
	static int objectI,objectJ;
	
	public static void mousePress(int mouseX,int mouseY,int mouseButton) {
		// Round to the nearest grid corner so the targeted cell matches the cursor box,
		// which is centered on the marching-squares corner (see TilesManager cursor draw).
		int pressBlockJ = (Main.tilesManager.getCameraX(false) + mouseX + tileSize/2)/tileSize;
		int pressBlockI = (Main.tilesManager.getCameraY(false) + mouseY + tileSize/2)/tileSize;
		if(Math.abs(pressBlockJ - Main.player.playerJ) <= 2 && Math.abs(pressBlockI - Main.player.playerI) <= 2) {
			switch (mouseButton) {
			case 1: 
				leftMousePress(pressBlockI,pressBlockJ);
				breaking = true;
				break;
			case 3:
				rightMousePress(pressBlockI,pressBlockJ);
				break;
			}
		}
		
	}
	
	public static void mouseReleased(int mouseX,int mouseY,int mouseButton) {
		int pressBlockJ = (Main.tilesManager.getCameraX(false) + mouseX + tileSize/2)/tileSize;
		int pressBlockI = (Main.tilesManager.getCameraY(false) + mouseY + tileSize/2)/tileSize;
		if(Math.abs(pressBlockJ - Main.player.playerJ) <= 2 && Math.abs(pressBlockI - Main.player.playerI) <= 2) {
			switch (mouseButton) {
			case 1: 
				leftMouseReleased(pressBlockI,pressBlockJ);
				breaking = false;
				Main.player.imagePosture = 0;
				break;
			case 3:
				rightMouseReleased(pressBlockI,pressBlockJ);
				break;
			}
		}
		
	}
	
	private static void leftMousePress(int pressBlockI,int pressBlockJ) {
		Main.player.fishing = false;
		if (CreatureManager.attackCreatureInRange()) return;
		if(objectToBreakId != Main.tilesManager.getObjects(pressBlockI,pressBlockJ).getId() && Main.tilesManager.getObjects(pressBlockI,pressBlockJ).getId() != 0) {
			objectI = pressBlockI;
			objectJ = pressBlockJ;
			objectToBreakId = Main.tilesManager.getObjects(pressBlockI,pressBlockJ).getId();
			timeOfFirstHitToObject = System.currentTimeMillis();
			Main.player.imagePosture = 2;
			Main.player.animationTimer = System.currentTimeMillis();
		}
	}
	
	private static void rightMousePress(int pressBlockI,int pressBlockJ) {
		Item itemInHand = Main.inventory.getItemInHand();
		int objId = Main.tilesManager.getObjects()[pressBlockI][pressBlockJ].getId();
		
		boolean isCookFish = (objId == ObjectIds.CAMPFIRE_ON && itemInHand.getId() == ItemIds.FISH) ||
				(objId == ObjectIds.CAMPFIRE && itemInHand.getId() == ItemIds.WOOD);
		
		// open chest
		if (objId == ObjectIds.CHEST) {
			Main.chestUI.open(pressBlockI, pressBlockJ);
			return;

		// open workbench
		} else if (objId == ObjectIds.WORKBENCH) {
			Main.workbenchUI.open(pressBlockI, pressBlockJ);
			return;

		//place block
		} else if(itemInHand.isPlaceable()) {
			Main.player.placeBlock(pressBlockI, pressBlockJ,itemInHand);
			
		//cook a fish
		}else if(isCookFish) {
			Main.player.cookFish(pressBlockI, pressBlockJ, itemInHand);
			
		//eat fish
		}else if(itemInHand.getId() == ItemIds.FISH) {
			Main.player.eatFood(1);
			Main.inventory.decreaseItemInHand();

		//eat baked fish
		}else if(itemInHand.getId() == ItemIds.BAKED_FISH) {
			Main.player.eatFood(6);
			Main.inventory.decreaseItemInHand();

		//fishing
		}else if(itemInHand.getId() == 3 && Main.tilesManager.getTiles()[pressBlockI][pressBlockJ].isWater()) {
			Main.player.startFishing(pressBlockI,pressBlockJ);

		}
		
	}
	
	private static void leftMouseReleased(int pressBlockI,int pressBlockJ) {
		objectToBreakId = 0;
		timeOfFirstHitToObject = 0;
	}
	
	private static void rightMouseReleased(int pressBlockI,int pressBlockJ) {
		
	}
}
