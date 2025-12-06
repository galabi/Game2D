package playerPackage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import MainPackage.Main;

public class PlayerAnimation {
	ImageIcon[][] runningAnimation ;
	int runningFrameSize = 12,currentFrame; 
	long LastFrame = 0;
	final static int playerSpritSize =  Player.playerSpritSize; 
	
	
	public PlayerAnimation() {
		runningAnimation = new ImageIcon[4][runningFrameSize];
		loadImg();
	}
	
	public void render(Graphics2D g2d,int x,int y ,int sizeX,int sizeY) {
		
		g2d.drawImage(runningAnimation[Main.player.getImageDirection()][currentFrame].getImage(), x,
				y, sizeX, sizeY,null);
		
		
		if (System.currentTimeMillis() - LastFrame > 83){ // 83 Milliseconds between frame to get 12 FPS of animation 
			LastFrame = System.currentTimeMillis();
			currentFrame = (currentFrame + 1)%12;
		}
	}
	
	private void loadImg() {
		BufferedImage temp;
		try {
			/**
			 * 0 = down
			 * 1 = left
			 * 2 = right
			 * 3 = up
				*/
			for(int i = 0;i<4;i++) {
				temp = ImageIO.read(getClass().getResourceAsStream("/walk"+(i+1)+".png"));
				for(int j = 0;j<runningFrameSize;j++) {
					runningAnimation[i][j] = new ImageIcon(temp.getSubimage(j*playerSpritSize, 0, playerSpritSize, playerSpritSize));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem in loading player Animation [class:player]");
		}
	}
	
	public int getCurrentFrame(){
		return currentFrame;
	}
}
