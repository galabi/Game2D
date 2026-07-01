package MainPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import creature.CreatureManager;
import creature.SpawnManager;
import entity.FontLoader;
import multiplayer.Client;
import multiplayer.ServerSelectScreen;

public class StartScreen {

    // Panel
    private static final int PANEL_W = 440;
    private static final int PANEL_H = 445;
    private static final int PANEL_X = (Main.width - PANEL_W) / 2;
    private static final int PANEL_Y = 215;

    // Player name input
    private static final int INPUT_W = 320;
    private static final int INPUT_X = (Main.width - INPUT_W) / 2;
    private static final int INPUT_Y = PANEL_Y + 38;
    private static final int INPUT_H = 40;

    // World list
    private static final int LIST_X      = PANEL_X + 16;
    private static final int LIST_W      = PANEL_W - 32;
    private static final int LIST_Y      = INPUT_Y + INPUT_H + 48;
    private static final int ROW_H       = 44;
    private static final int ROW_GAP     = 4;
    private static final int ROW_CELL    = ROW_H + ROW_GAP;
    private static final int MAX_ROWS    = 4;
    private static final int LIST_AREA_H = MAX_ROWS * ROW_CELL;

    // Play button inside each row
    private static final int PLAY_W = 72;
    private static final int PLAY_H = 30;

    // Delete button inside each row
    private static final int DEL_W = 28;
    private static final int DEL_H = PLAY_H;

    // Confirm-delete dialog
    private static final int CONFIRM_W     = 320;
    private static final int CONFIRM_H     = 140;
    private static final int CONFIRM_X     = (Main.width - CONFIRM_W) / 2;
    private static final int CONFIRM_Y     = (Main.height - CONFIRM_H) / 2;
    private static final int CONFIRM_BTN_W = 120;
    private static final int CONFIRM_BTN_H = 34;
    private static final int CONFIRM_BTN_Y = CONFIRM_Y + CONFIRM_H - CONFIRM_BTN_H - 16;
    private static final int CONFIRM_NO_X  = CONFIRM_X + CONFIRM_W / 2 - CONFIRM_BTN_W - 6;
    private static final int CONFIRM_YES_X = CONFIRM_X + CONFIRM_W / 2 + 6;

    // New world area
    private static final int NW_Y          = LIST_Y + LIST_AREA_H + 8;
    private static final int NW_H          = 38;
    private static final int WORLD_INPUT_W = 296;

    // Multiplayer button
    private static final int MULTI_Y = NW_Y + NW_H + 10;
    private static final int MULTI_W = 260;
    private static final int MULTI_X = (Main.width - MULTI_W) / 2;
    private static final int MULTI_H = 50;

    // Colors
    private static final Color BTN_DELETE        = new Color(160,  40,  40);
    private static final Color BTN_DELETE_HOVER  = new Color(210,  60,  60);
    private static final Color BTN_DELETE_BORDER = new Color(100,  20,  20);
    private static final Color BTN_NORMAL   = new Color(55,  90, 150);
    private static final Color BTN_HOVER    = new Color(85, 130, 205);
    private static final Color BTN_PRESSED  = new Color(35,  60, 110);
    private static final Color BTN_BORDER   = new Color(30,  60, 110);
    private static final Color PANEL_BG     = new Color(0, 0, 0, 160);
    private static final Color PANEL_BORDER = new Color(255, 255, 255, 70);
    private static final Color TITLE_SHADOW = new Color(0, 0, 0, 200);
    private static final Color LABEL_COLOR  = new Color(200, 220, 255);

    // State
    boolean multiPressed = false;
    boolean hoverMulti   = false;
    boolean hoverNewWorld = false;
    boolean hoverCreate  = false;
    boolean serverScreen = false;
    boolean showNewWorldInput = false;
    int hoveredRow       = -1;
    int hoveredDeleteRow = -1;
    String confirmDeleteWorld = null;
    boolean hoverConfirmYes = false;
    boolean hoverConfirmNo  = false;
    List<String> worldList = new ArrayList<>();
    int scrollOffset = 0;
    String errorMsg = "";

    // Fonts
    Font titleFont   = FontLoader.getPixelFont(60);
    Font buttonFont  = FontLoader.getPixelFont(22);
    Font labelFont   = FontLoader.getPixelFont(14);
    Font smallFont   = FontLoader.getPixelFont(11);
    Font versionFont = FontLoader.getPixelFont(11);

    public static ImageIcon background;
    ServerSelectScreen serverSelect;
    NameInputBox nameInput      = new NameInputBox(INPUT_X, INPUT_Y, INPUT_W, INPUT_H);
    NameInputBox worldNameInput = new NameInputBox(LIST_X, NW_Y, WORLD_INPUT_W, NW_H);

    public StartScreen() {
        background = new ImageIcon(getClass().getResource("/backgraond.png"));
        serverSelect = new ServerSelectScreen(800, MULTI_Y, 270, 30);
        refreshWorldList();
    }

    private void refreshWorldList() {
        worldList = WorldManager.listWorlds();
        scrollOffset = 0;
    }

    public void tick() {
        refreshWorldList();
    }

    public void render(Graphics2D g2d) {
        if (serverScreen) { serverSelect.render(g2d); return; }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(background.getImage(), 0, 0, Main.width, Main.height, null);

        drawTitle(g2d);
        drawPanel(g2d);

        // Player name
        g2d.setFont(labelFont);
        g2d.setColor(LABEL_COLOR);
        g2d.drawString("Enter your name", INPUT_X, INPUT_Y - 10);
        nameInput.render(g2d);

        // Worlds label
        g2d.setFont(labelFont);
        g2d.setColor(LABEL_COLOR);
        g2d.drawString("Worlds", LIST_X, INPUT_Y + INPUT_H + 30);

        // World list
        drawWorldList(g2d);

        // New World button / input
        if (showNewWorldInput) {
            worldNameInput.render(g2d);
            int createX = LIST_X + WORLD_INPUT_W + 6;
            int createW = LIST_X + LIST_W - createX;
            drawSmallButton(g2d, "Create", createX, NW_Y, createW, NW_H, false, hoverCreate);
        } else {
            drawSmallButton(g2d, "+ New World", LIST_X, NW_Y, 180, NW_H, false, hoverNewWorld);
        }

        if (!errorMsg.isEmpty()) {
            g2d.setFont(smallFont);
            g2d.setColor(new Color(255, 100, 100));
            g2d.drawString(errorMsg, LIST_X, NW_Y + NW_H + 14);
        }

        // Multiplayer
        drawButton(g2d, "Multiplayer", MULTI_X, MULTI_Y, MULTI_W, MULTI_H, multiPressed, hoverMulti);

        // Version
        g2d.setFont(versionFont);
        g2d.setColor(new Color(180, 180, 180, 160));
        g2d.drawString("v0.1", Main.width - 52, Main.height - 15);

        if (confirmDeleteWorld != null) drawConfirmDialog(g2d);
    }

    private void drawWorldList(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRoundRect(LIST_X, LIST_Y, LIST_W, LIST_AREA_H, 8, 8);
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(LIST_X, LIST_Y, LIST_W, LIST_AREA_H, 8, 8);

        if (worldList.isEmpty()) {
            g2d.setFont(smallFont);
            g2d.setColor(new Color(180, 180, 180, 150));
            String msg = "No worlds yet — create one below";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, LIST_X + (LIST_W - fm.stringWidth(msg)) / 2,
                           LIST_Y + LIST_AREA_H / 2 + 5);
            return;
        }

        int endIdx = Math.min(scrollOffset + MAX_ROWS, worldList.size());
        for (int i = scrollOffset; i < endIdx; i++) {
            int visRow = i - scrollOffset;
            int rowTop = LIST_Y + visRow * ROW_CELL;
            String name = worldList.get(i);
            long seed   = WorldManager.getSeedFor(name);
            boolean rowHov = (i == hoveredRow) || (i == hoveredDeleteRow);

            if (rowHov) {
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillRoundRect(LIST_X + 2, rowTop + 1, LIST_W - 4, ROW_H - 2, 6, 6);
            }

            g2d.setFont(labelFont);
            g2d.setColor(Color.WHITE);
            g2d.drawString(name, LIST_X + 10, rowTop + 26);

            g2d.setFont(smallFont);
            g2d.setColor(new Color(150, 170, 200));
            g2d.drawString("seed: " + seed, LIST_X + 10, rowTop + 38);

            int playX = LIST_X + LIST_W - PLAY_W - 8;
            int playY = rowTop + (ROW_H - PLAY_H) / 2;
            drawSmallButton(g2d, "Play", playX, playY, PLAY_W, PLAY_H, false, i == hoveredRow);
            int delX = playX - DEL_W - 4;
            drawRedButton(g2d, "X", delX, playY, DEL_W, DEL_H, i == hoveredDeleteRow);

            if (i < endIdx - 1) {
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(LIST_X + 8, rowTop + ROW_H, LIST_X + LIST_W - 8, rowTop + ROW_H);
            }
        }

        // Scroll arrows
        if (scrollOffset > 0) {
            g2d.setFont(smallFont);
            g2d.setColor(Color.WHITE);
            g2d.drawString("▲", LIST_X + LIST_W - 16, LIST_Y + 14);
        }
        if (scrollOffset + MAX_ROWS < worldList.size()) {
            g2d.setFont(smallFont);
            g2d.setColor(Color.WHITE);
            g2d.drawString("▼", LIST_X + LIST_W - 16, LIST_Y + LIST_AREA_H - 4);
        }
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(titleFont);
        FontMetrics tm = g2d.getFontMetrics();
        String title = "Game2D";
        int tx = (Main.width - tm.stringWidth(title)) / 2;
        int ty = 175;
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

    private void drawButton(Graphics2D g2d, String label, int bx, int by, int bw, int bh,
                             boolean pressed, boolean hover) {
        Color bg = pressed ? BTN_PRESSED : hover ? BTN_HOVER : BTN_NORMAL;
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(bx + 3, by + 4, bw, bh, 12, 12);
        g2d.setColor(bg);
        g2d.fillRoundRect(bx, by, bw, bh, 12, 12);
        g2d.setColor(BTN_BORDER);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(bx, by, bw, bh, 12, 12);
        if (!pressed) {
            g2d.setColor(new Color(255, 255, 255, 40));
            g2d.fillRoundRect(bx + 2, by + 2, bw - 4, bh / 3, 10, 10);
        }
        g2d.setFont(buttonFont);
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(label, bx + (bw - fm.stringWidth(label)) / 2,
                       by + (bh - fm.getHeight()) / 2 + fm.getAscent() + (pressed ? 1 : 0));
    }

    private void drawSmallButton(Graphics2D g2d, String label, int bx, int by, int bw, int bh,
                                  boolean pressed, boolean hover) {
        Color bg = pressed ? BTN_PRESSED : hover ? BTN_HOVER : BTN_NORMAL;
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillRoundRect(bx + 2, by + 3, bw, bh, 8, 8);
        g2d.setColor(bg);
        g2d.fillRoundRect(bx, by, bw, bh, 8, 8);
        g2d.setColor(BTN_BORDER);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(bx, by, bw, bh, 8, 8);
        g2d.setFont(labelFont);
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(label, bx + (bw - fm.stringWidth(label)) / 2,
                       by + (bh - fm.getHeight()) / 2 + fm.getAscent());
    }

    private void drawRedButton(Graphics2D g2d, String label, int bx, int by, int bw, int bh, boolean hover) {
        Color bg = hover ? BTN_DELETE_HOVER : BTN_DELETE;
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillRoundRect(bx + 2, by + 3, bw, bh, 8, 8);
        g2d.setColor(bg);
        g2d.fillRoundRect(bx, by, bw, bh, 8, 8);
        g2d.setColor(BTN_DELETE_BORDER);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(bx, by, bw, bh, 8, 8);
        g2d.setFont(labelFont);
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(label, bx + (bw - fm.stringWidth(label)) / 2,
                       by + (bh - fm.getHeight()) / 2 + fm.getAscent());
    }

    private void drawConfirmDialog(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, Main.width, Main.height);

        g2d.setColor(new Color(15, 15, 30, 245));
        g2d.fillRoundRect(CONFIRM_X, CONFIRM_Y, CONFIRM_W, CONFIRM_H, 14, 14);
        g2d.setColor(PANEL_BORDER);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(CONFIRM_X, CONFIRM_Y, CONFIRM_W, CONFIRM_H, 14, 14);

        g2d.setFont(buttonFont);
        g2d.setColor(Color.WHITE);
        String title = "Delete world?";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, CONFIRM_X + (CONFIRM_W - fm.stringWidth(title)) / 2, CONFIRM_Y + 40);

        g2d.setFont(smallFont);
        g2d.setColor(new Color(200, 200, 200));
        String msg = "\"" + confirmDeleteWorld + "\" will be deleted forever.";
        FontMetrics sfm = g2d.getFontMetrics();
        g2d.drawString(msg, CONFIRM_X + (CONFIRM_W - sfm.stringWidth(msg)) / 2, CONFIRM_Y + 63);

        drawSmallButton(g2d, "Cancel", CONFIRM_NO_X,  CONFIRM_BTN_Y, CONFIRM_BTN_W, CONFIRM_BTN_H, false, hoverConfirmNo);
        drawRedButton(g2d,   "Delete", CONFIRM_YES_X, CONFIRM_BTN_Y, CONFIRM_BTN_W, CONFIRM_BTN_H, hoverConfirmYes);
    }

    public static void renderBackScreen(Graphics2D g2d) {
        g2d.drawImage(background.getImage(), 0, 0, Main.width, Main.height, null);
    }

    public void mouseMoved(int mx, int my) {
        if (serverScreen) { serverSelect.mouseMoved(mx, my); return; }

        if (confirmDeleteWorld != null) {
            hoverConfirmYes = inRect(mx, my, CONFIRM_YES_X, CONFIRM_BTN_Y, CONFIRM_BTN_W, CONFIRM_BTN_H);
            hoverConfirmNo  = inRect(mx, my, CONFIRM_NO_X,  CONFIRM_BTN_Y, CONFIRM_BTN_W, CONFIRM_BTN_H);
            return;
        }

        hoverMulti    = inRect(mx, my, MULTI_X, MULTI_Y, MULTI_W, MULTI_H);
        hoverNewWorld = !showNewWorldInput && inRect(mx, my, LIST_X, NW_Y, 180, NW_H);

        int createX = LIST_X + WORLD_INPUT_W + 6;
        int createW = LIST_X + LIST_W - createX;
        hoverCreate = showNewWorldInput && inRect(mx, my, createX, NW_Y, createW, NW_H);

        hoveredRow = -1;
        hoveredDeleteRow = -1;
        int endIdx = Math.min(scrollOffset + MAX_ROWS, worldList.size());
        for (int i = scrollOffset; i < endIdx; i++) {
            int visRow = i - scrollOffset;
            int rowTop = LIST_Y + visRow * ROW_CELL;
            int playX  = LIST_X + LIST_W - PLAY_W - 8;
            int playY  = rowTop + (ROW_H - PLAY_H) / 2;
            if (inRect(mx, my, playX, playY, PLAY_W, PLAY_H))
                hoveredRow = i;
            int delX = playX - DEL_W - 4;
            if (inRect(mx, my, delX, playY, DEL_W, DEL_H))
                hoveredDeleteRow = i;
        }
    }

    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (showNewWorldInput && (c == '\r' || c == '\n')) { submitNewWorld(); return; }
        nameInput.keyTyped(c);
        if (showNewWorldInput) { worldNameInput.keyTyped(c); errorMsg = ""; }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (confirmDeleteWorld != null) { confirmDeleteWorld = null; return; }
            if (showNewWorldInput) { showNewWorldInput = false; errorMsg = ""; }
        }
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX() * Main.width / Main.screenWidth;
        int my = e.getY() * Main.height / Main.screenHeight;

        if (serverScreen) {
            serverSelect.checkpress(mx, my);
            if (serverSelect.isGoBack()) { serverScreen = false; serverSelect.resetGoBack(); }
            return;
        }

        if (confirmDeleteWorld != null) {
            if (inRect(mx, my, CONFIRM_YES_X, CONFIRM_BTN_Y, CONFIRM_BTN_W, CONFIRM_BTN_H)) {
                WorldManager.deleteWorld(confirmDeleteWorld);
                refreshWorldList();
            }
            confirmDeleteWorld = null;
            return;
        }

        nameInput.mousePressed(mx, my);
        if (showNewWorldInput) worldNameInput.mousePressed(mx, my);

        // Multiplayer
        if (inRect(mx, my, MULTI_X, MULTI_Y, MULTI_W, MULTI_H)) {
            if (nameInput.getText().isEmpty()) return;
            Main.playerName = nameInput.getText();
            multiPressed = true;
            serverScreen = true;
            new Client().checkAvailablePorts(serverSelect);
            return;
        }

        // "Create" button when world name input is visible
        if (showNewWorldInput) {
            int createX = LIST_X + WORLD_INPUT_W + 6;
            int createW = LIST_X + LIST_W - createX;
            if (inRect(mx, my, createX, NW_Y, createW, NW_H)) {
                submitNewWorld();
                return;
            }
            return; // swallow clicks while world input is open
        }

        // "+ New World" button
        if (inRect(mx, my, LIST_X, NW_Y, 180, NW_H)) {
            showNewWorldInput = true;
            errorMsg = "";
            return;
        }

        // Scroll arrows
        if (scrollOffset > 0
                && inRect(mx, my, LIST_X + LIST_W - 20, LIST_Y, 18, 16))
            scrollOffset--;
        if (scrollOffset + MAX_ROWS < worldList.size()
                && inRect(mx, my, LIST_X + LIST_W - 20, LIST_Y + LIST_AREA_H - 16, 18, 16))
            scrollOffset++;

        // Play button for each world row
        int endIdx = Math.min(scrollOffset + MAX_ROWS, worldList.size());
        for (int i = scrollOffset; i < endIdx; i++) {
            int visRow = i - scrollOffset;
            int rowTop = LIST_Y + visRow * ROW_CELL;
            int playX  = LIST_X + LIST_W - PLAY_W - 8;
            int playY  = rowTop + (ROW_H - PLAY_H) / 2;
            int delX   = playX - DEL_W - 4;
            if (inRect(mx, my, delX, playY, DEL_W, DEL_H)) {
                confirmDeleteWorld = worldList.get(i);
                return;
            }
            if (nameInput.getText().isEmpty()) continue;
            if (inRect(mx, my, playX, playY, PLAY_W, PLAY_H)) {
                loadWorld(worldList.get(i));
                return;
            }
        }
    }

    private void submitNewWorld() {
        String worldName = worldNameInput.getText();
        if (worldName.isEmpty()) return;
        if (nameInput.getText().isEmpty()) { errorMsg = "Enter your name first!"; return; }
        if (WorldManager.exists(worldName)) { errorMsg = "World already exists!"; return; }
        WorldManager.setWorld(worldName);
        WorldManager.writeSeed(new Random().nextLong());
        startGame();
    }

    private void loadWorld(String worldName) {
        WorldManager.setWorld(worldName);
        startGame();
    }

    private void startGame() {
        showNewWorldInput = false;
        errorMsg = "";
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
    }

    public void mouseReleased(MouseEvent e) {
        multiPressed = false;
        serverSelect.mouseReleased();
    }

    private boolean inRect(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
