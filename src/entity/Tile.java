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

    public Tile(int terrainType, int x, int y, int size) {
        super(x, y, size, size);
        this.id = terrainType;
    }

    @Override
    public void render(Graphics2D g2d) {
        int screenX = Main.tilesManager.getCameraX(false);
        int screenY = Main.tilesManager.getCameraY(false);
        int tileI = y / sizeY;
        int tileJ = x / sizeX;
        int visualId = Main.tilesManager.computeVisualId(tileI, tileJ);
        g2d.drawImage(GameTextures.getTileIcon(visualId).getImage(), x - screenX, y - screenY, sizeX, sizeY, null);
        if (Main.devmode) {
            g2d.setColor(Color.white);
            g2d.drawRect(x - screenX, y - screenY, sizeX, sizeY);
            if (id == WATER) {
                g2d.setColor(new Color(0, 100, 255, 120));
                g2d.fillRect(x - screenX, y - screenY, sizeX, sizeY);
            }
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

        Rectangle r = new Rectangle(playerXInTile, playerYInTile, width, height);
        MapObject topObj = ObjectPropertiesManager.getObject(Main.tilesManager.getObjects(tileI, tileJ).id);
        for (Rectangle rect : topObj.getSolidInTile()) {
            if (rect.intersects(r)) return true;
        }
        return false;
    }

    private byte neighborTerrain(int i, int j) {
        if (i < 0 || i >= Main.tilesManager.getmaxScreenCol()) return (byte) id;
        if (j < 0 || j >= Main.tilesManager.getmaxScreenRow()) return (byte) id;
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
