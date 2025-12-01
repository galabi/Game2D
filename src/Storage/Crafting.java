package Storage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Entity.FontLoader;
import Entity.gameColors;
import MainPackage.Main;
import MainPackage.TilesManager;

public class Crafting {
	Item[][] craftingItems;
	Item outPut = new Item();
	final int Tilesize = TilesManager.tileSize;
	int craftingSlots , x , y;
	
	Color boxColor = gameColors.inventoryBoxColor;
	Color hoverColor = gameColors.inventoryHoverColor;
	Color hotBarcolor = gameColors.inventoryHotBarcolor;
	
	Font font = FontLoader.getPixelFont(13);
	
	public Crafting(int slots,int x,int y) {
		this.x = x;
		this.y = y;
		craftingSlots = slots;
		craftingItems = new Item[slots][slots];
		for(int i = 0; i< slots;i++) {
			for(int j = 0; j< slots;j++) {
				craftingItems[i][j] = new Item();
			}
		}
	}
	
	public void checkCrafting(){
		int[][] itemsInCrafing = {{craftingItems[0][0].id,craftingItems[0][1].id},{craftingItems[1][0].id,craftingItems[1][1].id}};
		outPut = RecipeBook.checkRecipe(itemsInCrafing);
	}
	
	public void rander(Graphics2D g2d) {
		int mouseX = Main.inventory.mouseX,mouseY = Main.inventory.mouseY;
		g2d.setStroke(new BasicStroke(3));
		g2d.setFont(font);
		
		//draw the crafting background
		g2d.setColor(boxColor);
		g2d.fillRoundRect(x, y, craftingSlots*Tilesize,craftingSlots*Tilesize,15,15);
		g2d.fillRoundRect(x + Tilesize/craftingSlots, y + Tilesize*(craftingSlots+1), Tilesize,Tilesize,15,15);

		g2d.setColor(Color.black);
		g2d.drawRoundRect(x, y, craftingSlots*Tilesize,craftingSlots*Tilesize,15,15);
		g2d.drawRoundRect(x + Tilesize/craftingSlots, y + Tilesize*(craftingSlots+1), Tilesize,Tilesize,15,15);

		
		
		//draw the vertical and horizontal lines of the crafting
		for(int i = 1; i <craftingSlots;i++) {
			g2d.drawLine(x + i*Tilesize, y, x + i*Tilesize,y+Tilesize*craftingSlots);
			g2d.drawLine(x , y+ i*Tilesize, x+craftingSlots*Tilesize,y+ i*Tilesize);			
		}

		//draw the hover mouse
		g2d.setColor(hoverColor);
		if(mouseX > x && mouseX < x+Tilesize*craftingSlots && mouseY > y && mouseY < y+Tilesize*craftingSlots) {
			g2d.fillRoundRect(mouseX-((mouseX-x)%Tilesize),mouseY-((mouseY-y)%Tilesize), Tilesize,Tilesize,15,15);
		}
		if(mouseX > x + Tilesize/craftingSlots && mouseX < x + Tilesize/craftingSlots+Tilesize && mouseY > y + Tilesize*(craftingSlots+1) && mouseY < y + Tilesize*(craftingSlots+1)+Tilesize) {
			g2d.fillRoundRect(x + Tilesize/craftingSlots, y + Tilesize*(craftingSlots+1), Tilesize,Tilesize,15,15);
		}
		
		//draw items
		g2d.setColor(Color.black);;
		for(int i = 0; i< craftingSlots;i++) {
			for(int j = 0; j< craftingSlots;j++) {
				Inventory.renderItem(g2d, craftingItems[i][j], j*Tilesize, i*Tilesize, x, y);
			}
		}
		
		g2d.drawImage(outPut.getImage(),x + Tilesize/craftingSlots, y + Tilesize*(craftingSlots+1), Tilesize,Tilesize, null);

	}
	public void exitInventory() {
		for(int i = 0; i< craftingSlots;i++) {
			for(int j = 0; j< craftingSlots;j++) {
				Main.inventory.addToItemStack(craftingItems[i][j].Clone());
				craftingItems[i][j].setBlank();
			}
		}
		outPut.setBlank();
	}
	
	public Item craftItem() {
		for(int i = 0; i< craftingSlots;i++) {
			for(int j = 0; j< craftingSlots;j++) {
				if(--craftingItems[i][j].quantity == 0){
					craftingItems[i][j].setBlank();
				}
			}
		}
		Item temp = outPut.Clone();
		outPut.setBlank();
		checkCrafting();
		return temp;
	}
	
	public int getCraftingSlots() {
		return craftingSlots;
	}
	
	
}
