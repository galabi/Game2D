package MainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import entity.FontLoader;
import multiplayer.ServerClientHandler;


public class PauseScreen {

	final int buttonSizeX = 200;
	final int buttonSizeY = 50;
	final int buttonX = 500;

	final int resumeButtonY = 300;
	final int lanButtonY    = 380;
	final int quitButtonY   = 460;

	Font font = FontLoader.getPixelFont(24);
	FontMetrics metrics;

	boolean resumePressed = false;
	boolean lanPressed    = false;
	boolean quitPressed   = false;

	int resumeTextX = -1, resumeTextY = -1;
	int lanTextX    = -1, lanTextY    = -1;
	int quitTextX   = -1, quitTextY   = -1;

	public void render(Graphics2D g2d) {
		if (resumeTextX < 0) {
			metrics = g2d.getFontMetrics(font);
			setTextLocations();
		}
		g2d.setFont(font);
		g2d.setColor(new Color(0, 0, 0, 100));
		g2d.fillRect(0, 0, Main.width, Main.height);

		drawButton(g2d, resumeButtonY, !resumePressed, "Resume", resumeTextX, resumeTextY);
		drawButton(g2d, lanButtonY,    !lanPressed,    "Open-lan", lanTextX, lanTextY);
		drawButton(g2d, quitButtonY,   !quitPressed,   "Quit", quitTextX, quitTextY);
	}

	private void drawButton(Graphics2D g2d, int y, boolean raised, String label, int tx, int ty) {
		g2d.setColor(Color.gray);
		g2d.fill3DRect(buttonX, y, buttonSizeX, buttonSizeY, raised);
		g2d.draw3DRect(buttonX, y, buttonSizeX, buttonSizeY, raised);
		g2d.setColor(Color.BLACK);
		g2d.drawString(label, tx, ty);
	}

	public void mousePressed(MouseEvent e) {
		int mx = e.getX() * Main.width / Main.screenWidth;
		int my = e.getY() * Main.height / Main.screenHeight;

		if (hitTest(mx, my, resumeButtonY)) {
			mouseReleased();
			Main.gameState = Main.GameState.GAME;
		} else if (hitTest(mx, my, lanButtonY)) {
			lanPressed = true;
			ServerClientHandler.openServer();
		} else if (hitTest(mx, my, quitButtonY)) {
			mouseReleased();
			Main.gameState = Main.GameState.START;
		}
	}

	public void mouseReleased() {
		resumePressed = false;
		lanPressed    = false;
		quitPressed   = false;
	}

	private boolean hitTest(int mx, int my, int y) {
		return mx > buttonX && mx < buttonX + buttonSizeX
			&& my > y && my < y + buttonSizeY;
	}

	private void setTextLocations() {
		resumeTextX = buttonX + (buttonSizeX - metrics.stringWidth("Resume"))   / 2;
		resumeTextY = resumeButtonY + (buttonSizeY - metrics.getHeight()) / 2 + metrics.getAscent();

		lanTextX = buttonX + (buttonSizeX - metrics.stringWidth("Open-lan")) / 2;
		lanTextY = lanButtonY + (buttonSizeY - metrics.getHeight()) / 2 + metrics.getAscent();

		quitTextX = buttonX + (buttonSizeX - metrics.stringWidth("Quit")) / 2;
		quitTextY = quitButtonY + (buttonSizeY - metrics.getHeight()) / 2 + metrics.getAscent();
	}
}
