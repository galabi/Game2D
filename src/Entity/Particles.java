package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import MainPackage.Main;

public class Particles extends entity {
	
	int id;
	long time;
	private static ImageIcon[] particalsImg;
	
	static {
		particalsImg = new ImageIcon[1];
		
		/*
    	particalsImg[0] = bubble;
		*/
		
		BufferedImage temp;
		try {
			temp = ImageIO.read(GameTextures.class.getResourceAsStream("/particals.png"));
			for(int i = 0;i<1 ;i++) {
				particalsImg[i] = new ImageIcon(temp.getSubimage(i*16,0, 16, 16));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Particles(int x, int y, int size,int id) {
		super(x, y, size,size);
		this.id  = id;
		time = System.currentTimeMillis();
	}
	
	@Override
	public void render(Graphics2D g2d) {
		int screenX = Main.tilesManager.getCameraX(false),screenY = Main.tilesManager.getCameraY(false);
		g2d.drawImage(particalsImg[id].getImage(),x-screenX,y-screenY,sizeX ,sizeY,null);
	}

	
	public long getTime(){
		return time;
	}
	
}

