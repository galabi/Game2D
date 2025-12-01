package Storage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Entity.FontLoader;

public class hoverItemInInventoey {
	
	int offSetX,offSetY,mouseX,mouseY,size = 64;
	Item item;
	Font font = FontLoader.getPixelFont(13);
	
	public hoverItemInInventoey(int offSetX,int offSetY,Item item) {
		this.item = item;
		this.offSetX = offSetX;
		this.offSetY = offSetY;
	}
	
	public void rander(Graphics2D g2d) {
		g2d.drawImage(item.getImage(), mouseX-offSetX, mouseY-offSetY, size, size, null);
		
		if(item.quantity <= 1)return;
		g2d.setColor(Color.black);
		g2d.setFont(font);
		g2d.drawString(String.valueOf(item.quantity), mouseX-offSetX+7, mouseY-offSetY+68);
	}

	public void setOffSetX(int offSetX) {
		this.offSetX = offSetX;
	}

	public void setOffSetY(int offSetY) {
		this.offSetY = offSetY;
	}
	
	public int getId() {
		return item.getId();
	}
	
}
