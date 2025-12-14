package entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

import MainPackage.Main;

public class Image extends Entity{
	
	int sizeX,sizeY;
	String FileLocation;
	ImageIcon image;
	
	public Image(int x, int y,int size,String FileLocation){
		super(x,y,size,size);
		this.FileLocation = FileLocation; 
		image = new ImageIcon(getClass().getResource(FileLocation));		
		this.sizeX = size*image.getIconWidth()/image.getIconHeight();
	}
	@Override
	public void render(Graphics2D g2d) {
		AffineTransform old = g2d.getTransform();
		g2d.drawImage(image.getImage(), (int)(Main.width*x), (int)(Main.height*y), (int)(Main.width*sizeX), (int)(Main.width*sizeY),null);

        g2d.setTransform(old);
	}
	
	public String getFileLocation() {
		return FileLocation;
	}
	
	public void SetFileLocation(String FileLocation) {
		this.FileLocation = FileLocation;
	}
	
}
