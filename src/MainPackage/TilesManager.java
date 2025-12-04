package MainPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

import Creature.CreatureManager;
import Entity.Tile;
import Entity.ItemOnFloor;
import Regeneration.RegenerationManager;
import Entity.Object;
import Storage.Item;
import mutiplayer.ServerClientHandler;
import playerPackage.Player;

public class TilesManager {
	
//this class has sub-class named itemOnFloor
	
	public final static int tileSize = 64;
	final int maxScreenCol = 50;
	final int maxScreenRow = 50;
	int borderX=20,borderY=13,cameraY = 1000,cameraX = 900;
	Tile[][] tiles;
	Object[][] objects;
	private Scanner s;
	private Formatter x;
	ArrayList<ItemOnFloor> drops;
	boolean mapIsReady = false;
	
	String map = "";
	
	public TilesManager() {
		tiles = new Tile[maxScreenCol][maxScreenRow];
		objects = new Object[maxScreenCol][maxScreenRow];
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
		g2d.setStroke(new BasicStroke(2));
		for(int i = (int)(cameraY/tileSize);i<endCol && i<maxScreenCol;i++) {
			for(int j = (int)(cameraX/tileSize);j<endRow  && j<maxScreenRow;j++) {
		        if (i < 0 || j < 0) continue;
				tiles[i][j].render(g2d);
			}
		}
	}
	
	
	
	public void renderObjects(Graphics2D g2d) {
		int endRow = (cameraX/tileSize+borderX);
		int endCol = (cameraY/tileSize+borderY);
		
		for(int i = (int)(cameraY/tileSize);i<endCol && i<maxScreenCol;i++) {
			for(int j = (int)(cameraX/tileSize);j<endRow  && j<maxScreenRow;j++) {
		        if (i < 0 || j < 0) continue;
		        
		        if (Main.player != null && Main.player.playerI == i && Main.player.playerJ == j) {
		        	j += renderPlayerAndSurroundingObjects(g2d,Main.player,i,j);
		        	continue;
		        } if (Main.player2 != null && Main.player2.playerI == i && Main.player2.playerJ == j) {
		        	j += renderPlayerAndSurroundingObjects(g2d,Main.player2,i,j);
		        	continue;
		        }else {
			        objects[i][j].render(g2d);
		            objects[i][j].drawFront(g2d);
		        }
		    }
			
		}
		if(!Main.inventory.IsOpen() && Main.gameState == 2) {
			g2d.setColor(Color.black);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRect(((cameraX + Main.mouseManeger.getMouseX())/tileSize)*tileSize - cameraX, 
					((cameraY + Main.mouseManeger.getMouseY())/tileSize)*tileSize - cameraY,
					tileSize, tileSize);
		}
	}
	
	
	// Renders the player and nearby objects at position [i][j],
	// ensuring correct drawing order to avoid graphical overlap between players and tiles.
	private int renderPlayerAndSurroundingObjects(Graphics2D g2d, Player p,int i,int j) {
		int playersDistance = isPlayersClose();
	    // If the two players are not close (not 1 tile apart horizontally)
		if(playersDistance > 1) {
	        // Render the background tiles before the player
			objects[i][j].render(g2d);
			objects[i][j+1].render(g2d);
	        // Render the player on top
			p.render(g2d);
			
		}else if(playersDistance == 1){
			objects[i][j].render(g2d);
			objects[i][j+1].render(g2d);
			objects[i][j+2].render(g2d);
			
			Main.player.render(g2d);
			Main.player2.render(g2d);
			
		  	objects[i][j].drawFront(g2d);
		    objects[i][j+1].drawFront(g2d);
		    objects[i][j+2].drawFront(g2d);
			return 2;
		}else {
			objects[i][j].render(g2d);
			objects[i][j+1].render(g2d);
				
			// Determine render order based on Y-position (vertical)
            // The one with greater Y is farther back, so should be rendered first
			if(Main.player2 != null && Main.player2.getY()>Main.player.getY()) {
				if(Main.player2 != null)Main.player.render(g2d);
				if(Main.player != null)Main.player2.render(g2d);
			}else {
				if(Main.player2 != null)Main.player2.render(g2d);
				if(Main.player != null)Main.player.render(g2d);
			}
		}
		
	    // Draw the front layers of both objects to appear in front of the player
	  	objects[i][j].drawFront(g2d);
	    objects[i][j+1].drawFront(g2d);

		return 1;
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
					objects[i][j] = new Object(s.nextInt() ,j*tileSize, i*tileSize, tileSize);
					if(((Tile) objects[i][j]).getId() == 2 || (((Tile) objects[i][j]).getId() >= 30 && ((Tile) objects[i][j]).getId() < 34)) {
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
				objects[i][j] = new Object(Integer.parseInt(map[i*maxScreenRow + j + 1]), j*tileSize, i*tileSize, tileSize); 
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
	public Object[][] getObjects(){
		return objects;
	}
	public Object getObjects(int objectI,int objectJ){
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
