package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import MainPackage.Main;
import mapRender.MapObject;
import mapRender.ObjectPropertiesManager;

public class Tile extends Entity {

    public static final byte GRASS = 0, WATER = 1, GRAVEL = 2;

    int id; // terrain type: GRASS, WATER, or GRAVEL

    // Marching-squares visual tile id, cached. Terrain is static after map load, so this is
    // computed once (lazily) instead of every frame. Reset via invalidateVisual() if a
    // terrain cell ever changes at runtime (invalidate this tile and its up/left neighbours).
    private int visualId = -1;

    public Tile(int terrainType, int x, int y, int size) {
        super(x, y, size, size);
        this.id = terrainType;
    }

    public void invalidateVisual() {
        visualId = -1;
    }

    @Override
    public void render(Graphics2D g2d) {
        int screenX = Main.tilesManager.getCameraX(false);
        int screenY = Main.tilesManager.getCameraY(false);
        int tileI = y / sizeY;
        int tileJ = x / sizeX;
        if (visualId < 0) visualId = Main.tilesManager.computeVisualId(tileI, tileJ);
        g2d.drawImage(GameTextures.getTileIcon(visualId).getImage(), x - screenX, y - screenY, sizeX, sizeY, null);
        if (Main.devmode) {
            g2d.setColor(Color.white);
            g2d.drawRect(x - screenX, y - screenY, sizeX, sizeY);
        }
    }

    public boolean isSolid(int playerXInTile, int playerYInTile, int width, int height) {
        int tileI = y / sizeY;
        int tileJ = x / sizeX;
        int half  = sizeX / 2; // 32 px — midpoint of the 64-px tile
        int px1   = playerXInTile + width;
        int py1   = playerYInTile + height;

        // Each visual quadrant of this tile maps to the terrain of the corresponding neighbor.
        // Bit layout mirrors computeVisualId: self=TL, right=TR, below=BL, diag=BR.
        if (playerXInTile < half && playerYInTile < half && id == WATER) return true;
        if (px1 > half && playerYInTile < half && neighborTerrain(tileI, tileJ + 1) == WATER) return true;
        if (playerXInTile < half && py1   > half && neighborTerrain(tileI + 1, tileJ) == WATER) return true;
        if (px1 > half && py1   > half && neighborTerrain(tileI + 1, tileJ + 1) == WATER) return true;

        // Objects render shifted up-left by half a tile to sit on the marching-squares terrain,
        // so their collision is tested in that same shifted frame (world coordinates).
        int ts = sizeX;
        Rectangle player = new Rectangle(tileJ * ts + playerXInTile, tileI * ts + playerYInTile, width, height);
        // A shifted solid rect reaches up-left, so an object in this cell or the cell below/right
        // of the player can overlap it.
        for (int oi = tileI; oi <= tileI + 1 && oi < Main.tilesManager.getMaxScreenCol(); oi++) {
            for (int oj = tileJ; oj <= tileJ + 1 && oj < Main.tilesManager.getMaxScreenRow(); oj++) {
                MapObject obj = ObjectPropertiesManager.getObject(Main.tilesManager.getObjects(oi, oj).id);
                for (Rectangle rect : obj.getSolidInTile()) {
                    Rectangle world = new Rectangle(oj * ts + rect.x - ts / 2, oi * ts + rect.y - ts / 2,
                            rect.width, rect.height);
                    if (world.intersects(player)) return true;
                }
            }
        }
        return false;
    }

    private byte neighborTerrain(int i, int j) {
        if (i < 0 || i >= Main.tilesManager.getMaxScreenCol()) return (byte) id;
        if (j < 0 || j >= Main.tilesManager.getMaxScreenRow()) return (byte) id;
        return Main.tilesManager.getTiles(i, j).getTerrainType();
    }

    public boolean isWater() {
        return id == WATER;
    }

    public byte getTerrainType() {
        return (byte) id;
    }

    public int getId() {
        return id;
    }

    public void setId(int terrainType) {
        this.id = terrainType;
    }
}
