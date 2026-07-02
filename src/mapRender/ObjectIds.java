package mapRender;

public final class ObjectIds {
    // IDs follow the sprite order: tree/sapling first (standalone images),
    // then objects.png left-to-right, top-to-bottom.
    public static final int TREE          = 1;  // single-tile tree (tree.png)
    public static final int TREE_TRUNK    = 1;  // alias kept for existing references
    public static final int TREE_SAPLING  = 2;  // sapling (sapling.png)
    public static final int CHEST         = 3;
    public static final int WORKBENCH     = 4;
    public static final int FURNACE       = 5;  // new object (sprite only, no logic yet)
    public static final int CAMPFIRE      = 6;  // inactive campfire
    public static final int CAMPFIRE_ON   = 7;  // active campfire (cooking state)

    // Ores: three consecutive ids per material, MAX (largest) -> MID -> MIN (depleted).
    // Mining advances +1 (shrinks); regrowth advances -1 (grows) up to MAX. Ids 8..16 contiguous.
    public static final int STONE_MAX = 8,  STONE_MID = 9,  STONE_MIN = 10;
    public static final int GOLD_MAX  = 11, GOLD_MID  = 12, GOLD_MIN  = 13;
    public static final int IRON_MAX  = 14, IRON_MID  = 15, IRON_MIN  = 16;

    // Legacy aliases (stone) so older code keeps compiling.
    public static final int ROCK_MAX = STONE_MAX;
    public static final int ROCK     = STONE_MID;
    public static final int ROCK_MIN = STONE_MIN;

    // Fences (sprite only, decorative — ids 17..21).
    public static final int FENCE_FIRST = 17, FENCE_LAST = 21;

    /** Any ore tile (any material, any stage). */
    public static boolean isOre(int id) {
        return id >= STONE_MAX && id <= IRON_MIN;
    }

    /** The largest/full stage of a material — regrowth stops here. */
    public static boolean isOreMax(int id) {
        return id == STONE_MAX || id == GOLD_MAX || id == IRON_MAX;
    }

    /** Depleted stage — cannot be mined further (only regrows). */
    public static boolean isOreMin(int id) {
        return id == STONE_MIN || id == GOLD_MIN || id == IRON_MIN;
    }

    /** A stage that still yields resources when hit (MAX or MID). */
    public static boolean isMineableOre(int id) {
        return isOre(id) && !isOreMin(id);
    }

    private ObjectIds() {}
}
