package Creature;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CreactureImage {
	
	final static ImageIcon[] slimeImage = new ImageIcon[4];
	final static ImageIcon[] cowImage = new ImageIcon[4];
	final static ImageIcon[] chickenImage = new ImageIcon[4];
	final static ImageIcon[] sheepImage = new ImageIcon[4];
	
	static {
		loadImage(slimeImage,"/slime.png");
		loadImage(cowImage,"/cow.png");
		loadImage(chickenImage,"/chicken.png");
		loadImage(sheepImage,"/sheep.png");
		
	}
	
	private static void loadImage(ImageIcon[] Creacture , String fileLoaction) {
		BufferedImage temp;
		try {
			/**
			 * 0 = down
			 * 1 = left
			 * 2 = right
			 * 3 = up
				*/
			temp = ImageIO.read(CreactureImage.class.getResourceAsStream(fileLoaction));
			for(int i = 0;i<4;i++) {
				Creacture[i] = new ImageIcon(temp.getSubimage(i*32, 0, 32, 32));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(fileLoaction);
		}
	}
	
	public static ImageIcon[] getSlimeImage() {
		return slimeImage;
	}
	
	public static ImageIcon[] getCowImage() {
		return cowImage;
	}
	public static ImageIcon[] getChickenImage() {
		return chickenImage;
	}
	public static ImageIcon[] getSheepImage() {
		return sheepImage;
	}
	
	
}
