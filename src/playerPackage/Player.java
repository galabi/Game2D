package playerPackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Entity.Object;
import Entity.Tile;
import Entity.entity;
import Entity.FishingRod;
import Entity.GameColors;
import MainPackage.Main;
import MainPackage.TilesManager;
import Regeneration.RegenerationManager;
import Storage.Item;
import mutiplayer.ServerClientHandler;

public class Player extends entity implements KeyListener {
	
int tilesize = TilesManager.tileSize;
	
	final int playerCollisionBoxX = 20;
	final int playerCollisionBoxY = 50;
	final int playerCollisionBoxWidth = 24;
	final int playerCollisionBoxHeight = 14;
			
	public int playerI = 0, playerJ = 0;
	int speedX, speedY;
	final int speed = 2;
	int maxHealth = 10;
	int health = maxHealth;
	
	int imageDirection = 0;
	int imagePosture = 0;
	
	long animationTimer = System.currentTimeMillis();
	 
	
	boolean fishing = false;
	boolean sprint = false;
	
	PlayerAnimation playeranimation;
	ImageIcon[][] playerImage;
	ImageIcon heartImage = null;
	FishingRod rod;
	
	final static Color playerShadowColor = GameColors.playerShadowColor;
	

	public Player(int x, int y,int size) {
		super(x, y,size,size);	
		playerImage = new ImageIcon[4][7];
		playeranimation = new PlayerAnimation();
		
		loadImg();
		rod = new FishingRod();
		playerI = (int)((y + playerCollisionBoxY + playerCollisionBoxHeight)/tilesize);
		playerJ = (int)((x + playerCollisionBoxX+playerCollisionBoxWidth)/tilesize);
	}
	
	@Override
	public void tick() {
		//check if the player need to move
		if(speedX != 0 || speedY != 0) {
			fishing = false;
			PlayerInteraction.objectToBreakId = 0;
			
			moving();
			
		//check if fishing
		}else if(fishing) {
			rod.tick();
			ServerClientHandler.sendDataToServer(toString());
		}
		//punch animation timer
		if(PlayerInteraction.breaking && System.currentTimeMillis() - animationTimer >= 300){
			if(imagePosture == 2) {
				imagePosture = 0;
			}else {
				imagePosture = 2;
			}
			animationTimer = System.currentTimeMillis();
			ServerClientHandler.sendDataToServer(toString());
		}
		
		//check if the player break block
		if(PlayerInteraction.timeOfFirstHitToObject != 0) {
			breakBlock();
		}
		
		
	}
	
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(playerShadowColor);
		g2d.fillOval(x - Main.tilesManager.getCameraX(false)+(sizeX-25)/2, y - Main.tilesManager.getCameraY(false)+sizeY-7, 25,15);
		
		try {
			if(speedX != 0 || speedY != 0) {
				playeranimation.render(g2d, x - Main.tilesManager.getCameraX(false), y - Main.tilesManager.getCameraY(false), sizeX, sizeY);
			}else {
			g2d.drawImage(playerImage[imageDirection][imagePosture].getImage(), x - Main.tilesManager.getCameraX(false) ,
					y - Main.tilesManager.getCameraY(false), sizeX, sizeY,null);
			}
		} catch (Exception e) {
			System.out.println("problrm in player animation:" + imageDirection + " " + imagePosture);
		}

		//debag
		if(Main.devmode) {
			g2d.setColor(Color.white);
			g2d.drawRect(x+playerCollisionBoxX-Main.tilesManager.getCameraX(false), y+playerCollisionBoxY-Main.tilesManager.getCameraY(false), playerCollisionBoxWidth, playerCollisionBoxHeight);
			g2d.drawRect(getNearTile(imageDirection).getX()-Main.tilesManager.getCameraX(false), getNearTile(imageDirection).getY()-Main.tilesManager.getCameraY(false), 64,64);
		}
		
		if(fishing) {
			rod.render(g2d);
		}
		
		for(int i = 0;i<= health;i++) {
			g2d.drawImage(heartImage.getImage(), 30 + 40*i , 30, heartImage.getIconWidth()*2, heartImage.getIconHeight()*2, null);
		}
	}
	
	private void moving() {
		imagePosture = 1;
		fishing = false;
		if(sprint) {
			if(Math.abs(speedY) == speed) {
				speedY*=2;		
			}
			if(Math.abs(speedX) == speed) {
				speedX*=2;
			}
		}else {
			if(Math.abs(speedY) == speed*2) {
				speedY/=2;		
			}
			if(Math.abs(speedX) == speed*2) {
				speedX/=2;
			}
		}
		collision();
		
		if(x+speedX >= Main.tilesManager.getCameraX(true) - 350 || x+speedX <= Main.tilesManager.getCameraX(false) + 350 - sizeX) {
			Main.tilesManager.setCameraX(Main.tilesManager.getCameraX(false) + speedX);

		}
		if(y+speedY >= Main.tilesManager.getCameraY(true) - 270|| y+speedY <= Main.tilesManager.getCameraY(false) + 270 - sizeY) {
			Main.tilesManager.setCameraY(Main.tilesManager.getCameraY(false) + speedY);
		}
		
		y += speedY;
		x += speedX;

		
		playerI = (int)((y + playerCollisionBoxY + playerCollisionBoxHeight)/tilesize);
		playerJ = (int)((x + playerCollisionBoxX)/tilesize);
		
		ServerClientHandler.sendDataToServer(toString());
	}
	
	private void collision(){
		int playerXInTile = (x + playerCollisionBoxX + speedX) % tilesize;
		int playerYInTile = (y + playerCollisionBoxY + speedY) % tilesize;
		int playerXInTileAndWidth = (x + playerCollisionBoxX + playerCollisionBoxWidth + speedX) % tilesize;
		int playerYInTileAndHeight = (y + playerCollisionBoxY + playerCollisionBoxHeight + speedY) % tilesize;
		
		if(speedX > 0){
			if(Main.tilesManager.getTiles()[(int)((y + playerCollisionBoxY + speedY )/tilesize)]
					[(int)((x + playerCollisionBoxX + playerCollisionBoxWidth + speedX)/tilesize)].isSolid(playerXInTileAndWidth,playerYInTile,1,playerCollisionBoxHeight)) speedX = 0;			
			
			if(Main.tilesManager.getTiles()[(int)((y + playerCollisionBoxY + playerCollisionBoxHeight + speedY )/tilesize)]
					[(int)((x + playerCollisionBoxX + playerCollisionBoxWidth + speedX)/tilesize)].isSolid(playerXInTileAndWidth,playerYInTileAndHeight-playerCollisionBoxHeight,1,playerCollisionBoxHeight)) speedX = 0;
		}
		
		if(speedX < 0){
			if(Main.tilesManager.getTiles()[(int)((y + playerCollisionBoxY + speedY )/tilesize)]
					[(int)((x + playerCollisionBoxX + speedX)/tilesize)].isSolid(playerXInTile,playerYInTile,1,playerCollisionBoxHeight)) speedX = 0;
			
			if(Main.tilesManager.getTiles()[(int)((y + playerCollisionBoxY + playerCollisionBoxHeight + speedY )/tilesize)]
					[(int)((x + playerCollisionBoxX + speedX)/tilesize)].isSolid(playerXInTile,playerYInTileAndHeight-playerCollisionBoxHeight,1,playerCollisionBoxHeight)) speedX = 0;
		}
		
		if(speedY > 0){
			if(Main.tilesManager.getTiles()[(int)((y + playerCollisionBoxY + playerCollisionBoxHeight + speedY)/tilesize)]
					[(int)((x + playerCollisionBoxX + speedX)/tilesize)].isSolid(playerXInTile,playerYInTileAndHeight,playerCollisionBoxWidth,1)) speedY = 0;
			
			if(Main.tilesManager.getTiles()[(int)((y + playerCollisionBoxY + playerCollisionBoxHeight + speedY)/tilesize)]
					[(int)((x + playerCollisionBoxX + playerCollisionBoxWidth + speedX)/tilesize)].isSolid(playerXInTileAndWidth-playerCollisionBoxWidth,playerYInTileAndHeight,playerCollisionBoxWidth,1)) speedY = 0;
		}
		
		if(speedY < 0){
			if(Main.tilesManager.getTiles()[(int)((y + playerCollisionBoxY + speedY)/tilesize)]
					[(int)((x + playerCollisionBoxX + speedX )/tilesize)].isSolid(playerXInTile,playerYInTile,playerCollisionBoxWidth,1)) speedY = 0;
			
			if(Main.tilesManager.getTiles()[(int)((y + playerCollisionBoxY + speedY)/tilesize)]
					[(int)((x + playerCollisionBoxX + playerCollisionBoxWidth + speedX )/tilesize)].isSolid(playerXInTileAndWidth-playerCollisionBoxWidth,playerYInTile,playerCollisionBoxWidth,1)) speedY = 0;
		
		}
		
	}
	
	//break block 
	public void breakBlock() {
	    long timeDelta = System.currentTimeMillis() - PlayerInteraction.timeOfFirstHitToObject;
	    int objId = PlayerInteraction.objectToBreakId;
	    int i = PlayerInteraction.objectI;
	    int j = PlayerInteraction.objectJ;
	    int handItemId = Main.inventory.getItemInHand().getId();
	    ArrayList<Integer> itemWhenBroken = Main.tilesManager.getObjects(i, j).getItemWhenBroken();
	    
	    if (objId > 30 && objId <= 33) {
	        boolean canBreak = (handItemId == 10 && timeDelta >= PlayerInteraction.BreakTime / 2)
	                         || timeDelta >= PlayerInteraction.rockBreakTime;
	        if (canBreak) {
	            updateBlock(i, j, objId - 1);
	            RegenerationManager.insertToGrowthList(Main.tilesManager.getObjects()[i][j], j * tilesize, i * tilesize);
	            
	            for(Integer k:itemWhenBroken) {
	            	addDrop(i, j, k);
	            }
	            resetInteraction();
	        }
	    } 
	    else if (Main.tilesManager.getObjects(i, j).isBreakable()) {
	        boolean canBreak = (handItemId == 2 && timeDelta >= PlayerInteraction.BreakTime / 2)
	                         || timeDelta >= PlayerInteraction.BreakTime;
	        if (canBreak) {
	            updateBlock(i, j, 0);
	            for(Integer k:itemWhenBroken) {
	            	addDrop(i, j, k);
	            }
	            resetInteraction();
	        }
	    }
	}
	
	//player placing a block
	public void placeBlock(int pressBlockI,int pressBlockJ,Item itemToPlace) {
		
		Object obj = Main.tilesManager.getObjects()[pressBlockI][pressBlockJ];
		Tile tile = Main.tilesManager.getTiles()[pressBlockI][pressBlockJ];		

		if(obj.getId() == 0 && !tile.IsSolid()) {
			Main.player.imagePosture = 2;
			Main.player.animationTimer = System.currentTimeMillis();
			updateBlock(pressBlockI,pressBlockJ,itemToPlace.getIdToPlace());
			
			//sapling case (grow Tree)
			if(itemToPlace.getId() == 7) {
				RegenerationManager.insertToGrowthList(obj,pressBlockJ*tilesize,pressBlockI*tilesize);
			}
			Main.inventory.decreaseItemInHand();
			
		}
		
		ServerClientHandler.sendDataToServer(Main.player.toString());

	}
	
	//player interaction with camp Fire
	public void cookFish(int pressBlockI,int pressBlockJ,Item itemInhand) {
		if(itemInhand.getId() == 4) {
			updateBlock(pressBlockI,pressBlockJ,5);
			Main.inventory.addToItemStack(new Item(5));
		}else {
			updateBlock(pressBlockI,pressBlockJ,4);
		}
		Main.inventory.decreaseItemInHand();
	}
	
	//player fishing
	public void startFishing(int BlockI,int BlockJ) {
		if(playerI ==  BlockI && playerJ == BlockJ) return; // add an  later
			
		rod.startFishingAtClosestDirection();
	
		if(!fishing) {
			imagePosture = 3;
			fishing = true;
			rod.setFirstbubbletime();
		}else {
			if(rod.isFishOnRod()) {
				Main.inventory.addToItemStack(new Item(4));
			}
		fishing = false;
		imagePosture = 0;
		
		}
	}
	
	private void updateBlock(int i, int j, int newType) {
	    Main.tilesManager.getObjects()[i][j].setType(newType);
	    ServerClientHandler.sendDataToServer("update_block " + i + " " + j + " " + newType);
	}

	private void addDrop(int i, int j, int itemId) {
	    Main.tilesManager.addItemDrop(new Item(itemId), j * tilesize, i * tilesize);
	    ServerClientHandler.sendDataToServer("add_drop " + i + " " + j + " " + itemId + " " + 1);
	}

	private void resetInteraction() {
	    PlayerInteraction.objectToBreakId = 0;
	    PlayerInteraction.timeOfFirstHitToObject = 0;
	}
	
	//use this to lower the creature health points
	public void hitCreature(int demage) {
		health -= demage;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_ESCAPE) {
			if(Main.gameState == 2) {
				if(Main.inventory.IsOpen()) {
					Main.inventory.SetOpen(false);
					return;
				}
				Main.gameState = 3;
			}else if(Main.gameState == 3) {
				Main.gameState = 2;
			}	
		}
		
		if(Main.gameState != 2) return;

		if(key == KeyEvent.VK_A) {
			speedX = -speed;
			speedY = 0;
			imageDirection = 1;
			return;
		}
		if(key == KeyEvent.VK_D) {
			speedX = speed;
			speedY = 0;
			imageDirection = 2;
			return;
		}
		if(key == KeyEvent.VK_W) {
			speedY = -speed;
			speedX = 0;
			imageDirection = 3;
			return;
		}
		if(key == KeyEvent.VK_S) {
			speedY = speed;
			speedX = 0;
			imageDirection = 0;
			return;
		}if(key == KeyEvent.VK_SPACE) {
			sprint = true;
		}
		
		//1-5 numpad
		if(key >= KeyEvent.VK_1 && key <= KeyEvent.VK_5) {
			if(Main.inventory.getSelectedSlot() == key-KeyEvent.VK_0-1) {
				return;
			}
			fishing = false;
			PlayerInteraction.objectToBreakId = 0;
			Main.inventory.setSelectedSlot(key-KeyEvent.VK_0-1);
			Main.inventory.setTextLocation();
			return;
		}
		
		//Q - dev mode
		if(key == KeyEvent.VK_Q) {
			Main.devmode = !Main.devmode;
			return;
		}
		
		//E - open/close inventory
		if(key == KeyEvent.VK_E) {
			Main.inventory.SetOpen(!Main.inventory.IsOpen());
		}
	}
	

	@Override
	public void keyReleased(KeyEvent e) {	
		if(Main.gameState != 2) return;
		int key = e.getKeyCode();	
		if(key == KeyEvent.VK_W) { 
			speedY = 0;
			if(speedX == 0) {
				imagePosture = 0;
			}
		}
		if(key == KeyEvent.VK_S) { 
			speedY = 0;
			if(speedX == 0) {
				imagePosture = 0;
			}
		}
		if(key == KeyEvent.VK_A) {
			speedX = 0;
			if(speedY== 0) {
				imagePosture = 0;
			}
		}
		if(key == KeyEvent.VK_D) {
			speedX = 0;
			if(speedY == 0) {
				imagePosture = 0;
			}
		}
		if(key == KeyEvent.VK_SPACE) {
			sprint = false;
		}
		ServerClientHandler.sendDataToServer(toString());
	}	

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	private Tile getNearTile(int playerPosition) {
		float offsetX=0,offsetY=0;
		switch (playerPosition) {
		//down
			case 0: 
				offsetX = 0;
				offsetY = 1;
				break;
		//left
			case 1: 
				offsetX = -1;
				offsetY = 0;
				break;
		//right
			case 2: 
				offsetX = 0;
				offsetY = 0;
				break;
		//up
			case 3: 
				offsetX = 0;
				offsetY = -1;
				break;
			}
		return Main.tilesManager.getTiles()[(int)((y+sizeY*4/5)/tilesize+offsetY)][(int)((x+sizeX*4/5)/tilesize+offsetX)];
	}
	
	
	public void dropOnFloor(Item item) {
		Tile temp = getNearTile(imageDirection);
		Main.tilesManager.addItemDrop(item.Clone(),temp.getX(),temp.getY());
		ServerClientHandler.sendDataToServer("add_drop " + item.getId()+" "+item.getQuantity()+" "+temp.getX()+" "+temp.getY());
	}
	
	private void loadImg() {
		BufferedImage temp;
		try {
			/**
			 * 0 = down
			 * 1 = left
			 * 2 = right
			 * 3 = up
				*/
			temp = ImageIO.read(new File("playerIcons/player.png"));
			for(int i = 0;i<4;i++) {
				for(int j = 0; j<5;j++) {
					playerImage[i][j] = new ImageIcon(temp.getSubimage(j*64, i*64, 64, 64));
				}
			}
			
			heartImage = new ImageIcon(ImageIO.read(new File("playerIcons/heart.png")));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public int getImageDirection() {
		return imageDirection;
	}
	
	public int getImagePosture() {
		return imagePosture;
	}
	
	public void setImageDirection(int imageDirection) {
		this.imageDirection = imageDirection;
	}
	
	public void setImagePosture(int imagePosture) {
		this.imagePosture = imagePosture;
	}
	
	public void setSpeed(int speedX,int speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	@Override
	public String toString() {
		return "player: " + x + " " + y + " " +imageDirection + " " + imagePosture +" "+ speedX +" "+ speedY + " " + playerI + " "+ playerJ;
	}
}
