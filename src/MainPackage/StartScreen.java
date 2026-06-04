package MainPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import creature.CreatureManager;
import creature.SpawnManager;
import entity.FontLoader;
import multiplayer.Client;
import multiplayer.ServerSelectScreen;


public class StartScreen {

    private static final int BTN_W = 260, BTN_H = 55;
    private static final int BTN_X = (Main.width - BTN_W) / 2;
    private static final int START_BTN_Y = 415;
    private static final int MULTI_BTN_Y  = 495;
    private static final int INPUT_X = (Main.width - 300) / 2;
    private static final int INPUT_Y = 345;
    private static final int PANEL_X = (Main.width - 360) / 2;
    private static final int PANEL_Y = 275;
    private static final int PANEL_W = 360;
    private static final int PANEL_H = 335;

    private static final Color BTN_NORMAL   = new Color(55,  90, 150);
    private static final Color BTN_HOVER    = new Color(85, 130, 205);
    private static final Color BTN_PRESSED  = new Color(35,  60, 110);
    private static final Color BTN_BORDER   = new Color(30,  60, 110);
    private static final Color PANEL_BG     = new Color(0, 0, 0, 160);
    private static final Color PANEL_BORDER = new Color(255, 255, 255, 70);
    private static final Color TITLE_SHADOW = new Color(0, 0, 0, 200);
    private static final Color LABEL_COLOR  = new Color(200, 220, 255);

    boolean startPressed = false, multiPressed = false;
    boolean hoverStart   = false, hoverMulti  = false;
    boolean serverScreen = false;

    Font titleFont  = FontLoader.getPixelFont(60);
    Font buttonFont = FontLoader.getPixelFont(22);
    Font labelFont  = FontLoader.getPixelFont(14);
    Font versionFont = FontLoader.getPixelFont(11);

    public static ImageIcon background;
    ServerSelectScreen serverSelect;
    NameInputBox nameInput = new NameInputBox(INPUT_X, INPUT_Y, 300, 45);

    public StartScreen() {
        background = new ImageIcon(getClass().getResource("/backgraond.png"));
        serverSelect = new ServerSelectScreen(800, START_BTN_Y, 270, 30);
    }

    public void tick() { }

    public void render(Graphics2D g2d) {
        if (serverScreen) {
            serverSelect.render(g2d);
            return;
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(background.getImage(), 0, 0, Main.width, Main.height, null);

        drawTitle(g2d);
        drawPanel(g2d);

        g2d.setFont(labelFont);
        g2d.setColor(LABEL_COLOR);
        g2d.drawString("Enter your name", INPUT_X, INPUT_Y - 8);

        nameInput.render(g2d);

        drawButton(g2d, "Singleplayer", BTN_X, START_BTN_Y, startPressed, hoverStart);
        drawButton(g2d, "Multiplayer",  BTN_X, MULTI_BTN_Y,  multiPressed, hoverMulti);

        g2d.setFont(versionFont);
        g2d.setColor(new Color(180, 180, 180, 160));
        g2d.drawString("v0.1", Main.width - 52, Main.height - 15);
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(titleFont);
        FontMetrics tm = g2d.getFontMetrics();
        String title = "Game2D";
        int tx = (Main.width - tm.stringWidth(title)) / 2;
        int ty = 210;
        g2d.setColor(TITLE_SHADOW);
        g2d.drawString(title, tx + 4, ty + 4);
        g2d.setColor(Color.WHITE);
        g2d.drawString(title, tx, ty);
    }

    private void drawPanel(Graphics2D g2d) {
        g2d.setColor(PANEL_BG);
        g2d.fillRoundRect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H, 18, 18);
        g2d.setColor(PANEL_BORDER);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H, 18, 18);
    }

    private void drawButton(Graphics2D g2d, String label, int bx, int by, boolean pressed, boolean hover) {
        Color bg = pressed ? BTN_PRESSED : hover ? BTN_HOVER : BTN_NORMAL;

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(bx + 3, by + 4, BTN_W, BTN_H, 12, 12);

        g2d.setColor(bg);
        g2d.fillRoundRect(bx, by, BTN_W, BTN_H, 12, 12);

        g2d.setColor(BTN_BORDER);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(bx, by, BTN_W, BTN_H, 12, 12);

        if (!pressed) {
            g2d.setColor(new Color(255, 255, 255, 40));
            g2d.fillRoundRect(bx + 2, by + 2, BTN_W - 4, BTN_H / 3, 10, 10);
        }

        g2d.setFont(buttonFont);
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int tx = bx + (BTN_W - fm.stringWidth(label)) / 2;
        int ty = by + (BTN_H - fm.getHeight()) / 2 + fm.getAscent() + (pressed ? 1 : 0);
        g2d.drawString(label, tx, ty);
    }

    public static void renderBackScreen(Graphics2D g2d) {
        g2d.drawImage(background.getImage(), 0, 0, Main.width, Main.height, null);
    }

    public void mouseMoved(int mx, int my) {
        if (serverScreen) {
            serverSelect.mouseMoved(mx, my);
            return;
        }
        hoverStart = mx >= BTN_X && mx <= BTN_X + BTN_W && my >= START_BTN_Y && my <= START_BTN_Y + BTN_H;
        hoverMulti = mx >= BTN_X && mx <= BTN_X + BTN_W && my >= MULTI_BTN_Y  && my <= MULTI_BTN_Y  + BTN_H;
    }

    public void keyTyped(KeyEvent e) {
        nameInput.keyTyped(e.getKeyChar());
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX() * Main.width / Main.screenWidth;
        int my = e.getY() * Main.height / Main.screenHeight;

        if (serverScreen) {
            serverSelect.checkpress(mx, my);
            if (serverSelect.isGoBack()) {
                serverScreen = false;
                serverSelect.resetGoBack();
            }
            return;
        }

        nameInput.mousePressed(mx, my);

        if (mx >= BTN_X && mx <= BTN_X + BTN_W && my >= START_BTN_Y && my <= START_BTN_Y + BTN_H) {
            if (nameInput.getText().isEmpty()) return;
            startPressed = true;
            Main.playerName = nameInput.getText();
            CreatureManager.getCreatures().clear();
            SpawnManager.reset();
            Main.tilesManager.resetMap();
            Main.tilesManager.readFile();
            Main.inventory.loadInventory();
            Main.tilesManager.setCameraX(Main.player.getX() - Main.width / 2 + Main.player.getSizeX() / 2);
            Main.tilesManager.setCameraY(Main.player.getY() - Main.height / 2 + Main.player.getSizeY() / 2);
            try { Thread.sleep(100); } catch (InterruptedException ex) { ex.printStackTrace(); }
            Main.gameState = Main.GameState.GAME;
            Main.host = true;

        } else if (mx >= BTN_X && mx <= BTN_X + BTN_W && my >= MULTI_BTN_Y && my <= MULTI_BTN_Y + BTN_H) {
            if (nameInput.getText().isEmpty()) return;
            Main.playerName = nameInput.getText();
            multiPressed = true;
            serverScreen = true;
            new Client().checkAvailablePorts(serverSelect);
        }
    }

    public void mouseReleased(MouseEvent e) {
        startPressed = false;
        multiPressed = false;
        serverSelect.mouseReleased();
    }
}
