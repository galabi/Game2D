package Entity;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import MainPackage.Main;

public class entity {
	protected int x,y,sizeX,sizeY;
	
	ImageIcon image;

	
	public entity(int x, int y ,int sizeX ,int sizeY) {
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	public entity(int x, int y ,int size,String FileLocation) {
		this.x = x;
		this.y = y;
		image = new ImageIcon(getClass().getResource(FileLocation));
		this.sizeX = size*image.getIconWidth()/image.getIconHeight();
		this.sizeY = size;
	}

	public void tick() {

	}
	
	public void render(Graphics2D g2d) {
		int screenX = Main.tilesManager.getCameraX(false),screenY = Main.tilesManager.getCameraY(false);
		try {
			g2d.drawImage(image.getImage(), x-screenX,y-screenY, sizeX,sizeY,null);
		} catch (Exception e) {
			System.out.println(x/sizeX +" "+y/sizeY);
		}
		if(Main.devmode) {
			g2d.drawRect(x-screenX, y-screenY, sizeX,sizeY);
		}
	}
	
	public void setLocation(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public int getSizeX() {
		return sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

}