package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

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
		
		BufferedImage image;
		try {
			image = ImageIO.read(new File("itemicon/items.png"));
			for(int i = 0;i<3 ;i++) {
				for(int j = 0; j<4 && i*4+j < 11;j++) {
					itemIcons[i*4+j] = new ImageIcon(image.getSubimage(j*64, i*64, 64, 64));
				}
			}
			
			image = ImageIO.read(new File("tiles_source/tiles.png"));
			for(int i = 0;i<8;i++) {
				for(int j = 0; j<8 && j + i*8 < tileIconSize;j++) {
					tileIcons[i*8+j] = new ImageIcon(image.getSubimage(j*64, i*64, 64, 64));
				}
			}
			
			image = ImageIO.read(new File("tiles_source/objects.png"));
			for(int i = 0;i<6;i++) {
				for(int j = 0; j<6 && j + i*6 < objectIconSize;j++) {
					objectIcons[i*6+j] = new ImageIcon(image.getSubimage(j*64, i*64, 64, 64));
				}
			}
			
			image = ImageIO.read(new File("tiles_source/objectsFront.png"));
			for(int i = 0;i<6;i++) {
				for(int j = 0; j<6 && j + i*6 < objectIconSize;j++) {
					frontObjectIcon[i*6+j] = new ImageIcon(image.getSubimage(j*64, i*64, 64, 64));
				}
			}
			
		} catch (Exception e) {
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
