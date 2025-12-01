package Creature;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CreactureImage {
	
	final static ImageIcon[] slimeImage = new ImageIcon[4];
	final static ImageIcon[] cowImage = new ImageIcon[4];
	final static ImageIcon[] chickenImage = new ImageIcon[4];
	final static ImageIcon[] sheepImage = new ImageIcon[4];
	
	static {
		loadImage(slimeImage,"Creature_image_file/slime.png");
		loadImage(cowImage,"Creature_image_file/cow.png");
		loadImage(chickenImage,"Creature_image_file/chicken.png");
		loadImage(sheepImage,"Creature_image_file/sheep.png");
		
	}
	
	private static void loadImage(ImageIcon[] Creacture , String fileLoaction) {
		BufferedImage temp;
		try {
			/**
			 * 0 = dawn
			 * 1 = left
			 * 2 = right
			 * 3 = up
				*/
			temp = ImageIO.read(new File(fileLoaction));
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
