package entity;

import java.awt.Graphics2D;

import MainPackage.Main;
import MainPackage.TilesManager;
import storage.Item;

public class ItemOnFloor {

    static int tileSize = TilesManager.tileSize;

    Item item;
    private float x, y;          // pixel position in the world
    private float worldVX, worldVY; // lateral movement while airborne
    private float bounceY  = 0;  // visual vertical offset
    private float bounceVY = 0;
    private boolean bouncing = false;

    private static final float GRAVITY        = 0.45f;
    private static final float INITIAL_BOUNCE = -5.5f;
    private static final float SCATTER        = 2.2f;

    /** Block / creature drop — small random scatter. */
    public ItemOnFloor(Item item, int x, int y) {
        this.item = item;
        this.x = x;
        this.y = y;
        this.bounceVY = INITIAL_BOUNCE;
        this.bouncing = true;
        this.worldVX  = (float)(Math.random() * SCATTER * 2 - SCATTER);
        this.worldVY  = (float)(Math.random() * SCATTER * 2 - SCATTER);
    }

    /** Player throw — directed velocity. */
    public ItemOnFloor(Item item, int x, int y, float worldVX, float worldVY) {
        this.item = item;
        this.x = x;
        this.y = y;
        this.bounceVY = INITIAL_BOUNCE;
        this.bouncing = true;
        this.worldVX  = worldVX;
        this.worldVY  = worldVY;
    }

    /** Network sync / save load — no animation. */
    public ItemOnFloor(Item item, int x, int y, boolean withBounce) {
        this.item = item;
        this.x = x;
        this.y = y;
        if (withBounce) {
            this.bounceVY = INITIAL_BOUNCE;
            this.bouncing = true;
            this.worldVX  = (float)(Math.random() * SCATTER * 2 - SCATTER);
            this.worldVY  = (float)(Math.random() * SCATTER * 2 - SCATTER);
        }
    }

    public void tick() {
        if (!bouncing) return;
        x        += worldVX;
        y        += worldVY;
        bounceVY += GRAVITY;
        bounceY  += bounceVY;
        if (bounceY >= 0) {
            bounceY  = 0;
            worldVX  = 0;
            worldVY  = 0;
            bouncing = false;
        }
    }

    public void render(Graphics2D g2d) {
        int screenX = Main.tilesManager.getCameraX(false);
        int screenY = Main.tilesManager.getCameraY(false);
        g2d.drawImage(item.getImage(),
                (int) x - screenX,
                (int) y - screenY + (int) bounceY,
                tileSize, tileSize, null);
    }

    public int getX()        { return (int) x; }
    public int getY()        { return (int) y; }
    public int getTileX()    { return (int) x / tileSize; }
    public int getTileY()    { return (int) y / tileSize; }
    public boolean isAirborne() { return bouncing; }
    public Item getItem()    { return item; }
}
