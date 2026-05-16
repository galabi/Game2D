package playerPackage;

import MainPackage.Main;
import MainPackage.TilesManager;
import storage.Item;
import storage.ItemIds;
import mapRender.ObjectIds;
import mapRender.ObjectPropertiesManager;
import mapRender.TilePropertiesManager;

public class PlayerInteraction {
	
	static int objectToBreakId = 0;
	static long timeOfFirstHitToObject = 0;
	private final static int tileSize = TilesManager.tileSize;
	final static int BreakTime = 4000;
	final static int rockBreakTime = 10000;
	static boolean breaking = false; 
	static int objectI,objectJ;
	
	public static void mousePress(int mouseX,int mouseY,int mouseButton) {
		int pressBlockJ = (Main.tilesManager.getCameraX(false) + mouseX)/tileSize;
		int pressBlockI = (Main.tilesManager.getCameraY(false) + mouseY)/tileSize;
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
		int pressBlockJ = (Main.tilesManager.getCameraX(false) + mouseX)/tileSize;
		int pressBlockI = (Main.tilesManager.getCameraY(false) + mouseY)/tileSize;
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
		int tileId = Main.tilesManager.getTiles()[pressBlockI][pressBlockJ].getId();
		
		boolean isCookFish = (objId == ObjectIds.CAMPFIRE_ON && itemInHand.getId() == ItemIds.FISH) ||
				(objId == ObjectIds.CAMPFIRE && itemInHand.getId() == ItemIds.WOOD);
		
		//place block
		if(itemInHand.isPlaceable()) {
			Main.player.placeBlock(pressBlockI, pressBlockJ,itemInHand);
			
		//cook a fish
		}else if(isCookFish) {
			Main.player.cookFish(pressBlockI, pressBlockJ, itemInHand);
			
		//fishing
		}else if(itemInHand.getId() == 3 && TilePropertiesManager.getTile(tileId).isWater(Main.mouseManager.getMouseX(),Main.mouseManager.getMouseY())) {
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
