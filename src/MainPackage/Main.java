package MainPackage;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Creature.CreatureManager;
import Entity.GameTextures;
import Entity.Text;
import Storage.Inventory;
import mutiplayer.ServerClientHandler;
import playerPackage.MouseManager;
import playerPackage.Player;


public class Main extends Canvas implements Runnable{

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
	
	public static Player player ,player2;
	public static Inventory inventory;
	public static TilesManager tilesManager;
	public static MouseManager mouseManeger; 
	public static BufferedImage tempScreen;
	
	public static StartScreen startscreen;
	public static PauseScreen pausescreen;
	Graphics2D g2;
	
	Text text;
	
	public static int gameState = 1;
	//1 - Start screen
	//2 - Game
	//3 - Pause
	
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
		
		while(delta >= 1) {
			tick();
			render();
			delta--;
			drawCount++;
		}	
		
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
		case 1: 
			startscreen.tick();
			break;
		case 2: 
			if(tilesManager.isMapIsReady()) {

				player.tick();
				inventory.tick();
				tilesManager.tick();
				CreatureManager.tick();
			}
			
			if(host && System.currentTimeMillis() >lastSave+10000) {
				tilesManager.saveMap();
				inventory.saveInventory();
				lastSave = System.currentTimeMillis();
			}
			break;
		}
		

	}
	
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		
		//pre-load all the images
		if(!preLoad) {
			preLoad = true;
			GameTextures.preloadImages(g2);
		}
		
		switch (gameState) {
		case 1: 
			startscreen.render(g2);
			break;
		case 2: 
			//draw the game
			if(tilesManager.isMapIsReady()) {
				tilesManager.renderTile(g2);
				tilesManager.renderObjects(g2);
				tilesManager.renderDrops(g2);
				inventory.render(g2);
			}else {
				StartScreen.renderBackScreen(g2);
			}
			break;
		case 3:
			tilesManager.renderTile(g2);
			tilesManager.renderObjects(g2);
			pausescreen.render(g2);
			break;
		}


		//resize the screen
		screenHeight = Frame.getHeight();
		screenWidth = Frame.getWidth();
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		
		g.drawImage(tempScreen, 0, 0, screenWidth, screenHeight, null);

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
	
	tilesManager = new TilesManager();
	mouseManeger = new MouseManager();
	player = new Player(1470, 1330,64);
	inventory = new Inventory(450, 630, 64);
	tempScreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
	g2 = (Graphics2D)tempScreen.getGraphics();
	

	
	main.start();
	
	
	
	Frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
	        // אם יש שמירת מצב שדורשת זמן, תעשה אותה בצורה אסינכרונית (ברקע) או שתנסה ללכוד חריגות
	        try {
	            if(host) {
	                tilesManager.saveMap();
	                inventory.saveInventory();
	            }
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
	main.addMouseWheelListener(inventory);
	main.addMouseMotionListener(mouseManeger);
	main.addMouseListener(mouseManeger);

	}


	public static void main(String[] args) {
		
		new Main();
	}



}

