package MainPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import entity.FontLoader;

public class NameInputBox {

    private String text = "";
    private final int x, y, w, h;
    private boolean focused = true;
    private static final int MAX_LENGTH = 16;

    public NameInputBox(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
    }

    public void keyTyped(char c) {
        if (!focused) return;
        if (c == '\b') {
            if (!text.isEmpty()) text = text.substring(0, text.length() - 1);
        } else if (text.length() < MAX_LENGTH && isPrintable(c)) {
            text += c;
        }
    }

    public void mousePressed(int mx, int my) {
        focused = (mx >= x && mx <= x + w && my >= y && my <= y + h);
    }

    public String getText() {
        return text.trim();
    }

    public void render(Graphics2D g2d) {
        // background
        g2d.setColor(focused ? new Color(255, 255, 255, 220) : new Color(200, 200, 200, 180));
        g2d.fillRoundRect(x, y, w, h, 10, 10);
        g2d.setColor(focused ? Color.BLACK : Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(focused ? 2f : 1f));
        g2d.drawRoundRect(x, y, w, h, 10, 10);

        // text + cursor
        g2d.setFont(FontLoader.getPixelFont(18));
        FontMetrics fm = g2d.getFontMetrics();
        String display = text;
        boolean showCursor = focused && (System.currentTimeMillis() / 500) % 2 == 0;
        if (showCursor) display += "|";
        g2d.setColor(Color.BLACK);
        g2d.drawString(display, x + 10, y + (h - fm.getHeight()) / 2 + fm.getAscent());
    }

    private boolean isPrintable(char c) {
        return c >= 32 && c < 127;
    }
}
