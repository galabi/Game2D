package storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import playerPackage.Player;

public class PlayerSaveManager {

    private static final int MAGIC   = 0x504C5952;
    private static final int VERSION = 2;
    private static final int SLOTS   = 5;

    private static File fileFor(String name) {
        File dir = new File(MainPackage.WorldManager.path() + "players");
        dir.mkdirs();
        return new File(dir, sanitize(name) + ".bin");
    }

    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }

    public static void save(String name, Player player, Inventory inv) {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileFor(name))))) {
            dos.writeInt(MAGIC);
            dos.writeByte(VERSION);
            dos.writeInt(player.getX());
            dos.writeInt(player.getY());
            dos.writeByte(Math.max(1, player.getHealth()));
            dos.writeByte(player.getHunger());
            for (int i = 0; i < SLOTS; i++)
                for (int j = 0; j < SLOTS; j++) {
                    Item item = inv.items[i][j];
                    dos.writeByte(item.getId());
                    dos.writeByte(item.getQuantity());
                }
        } catch (Exception e) {
            System.err.println("PlayerSaveManager: failed to save " + name + ": " + e.getMessage());
        }
    }

    public static boolean load(String name, Player player, Inventory inv) {
        File f = fileFor(name);
        if (!f.exists()) return false;
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(f)))) {
            if (dis.readInt() != MAGIC) throw new Exception("Invalid magic");
            dis.readUnsignedByte(); // version
            int x = dis.readInt();
            int y = dis.readInt();
            int health = dis.readUnsignedByte();
            int hunger = dis.readUnsignedByte();
            Item[][] grid = new Item[SLOTS][SLOTS];
            for (int i = 0; i < SLOTS; i++)
                for (int j = 0; j < SLOTS; j++) {
                    int id  = dis.readUnsignedByte();
                    int qty = dis.readUnsignedByte();
                    grid[i][j] = id == 0 ? new Item() : new Item(id);
                    if (id != 0) grid[i][j].setQuantity(qty);
                }
            // Apply to player and inventory
            player.setLocation(x, y);
            player.setHealth(health);
            player.setHunger(hunger);
            inv.items = grid;
            return true;
        } catch (Exception e) {
            System.err.println("PlayerSaveManager: failed to load " + name + ": " + e.getMessage());
            return false;
        }
    }
}
