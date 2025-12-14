package MainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import entity.FontLoader;
import multiplayer.ServerClientHandler;


public class PauseScreen{

	final int buttonSizeX = 200;
	final int buttonSizeY = 50;
	final int lanButtonX = 500;
	final int lanButtonY = 400;
	int lanTextX = -1,lanTextY=-1;
	
	Font font = FontLoader.getPixelFont(24);
	boolean lanButton = true;
	FontMetrics metrics;
	
	public void render(Graphics2D g2d) {
		if(lanTextX < 0) {
			metrics = g2d.getFontMetrics(font);
			setTextLocation();
		}
		g2d.setFont(font);
		g2d.setColor(new Color(0,0,0,100));
		g2d.fillRect(0, 0, Main.width, Main.height);
		g2d.setColor(Color.gray);
		g2d.fill3DRect(lanButtonX, lanButtonY, buttonSizeX, buttonSizeY,lanButton);
		g2d.draw3DRect(lanButtonX, lanButtonY, buttonSizeX, buttonSizeY,lanButton);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Open-lan", lanTextX, lanTextY);
		
	}


	
	public void mousePressed(MouseEvent e) {
		int mouseX = e.getX()*Main.width/Main.screenWidth ,mouseY = e.getY()*Main.height/Main.screenHeight;
		//start game
		if(mouseX > lanButtonX && mouseX < (lanButtonX + buttonSizeX) && mouseY > lanButtonY && mouseY < lanButtonY + buttonSizeY) {
			
			lanButton = false;
			ServerClientHandler.openServer();
		}
	}
	
	public void setTextLocation() {
		lanTextX = (lanButtonX + ((buttonSizeX - metrics.stringWidth("Open-lan")) / 2));		
		lanTextY = (lanButtonY + ((buttonSizeY - metrics.getHeight()) / 2) + metrics.getAscent());		

	}
	
}
