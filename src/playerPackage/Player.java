package playerPackage;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import MainPackage.Main;
import MainPackage.TilesManager;
import entity.FontLoader;
import regeneration.RegenerationManager;
import storage.Item;
import entity.Entity;
import entity.FishingRod;
import entity.GameColors;
import entity.GameObject;
import entity.Tile;
import storage.ItemIds;
import multiplayer.ServerClientHandler;

public class Player extends Entity implements KeyListener, FocusListener {
	
	int tilesize = TilesManager.tileSize;
	
	public final static int playerCollisionBoxX = 20;
	public final static int playerCollisionBoxY = 50;
	public final static int playerCollisionBoxWidth = 24;
	public final static int playerCollisionBoxHeight = 14;
	final static int playerSpritSize = 64;
	
	public int playerI = 0, playerJ = 0;
	int speedX, speedY;
	final int walkSpeed = 2,runSpeed = 40;
	int maxHealth = 10;
	int health = maxHealth;
	int maxHunger = 10;
	int hunger = maxHunger;

	int imageDirection = 0;
	int imagePosture = 0;

	long animationTimer = System.currentTimeMillis();
	private long lastHitTime = 0;
	private static final long HIT_COOLDOWN_MS = 1000;
	private long lastRegenTime = System.currentTimeMillis();
	private static final long REGEN_INTERVAL_MS = 8000;
	private long lastHungerDecayTime = System.currentTimeMillis();
	private static final long HUNGER_DECAY_INTERVAL_MS = 60000;
	private long lastStarveTime = System.currentTimeMillis();
	private String name = "";
	 
	
	boolean fishing = false;
	boolean sprint = false;
	
	PlayerAnimation playeranimation;
	ImageIcon[][] playerImage;
	ImageIcon heartImage = null;
	ImageIcon breadImage = null;
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
	
	public void tick() {
		long now = System.currentTimeMillis();

		// hunger decay
		if (now - lastHungerDecayTime >= HUNGER_DECAY_INTERVAL_MS) {
			if (hunger > 0) hunger--;
			lastHungerDecayTime = now;
		}

		// health regen only when not starving
		if (hunger > 0 && health > 0 && health < maxHealth && now - lastRegenTime >= REGEN_INTERVAL_MS) {
			health++;
			lastRegenTime = now;
		}

		// starvation damage
		if (hunger == 0 && health > 0 && now - lastStarveTime >= REGEN_INTERVAL_MS) {
			health = Math.max(0, health - 1);
			lastStarveTime = now;
			if (health <= 0) Main.gameState = Main.GameState.GAME_OVER;
		}
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
			PlayerBreakBlock.breakBlock();
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
			System.out.println("problem in player animation:" + imageDirection + " " + imagePosture);
		}

		if (this != Main.player && !name.isEmpty()) {
			g2d.setFont(FontLoader.getPixelFont(8));
			FontMetrics fm = g2d.getFontMetrics();
			int nameW = fm.stringWidth(name);
			int nameX = x - Main.tilesManager.getCameraX(false) + (sizeX - nameW) / 2;
			int nameY = y - Main.tilesManager.getCameraY(false) +6;
			g2d.setColor(new Color(0, 0, 0, 140));
			g2d.fillRoundRect(nameX - 3, nameY - fm.getAscent(), nameW + 6, fm.getHeight(), 4, 4);
			g2d.setColor(Color.WHITE);
			g2d.drawString(name, nameX, nameY);
		}

		//debug
		if(Main.devmode) {
			g2d.setColor(Color.white);
			g2d.drawRect(x+playerCollisionBoxX-Main.tilesManager.getCameraX(false), y+playerCollisionBoxY-Main.tilesManager.getCameraY(false), playerCollisionBoxWidth, playerCollisionBoxHeight);
		}
		
		if(fishing) {
			rod.render(g2d);
		}
		
		if (heartImage != null) {
			int heartSize = heartImage.getIconWidth() * 2;
			int heartSpacing = 6;
			for (int i = 0; i < maxHealth; i++) {
				if (i < health) {
					g2d.drawImage(heartImage.getImage(), 30 + i * (heartSize + heartSpacing), 30, heartSize, heartSize, null);
				} else {
					g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.3f));
					g2d.drawImage(heartImage.getImage(), 30 + i * (heartSize + heartSpacing), 30, heartSize, heartSize, null);
					g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
				}
			}
		}

		// hunger bar
		if (breadImage != null) {
			int breadSize = breadImage.getIconWidth() * 2;
			int breadSpacing = 6;
			for (int i = 0; i < maxHunger; i++) {
				if (i < hunger) {
					g2d.drawImage(breadImage.getImage(), 30 + i * (breadSize + breadSpacing), 80, breadSize, breadSize, null);
				} else {
					g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.3f));
					g2d.drawImage(breadImage.getImage(), 30 + i * (breadSize + breadSpacing), 80, breadSize, breadSize, null);
					g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
				}
			}
		}
	}
	
	private void moving() {
		int speed;
		imagePosture = 1;
		fishing = false;
		
		
	    speed = sprint ? runSpeed : walkSpeed;

	    if (speedX != 0) {
	        speedX = (speedX > 0) ? speed : -speed;
	    }
	    if (speedY != 0) {
	        speedY = (speedY > 0) ? speed : -speed;
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
	
	private boolean checkTileCollision(int tileI, int tileJ, int xInTile, int yInTile, int w, int h) {
		Tile tile = Main.tilesManager.getTiles()[tileI][tileJ];
		if(tile.isSolid(xInTile, yInTile, w, h)) {
			return true;
		}
		return false;
	}

	private void collision(){
		int playerXInTile = (x + playerCollisionBoxX + speedX) % tilesize;
		int playerYInTile = (y + playerCollisionBoxY + speedY) % tilesize;
		int playerXInTileAndWidth = (x + playerCollisionBoxX + playerCollisionBoxWidth + speedX) % tilesize;
		int playerYInTileAndHeight = (y + playerCollisionBoxY + playerCollisionBoxHeight + speedY) % tilesize;

		if(speedX > 0){
			if(checkTileCollision(
					(int)((y + playerCollisionBoxY + speedY)/tilesize),
					(int)((x + playerCollisionBoxX + playerCollisionBoxWidth + speedX)/tilesize),
					playerXInTileAndWidth, playerYInTile, 1, playerCollisionBoxHeight)) speedX = 0;

			if(checkTileCollision(
					(int)((y + playerCollisionBoxY + playerCollisionBoxHeight + speedY)/tilesize),
					(int)((x + playerCollisionBoxX + playerCollisionBoxWidth + speedX)/tilesize),
					playerXInTileAndWidth, playerYInTileAndHeight - playerCollisionBoxHeight, 1, playerCollisionBoxHeight)) speedX = 0;
		}

		if(speedX < 0){
			if(checkTileCollision(
					(int)((y + playerCollisionBoxY + speedY)/tilesize),
					(int)((x + playerCollisionBoxX + speedX)/tilesize),
					playerXInTile, playerYInTile, 1, playerCollisionBoxHeight)) speedX = 0;

			if(checkTileCollision(
					(int)((y + playerCollisionBoxY + playerCollisionBoxHeight + speedY)/tilesize),
					(int)((x + playerCollisionBoxX + speedX)/tilesize),
					playerXInTile, playerYInTileAndHeight - playerCollisionBoxHeight, 1, playerCollisionBoxHeight)) speedX = 0;
		}

		if(speedY > 0){
			if(checkTileCollision(
					(int)((y + playerCollisionBoxY + playerCollisionBoxHeight + speedY)/tilesize),
					(int)((x + playerCollisionBoxX + speedX)/tilesize),
					playerXInTile, playerYInTileAndHeight, playerCollisionBoxWidth, 1)) speedY = 0;

			if(checkTileCollision(
					(int)((y + playerCollisionBoxY + playerCollisionBoxHeight + speedY)/tilesize),
					(int)((x + playerCollisionBoxX + playerCollisionBoxWidth + speedX)/tilesize),
					playerXInTileAndWidth - playerCollisionBoxWidth, playerYInTileAndHeight, playerCollisionBoxWidth, 1)) speedY = 0;
		}

		if(speedY < 0){
			if(checkTileCollision(
					(int)((y + playerCollisionBoxY + speedY)/tilesize),
					(int)((x + playerCollisionBoxX + speedX)/tilesize),
					playerXInTile, playerYInTile, playerCollisionBoxWidth, 1)) speedY = 0;

			if(checkTileCollision(
					(int)((y + playerCollisionBoxY + speedY)/tilesize),
					(int)((x + playerCollisionBoxX + playerCollisionBoxWidth + speedX)/tilesize),
					playerXInTileAndWidth - playerCollisionBoxWidth, playerYInTile, playerCollisionBoxWidth, 1)) speedY = 0;
		}
	}
	
	
	//player placing a block
	public void placeBlock(int pressBlockI,int pressBlockJ,Item itemToPlace) {
		
		GameObject obj = Main.tilesManager.getObjects(pressBlockI,pressBlockJ);
		Tile tile = Main.tilesManager.getTiles(pressBlockI,pressBlockJ);		
		//sapling case (grow Tree)
		if(itemToPlace.getId() == 7 && Main.tilesManager.canPlaceTree(pressBlockI, pressBlockJ)) {
			Main.player.imagePosture = 2;
			Main.player.animationTimer = System.currentTimeMillis();
			Main.tilesManager.updateBlock(pressBlockI,pressBlockJ,itemToPlace.getIdToPlace());
			Main.tilesManager.updateBlock(pressBlockI-1,pressBlockJ,4);
			RegenerationManager.insertToGrowthList(obj,pressBlockJ*tilesize,pressBlockI*tilesize);
			Main.inventory.decreaseItemInHand();

			
		//general case
		}else if(obj.getId() == 0 && !tile.isWater()) {
			Main.player.imagePosture = 2;
			Main.player.animationTimer = System.currentTimeMillis();
			Main.tilesManager.updateBlock(pressBlockI,pressBlockJ,itemToPlace.getIdToPlace());
		
			Main.inventory.decreaseItemInHand();			
		}
		
		ServerClientHandler.sendDataToServer(Main.player.toString());

	}
	
	public void eatFood(int hungerRestore) {
		hunger = Math.min(maxHunger, hunger + hungerRestore);
	}

	public int getHunger() { return hunger; }
	public void setHunger(int hunger) { this.hunger = hunger; }

	public void takeDamage(int amount) {
		long now = System.currentTimeMillis();
		if (now - lastHitTime >= HIT_COOLDOWN_MS) {
			health = Math.max(0, health - amount);
			lastHitTime = now;
			if (health <= 0) {
				Main.gameState = Main.GameState.GAME_OVER;
			}
		}
	}

	//player interaction with camp Fire
	public void cookFish(int pressBlockI,int pressBlockJ,Item itemInhand) {
		if(itemInhand.getId() == ItemIds.FISH) {
			Main.tilesManager.updateBlock(pressBlockI,pressBlockJ,19);
			Main.inventory.addToItemStack(new Item(5));
		}else {
			Main.tilesManager.updateBlock(pressBlockI,pressBlockJ,7);
		}
		Main.inventory.decreaseItemInHand();
	}
	
	//player fishing
	public void startFishing(int BlockI,int BlockJ) {		
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

	
	//use this to lower the player health points
	public void hitCreature(int demage) {
		health -= demage;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (Main.gameState == Main.GameState.START) {
			Main.startscreen.keyPressed(e);
			return;
		}

		int key = e.getKeyCode();

		if(key == KeyEvent.VK_ESCAPE) {
			if(Main.gameState == Main.GameState.GAME) {
				if(Main.chestUI.isOpen()) {
					Main.chestUI.close();
					return;
				}
				if(Main.workbenchUI.isOpen()) {
					Main.workbenchUI.close();
					return;
				}
				if(Main.inventory.isOpen()) {
					Main.inventory.setOpen(false);
					return;
				}
				speedX = 0;
				speedY = 0;
				sprint = false;
				playeranimation.setRunningAnimationSpeed(false);
				Main.gameState = Main.GameState.PAUSE;
			}else if(Main.gameState == Main.GameState.PAUSE) {
				Main.gameState = Main.GameState.GAME;
			}
		}

		if(Main.gameState != Main.GameState.GAME) return;

		if(key == KeyEvent.VK_A) {
			speedX = -walkSpeed;
			speedY = 0;
			imageDirection = 1;
			return;
		}
		if(key == KeyEvent.VK_D) {
			speedX = walkSpeed;
			speedY = 0;
			imageDirection = 2;
			return;
		}
		if(key == KeyEvent.VK_W) {
			speedY = -walkSpeed;
			speedX = 0;
			imageDirection = 3;
			return;
		}
		if(key == KeyEvent.VK_S) {
			speedY = walkSpeed;
			speedX = 0;
			imageDirection = 0;
			return;
		}if(key == KeyEvent.VK_SPACE) {
			sprint = true;
			playeranimation.setRunningAnimationSpeed(sprint);
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
		
		//E - open/close inventory or close chest
		if(key == KeyEvent.VK_E) {
			if (Main.chestUI.isOpen()) {
				Main.chestUI.close();
			} else {
				Main.inventory.setOpen(!Main.inventory.isOpen());
			}
		}
	}
	

	@Override
	public void keyReleased(KeyEvent e) {	
		if(Main.gameState != Main.GameState.GAME) return;
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
			playeranimation.setRunningAnimationSpeed(sprint);
		}
		ServerClientHandler.sendDataToServer(toString());
	}	

	@Override
	public void keyTyped(KeyEvent e) {
		if (Main.gameState == Main.GameState.START) {
			Main.startscreen.keyTyped(e);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		speedX = 0;
		speedY = 0;
		sprint = false;
		playeranimation.setRunningAnimationSpeed(false);
	}

	@Override
	public void focusGained(FocusEvent e) {}
	
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
		int startX = x + sizeX / 2 - TilesManager.tileSize / 2;
		int startY = y + sizeY / 2 - TilesManager.tileSize / 2;
		float throwSpeed = 4.5f;
		float vx = 0, vy = 0;
		switch (imageDirection) {
			case 0: vy =  throwSpeed; break; // down
			case 1: vx = -throwSpeed; break; // left
			case 2: vx =  throwSpeed; break; // right
			case 3: vy = -throwSpeed; break; // up
		}
		Main.tilesManager.addItemDrop(item.Clone(), startX, startY, vx, vy);
		ServerClientHandler.sendDataToServer("add_drop " + item.getId() + " " + item.getQuantity() + " " + startX + " " + startY);
	}
	
	private void loadImg() {
		/**
		 * 0 = down
		 * 1 = left
		 * 2 = right
		 * 3 = up
			*/
		try {
			BufferedImage temp = ImageIO.read(getClass().getResourceAsStream("/player.png"));
			for(int i = 0;i<4;i++) {
				for(int j = 0; j<5;j++) {
					playerImage[i][j] = new ImageIcon(temp.getSubimage(j*playerSpritSize, i*playerSpritSize, playerSpritSize, playerSpritSize));
				}
			}
			heartImage = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/heart.png")));
			breadImage = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/hanger.png")));
			
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
	
	public int getMaxHunger() {
		return maxHunger;
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
	
	public boolean isSprint() {
		return sprint;
	}
	
	public void setName(String name) { this.name = name; }

	public String toString() {
		return "player: " + x + " " + y + " " +imageDirection + " " + imagePosture +" "+ speedX +" "+ speedY + " " + playerI + " "+ playerJ;
	}
}
