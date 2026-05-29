package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestStorage {

    public static final int ROWS = 3, COLS = 3;

    private final Map<String, Item[][]> chests = new HashMap<>();
    private String currentMap = "";

    public void setMap(String mapName) {
        currentMap = mapName;
    }

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

    public void clearCurrentMap() {
        String prefix = currentMap + "_";
        chests.keySet().removeIf(k -> k.startsWith(prefix));
    }

    public List<int[]> getChestPositions() {
        List<int[]> positions = new ArrayList<>();
        String prefix = currentMap + "_";
        for (String k : chests.keySet()) {
            if (!k.startsWith(prefix)) continue;
            String[] parts = k.substring(prefix.length()).split("_");
            positions.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }
        return positions;
    }

    private String key(int i, int j) {
        return currentMap + "_" + i + "_" + j;
    }

    private Item[][] emptyGrid() {
        Item[][] grid = new Item[ROWS][COLS];
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++)
                grid[i][j] = new Item();
        return grid;
    }
}
