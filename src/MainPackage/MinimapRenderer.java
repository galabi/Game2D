package MainPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import entity.Tile;

public class MinimapRenderer {

    private static final int VIEW_HALF       = 25;             // tiles visible in each direction
    private static final int PIXEL_PER_TILE  = 3;             // pixels per tile on the minimap
    private static final int MINIMAP_SIZE    = VIEW_HALF * 2 * PIXEL_PER_TILE; // 150px diameter
    private static final int RADIUS          = MINIMAP_SIZE / 2;
    private static final int MARGIN          = 10;

    private static final Color COLOR_GRASS = new Color(86, 130, 62);
    private static final Color COLOR_WATER = new Color(64, 120, 200);
    private static final Color COLOR_GRAVEL  = new Color(194, 178, 128);

    public void markDirty() { /* rendered dynamically each frame — no cache to invalidate */ }

    public void render(Graphics2D g2d) {
        if (Main.tilesManager == null || !Main.tilesManager.isMapIsReady()) return;
        if (Main.player == null) return;

        int tileSize  = TilesManager.tileSize;
        int playerTileI = Main.player.getY() / tileSize;
        int playerTileJ = Main.player.getX() / tileSize;
        int mapMax    = Main.tilesManager.getMaxScreenCol();

        // Position: top-right corner
        int drawX = Main.width - MINIMAP_SIZE - MARGIN;
        int drawY = MARGIN;
        int cx    = drawX + RADIUS;
        int cy    = drawY + RADIUS;

        // Dark backing circle (slightly larger)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillOval(drawX - 3, drawY - 3, MINIMAP_SIZE + 6, MINIMAP_SIZE + 6);

        // Clip to circle so tile pixels don't bleed outside
        Shape oldClip = g2d.getClip();
        g2d.setClip(new Ellipse2D.Float(drawX, drawY, MINIMAP_SIZE, MINIMAP_SIZE));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Draw the 50×50 tile area around the player
        entity.Tile[][] tiles = Main.tilesManager.getTiles();
        for (int ti_off = -VIEW_HALF; ti_off < VIEW_HALF; ti_off++) {
            for (int tj_off = -VIEW_HALF; tj_off < VIEW_HALF; tj_off++) {
                int ti = playerTileI + ti_off;
                int tj = playerTileJ + tj_off;

                int px = cx + tj_off * PIXEL_PER_TILE;
                int py = cy + ti_off * PIXEL_PER_TILE;

                Color c;
                if (ti < 0 || ti >= mapMax || tj < 0 || tj >= mapMax) {
                    c = Color.BLACK;
                } else {
                    byte terrain = tiles[ti][tj].getTerrainType();
                    if (terrain == Tile.WATER)      c = COLOR_WATER;
                    else if (terrain == Tile.GRAVEL)  c = COLOR_GRAVEL;
                    else                            c = COLOR_GRASS;
                }
                g2d.setColor(c);
                g2d.fillRect(px, py, PIXEL_PER_TILE, PIXEL_PER_TILE);
            }
        }

        // Restore clip
        g2d.setClip(oldClip);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // White border ring
        g2d.setColor(new Color(255, 255, 255, 160));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(drawX, drawY, MINIMAP_SIZE, MINIMAP_SIZE);

        // Player dot at center
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(cx - 3, cy - 3, 6, 6);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawOval(cx - 3, cy - 3, 6, 6);
    }
}
