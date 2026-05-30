package MainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import creature.CreatureManager;
import creature.SpawnManager;
import entity.FontLoader;
import multiplayer.Client;
import multiplayer.ServerSelectScreen;


public class StartScreen{

	int buttonSizeX = 200,buttonSizeY = 50,startButtonX = 500,startButtonY = 520,multiButtonX = 500,multiButtonY = 600;
	int startTextX = -1, multiTextX =-1,startTextY = -1, multiTextY =-1;
	boolean multiButton = true,startButton = true,serverScreen = false;
	Font font = FontLoader.getPixelFont(24);
	public static ImageIcon backgraond;
	ServerSelectScreen serverSelect;
	NameInputBox nameInput = new NameInputBox(450, 440, 300, 45);

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

			// name label + input box
			g2d.setFont(FontLoader.getPixelFont(16));
			g2d.setColor(Color.WHITE);
			g2d.drawString("Name:", 450, 430);
			nameInput.render(g2d);

			g2d.setFont(font);
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
	

	public void keyTyped(KeyEvent e) {
		nameInput.keyTyped(e.getKeyChar());
	}

	public void mousePressed(MouseEvent e) {

		int mouseX = e.getX()*Main.width/Main.screenWidth ,mouseY = e.getY()*Main.height/Main.screenHeight;
		nameInput.mousePressed(mouseX, mouseY);

		//start game
		if(mouseX > startButtonX && mouseX < (startButtonX + buttonSizeX) && mouseY > startButtonY && mouseY < startButtonY + buttonSizeY) {
			if(nameInput.getText().isEmpty()) return;
			startButton = false;
			Main.playerName = nameInput.getText();
			CreatureManager.getCreatures().clear();
			SpawnManager.reset();
			Main.tilesManager.resetMap();
			Main.tilesManager.readFile();
			Main.inventory.loadInventory();
			Main.tilesManager.setCameraX(Main.player.getX() - Main.width / 2 + Main.player.getSizeX() / 2);
			Main.tilesManager.setCameraY(Main.player.getY() - Main.height / 2 + Main.player.getSizeY() / 2);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			Main.gameState = Main.GameState.GAME;
			Main.host = true;

		//multi-player button
		}else if(mouseX > multiButtonX && mouseX < (multiButtonX + buttonSizeX) && mouseY > multiButtonY && mouseY < multiButtonY + buttonSizeY) {
			if(nameInput.getText().isEmpty()) return;
			Main.playerName = nameInput.getText();
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
