package MainPackage;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import creature.CreatureManager;
import entity.FontLoader;

public class GameOverScreen {

	private final int buttonX = 500, buttonY = 450;
	private final int buttonW = 200, buttonH = 50;
	private boolean buttonPressed = false;
	private java.awt.Font font = FontLoader.getPixelFont(24);

	public void render(Graphics2D g2d) {
		g2d.setColor(new Color(0, 0, 0, 180));
		g2d.fillRect(0, 0, Main.width, Main.height);

		g2d.setFont(FontLoader.getPixelFont(48));
		g2d.setColor(Color.RED);
		FontMetrics fm = g2d.getFontMetrics();
		String title = "Game Over";
		g2d.drawString(title, (Main.width - fm.stringWidth(title)) / 2, Main.height / 2 - 40);

		g2d.setFont(font);
		g2d.setColor(new Color(184, 223, 255));
		g2d.fill3DRect(buttonX, buttonY, buttonW, buttonH, !buttonPressed);
		g2d.setColor(Color.BLACK);
		g2d.draw3DRect(buttonX, buttonY, buttonW, buttonH, !buttonPressed);
		fm = g2d.getFontMetrics();
		String label = "Restart";
		g2d.drawString(label,
			buttonX + (buttonW - fm.stringWidth(label)) / 2,
			buttonY + (buttonH - fm.getHeight()) / 2 + fm.getAscent());
	}

	public void mousePressed(MouseEvent e) {
		int mx = e.getX() * Main.width / Main.screenWidth;
		int my = e.getY() * Main.height / Main.screenHeight;
		if (mx > buttonX && mx < buttonX + buttonW && my > buttonY && my < buttonY + buttonH) {
			buttonPressed = true;
			restart();
		}
	}

	public void mouseReleased() {
		buttonPressed = false;
	}

	private void restart() {
		CreatureManager.getCreatures().clear();
		Main.player.setHealth(Main.player.getMaxHealth());
		Main.tilesManager.resetMap();
		Main.tilesManager.readFile();
		CreatureManager.createCreature(1350, 1350, "slime");
		Main.gameState = Main.GameState.GAME;
		Main.host = true;
	}
}
