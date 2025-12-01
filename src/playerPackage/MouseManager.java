package playerPackage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import MainPackage.Main;

public class MouseManager implements MouseListener,MouseMotionListener, MouseWheelListener{

	int mouseX = 0, mouseY = 0;
	boolean mousePress = false;
	
	
	private void setMouselocation(MouseEvent e) {
		mouseX = e.getX()*Main.width/Main.screenWidth ;
		mouseY = e.getY()*Main.height/Main.screenHeight;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(Main.gameState == 2 && Main.inventory.IsOpen()) {
			Main.inventory.mouseDragged(e);
			
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMouselocation(e);
		if(Main.inventory.IsOpen()) {
			Main.inventory.mouseMoved(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePress = true;
		setMouselocation(e);
	
		switch (Main.gameState) {
		case 1: 
			Main.startscreen.mousePressed(e);
			break;
		case 2:
			if(!Main.inventory.IsOpen()) {
				PlayerInteraction.mousePress(mouseX, mouseY,e.getButton());
			}else {
				Main.inventory.mousePressed(e);
			}
			break;
		case 3:
			Main.pausescreen.mousePressed(e);
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePress = false;	
		setMouselocation(e);
		
		switch (Main.gameState) {
		case 1: 
			Main.startscreen.mouseReleased(e);
			break;
		case 2:
			if(!Main.inventory.IsOpen()) {
				PlayerInteraction.mouseReleased(mouseX, mouseY,e.getButton());
			}else {
				
			}
			break;
		case 3:
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public int getMouseX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}
	
}
