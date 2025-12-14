package Storage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.Stack;

import MainPackage.Main;
import entity.FontLoader;
import entity.GameColors;
	
public class Inventory implements MouseWheelListener{

	boolean open = false;
	boolean preLoad = false;
	
	Formatter x;
	Scanner s;
	Stack<Item> AddItemList;
	Item[][] items;
	Crafting crafting;
	HoverItemInInventoey hoverItem;
	
	final int toolbarLength = 5;
	final int HotBarX;
	final int HotBarY;
	static int slotSize;
	int selectedSlot = 0;
	FontMetrics metrics;
	
	BufferedImage inventoryImage = null;
	 
	int mouseHaloX=-100,mouseHaloY=-100,mouseX=0,mouseY=0;
	int inventoryX,inventoryY,craftingX = 800,craftingY = 350,textX=-1;
	
	Color boxColor = GameColors.inventoryBoxColor;
	Color hoverColor = GameColors.inventoryHoverColor;
	Color hotBarcolor = GameColors.inventoryHotBarcolor;
	
	Font titelfont = FontLoader.getPixelFont(16),itemsfont = FontLoader.getPixelFont(13);
	
	public Inventory(int x, int y, int size) {
		HotBarX = x;
		HotBarY = y;
		inventoryX = x;
		inventoryY = y-300;
		craftingY = y-300;
		slotSize = size;
				
		AddItemList = new Stack<>();
		
		//create and start the tool bar with blank items
		loadInventory();
		crafting = new Crafting(2,craftingX,craftingY);
	}
	
	public void tick() {
		while(!AddItemList.isEmpty()) {
			addItem(AddItemList.pop());
		}
	}
	
	
	public void render(Graphics2D g2d) {
		
		g2d.setStroke(new BasicStroke(3));
		g2d.setFont(itemsfont);
		
		
		//draw the hot bar
		g2d.setColor(boxColor);
		g2d.fillRoundRect(HotBarX, HotBarY, toolbarLength*slotSize,slotSize,15,15);
		g2d.setColor(Color.black);
		g2d.drawRoundRect(HotBarX, HotBarY, toolbarLength*slotSize,slotSize,15,15);
		for(int i = 1;i<toolbarLength;i++) {
			g2d.drawLine(HotBarX + i*slotSize, HotBarY, HotBarX + i*slotSize,HotBarY+slotSize);			
		}
		//draw blue box over the item that player use
		g2d.setColor(hotBarcolor);
		g2d.drawRoundRect(HotBarX + selectedSlot*slotSize, HotBarY, slotSize,slotSize,15,15);
		
		//draw the full inventory
		if(open) {
			
			//first create the inventory background
			if(inventoryImage == null) {
				inventoryImage = new BufferedImage(Main.screenWidth,Main.screenHeight, BufferedImage.TYPE_INT_ARGB_PRE);
				Graphics2D g = (Graphics2D) inventoryImage.getGraphics();
				drawInventory(g);
			}else {
			//draw to screen the inventory background
				g2d.drawImage(inventoryImage, 0, 0, Main.screenWidth,Main.screenHeight, null);
			}
			
			//mouse enter the inventory
			g2d.setColor(hoverColor);
			if(mouseHaloX >=0) {
				g2d.fillRoundRect(mouseHaloX,mouseHaloY, slotSize,slotSize,15,15);
			}
			
			//draw items from inventory
			g2d.setColor(Color.black);
			for(int i = 1;i<toolbarLength;i++) {
				for(int j = 0;j<toolbarLength;j++) {
					renderItem(g2d, items[i][j],j*slotSize,(i-1)*slotSize,inventoryX,inventoryY);
				}
			}
			
			crafting.rander(g2d);
			if(hoverItem != null) {
				hoverItem.rander(g2d);
			}
			
		}
		
		//draw items from hot-bar
		g2d.setColor(Color.black);
		for(int i = 0;i<toolbarLength;i++) {
			renderItem(g2d, items[0][i], i*slotSize, 0,HotBarX,HotBarY);
		}
		
		g2d.setFont(titelfont);
		
		if(textX < 0) {
			metrics = g2d.getFontMetrics(titelfont);
			setTextLocation();
		}
		//selected item name
		TextLayout textLayout = new TextLayout(items[0][selectedSlot].getName(), titelfont, g2d.getFontRenderContext());
        Shape outline = textLayout.getOutline(AffineTransform.getTranslateInstance(textX, HotBarY));


        // ציור ההילה (קו מתאר עבה בצבע בהיר)
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(2f)); // עובי הקו
        g2d.draw(outline);

        // ציור הטקסט עצמו
        g2d.setColor(Color.BLACK);
        g2d.fill(outline);

	}
	
	public static void renderItem(Graphics2D g2d,Item item,int x,int y,int inventoryX,int inventoryY) {
		if(item.getId() != 0) {
			g2d.drawImage(item.getImage(), inventoryX + x, inventoryY + y, slotSize,slotSize, null);
			if(item.quantity > 0 && item.stackable) {
				g2d.drawString(String.valueOf(item.quantity), inventoryX + x + 7, inventoryY + y + 68);
			}
		}
		
	}
	
	
	public void setTextLocation() {
		textX = (HotBarX + ((slotSize*toolbarLength - metrics.stringWidth(items[0][selectedSlot].getName())) / 2));		
	}

	private void drawInventory(Graphics2D g2d) {
		//draw the background and the border
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(boxColor);
		g2d.fillRoundRect(inventoryX, inventoryY, toolbarLength*slotSize,(toolbarLength-1)*slotSize,15,15);
		g2d.setColor(Color.black);
		g2d.drawRoundRect(inventoryX, inventoryY, toolbarLength*slotSize,(toolbarLength-1)*slotSize,15,15);
		
		//draw the line in the inside
		for(int i = 1;i<toolbarLength;i++) {
			g2d.drawLine(inventoryX + i*slotSize, inventoryY, inventoryX + i*slotSize,inventoryY+slotSize*(toolbarLength-1));
		}
		for(int i = 1;i<toolbarLength-1;i++) {
			g2d.drawLine(inventoryX , inventoryY+ i*slotSize, inventoryX+toolbarLength*slotSize,inventoryY+ i*slotSize);			

		}
	}
	
	public void loadInventory() {
		int itemId,quantity;
		items = new Item[toolbarLength][toolbarLength];
		while(true){
			try {
			s = new Scanner(new File("saves/inventory.txt"));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("file do not find");
			}	
			try {
				for(int i = 0;i<toolbarLength;i++) {
					for(int j = 0;j<toolbarLength;j++) {
						itemId = s.nextInt();
						quantity = s.nextInt();
						if(itemId == 0) {
							items[i][j] = new Item();
						}else{
							items[i][j] = new Item(itemId);
							items[i][j].quantity = quantity;
						}
					}
				}
			s.close();

			break;
			}catch (Exception e) {
			      e.printStackTrace();
			}
		}
		

	}
	
	
	public void saveInventory() {
		if(hoverItem != null) {
			
		}
		try {
			x = new Formatter("saves/inventory.txt");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(int i = 0;i<toolbarLength;i++) {
			for(int j = 0;j<toolbarLength;j++) {
				x.format("%s %s ",items[i][j].id,items[i][j].quantity);
			}
			x.format("%n ");
		}
		x.close();
	}
	
	private void addItem(Item item) {
		//check if the item is already in the inventory to increase the quantity
		for(int i = 0;i<toolbarLength;i++) {
			for(int j = 0;j<toolbarLength;j++) {
				if (items[i][j].id == item.id && item.stackable) {
					items[i][j].quantity += item.quantity;
					return;
				}
			}
		}
		//check for free slot to increase the item 
		for(int i = 0;i<toolbarLength;i++) {
			for(int j = 0;j<toolbarLength;j++) {
				if (items[i][j].id == 0) {
					items[i][j] = item;
					return;
				}
			}
		}
	}
	

	public void mousePressed(MouseEvent e) {
		mouseX = e.getX()*Main.width/Main.screenWidth ;
		mouseY = e.getY()*Main.height/Main.screenHeight;
		//hot-bar press
		if(mouseX > HotBarX && mouseX < (HotBarX + toolbarLength*slotSize) && mouseY > HotBarY && mouseY < (HotBarY + slotSize)) {
			DragAndDrop(items[0][(mouseX-HotBarX)/slotSize], HotBarX+((mouseX-HotBarX)/slotSize)*slotSize, HotBarY,e.getButton());
		//inventory press
		}else if(mouseX > inventoryX && mouseX < inventoryX+slotSize*toolbarLength && mouseY > inventoryY && mouseY < inventoryY+slotSize*(toolbarLength-1)){
				DragAndDrop(items[1+(mouseY-inventoryY)/slotSize][(mouseX-inventoryX)/slotSize], inventoryX+((mouseX-inventoryX)/slotSize)*slotSize, inventoryY + ((mouseY-inventoryY)/slotSize)*slotSize,e.getButton());
		//crafting press
		}else if(mouseX > craftingX && mouseX < craftingX+slotSize*crafting.getCraftingSlots() && mouseY > craftingY && mouseY < craftingY+slotSize*crafting.getCraftingSlots()) {
			DragAndDrop(crafting.craftingItems[(mouseY-craftingY)/slotSize][(mouseX-craftingX)/slotSize], craftingX+((mouseX-craftingX)/slotSize)*slotSize, craftingY + ((mouseY-craftingY)/slotSize)*slotSize,e.getButton());
			crafting.checkCrafting();
		//drop outside the inventory
		}else if(mouseX > craftingX+slotSize/crafting.getCraftingSlots() && mouseX < craftingX+slotSize/crafting.getCraftingSlots()+slotSize && mouseY > craftingY + slotSize*(crafting.getCraftingSlots()+1) && mouseY < craftingY + slotSize*(crafting.getCraftingSlots()+1)+slotSize){
			if(crafting.outPut.id != 0 && hoverItem == null) {
				DragAndDrop(crafting.craftItem(),(craftingX+slotSize/crafting.getCraftingSlots())+((mouseX-(craftingX+slotSize/crafting.getCraftingSlots()))/slotSize)*slotSize, (craftingY + slotSize*(crafting.getCraftingSlots()+1)) + ((mouseY-(craftingY + slotSize*(crafting.getCraftingSlots()+1)))/slotSize)*slotSize,e.getButton());
			}else if(hoverItem != null && crafting.outPut.getId() == hoverItem.getId()) {
				crafting.craftItem();
				hoverItem.item.quantity++;
				}
		}else if(hoverItem != null){
			if(e.getButton() == 1) {
				Main.player.dropOnFloor(hoverItem.item);
				hoverItem = null;
					}else if(e.getButton() == 3) {
				Main.player.dropOnFloor(new Item(hoverItem.item.id));
				hoverItem.item.quantity--;
			}
		}
	}
	
	//the drag and drop settings
	public void DragAndDrop(Item item, int itemX, int itemY,int mouseButton){
		
		//item on mouse
		if(hoverItem != null) {
			//left press
			if(mouseButton == 1) {
				if(item.id == 0) {
					item.setItem(hoverItem.item.id);
					item.quantity = hoverItem.item.quantity;
					hoverItem = null;
				}else {
					if(hoverItem.item.id == item.id && item.stackable) {
						item.quantity += hoverItem.item.quantity;
						hoverItem = null;
					}else {
						Item temp = new Item(hoverItem.item.id);
						temp.quantity = hoverItem.item.quantity;
						
						hoverItem.item.setItem(item.id);
						hoverItem.item.setQuantity(item.quantity);
						
						item.setItem(temp.id);
						item.setQuantity(temp.quantity);
					}
				}
				
			//Right press
			}else if(mouseButton == 3) {
				if(hoverItem.item.id == item.id && item.stackable) {
					item.quantity++;

				}else if(item.id == 0){
					item.setItem(hoverItem.item.id);
				}else{
					return;
				}
				//decrease if item in the hand and check if equals 0
				if(--hoverItem.item.quantity <= 0) {
					hoverItem = null;
				}
			}
			
			
		//mouse free
		}else {
			//left press
			if(mouseButton == 1) {
				hoverItem = new HoverItemInInventoey(mouseX -itemX, mouseY - itemY, item.Clone());
				item.setBlank();
	
			//Right press
			}else if(mouseButton == 3) {
				hoverItem = new HoverItemInInventoey(mouseX -itemX, mouseY - itemY, item.Clone());
				hoverItem.item.quantity = (hoverItem.item.quantity+1)/2;
				item.quantity /= 2;
				
				if(item.quantity <= 0) {
					item.setBlank();
				}
			}
		}
		if(hoverItem != null) {
			hoverItem.mouseX = mouseX;
			hoverItem.mouseY = mouseY;
		}
		setTextLocation();
	}
	
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX()*Main.width/Main.screenWidth ;
		mouseY = e.getY()*Main.height/Main.screenHeight;
			//mouse enter the inventory
			if(mouseX > inventoryX && mouseX < inventoryX+slotSize*toolbarLength && mouseY > inventoryY && mouseY < inventoryY+slotSize*(toolbarLength-1)) {
				mouseHaloX = mouseX-((mouseX-inventoryX)%slotSize);
				mouseHaloY = mouseY-((mouseY-inventoryY)%slotSize);
			}
			//mouse enter the hot-bar
			else if(mouseX > HotBarX && mouseX < (HotBarX + toolbarLength*slotSize) && mouseY > HotBarY && mouseY < (HotBarY + slotSize)) {
				mouseHaloX = mouseX-((mouseX-HotBarX)%slotSize);
				mouseHaloY = mouseY-((mouseY-HotBarY)%slotSize);
			}else {
				mouseHaloX = -slotSize;
				mouseHaloY = -slotSize;
			}

			if(hoverItem != null) {
				hoverItem.mouseX = mouseX;
				hoverItem.mouseY = mouseY;
			}
	}
	
	public void setSelectedSlot(int slot) {
		this.selectedSlot = slot;
	}
	public int getSelectedSlot() {
		return selectedSlot;
	}
	
	public Item getItemInHand() {
		return items[0][selectedSlot];
	}
	
	public void decreaseItemInHand() {
		items[0][selectedSlot].quantity--;
		if(items[0][selectedSlot].quantity <= 0) {
			items[0][selectedSlot] = new Item();
		}
	}
	public void addToItemStack(Item item) {
		AddItemList.add(item);
	}
	
	
	public boolean IsOpen() {
		return open;
	}
	
	public void SetOpen(boolean open) {
		this.open = open;
		if(!open) {
			crafting.exitInventory();
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(open) {
			mouseX = e.getX()*Main.width/Main.screenWidth ;
			mouseY = e.getY()*Main.height/Main.screenHeight;
			if(hoverItem != null) {
				hoverItem.mouseX = mouseX;
				hoverItem.mouseY = mouseY;
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(Main.gameState != 2) return;
		selectedSlot = (selectedSlot+e.getWheelRotation())%toolbarLength;
		if(selectedSlot < 0) selectedSlot += toolbarLength;
		
		setTextLocation();
	}
}
