package multiplayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import MainPackage.Main;
import entity.FontLoader;

public class ServerSelectScreen{
	
	int x,y,sizeX,sizeY;
	int maxServers = 3;
	serverToJoin buttons[] = new serverToJoin[maxServers];
	
	
	public ServerSelectScreen(int x, int y, int sizeX, int sizeY) {
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	//make from an available server a button so the player can press
	public void addIp(String ip,int port) {
		for(int i = 0;i<maxServers;i++) {
			if(buttons[i] == null) {
				buttons[i] = new serverToJoin(x , y + (sizeY+20)*i, sizeX, sizeY, ip,port);
				return;
			}else if(buttons[i].ip.equals(ip+"."+port)) return;
		}
	}
	
	public void render(Graphics2D g2d) {
		for(int i = 0;i<maxServers;i++) {
			if(buttons[i] != null){
				buttons[i].render(g2d);
			}
		}
	}
	
	public void checkpress(int mouseX,int mouseY) {
		//check if the player press any of the available servers
		for(serverToJoin availableServers: buttons) {
			try {
				availableServers.ispressd(mouseX, mouseY);
			} catch (Exception e) {}
		}
	}
	
	
	//sub_class
	private class serverToJoin{
		String ip;
		int port,x,y,sizeX,sizeY;
		boolean press = true;
		Font font = FontLoader.getPixelFont(16);

		
		public serverToJoin(int x, int y, int sizeX,int sizeY, String ip,int port) {
			this.x = x;
			this.y = y;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.ip = ip;
			this.port = port;
		}
		public void render(Graphics2D g2d) {

			g2d.setFont(font);
			g2d.setColor(Color.decode("#b8dfff"));
			g2d.draw3DRect(x, y, sizeX, sizeY, press);
			g2d.fill3DRect(x, y, sizeX, sizeY, press);
			g2d.setColor(Color.black);
			g2d.drawString(ip+"."+port, x + 15, y + 30);
		}
		
		private boolean ispressd(int mouseX,int mouseY){
			if(mouseX > x && mouseX < (x + sizeX) && mouseY > y && mouseY < y + sizeY) {
				ServerClientHandler.openClient(port);
				
				
				while(!Main.tilesManager.isMapIsReady()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				Main.gameState = Main.GameState.GAME;
				Main.host = false;
				
				press = true;
				return press;
			}
			return false;
		}
	}
}
