package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestStorage {

    public static final int ROWS = 3, COLS = 3;

    private final Map<String, Item[][]> chests = new HashMap<>();

    public Item[][] getChest(int i, int j) {
        return chests.computeIfAbsent(key(i, j), k -> emptyGrid());
    }

    public void setChest(int i, int j, Item[][] grid) {
        chests.put(key(i, j), grid);
    }

    public boolean hasChest(int i, int j) {
        return chests.containsKey(key(i, j));
    }

    public void removeChest(int i, int j) {
        chests.remove(key(i, j));
    }

    public void clear() {
        chests.clear();
    }

    public List<int[]> getChestPositions() {
        List<int[]> positions = new ArrayList<>();
        for (String k : chests.keySet()) {
            String[] parts = k.split("_");
            positions.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }
        return positions;
    }

    /** Serialises all chests for the current map.
     *  Format: "chests: N  i1 j1 id00 qty00 ... id22 qty22  i2 j2 ..." */
    public String toSyncString() {
        List<int[]> positions = getChestPositions();
        StringBuilder sb = new StringBuilder("chests: ").append(positions.size());
        for (int[] pos : positions) {
            sb.append(' ').append(pos[0]).append(' ').append(pos[1]);
            Item[][] grid = getChest(pos[0], pos[1]);
            for (int r = 0; r < ROWS; r++)
                for (int c = 0; c < COLS; c++) {
                    sb.append(' ').append(grid[r][c].getId())
                      .append(' ').append(grid[r][c].getQuantity());
                }
        }
        return sb.toString();
    }

    /** Serialises a single chest for network update.
     *  Format: "chest_update i j id00 qty00 ... id22 qty22" */
    public String chestToSyncString(int i, int j) {
        StringBuilder sb = new StringBuilder("chest_update ")
                .append(i).append(' ').append(j);
        Item[][] grid = getChest(i, j);
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                sb.append(' ').append(grid[r][c].getId())
                  .append(' ').append(grid[r][c].getQuantity());
            }
        return sb.toString();
    }

    /** Applies a full chest sync received from the server. */
    public void applyFromNetwork(String[] arr) {
        int count = Integer.parseInt(arr[1]);
        int idx = 2;
        for (int n = 0; n < count; n++) {
            int ci = Integer.parseInt(arr[idx++]);
            int cj = Integer.parseInt(arr[idx++]);
            Item[][] grid = new Item[ROWS][COLS];
            for (int r = 0; r < ROWS; r++)
                for (int c = 0; c < COLS; c++) {
                    int id  = Integer.parseInt(arr[idx++]);
                    int qty = Integer.parseInt(arr[idx++]);
                    grid[r][c] = id == 0 ? new Item() : new Item(id);
                    if (id != 0) grid[r][c].setQuantity(qty);
                }
            setChest(ci, cj, grid);
        }
    }

    /** Applies a single-chest update received from the server. */
    public void applyChestUpdate(String[] arr) {
        int ci = Integer.parseInt(arr[1]);
        int cj = Integer.parseInt(arr[2]);
        Item[][] grid = new Item[ROWS][COLS];
        int idx = 3;
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                int id  = Integer.parseInt(arr[idx++]);
                int qty = Integer.parseInt(arr[idx++]);
                grid[r][c] = id == 0 ? new Item() : new Item(id);
                if (id != 0) grid[r][c].setQuantity(qty);
            }
        setChest(ci, cj, grid);
    }

    private String key(int i, int j) {
        return i + "_" + j;
    }

    private Item[][] emptyGrid() {
        Item[][] grid = new Item[ROWS][COLS];
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++)
                grid[i][j] = new Item();
        return grid;
    }
}
