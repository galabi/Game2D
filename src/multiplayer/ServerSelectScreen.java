package multiplayer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import MainPackage.Main;
import MainPackage.StartScreen;
import entity.FontLoader;

public class ServerSelectScreen {

    private static final int PANEL_W = 400;
    private static final int PANEL_H = 390;
    private static final int PANEL_X = (Main.width - PANEL_W) / 2;
    private static final int PANEL_Y = 160;

    private static final int BTN_W = 340;
    private static final int BTN_H = 55;
    private static final int BTN_X = (Main.width - BTN_W) / 2;
    private static final int FIRST_BTN_Y = PANEL_Y + 95;
    private static final int BTN_SPACING = 72;

    private static final int BACK_W = 160;
    private static final int BACK_H = 40;
    private static final int BACK_X = (Main.width - BACK_W) / 2;
    private static final int BACK_Y = PANEL_Y + PANEL_H - 62;

    private static final Color BTN_NORMAL  = new Color(55,  90, 150);
    private static final Color BTN_HOVER   = new Color(85, 130, 205);
    private static final Color BTN_PRESSED = new Color(35,  60, 110);
    private static final Color BTN_BORDER  = new Color(30,  60, 110);
    private static final Color BACK_NORMAL = new Color(75,  75,  75);
    private static final Color BACK_HOVER  = new Color(110, 110, 110);
    private static final Color PANEL_BG    = new Color(0, 0, 0, 160);
    private static final Color PANEL_BORDER = new Color(255, 255, 255, 70);

    private static final int MAX_SERVERS = 3;

    private final ServerEntry[] entries = new ServerEntry[MAX_SERVERS];
    private final boolean[] serverHover   = new boolean[MAX_SERVERS];
    private final boolean[] serverPressed = new boolean[MAX_SERVERS];

    private boolean goBack  = false;
    boolean backHover       = false;
    boolean backPressed     = false;

    public boolean isGoBack() { return goBack; }
    public void resetGoBack() { goBack = false; }

    // Legacy constructor params kept for API compatibility
    public ServerSelectScreen(int x, int y, int sizeX, int sizeY) { }

    public void addIp(String ip, int port) {
        for (int i = 0; i < MAX_SERVERS; i++) {
            if (entries[i] == null) {
                entries[i] = new ServerEntry(ip, port);
                return;
            }
            if (entries[i].ip.equals(ip) && entries[i].port == port) return;
        }
    }

    public void render(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        StartScreen.renderBackScreen(g2d);

        // Panel
        g2d.setColor(PANEL_BG);
        g2d.fillRoundRect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H, 18, 18);
        g2d.setColor(PANEL_BORDER);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H, 18, 18);

        // Title
        g2d.setFont(FontLoader.getPixelFont(26));
        g2d.setColor(Color.WHITE);
        FontMetrics tm = g2d.getFontMetrics();
        String title = "Join a Game";
        g2d.drawString(title, (Main.width - tm.stringWidth(title)) / 2, PANEL_Y + 55);

        // Divider
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawLine(PANEL_X + 20, PANEL_Y + 68, PANEL_X + PANEL_W - 20, PANEL_Y + 68);

        // Server list or empty message
        boolean anyFound = false;
        for (int i = 0; i < MAX_SERVERS; i++) {
            if (entries[i] != null) {
                anyFound = true;
                drawServerButton(g2d, entries[i], i);
            }
        }
        if (!anyFound) {
            g2d.setFont(FontLoader.getPixelFont(14));
            g2d.setColor(new Color(170, 170, 170));
            String msg = "No servers found on local network";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, (Main.width - fm.stringWidth(msg)) / 2, FIRST_BTN_Y + 38);
        }

        drawBackButton(g2d);
    }

    private void drawServerButton(Graphics2D g2d, ServerEntry entry, int idx) {
        int bx = BTN_X;
        int by = FIRST_BTN_Y + idx * BTN_SPACING;
        Color bg = serverPressed[idx] ? BTN_PRESSED : serverHover[idx] ? BTN_HOVER : BTN_NORMAL;

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(bx + 3, by + 4, BTN_W, BTN_H, 12, 12);

        g2d.setColor(bg);
        g2d.fillRoundRect(bx, by, BTN_W, BTN_H, 12, 12);

        g2d.setColor(BTN_BORDER);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(bx, by, BTN_W, BTN_H, 12, 12);

        if (!serverPressed[idx]) {
            g2d.setColor(new Color(255, 255, 255, 40));
            g2d.fillRoundRect(bx + 2, by + 2, BTN_W - 4, BTN_H / 3, 10, 10);
        }

        g2d.setFont(FontLoader.getPixelFont(16));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        String label = entry.cleanIp() + "  :  " + entry.port;
        int lx = bx + (BTN_W - fm.stringWidth(label)) / 2;
        int ly = by + (BTN_H - fm.getHeight()) / 2 + fm.getAscent() + (serverPressed[idx] ? 1 : 0);
        g2d.drawString(label, lx, ly);
    }

    private void drawBackButton(Graphics2D g2d) {
        Color bg = backPressed ? new Color(50, 50, 50) : backHover ? BACK_HOVER : BACK_NORMAL;

        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillRoundRect(BACK_X + 2, BACK_Y + 3, BACK_W, BACK_H, 10, 10);

        g2d.setColor(bg);
        g2d.fillRoundRect(BACK_X, BACK_Y, BACK_W, BACK_H, 10, 10);

        g2d.setColor(new Color(55, 55, 55));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(BACK_X, BACK_Y, BACK_W, BACK_H, 10, 10);

        g2d.setFont(FontLoader.getPixelFont(16));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        String label = "< Back";
        g2d.drawString(label,
                BACK_X + (BACK_W - fm.stringWidth(label)) / 2,
                BACK_Y + (BACK_H - fm.getHeight()) / 2 + fm.getAscent() + (backPressed ? 1 : 0));
    }

    public void mouseMoved(int mx, int my) {
        backHover = mx >= BACK_X && mx <= BACK_X + BACK_W && my >= BACK_Y && my <= BACK_Y + BACK_H;
        for (int i = 0; i < MAX_SERVERS; i++) {
            if (entries[i] != null) {
                int by = FIRST_BTN_Y + i * BTN_SPACING;
                serverHover[i] = mx >= BTN_X && mx <= BTN_X + BTN_W && my >= by && my <= by + BTN_H;
            }
        }
    }

    public void checkpress(int mx, int my) {
        if (mx >= BACK_X && mx <= BACK_X + BACK_W && my >= BACK_Y && my <= BACK_Y + BACK_H) {
            goBack = true;
            backPressed = true;
            return;
        }
        for (int i = 0; i < MAX_SERVERS; i++) {
            if (entries[i] != null) {
                int by = FIRST_BTN_Y + i * BTN_SPACING;
                if (mx >= BTN_X && mx <= BTN_X + BTN_W && my >= by && my <= by + BTN_H) {
                    serverPressed[i] = true;
                    ServerClientHandler.openClient(entries[i].port);
                    while (!Main.tilesManager.isMapIsReady()) {
                        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    Main.gameState = Main.GameState.GAME;
                    Main.host = false;
                    serverPressed[i] = false;
                }
            }
        }
    }

    public void mouseReleased() {
        backPressed = false;
        for (int i = 0; i < MAX_SERVERS; i++) serverPressed[i] = false;
    }

    private static class ServerEntry {
        final String ip;
        final int port;

        ServerEntry(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        String cleanIp() {
            return ip.startsWith("/") ? ip.substring(1) : ip;
        }
    }
}
