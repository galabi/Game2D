package MainPackage;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.util.concurrent.ConcurrentHashMap;
import storage.ChestStorage;
import storage.ChestUI;
import storage.Inventory;
import storage.WorkbenchUI;
import creature.CreatureManager;
import entity.GameTextures;
import entity.Text;
import multiplayer.ServerClientHandler;
import playerPackage.MouseManager;
import playerPackage.Player;


public class Main extends Canvas implements Runnable{

	public enum GameState { START, GAME, PAUSE, GAME_OVER }

	private static final long serialVersionUID = 1L;
	long lastSave = System.currentTimeMillis();
	public static final int width = 1200 ,height = width /16 * 10; 
	int FPS = 60;
	public static int screenWidth= width,screenHeight = height;
	private Thread thread;
	private boolean running = false;
	public static boolean devmode = false,host;
	public static JFrame Frame;
	boolean preLoad = false;
	
	public static Player player;
	public static ConcurrentHashMap<Integer, Player> remotePlayers = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Integer, String> remotePlayerNames = new ConcurrentHashMap<>();
	public static String playerName = "";
	public static Inventory inventory;
	public static TilesManager tilesManager;
	public static MouseManager mouseManager;
	public static ChestStorage chestStorage = new ChestStorage();
	public static ChestUI chestUI = new ChestUI();
	public static WorkbenchUI workbenchUI = new WorkbenchUI();

	public static StartScreen startscreen;
	public static PauseScreen pausescreen;
	public static GameOverScreen gameOverScreen;
	public static MinimapRenderer minimap;
	Graphics2D g2;
	
	Text text;
	public static GameState gameState = GameState.START;
	
	public Main() {
		
	window(width, height, "My Game", this);
	
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		
		running = true;
		
	}
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
		    System.out.println(e);
		    e.printStackTrace();
		      
		}
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
	while(running) {
		
		currentTime = System.nanoTime();
		
		delta += (currentTime - lastTime) / drawInterval;
		timer += (currentTime - lastTime);
		lastTime = currentTime;

		if (delta > 5) delta = 5; // prevent spiral of death

		// Fixed-timestep: tick as many times as we've fallen behind, but render only
		// once per frame afterwards (rendering identical catch-up frames is wasted work).
		boolean ticked = false;
		while(delta >= 1) {
			tick();
			delta--;
			ticked = true;
		}

		if (ticked) {
			render();
			drawCount++;
		}

		// Yield the core instead of busy-spinning until the next frame is due.
		try { Thread.sleep(1); } catch (InterruptedException ignored) {}

		if(timer >= 1000000000) {
			Frame.setTitle("FPS: "+drawCount);
			drawCount = 0;
			timer = 0;
		}
	}
		stop();
		
	}
	
	public void tick() {
		switch (gameState) {
		case START:
			startscreen.tick();
			break;
		case GAME:
			if(tilesManager.isMapIsReady()) {

				player.tick();
				inventory.tick();
				workbenchUI.tick();
				tilesManager.tick();
				CreatureManager.tick();
			}

			if(System.currentTimeMillis() >lastSave+10000) {
				if(host) tilesManager.saveMap();
				inventory.saveInventory();
				lastSave = System.currentTimeMillis();
			}
			break;
		case PAUSE:
			break;
		case GAME_OVER:
			break;
		}


	}
	
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		//resize the screen
		screenHeight = Frame.getHeight();
		screenWidth = Frame.getWidth();

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		// Clear the full window, then scale logical (width x height) -> device resolution.
		// Rendering straight onto the device buffer keeps text/shapes crisp instead of
		// rasterizing them into a fixed-size buffer and upscaling the whole image.
		g.setColor(java.awt.Color.black);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.scale((double) screenWidth / width, (double) screenHeight / height);

		// Pixel art stays crisp; text/shapes antialias at the true window resolution.
		g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
				java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
				java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

		g2 = g;

		//pre-load all the items
		if(!preLoad) {
			preLoad = true;
			GameTextures.preloadImages(g2);
		}

		switch (gameState) {
		case START:
			startscreen.render(g2);
			break;
		case GAME:
			if(tilesManager.isMapIsReady()) {
				tilesManager.renderTile(g2);
				tilesManager.renderObjects(g2);
				tilesManager.renderDrops(g2);
				inventory.render(g2);
				chestUI.render(g2);
				workbenchUI.render(g2);
				if (chestUI.isOpen()) inventory.renderHoverItem(g2);
				if (workbenchUI.isOpen()) inventory.renderHoverItem(g2);
				minimap.render(g2);
			}else {
				StartScreen.renderBackScreen(g2);
			}
			break;
		case PAUSE:
			tilesManager.renderTile(g2);
			tilesManager.renderObjects(g2);
			pausescreen.render(g2);
			break;
		case GAME_OVER:
			gameOverScreen.render(g2);
			break;
		}

		g.dispose();
		bs.show();
		Toolkit.getDefaultToolkit().sync();
	}

	
public void window(int width,int height,String title,Main main){

	//set the game icon
	
	try {
		Taskbar.getTaskbar().setIconImage(ImageIO.read(getClass().getResource("/icon.png")));
	} catch (IOException e) {
	      System.out.println(e);
	      e.printStackTrace();
	  }
	
	Frame = new JFrame(title);
	Frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	Frame.setSize(width, height);
	Frame.setMinimumSize(new Dimension(width,height));
	Frame.setResizable(true);
	Frame.setLocationRelativeTo(null);
	Frame.add(main);
	Frame.setVisible(true);
	main.setFocusable(true);

	startscreen = new StartScreen();
	pausescreen = new PauseScreen();
	gameOverScreen = new GameOverScreen();
	minimap = new MinimapRenderer();
	
	tilesManager = new TilesManager();
	mouseManager = new MouseManager();
	player = new Player(1470, 1330,64);
	inventory = new Inventory(450, 630, 64);


	
	main.start();
	
	
	
	Frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
	        // אם יש שמירת מצב שדורשת זמן, תעשה אותה בצורה אסינכרונית (ברקע) או שתנסה ללכוד חריגות
	        try {
	            if(host) tilesManager.saveMap();
	            inventory.saveInventory();
	            ServerClientHandler.sendDataToServer("stop");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        // אחרי שסיימת את העבודה, סגור את החלון
	        Frame.dispose();
	        System.exit(0);
	    }
	});
	
	main.addKeyListener(player);
	main.addFocusListener(player);
	main.addMouseWheelListener(inventory);
	main.addMouseMotionListener(mouseManager);
	main.addMouseListener(mouseManager);

	}


	/**
	 * Client-side: apply the host's world to this player. Sets the world so per-player saves land
	 * in the host world's players/ folder, restores our saved inventory + position if we've played
	 * here before, otherwise blanks the inventory and spawns us at the world's spawn point.
	 */
	public static void joinWorld(String worldName, int spawnI, int spawnJ) {
		WorldManager.setWorld(worldName);
		boolean restored = inventory.loadInventory();
		if (!restored) {
			player.setLocation(spawnJ * TilesManager.tileSize, spawnI * TilesManager.tileSize);
		}
		tilesManager.setCameraX(player.getX() - width / 2 + player.getSizeX() / 2);
		tilesManager.setCameraY(player.getY() - height / 2 + player.getSizeY() / 2);
		// Tell the host (and other clients) our real position now that it's restored.
		ServerClientHandler.sendDataToServer(player.toString());
	}

	public static void main(String[] args) {

		new Main();
	}



}

