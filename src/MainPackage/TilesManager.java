package MainPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

import Regeneration.RegenerationManager;
import Storage.Item;
import creature.CreatureManager;
import entity.ItemOnFloor;
import entity.GameObject;
import entity.Tile;
import multiplayer.ServerClientHandler;

public class TilesManager {
	
//this class has sub-class named itemOnFloor
	
	public final static int tileSize = 64;
	final int maxScreenCol = 50;
	final int maxScreenRow = 50;
	int borderX=20,borderY=13,cameraY = 1000,cameraX = 900;
	Tile[][] tiles;
	GameObject[][] objects;
	private Scanner s;
	private Formatter x;
	ArrayList<ItemOnFloor> drops;
	private ArrayList<entity.Entity> renderList = new ArrayList<>();
	boolean mapIsReady = false;
	
	
	String map = "";
	
	public TilesManager() {
		tiles = new Tile[maxScreenCol][maxScreenRow];
		objects = new GameObject[maxScreenCol][maxScreenRow];
		drops = new ArrayList<ItemOnFloor>();
	}
	
	public void tick() {
		RegenerationManager.tick();
		
		//check if the player pick up item from the floor
		if(!drops.isEmpty()) {
			
			int playerX = Main.player.getX()+(Main.player.getSizeX()/2),playerY = Main.player.getY()+(Main.player.getSizeY()/2);
			for(ItemOnFloor i:drops) {
				if(playerX >= i.getTileX()*tileSize && playerX <= i.getTileX()*tileSize+tileSize
						&& playerY >= i.getTileY()*tileSize && playerY <= i.getTileY()*tileSize+tileSize) {
					Main.inventory.addToItemStack(i.getItem().Clone());
					ServerClientHandler.sendDataToServer("remove_drop "+ i.getItem().getId()+" "+ i.getItem().getQuantity());
					drops.remove(i);
					return;
				}
			}
		}
	}
	
	public void renderTile(Graphics2D g2d) {
		int endRow = (cameraX/tileSize+borderX);
		int endCol = (cameraY/tileSize+borderY);
		int startCol = (int)(cameraY / tileSize);
		int startRow = (int)(cameraX / tileSize);
		g2d.setStroke(new BasicStroke(2));
		for(int i = startCol;i<endCol && i<maxScreenCol;i++) {
			for(int j = startRow;j<endRow  && j<maxScreenRow;j++) {
		        if (i < 0 || j < 0) continue;
				tiles[i][j].render(g2d);
			}
		}
	}
	
	
	
		public void renderObjects(Graphics2D g2d) {
			renderList.clear(); // Clear the list for the new frame

			int startCol = (int)(cameraY / tileSize);
			int endCol = (int)(cameraY / tileSize + borderY);
			int startRow = (int)(cameraX / tileSize);
			int endRow = (int)(cameraX / tileSize + borderX);

			for (int i = startCol; i < endCol && i < maxScreenCol; i++) {
				for (int j = startRow; j < endRow && j < maxScreenRow; j++) {
					if (i < 0 || j < 0) continue;
					
					// Only add objects that exist (ID != 0)
					if (objects[i][j].getId() != 0) {
						renderList.add(objects[i][j]);
					}
				}
			}

			//Add Players to the list
			if (Main.player != null) renderList.add(Main.player);
			if (Main.player2 != null) renderList.add(Main.player2);
			 renderList.addAll(creature.CreatureManager.getCreatures()); 

			// Sort the list based on Y depth
			renderList.sort((e1, e2) -> {
				int y1 = getSortY(e1);
				int y2 = getSortY(e2);
				return Integer.compare(y1, y2);
			});

			//Render everything in order
			for (entity.Entity e : renderList) {
				e.render(g2d);
			}
			
			// Draw mouse cursor selection box
			if(!Main.inventory.IsOpen() && Main.gameState == 2) {
				g2d.setColor(Color.black);
				g2d.setStroke(new BasicStroke(2));
				g2d.drawRect(((cameraX + Main.mouseManeger.getMouseX())/tileSize)*tileSize - cameraX, 
						((cameraY + Main.mouseManeger.getMouseY())/tileSize)*tileSize - cameraY,
						tileSize, tileSize);
			}
		}

		
		// Helper function to calculate the sorting Y position.
		//It handles the "Depth" logic.
		private int getSortY(entity.Entity e) {
	    	// Default sort Y is the bottom of the sprite (the feet)
	    	 int sortY = e.getY() + e.getSizeY();

	    	 // Special handling for multi-tile objects (like Trees)
	    	 if (e instanceof entity.GameObject) {
				entity.GameObject obj = (entity.GameObject) e;
				
				// If this object is a "Top" part of a tree (Leaves)
	            // we treat its depth as if it were one tile lower (at the Trunk level)
				if (obj.isTopPart()) {
					sortY += tileSize;
				}
				
			}
			
			return sortY;
		}
	
	public int isPlayersClose() {
		if(Main.player2 == null || ( Main.player2 != null && Main.player2.playerI != Main.player.playerI)) {
			return 2;
		}
		return(Math.abs( Main.player2.playerJ - Main.player.playerJ));
	}
	
	
	public void renderDrops(Graphics2D g2d) {
		if(!drops.isEmpty()) {
			for(ItemOnFloor d:drops) {
				d.render(g2d);
			}
		}
	}
	
	//read from the file
	public void readFile() {
		mapIsReady = false;
		RegenerationManager.resetList();
		
		if(map.equals("cave") || map.equals("")) {
			map = "map";
			cameraY = 1000;
			cameraX = 900;
			Main.player.setLocation(1470, 1330);
		}else {
			map = "cave";
			Main.player.setLocation(1534, 702);
			cameraX =1064;
			cameraY =494;
		}
		
		while(true){
			try {	
			s = new Scanner(new File("saves/"+map+".txt"));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("file do not find");
			}	
			try {
			//adding tiles to the screen from map
			for(int i=0;i<maxScreenCol;i++) {
				for(int j=0;j<maxScreenRow;j++) {
					tiles[i][j] = new Tile(s.nextInt(), j*tileSize, i*tileSize, tileSize);
				}
			}
			s.close();

			break;
			}catch (Exception e) {
			      e.printStackTrace();
			}
		}
		while(true){
			try {	
			s = new Scanner(new File("saves/"+map+"_items.txt"));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("file do not find");
			}	
			try {
			//adding items to the screen from map
			for(int i=0;i<maxScreenCol;i++) {
				for(int j=0;j<maxScreenRow;j++) {
					objects[i][j] = new GameObject(s.nextInt() ,j*tileSize, i*tileSize, tileSize);
					if(objects[i][j].getName().equals("Tree sapling") || objects[i][j].getName().equals("Rock")) {
						RegenerationManager.insertToGrowthList(objects[i][j],j*tileSize,i*tileSize);
					}
				}
			}
			s.close();

			break;
			}catch (Exception e) {
			      e.printStackTrace();
			}
		}
		mapIsReady = true;
		
		CreatureManager.CreateCreature(1350, 1350, "cow");
	}
	
	
	public void saveMap() {
		try {
			x = new Formatter("saves/"+map+".txt");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<maxScreenCol;i++) {
			for(int j=0;j<maxScreenRow;j++) {
				x.format("%s ",tiles[i][j].getId());
			}
			x.format("%n");
		}			
		x.close();
		
		try {
			x = new Formatter("saves/"+map+"_items.txt");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<maxScreenCol;i++) {
			for(int j=0;j<maxScreenRow;j++) {
				x.format("%s ",objects[i][j].getId());
			}
			x.format("%n");
		}			


		x.close();
	}
	
	
	public void setMapFromMultiplayer(String[] map) {
		for(int i=0;i<maxScreenCol;i++) {
			for(int j=0;j<maxScreenRow;j++) {
				tiles[i][j] = new Tile(Integer.parseInt(map[i*maxScreenRow +j+1]), j*tileSize, i*tileSize, tileSize); 
			}
		}
		mapIsReady = true;
	}
	
	public void setItemsOnTilesFromMultiplayer(String[] map) {
		for(int i=0;i<maxScreenCol;i++) {
			for(int j=0;j<maxScreenRow;j++) {
				objects[i][j] = new GameObject(Integer.parseInt(map[i*maxScreenRow + j + 1]), j*tileSize, i*tileSize, tileSize); 
			}
		}
	}
	
	public void setDropsFromMultiplayer(String[] map) {
		int n = map.length;
		for(int i = 1;i<n;i+=4) {
			if(map[i] == null)return;
			ItemOnFloor temp = new ItemOnFloor(new Item(Integer.parseInt(map[i])),Integer.parseInt(map[i+2]),Integer.parseInt(map[i+3]));
			temp.getItem().setQuantity(Integer.parseInt(map[i+1]));
			drops.add(temp);
		}
	}
	
	
	public boolean IsLocationFree(int x,int y,int width,int height) {
		return(tiles[y/tileSize][x/tileSize].isSolid(x%tileSize, y%tileSize, width, height));
	}
	
	//check if the player can place a tree in the location
	public boolean canPlaceTree(int objectI,int objectJ) {
		boolean canPlace = objects[objectI][objectJ].getId() == 0 && objects[objectI-1][objectJ].getId() == 0 && //middle
				(objects[objectI][objectJ+1].getId() == 0 || objects[objectI][objectJ+1].getId() == 17) && // right
				(objects[objectI-1][objectJ+1].getId() == 0 || objects[objectI-1][objectJ+1].getId() == 5) && // right
				(objects[objectI][objectJ-1].getId() == 0 || objects[objectI][objectJ-1].getId() == 17) && // left
				(objects[objectI-1][objectJ-1].getId() == 0 || objects[objectI-1][objectJ-1].getId() == 5); // left
		return !canPlace;
	}
	
	public int getTileSize() {
		return tileSize;
	}
	public int getCameraX(boolean border) {
		if(border) return cameraX+Main.width;
		return cameraX;
	}
	public void setCameraX(int screenX) {
		this.cameraX = screenX;
	}
	public void setCameraY(int screenY) {
		this.cameraY = screenY;
	}
	public int getCameraY(boolean border) {
		if(border) return cameraY+Main.height;
		return cameraY;
	}
	public int getMapWidth() {
		return maxScreenRow*tileSize;
	}
	public int getMapHeight() {
		return maxScreenCol*tileSize;
	}
	
	public int getmaxScreenCol() {
		return maxScreenCol;
	}
	public int getmaxScreenRow() {
		return maxScreenRow;
	}
	public Tile[][] getTiles() {
		return tiles;
	}
	public Tile getTiles(int TileI,int TileJ) {
		return tiles[TileI][TileJ];
	}
	public GameObject[][] getObjects(){
		return objects;
	}
	public GameObject getObjects(int objectI,int objectJ){
		return objects[objectI][objectJ];
	}
	public void addItemDrop(Item item,int x,int y) {
		drops.add(new ItemOnFloor(item, x/tileSize, y/tileSize));
	}
	public ArrayList<ItemOnFloor> getItemOnFloorList() {
		return drops;
	}
	public int getBorderX() {
		return borderX;
	}
	public int getBorderY() {
		return borderY;
	}
	
	public boolean isMapIsReady() {
		return mapIsReady;
	}
	public void setMapIsReady(boolean mapIsReady) {
		this.mapIsReady = mapIsReady;
	}
	
	public void updateBlock(int mapI,int mapJ, int newId) {
	    objects[mapI][mapJ].setType(newId);
	    ServerClientHandler.sendDataToServer("update_block " + mapI + " " + mapJ + " " + newId);
	    }
	
	public void addDrop(int mapI, int mapJ, int itemId) {
	    Main.tilesManager.addItemDrop(new Item(itemId), mapJ * tileSize, mapI * tileSize);
	    ServerClientHandler.sendDataToServer("add_drop " + mapI + " " + mapI + " " + itemId + " " + 1);
	}

	
	@Override
		public String toString() {
		String map = "";
		for(int i=0;i<maxScreenCol;i++) {
			for(int j=0;j<maxScreenRow;j++) {
				map += tiles[i][j].getId() + " ";
			}
		}
			return "map: " + map;
	}
	
	public String DropsToString() {
		String map = "drops: ";
		for(ItemOnFloor i:drops) {
			map = map + i.getItem().getId()+ " "+ i.getItem().getQuantity()+ " " +i.getTileX()+ " "+i.getTileY() + " ";
		}
		
		return map;
	}
	
	public String ObjectsToString() {
		String map = "";
		for(int i=0;i<maxScreenCol;i++) {
			for(int j=0;j<maxScreenRow;j++) {
				map += ((Tile) objects[i][j]).getId() + " ";
			}
		}
			return "items: " + map;
		}
	
	public void removeDrop(int id,int quantity) {
		for(ItemOnFloor i:drops) {
			if(i.getItem().getId() == id && i.getItem().getQuantity() == quantity) {
				drops.remove(i);
				return;
			}
		}
	}
	
}
