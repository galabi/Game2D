package Entity;

import java.awt.*;
import java.io.*;

public class FontLoader {
	private static Font pixelFont;
	static {
	    try (InputStream is = FontLoader.class.getResourceAsStream("/Minecraftia-Regular.ttf")) {
	    	pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
	    } catch (Exception e) {
	        e.printStackTrace();
	        pixelFont = new Font("Monospaced", Font.BOLD, 16);
	    }
	}
	
	public static Font getPixelFont(float size) {
		return pixelFont.deriveFont(size); 
	}
}