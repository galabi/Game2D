package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Text{
	String text;
	int x;
	int y;
	int size;
	BufferedImage textImage;
	Font font;
	
	public Text(String text,int x, int y, int size) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.size = size;
		font = new Font("SansSerif", Font.BOLD, size);
		
		createText();
	}
	
	public void createText() {
	    BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = tempImg.createGraphics();
	    g2.setFont(font);
	    FontMetrics metrics = g2.getFontMetrics();
	    int width = metrics.stringWidth(text);
	    int height = metrics.getHeight();
	    g2.dispose();

	    textImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    g2 = textImage.createGraphics();

	    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	    g2.setFont(font);
	    g2.setColor(Color.black); // 
	    metrics = g2.getFontMetrics();
	    int ascent = metrics.getAscent();

	    // צייר את הטקסט
	    g2.drawString(text, 0, ascent);
	    g2.dispose();
	
	}
	
	public void render(Graphics2D g2d) {
		g2d.drawImage(textImage, x, y, null);
	}
	
}
