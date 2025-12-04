package MainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import Entity.FontLoader;
import mutiplayer.Client;
import mutiplayer.ServerSelectScreen;


public class StartScreen{

	int buttonSizeX = 200,buttonSizeY = 50,startButtonX = 500,startButtonY = 520,multiButtonX = 500,multiButtonY = 600;
	int startTextX = -1, multiTextX =-1,startTextY = -1, multiTextY =-1;
	boolean multiButton = true,startButton = true,serverScreen = false;
	Font font = FontLoader.getPixelFont(24);
	public static ImageIcon backgraond;
	ServerSelectScreen serverSelect;
	
	FontMetrics metrics;
	
	public StartScreen(){
		backgraond = new ImageIcon(getClass().getResource("/backgraond.png"));
		serverSelect = new ServerSelectScreen(800,startButtonY,270,30);
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics2D g2d) {
		if(serverScreen) {
			serverSelect.render(g2d);
		}else {
			if(startTextX < 0) {
				metrics = g2d.getFontMetrics(font);
				setTextLocation();
			}
			
			g2d.setFont(font);
			g2d.drawImage(backgraond.getImage(),0, 0, Main.width, Main.height,null);
			g2d.setColor(Color.decode("#b8dfff"));
			g2d.fill3DRect(startButtonX, startButtonY, buttonSizeX, buttonSizeY,startButton);
			g2d.fill3DRect(multiButtonX, multiButtonY, buttonSizeX, buttonSizeY, multiButton);
			g2d.setColor(Color.BLACK);
			g2d.draw3DRect(startButtonX, startButtonY, buttonSizeX, buttonSizeY,startButton);
			g2d.draw3DRect(multiButtonX, multiButtonY, buttonSizeX, buttonSizeY, multiButton);
			g2d.drawString("Start", startTextX, startTextY);
			g2d.drawString("Multiplayer", multiTextX, multiTextY);
		}
	}

	
	
	public static void renderBackScreen(Graphics2D g2d) {
		g2d.drawImage(backgraond.getImage(),0, 0, Main.width, Main.height,null);
	}
	
	public void setTextLocation() {
		startTextX = (startButtonX + ((buttonSizeX - metrics.stringWidth("Start")) / 2));		
		multiTextX = (multiButtonX + ((buttonSizeX - metrics.stringWidth("Multiplayer")) / 2));		
		
		startTextY = (startButtonY + ((buttonSizeY - metrics.getHeight()) / 2) + metrics.getAscent());		
		multiTextY = (multiButtonY + ((buttonSizeY - metrics.getHeight()) / 2) + metrics.getAscent());		

	}
	

	public void mousePressed(MouseEvent e) {

		int mouseX = e.getX()*Main.width/Main.screenWidth ,mouseY = e.getY()*Main.height/Main.screenHeight;
		//start game
		if(mouseX > startButtonX && mouseX < (startButtonX + buttonSizeX) && mouseY > startButtonY && mouseY < startButtonY + buttonSizeY) {
			startButton = false;
			Main.tilesManager.readFile();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			Main.gameState = 2;
			Main.host = true;
				
		//multi-player button
		}else if(mouseX > multiButtonX && mouseX < (multiButtonX + buttonSizeX) && mouseY > multiButtonY && mouseY < multiButtonY + buttonSizeY) {
			multiButton = false;
			serverScreen = true;
			new Client().checkAvailablePorts(serverSelect);
		}
		serverSelect.checkpress(mouseX, mouseY);
	}
	

	public void mouseReleased(MouseEvent e) {
		startButton = true;
		multiButton = true;
	}


	
}
