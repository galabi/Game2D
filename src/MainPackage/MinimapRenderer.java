package MainPackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import entity.GameTextures;

public class MinimapRenderer {

    private static final int TILE_PX = 2;
    private static final int MAP_TILES = 50;
    private static final int MINIMAP_SIZE = MAP_TILES * TILE_PX; // 200
    private static final int MARGIN = 10;
    private static final int TILE_COLOR_COUNT = 85;

    private BufferedImage mapImage;
    private final Color[] tileColors = new Color[TILE_COLOR_COUNT];
    private boolean colorsBuilt = false;
    private boolean dirty = true;

    private void buildTileColors() {
        for (int id = 0; id < TILE_COLOR_COUNT; id++) {
            ImageIcon icon = GameTextures.getTileIcon(id);
            if (icon != null && icon.getImage() instanceof BufferedImage img) {
                int w = img.getWidth(), h = img.getHeight();
                long r = 0, g = 0, b = 0;
                for (int px = 0; px < w; px++) {
                    for (int py = 0; py < h; py++) {
                        int rgb = img.getRGB(px, py);
                        r += (rgb >> 16) & 0xFF;
                        g += (rgb >> 8) & 0xFF;
                        b += rgb & 0xFF;
                    }
                }
                int count = w * h;
                tileColors[id] = new Color((int) (r / count), (int) (g / count), (int) (b / count));
            }
        }
        colorsBuilt = true;
    }

    private void buildMapImage() {
        if (!colorsBuilt) buildTileColors();
        mapImage = new BufferedImage(MINIMAP_SIZE, MINIMAP_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = mapImage.createGraphics();
        entity.Tile[][] tiles = Main.tilesManager.getTiles();
        for (int i = 0; i < MAP_TILES; i++) {
            for (int j = 0; j < MAP_TILES; j++) {
                int id = tiles[i][j].getId();
                Color c = (id >= 0 && id < TILE_COLOR_COUNT && tileColors[id] != null)
                        ? tileColors[id] : Color.DARK_GRAY;
                g.setColor(c);
                g.fillRect(j * TILE_PX, i * TILE_PX, TILE_PX, TILE_PX);
            }
        }
        g.dispose();
        dirty = false;
    }

    public void markDirty() {
        dirty = true;
    }

    public void render(Graphics2D g2d) {
        if (Main.tilesManager == null || !Main.tilesManager.isMapIsReady()) return;
        if (dirty || mapImage == null) buildMapImage();

        int mapX = Main.width - MINIMAP_SIZE - MARGIN;
        int mapY = MARGIN;

        // dark border background
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(mapX - 3, mapY - 3, MINIMAP_SIZE + 6, MINIMAP_SIZE + 6);

        // terrain image
        g2d.drawImage(mapImage, mapX, mapY, null);

        // player dot
        if (Main.player != null) {
            int tileSize = TilesManager.tileSize;
            int dotX = mapX + (Main.player.getX() / tileSize) * TILE_PX + TILE_PX / 2;
            int dotY = mapY + (Main.player.getY() / tileSize) * TILE_PX + TILE_PX / 2;
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(dotX - 3, dotY - 3, 6, 6);
        }
    }
}
