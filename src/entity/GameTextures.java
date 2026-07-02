package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import mapRender.MapEntity;
import mapRender.MapObject;

public class GameTextures {
	private static final ImageIcon[] itemIcons,tileIcons,objectIcons;
	
	static final int TileSize = 32;
	
	static{
		int itemIconSize = 18,tileIconSize = 32,objectIconSize = 85;
		itemIcons = new ImageIcon[itemIconSize];
		tileIcons = new ImageIcon[tileIconSize];
		objectIcons = new ImageIcon[objectIconSize];
		
		/*
    	itemIcons[0] = clear;
		itemIcons[1] = log
		itemIcons[2] = axe
		itemIcons[3] = fishing rod
		itemIcons[4] = fish
		itemIcons[5] = baked fish
		itemIcons[6] = fire pit
		itemIcons[7] = tree seed
		itemIcons[8] = stick
		itemIcons[9] = stone
		itemIcons[10] = pickaxe
		*/
		
		try {
			
			// loading items.png
	        loadSheet(itemIcons, "/items.png", 4, 4, itemIconSize,64);

	        // loading tiles.png
	        loadSheet(tileIcons, "/tiles.png", 4, 8, tileIconSize,TileSize);

	        // loading objects.png (irregularly packed — explicit source reacts per object id)
	        loadObjectSprites();

	        // trees and saplings are standalone images, loaded into their object slots
	        objectIcons[1] = loadImage("/tree.png");    // single-tile tree
	        objectIcons[2] = loadImage("/sapling.png"); // sapling



		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// objects.png is a clean 32x32 grid (8 cols x 3 rows). Object ids are contiguous in
	// reading order (left-to-right, top-to-bottom), starting at OBJECT_SHEET_FIRST_ID.
	private static final int OBJECT_SHEET_TILE = 32;
	private static final int OBJECT_SHEET_COLS = 8;
	private static final int OBJECT_SHEET_ROWS = 3;
	private static final int OBJECT_SHEET_FIRST_ID = 3; // ids 1,2 are tree/sapling (standalone images)

	private static void loadObjectSprites() {
		try {
			BufferedImage sheet = ImageIO.read(GameTextures.class.getResourceAsStream("/objects.png"));
			int W = sheet.getWidth(), H = sheet.getHeight();
			for (int row = 0; row < OBJECT_SHEET_ROWS; row++) {
				for (int col = 0; col < OBJECT_SHEET_COLS; col++) {
					int id = OBJECT_SHEET_FIRST_ID + row * OBJECT_SHEET_COLS + col;
					int x = col * OBJECT_SHEET_TILE, y = row * OBJECT_SHEET_TILE;
					int w = Math.min(OBJECT_SHEET_TILE, W - x), h = Math.min(OBJECT_SHEET_TILE, H - y);
					if (id < objectIcons.length && w > 0 && h > 0)
						objectIcons[id] = new ImageIcon(sheet.getSubimage(x, y, w, h));
				}
			}
		} catch (Exception e) {
			System.err.println("Error while loading object sprites");
			e.printStackTrace();
		}
	}

	private static ImageIcon loadImage(String path) {
		try {
			return new ImageIcon(ImageIO.read(GameTextures.class.getResourceAsStream(path)));
		} catch (Exception e) {
			System.err.println("Error while loading image: " + path);
			return null;
		}
	}

	private static void loadSheet(ImageIcon[] targetArray, String path, int rows, int cols, int maxIcons,int TileSize) {
        try {
        	BufferedImage temp = ImageIO.read(GameTextures.class.getResourceAsStream(path));
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols && (i * cols + j) < maxIcons; j++) {
                    targetArray[i * cols + j] = new ImageIcon(temp.getSubimage(j * TileSize, i * TileSize, TileSize, TileSize));
                }
            }
        } catch (Exception e) {
            System.err.println("Error while loading GameTexture: "+path);
            e.printStackTrace();
        }
    }
	
	public static ImageIcon getItemIcon(int id) {
		if (id < 0 || id >= itemIcons.length) return null;
		return itemIcons[id];
	}
	
	
	public static ImageIcon getTileIcon(int id) {
		if (id < 0 || id >= tileIcons.length || tileIcons[id] == null) return tileIcons[0];
		return tileIcons[id];
	}
	
	
	public static ImageIcon getObjectIcon(int id) {
		if (id < 0 || id >= objectIcons.length) return null;
		return objectIcons[id];
	}
	
	public static ImageIcon getMapRenderImage(MapEntity e) {
		if(e instanceof MapObject) {
			return getObjectIcon(e.getId());
		}else return null;
	}
	
	public static void preloadImages(Graphics2D g2d) {
	    for (ImageIcon icon : itemIcons) {
	        if (icon != null) {
	           g2d.drawImage(icon.getImage(), -1000, -1000, null);
	        }
	    }
		for (ImageIcon icon : tileIcons) {
	        if (icon != null) {
	           g2d.drawImage(icon.getImage(), -1000, -1000, null);
	        }
	    }
		for (ImageIcon icon : objectIcons) {
	        if (icon != null) {
	           g2d.drawImage(icon.getImage(), -1000, -1000, null);
	        }
	    }
	}
}
