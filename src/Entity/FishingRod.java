package Entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import MainPackage.Main;
import MainPackage.TilesManager;

public class FishingRod{

	Random rand;
	final int tilesize = TilesManager.tileSize;
	long timerNextPullAnimation = 600;
	long nextBubblesTimer = 0;
	long firstBubblesTime;
	int XoffsetRop=0,YoffsetRop=0;
	int XoffsetPullRop=0,YoffsetPullRop=0;
	int baitX,baitY;
	boolean fishOnRod = false,pull = false;
	public ArrayList<Particles> particlesList; 
	
	QuadCurve2D lineCurve , lineCurvePull;
	
	public FishingRod() {
		 rand = new Random();
		 particlesList = new ArrayList<Particles>();
		 lineCurve = new QuadCurve2D.Float();
		 lineCurvePull = new QuadCurve2D.Float();
	}
		
	public void tick() { 
		//the bubbles create
		long currentTime = System.currentTimeMillis();
		
		if(currentTime > nextBubblesTimer && currentTime>=firstBubblesTime) {
			//the player movement
			if(currentTime > timerNextPullAnimation) {
				timerNextPullAnimation = currentTime + rand.nextInt(400,800);
				if(Main.player.getImagePosture() == 3) {
					Main.player.setImagePosture(4);
					pull = true;
				}else {
					Main.player.setImagePosture(3);
					pull = false;
				}
			}
			
			fishOnRod = true;
			nextBubblesTimer = currentTime + rand.nextInt(100,900);
			
			//create bubble particle
			particlesList.add(new Particles(baitX + Main.tilesManager.getCameraX(false)+ rand.nextInt(-10,18),
					baitY +  Main.tilesManager.getCameraY(false) + rand.nextInt(-10,18),
					16,
					0));
		}
		
		for(int i = 0;i< particlesList.size();i++) {
			if(particlesList.get(i).getTime() < currentTime - 400) {
				particlesList.remove(i);
				break;
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		for(Particles p: particlesList) {
			p.render(g2d);
		}
		g2d.setColor(Color.white);
		g2d.setStroke(new BasicStroke(1));
		if(pull) {
			g2d.draw(lineCurvePull);
		}else {
			g2d.draw(lineCurve);

		}
	}
	
	public void startFish(int Direction) {
		
		setBiteLocation();
		switch (Direction) {
		case 0:
			//dawn
			setRopOffSet(23, 36);
			setPullRopOffSet(23, 36);
			break;
		case 1:
			//left
			setRopOffSet(7, 30);
			setPullRopOffSet(10, 28);
			break;
		case 2:
			//right
			setRopOffSet(58, 28);
			setPullRopOffSet(54, 28);
			break;
		case 3:
			//up
			setRopOffSet(48, 29);
			setPullRopOffSet(46, 33);
			break;
		}
		
		int startX = Main.player.getX() - Main.tilesManager.getCameraX(false) + XoffsetRop;
		int startY = Main.player.getY() - Main.tilesManager.getCameraY(false) + YoffsetRop;
		
		int ctrlX = (startX + baitX) / 2; 
		int ctrlY = ((startY + baitY) / 2) - 30;
		
		lineCurve.setCurve(startX,startY,
				ctrlX,ctrlY,
				baitX,baitY);
		
		startX = Main.player.getX() - Main.tilesManager.getCameraX(false) + XoffsetPullRop;
		startY = Main.player.getY() - Main.tilesManager.getCameraY(false) + YoffsetPullRop;
		
		ctrlX = (startX + baitX) / 2; 
		ctrlY = ((startY + baitY) / 2) - 30;
		
		lineCurvePull.setCurve(startX,startY,
				ctrlX,ctrlY,
				baitX,baitY);
	}
	
	//changes the player's direction depending on their distance from the bait
	public void startFishingAtClosestDirection() {
		double leftRoute,rightRoute,upRoute,downRoute;
		int Direction;
		setBiteLocation();
		
		setRopOffSet(23, 36);
		downRoute = distance(Main.player.getX()-Main.tilesManager.getCameraX(false)+XoffsetRop,
				Main.player.getY()-Main.tilesManager.getCameraY(false)+YoffsetRop,
				baitX,
				baitY);
		
		
		setRopOffSet(7, 30);
		leftRoute = distance(Main.player.getX()-Main.tilesManager.getCameraX(false)+XoffsetRop,
				Main.player.getY()-Main.tilesManager.getCameraY(false)+YoffsetRop,
				baitX,
				baitY);
		
		
		setRopOffSet(58, 30);
		rightRoute = distance(Main.player.getX()-Main.tilesManager.getCameraX(false)+XoffsetRop,
				Main.player.getY()-Main.tilesManager.getCameraY(false)+YoffsetRop,
				baitX,
				baitY);
		
		
		setRopOffSet(48, 29);
		upRoute = distance(Main.player.getX()-Main.tilesManager.getCameraX(false)+XoffsetRop,
				Main.player.getY()-Main.tilesManager.getCameraY(false)+YoffsetRop,
				baitX,
				baitY);
		
		Direction = getMinRoute(downRoute,leftRoute,rightRoute,upRoute);
		Main.player.setImageDirection(Direction);
		startFish(Direction);
	}
	
	
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    private int getMinRoute(double num1, double num2, double num3, double num4) {
    	double nums[] = {num1,num2,num3,num4};
    	Arrays.sort(nums);
    	    	
    	if(nums[0] == num1 ) {
    		return 0;
    	}else if(nums[0] == num2){
    		return 1;
    	}else if(nums[0] == num3){
    		return 2;
    	}else if(nums[0] == num4){
    		return 3;
    	}
    	return 0;
    }
	
	private void setBiteLocation() {
		baitX = Main.mouseManeger.getMouseX();
		baitY = Main.mouseManeger.getMouseY();
	}
	
	public void setRopOffSet(int Xoffset,int Yoffset) {
		this.XoffsetRop = Xoffset;
		this.YoffsetRop = Yoffset;
	}
	
	public void setPullRopOffSet(int Xoffset,int Yoffset) {
		this.XoffsetPullRop = Xoffset;
		this.YoffsetPullRop = Yoffset;
	}
	
	public void setFirstbubbletime() {
		particlesList.clear();
		fishOnRod = false;
		firstBubblesTime = System.currentTimeMillis() + rand.nextInt(4000,6000);
	}
	public boolean isFishOnRod() {
		return fishOnRod;
	}
}
