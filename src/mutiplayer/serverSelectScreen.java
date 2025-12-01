package mutiplayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Entity.FontLoader;
import Entity.entity;
import MainPackage.Main;

public class serverSelectScreen extends entity {
	
	int maxServers = 3;
	serverToJoin buttons[] = new serverToJoin[maxServers];
	
	
	public serverSelectScreen(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
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
	
	@Override
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
	private class serverToJoin extends entity{
		String ip;
		int port;
		boolean press = true;
		Font font = FontLoader.getPixelFont(16);

		
		public serverToJoin(int x, int y, int sizeX,int sizeY, String ip,int port) {
			super(x, y, sizeX,sizeY);
			this.ip = ip;
			this.port = port;
		}
		@Override
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
				serverClientHandler.openClient(port);
				
				
				while(!Main.tilesManager.isMapIsReady()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				Main.gameState = 2;
				Main.host = false;
				
				press = true;
				return press;
			}
			return false;
		}
	}
}
