package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import MainPackage.TilesManager;

public class GameTextures {
	private static ImageIcon[] itemIcons,tileIcons,objectIcons,frontObjectIcon;
	
	static{
		int itemIconSize = 11,tileIconSize = 61,objectIconSize = 34;
		itemIcons = new ImageIcon[itemIconSize];
		tileIcons = new ImageIcon[tileIconSize];
		objectIcons = new ImageIcon[objectIconSize];
		frontObjectIcon = new ImageIcon[objectIconSize];
		
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
	        loadSheet(itemIcons, "/items.png", 3, 4, itemIconSize);

	        // loading tiles.png
	        loadSheet(tileIcons, "/tiles.png", 8, 8, tileIconSize);

	        // loading objects.png
	        loadSheet(objectIcons, "/objects.png", 6, 6, objectIconSize);

	        // loading objectsFront.png
	        loadSheet(frontObjectIcon, "/objectsFront.png", 6, 6, objectIconSize);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void loadSheet(ImageIcon[] targetArray, String path, int rows, int cols, int maxIcons) {
        try {
        	BufferedImage temp = ImageIO.read(GameTextures.class.getResourceAsStream(path));
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols && (i * cols + j) < maxIcons; j++) {
                    targetArray[i * cols + j] = new ImageIcon(temp.getSubimage(j * TilesManager.tileSize, i * TilesManager.tileSize, TilesManager.tileSize, TilesManager.tileSize));
                }
            }
        } catch (Exception e) {
            System.err.println("Error while loading GameTexture: "+path);
            e.printStackTrace();
        }
    }
	
	public static ImageIcon getItemIcon(int id) {
		return itemIcons[id];
	}
	
	
	public static ImageIcon getTileIcon(int id) {
		return tileIcons[id];
	}
	
	
	public static ImageIcon getObjectIcon(int id) {
		return objectIcons[id];
	}
	
	public static ImageIcon getFrontObjectIcon(int id) {
		return frontObjectIcon[id];
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
		for (ImageIcon icon : frontObjectIcon) {
	        if (icon != null) {
	           g2d.drawImage(icon.getImage(), -1000, -1000, null);
	        }
	    }
	}
}
