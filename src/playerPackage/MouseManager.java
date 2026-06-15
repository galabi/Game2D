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
		if(Main.gameState == Main.GameState.GAME && Main.inventory.isOpen()) {
			Main.inventory.mouseDragged(e);
			
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMouselocation(e);
		if (Main.gameState == Main.GameState.START) {
			Main.startscreen.mouseMoved(mouseX, mouseY);
		}
		if(Main.chestUI.isOpen()) {
			Main.chestUI.mouseMoved(e);
		}
		if(Main.workbenchUI.isOpen()) {
			Main.workbenchUI.mouseMoved(e);
		}
		if(Main.inventory.isOpen()) {
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
		case START:
			Main.startscreen.mousePressed(e);
			break;
		case GAME:
			if (Main.chestUI.isOpen()) {
				Main.chestUI.mousePressed(e);
			} else if (Main.workbenchUI.isOpen()) {
				Main.workbenchUI.mousePressed(e);
			} else if(!Main.inventory.isOpen()) {
				PlayerInteraction.mousePress(mouseX, mouseY,e.getButton());
			}else {
				Main.inventory.mousePressed(e);
			}
			break;
		case PAUSE:
			Main.pausescreen.mousePressed(e);
			break;
		case GAME_OVER:
			Main.gameOverScreen.mousePressed(e);
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePress = false;	
		setMouselocation(e);
		
		switch (Main.gameState) {
		case START:
			Main.startscreen.mouseReleased(e);
			break;
		case GAME:
			if(!Main.inventory.isOpen()) {
				PlayerInteraction.mouseReleased(mouseX, mouseY,e.getButton());
			}
			break;
		case PAUSE:
			Main.pausescreen.mouseReleased();
			break;
		case GAME_OVER:
			Main.gameOverScreen.mouseReleased();
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
