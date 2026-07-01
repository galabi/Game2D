package MainPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import regeneration.RegenerationManager;
import storage.ChestStorage;
import storage.Item;
import entity.GameObject;
import entity.GameTextures;
import entity.ItemOnFloor;
import entity.Tile;
import mapRender.MapEntity;
import mapRender.ObjectIds;
import mapRender.ObjectPropertiesManager;
import multiplayer.ServerClientHandler;

public class TilesManager {
	
//this class has sub-class named itemOnFloor
	
	public final static int tileSize = 64;
	final int maxScreenCol = 1000;
	final int maxScreenRow = 1000;
	int borderX=20,borderY=13,cameraY = 1000,cameraX = 900;
	Tile[][] tiles;
	GameObject[][] objects;
	List<ItemOnFloor> drops;
	private ArrayList<entity.Entity> renderList = new ArrayList<>();
	boolean mapIsReady = false;
	String map = "map";

	public TilesManager() {
		ObjectPropertiesManager.loadObjects();
		tiles = new Tile[maxScreenCol][maxScreenRow];
		objects = new GameObject[maxScreenCol][maxScreenRow];
		drops = Collections.synchronizedList(new ArrayList<>());
		
	}
	
	public void tick() {
		RegenerationManager.tick();
		
		//check if the player pick up item from the floor
		if(!drops.isEmpty()) {
			for(ItemOnFloor i:drops) i.tick();

			int playerX = Main.player.getX()+(Main.player.getSizeX()/2),playerY = Main.player.getY()+(Main.player.getSizeY()/2);
			for(ItemOnFloor i:drops) {
				if(!i.isAirborne() && playerX >= i.getX() && playerX <= i.getX()+tileSize
						&& playerY >= i.getY() && playerY <= i.getY()+tileSize) {
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
		
		startRow = Math.max(0,startRow);
		startCol = Math.max(0,startCol);

		
		g2d.setStroke(new BasicStroke(2));
		for(int i = startCol;i<endCol && i<maxScreenCol;i++) {
			for(int j = startRow;j<endRow  && j<maxScreenRow;j++) {
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

		startRow = Math.max(0,startRow);
		startCol = Math.max(0,startCol);
			
		for (int i = startCol; i < endCol && i < maxScreenCol; i++) {
			for (int j = startRow; j < endRow && j < maxScreenRow; j++) {
					
				// Only add objects that exist (ID != 0)
				if (objects[i][j].getId() != 0) {
					renderList.add(objects[i][j]);
				}
			}
		}
			
		//Add Players to the list
		if (Main.player != null) renderList.add(Main.player);
		renderList.addAll(Main.remotePlayers.values());
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
		if(!Main.inventory.isOpen() && Main.gameState == Main.GameState.GAME) {
			g2d.setColor(Color.black);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRect(((cameraX + Main.mouseManager.getMouseX())/tileSize)*tileSize - cameraX, 
					((cameraY + Main.mouseManager.getMouseY())/tileSize)*tileSize - cameraY,
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
				
			// If this object is a "Top" part of a tree (Leaves) or "Low" part (Fence)
	        // we treat its depth as if it were one tile lower (at the Trunk level)
			sortY = (int) (sortY + ObjectPropertiesManager.getObject(obj.getId()).getObjectRenderOffSet()*tileSize);
				
		}
			
		return sortY;
	}
	
	public void render(Graphics2D g2d,MapEntity e,int x,int y) {
		try {
			g2d.drawImage(GameTextures.getMapRenderImage(e).getImage(), x-cameraX,y-cameraY, tileSize,tileSize,null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(Main.devmode) {
			g2d.setColor(Color.white);
			g2d.drawRect(x-cameraX, y-cameraY, tileSize,tileSize);
			for(Rectangle i: e.getSolidInTile()) {
				g2d.drawRect(x+i.x - cameraX,y+i.y- cameraY, i.width, i.height);

			}
		}
	}
	
	public int isPlayersClose() {
		for (playerPackage.Player rp : Main.remotePlayers.values()) {
			if (rp.playerI == Main.player.playerI) {
				return Math.abs(rp.playerJ - Main.player.playerJ);
			}
		}
		return 2;
	}
	
	
	public void renderDrops(Graphics2D g2d) {
		if(!drops.isEmpty()) {
			for(ItemOnFloor d:drops) {
				d.render(g2d);
			}
		}
	}
	
	static final int MAGIC = 0x47414D45;
	static final int VERSION = 1;

	public void readFile() {
		mapIsReady = false;
		RegenerationManager.resetList();

		Main.chestStorage.clear();

		File binFile = new File(WorldManager.path() + "map.bin");
		if (binFile.exists()) {
			loadBinary();
		} else {
			// First visit — generate overworld procedurally
			long seed = WorldManager.readSeed();
			int[] spawn = MapGenerator.generate(tiles, objects, seed);
			Main.player.setLocation(spawn[1] * tileSize, spawn[0] * tileSize);
			cameraX = Main.player.getX() - Main.width / 2 + Main.player.getSizeX() / 2;
			cameraY = Main.player.getY() - Main.height / 2 + Main.player.getSizeY() / 2;
			saveMap();
		}

		if (Main.host) creature.CreatureManager.loadCreatures(map);
		mapIsReady = true;
		if (Main.minimap != null) Main.minimap.markDirty();
	}

	private void loadBinary() {
		try (DataInputStream dis = new DataInputStream(
				new BufferedInputStream(new FileInputStream(WorldManager.path() + "map.bin")))) {

			if (dis.readInt() != MAGIC) throw new IOException("Invalid save file magic");
			dis.readUnsignedByte(); // version

			for (int i = 0; i < maxScreenCol; i++)
				for (int j = 0; j < maxScreenRow; j++)
					tiles[i][j] = new Tile(dis.readUnsignedByte(), j * tileSize, i * tileSize, tileSize);

			for (int i = 0; i < maxScreenCol; i++) {
				for (int j = 0; j < maxScreenRow; j++) {
					int id = dis.readUnsignedByte();
					objects[i][j] = new GameObject(id, j * tileSize, i * tileSize, tileSize);
					if (id == ObjectIds.TREE_SAPLING || id == ObjectIds.ROCK)
						RegenerationManager.insertToGrowthList(objects[i][j], j * tileSize, i * tileSize);
				}
			}

			int chestCount = dis.readUnsignedShort();
			for (int c = 0; c < chestCount; c++) {
				int ci = dis.readUnsignedShort();
				int cj = dis.readUnsignedShort();
				storage.Item[][] grid = new storage.Item[ChestStorage.ROWS][ChestStorage.COLS];
				for (int si = 0; si < ChestStorage.ROWS; si++)
					for (int sj = 0; sj < ChestStorage.COLS; sj++) {
						int itemId = dis.readUnsignedByte();
						int qty    = dis.readUnsignedByte();
						grid[si][sj] = itemId == 0 ? new storage.Item() : new storage.Item(itemId);
						if (itemId != 0) grid[si][sj].setQuantity(qty);
					}
				Main.chestStorage.setChest(ci, cj, grid);
			}

		} catch (Exception e) {
			System.err.println("Failed to load binary save: " + e.getMessage());
			initDefaultTiles();
			initDefaultObjects();
		}
	}


	public void saveMap() {
		try (DataOutputStream dos = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(WorldManager.path() + map + ".bin")))) {

			dos.writeInt(MAGIC);
			dos.writeByte(VERSION);

			for (int i = 0; i < maxScreenCol; i++)
				for (int j = 0; j < maxScreenRow; j++)
					dos.writeByte(tiles[i][j].getId());

			for (int i = 0; i < maxScreenCol; i++)
				for (int j = 0; j < maxScreenRow; j++)
					dos.writeByte(objects[i][j].getId());

			List<int[]> positions = Main.chestStorage.getChestPositions();
			dos.writeShort(positions.size());
			for (int[] pos : positions) {
				dos.writeShort(pos[0]);
				dos.writeShort(pos[1]);
				storage.Item[][] grid = Main.chestStorage.getChest(pos[0], pos[1]);
				for (int si = 0; si < ChestStorage.ROWS; si++)
					for (int sj = 0; sj < ChestStorage.COLS; sj++) {
						dos.writeByte(grid[si][sj].getId());
						dos.writeByte(grid[si][sj].getQuantity());
					}
			}

			if (Main.host) creature.CreatureManager.saveCreatures(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
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
		drops.clear();
		int n = map.length;
		for(int i = 1;i<n;i+=4) {
			if(map[i] == null)return;
			ItemOnFloor temp = new ItemOnFloor(new Item(Integer.parseInt(map[i])),Integer.parseInt(map[i+2]),Integer.parseInt(map[i+3]), false);
			temp.getItem().setQuantity(Integer.parseInt(map[i+1]));
			drops.add(temp);
		}
	}
	
	
	public boolean IsLocationFree(int x,int y,int width,int height) {
		return(tiles[y/tileSize][x/tileSize].isSolid(x%tileSize, y%tileSize, width, height));
	}
	
	//check if the player can place a tree in the location
	public boolean canPlaceTree(int objectI,int objectJ) {
		boolean cantPlace = objects[objectI][objectJ].getId() == 0 && objects[objectI-1][objectJ].getId() == 0 && //middle
				(objects[objectI][objectJ+1].getId() == 0 || objects[objectI][objectJ+1].getId() == 17) && // right
				(objects[objectI-1][objectJ+1].getId() == 0 || objects[objectI-1][objectJ+1].getId() == 5) && // right
				(objects[objectI][objectJ-1].getId() == 0 || objects[objectI][objectJ-1].getId() == 17) && // left
				(objects[objectI-1][objectJ-1].getId() == 0 || objects[objectI-1][objectJ-1].getId() == 5); // left
		return !cantPlace;
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
	public String getMap() {
		return map;
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
	public void addItemDrop(Item item, int x, int y) {
		drops.add(new ItemOnFloor(item, x, y));
	}

	public void addItemDrop(Item item, int x, int y, float vx, float vy) {
		drops.add(new ItemOnFloor(item, x, y, vx, vy));
	}

	public void addItemDropNoAnim(Item item, int x, int y) {
		drops.add(new ItemOnFloor(item, x, y, false));
	}
	public List<ItemOnFloor> getItemOnFloorList() {
		return drops;
	}

	private void initDefaultTiles() {
		for(int i = 0; i < maxScreenCol; i++) {
			for(int j = 0; j < maxScreenRow; j++) {
				tiles[i][j] = new Tile(Tile.GRASS, j * tileSize, i * tileSize, tileSize);
			}
		}
	}

	private void initDefaultObjects() {
		for(int i = 0; i < maxScreenCol; i++) {
			for(int j = 0; j < maxScreenRow; j++) {
				objects[i][j] = new GameObject(0, j * tileSize, i * tileSize, tileSize);
			}
		}
	}

	public int computeVisualId(int i, int j) {
		byte self  = tiles[i][j].getTerrainType();
		byte right = (j + 1 < maxScreenRow) ? tiles[i][j + 1].getTerrainType() : self;
		byte below = (i + 1 < maxScreenCol) ? tiles[i + 1][j].getTerrainType() : self;
		byte diag  = (i + 1 < maxScreenCol && j + 1 < maxScreenRow) ? tiles[i + 1][j + 1].getTerrainType() : self;

		byte gravelBit = 0;
		if (self  == Tile.GRAVEL) gravelBit |= 1;
		if (right == Tile.GRAVEL) gravelBit |= 2;
		if (below == Tile.GRAVEL) gravelBit |= 4;
		if (diag  == Tile.GRAVEL) gravelBit |= 8;
		int id = calculateID(gravelBit);

		byte waterBit = 0;
		if (self  == Tile.WATER) waterBit |= 1;
		if (right == Tile.WATER) waterBit |= 2;
		if (below == Tile.WATER) waterBit |= 4;
		if (diag  == Tile.WATER) waterBit |= 8;
		if (waterBit != 0) id = 4 + calculateID(waterBit);

		return id;
	}

	private static int calculateID(byte b) {
		switch (b) {
			case 0:  return 10;
			case 1:  return 9;
			case 2:  return 2;
			case 3:  return 3;
			case 4:  return 18;
			case 5:  return 1;
			case 6:  return 8;
			case 7:  return 25;
			case 8:  return 11;
			case 9:  return 26;
			case 10: return 19;
			case 11: return 0;
			case 12: return 17;
			case 13: return 16;
			case 14: return 27;
			case 15: return 24;
			default: return 10;
		}
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
	    objects[mapI][mapJ].setId(newId);
	    ServerClientHandler.sendDataToServer("update_block " + mapI + " " + mapJ + " " + newId);
	    if (Main.minimap != null) Main.minimap.markDirty();
	    }
	
	public void addDrop(int mapI, int mapJ, int itemId) {
		addDrop(mapI, mapJ, itemId, 1);
	}

	public void addDrop(int mapI, int mapJ, int itemId, int quantity) {
		int px = mapJ * tileSize;
		int py = mapI * tileSize;
		Item item = new Item(itemId);
		item.setQuantity(quantity);
		Main.tilesManager.addItemDrop(item, px, py);
		ServerClientHandler.sendDataToServer("add_drop " + itemId + " " + quantity + " " + px + " " + py);
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("map: ");
		for(int i = 0; i < maxScreenCol; i++) {
			for(int j = 0; j < maxScreenRow; j++) {
				sb.append(tiles[i][j].getId()).append(' ');
			}
		}
		return sb.toString();
	}

	public String DropsToString() {
		StringBuilder sb = new StringBuilder("drops: ");
		for(ItemOnFloor i : drops) {
			sb.append(i.getItem().getId()).append(' ')
			  .append(i.getItem().getQuantity()).append(' ')
			  .append(i.getX()).append(' ')
			  .append(i.getY()).append(' ');
		}
		return sb.toString();
	}

	public String ObjectsToString() {
		StringBuilder sb = new StringBuilder("items: ");
		for(int i = 0; i < maxScreenCol; i++) {
			for(int j = 0; j < maxScreenRow; j++) {
				sb.append(objects[i][j].getId()).append(' ');
			}
		}
		return sb.toString();
	}
	
	public void removeDrop(int id,int quantity) {
		for(ItemOnFloor i:drops) {
			if(i.getItem().getId() == id && i.getItem().getQuantity() == quantity) {
				drops.remove(i);
				return;
			}
		}
	}

	public void resetMap() {
		map = "";
	}

}
