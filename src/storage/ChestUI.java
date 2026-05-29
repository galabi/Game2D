package storage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import MainPackage.Main;
import entity.FontLoader;
import entity.GameColors;

public class ChestUI {

    private static final int SLOT_SIZE = 64;
    private static final int PADDING   = 8;
    private static final int COLS = ChestStorage.COLS;
    private static final int ROWS = ChestStorage.ROWS;

    private boolean open = false;
    private int chestI, chestJ;

    private final int panelW = COLS * SLOT_SIZE + (COLS + 1) * PADDING;
    private final int panelH = ROWS * SLOT_SIZE + (ROWS + 1) * PADDING + 30;
    private int panelX, panelY;

    private int mouseX = -1, mouseY = -1;

    public void open(int i, int j) {
        chestI = i;
        chestJ = j;
        open = true;
        panelX = (Main.width  - panelW) / 2;
        panelY = 30;
        Main.inventory.setOpen(true);
    }

    public void close() {
        open = false;
        Main.inventory.setOpen(false);
    }

    public boolean isOpen() {
        return open;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX() * Main.width  / Main.screenWidth;
        mouseY = e.getY() * Main.height / Main.screenHeight;
    }

    public void render(Graphics2D g2d) {
        if (!open) return;
        Item[][] slots = Main.chestStorage.getChest(chestI, chestJ);

        // background
        g2d.setColor(GameColors.inventoryBoxColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.fillRoundRect(panelX, panelY, panelW, panelH, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(panelX, panelY, panelW, panelH, 12, 12);

        // title
        g2d.setFont(FontLoader.getPixelFont(14));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.drawString("Chest", panelX + (panelW - fm.stringWidth("Chest")) / 2, panelY + 30);

        // slots: background, border, hover highlight, items
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int sx = panelX + PADDING + col * (SLOT_SIZE + PADDING);
                int sy = panelY + 30 + PADDING + row * (SLOT_SIZE + PADDING);

                g2d.setColor(new Color(0, 0, 0, 60));
                g2d.fillRoundRect(sx, sy, SLOT_SIZE, SLOT_SIZE, 8, 8);

                // hover highlight — same colour as inventory
                if (mouseX >= sx && mouseX < sx + SLOT_SIZE && mouseY >= sy && mouseY < sy + SLOT_SIZE) {
                    g2d.setColor(GameColors.inventoryHoverColor);
                    g2d.fillRoundRect(sx, sy, SLOT_SIZE, SLOT_SIZE, 8, 8);
                }

                g2d.setColor(Color.BLACK);
                g2d.drawRoundRect(sx, sy, SLOT_SIZE, SLOT_SIZE, 8, 8);

                Item item = slots[row][col];
                if (item != null && item.getId() != 0) {
                    g2d.drawImage(item.getImage(), sx, sy, SLOT_SIZE, SLOT_SIZE, null);
                    if (item.stackable && item.quantity > 0) {
                        // match inventory text style: font 13, position +7/+68
                        g2d.setFont(FontLoader.getPixelFont(13));
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(String.valueOf(item.quantity), sx + 7, sy + 68);
                    }
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (!open) return;

        int mx = e.getX() * Main.width  / Main.screenWidth;
        int my = e.getY() * Main.height / Main.screenHeight;

        Item[][] slots = Main.chestStorage.getChest(chestI, chestJ);

        // Check chest panel click
        if (mx >= panelX && mx <= panelX + panelW && my >= panelY && my <= panelY + panelH) {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    int sx = panelX + PADDING + col * (SLOT_SIZE + PADDING);
                    int sy = panelY + 30 + PADDING + row * (SLOT_SIZE + PADDING);
                    if (mx >= sx && mx < sx + SLOT_SIZE && my >= sy && my < sy + SLOT_SIZE) {
                        handleChestSlotClick(slots, row, col);
                        return;
                    }
                }
            }
            return; // clicked panel background, not a slot
        }

        // Check inventory / hotbar click — take item and put in chest
        Item taken = Main.inventory.takeItemAt(mx, my);
        if (taken != null) {
            if (!putInChest(slots, taken)) {
                // Chest is full, return the item
                Main.inventory.addToItemStack(taken);
            }
            return;
        }

        // Clicked outside both panels — close
        close();
    }

    // Click on a chest slot: if it has an item move it to inventory; if empty move hotbar item here
    private void handleChestSlotClick(Item[][] slots, int row, int col) {
        Item slotItem = slots[row][col];
        if (slotItem != null && slotItem.getId() != 0) {
            Main.inventory.addToItemStack(slotItem.Clone());
            slots[row][col] = new Item();
        } else {
            Item inHand = Main.inventory.getItemInHand();
            if (inHand != null && inHand.getId() != 0) {
                slots[row][col] = inHand.Clone();
                Main.inventory.clearItemInHand();
            }
        }
    }

    // Put an item into the first available chest slot (stack if possible)
    private boolean putInChest(Item[][] slots, Item item) {
        // Try to stack with existing same stackable item
        if (item.isStackable()) {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (slots[r][c] != null && slots[r][c].getId() == item.getId()) {
                        slots[r][c].setQuantity(slots[r][c].getQuantity() + item.getQuantity());
                        return true;
                    }
                }
            }
        }
        // Find empty slot
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (slots[r][c] == null || slots[r][c].getId() == 0) {
                    slots[r][c] = item;
                    return true;
                }
            }
        }
        return false;
    }
}
