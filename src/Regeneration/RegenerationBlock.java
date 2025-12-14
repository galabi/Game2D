package Regeneration;

import MainPackage.TilesManager;
import entity.GameObject;

public class RegenerationBlock implements Comparable<RegenerationBlock> {
	private int x,y,mapI,mapJ;
	private GameObject objectToGrow;
	private long GrowthTime;
	private String targetGrow;
	
	public RegenerationBlock(GameObject objectToGrow,int x,int y,String targetGrow) {
		this.objectToGrow = objectToGrow;
		this.targetGrow = targetGrow;
		this.x = x;
		this.y = y;
		this.mapI = y/TilesManager.tileSize;
		this.mapJ = x/TilesManager.tileSize;
		setNextGowthTime();
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getMapI() {
		return mapI;
	}
	public int getMapJ() {
		return mapJ;
	}
	public GameObject getObject() {
		return objectToGrow;
	}
	public long getGrowthTime() {
		return GrowthTime;
	}
	public String getTargetGrow() {
		return targetGrow;
	}
	public void setTargetGrow(String targetGrow) {
		this.targetGrow = targetGrow;
	}
	public void setNextGowthTime() {
		GrowthTime = RegenerationManager.getNextGowthTime() ;
	}
	
	public boolean isReadyToGrow() {
		return (GrowthTime <= System.currentTimeMillis());
	}

	@Override
	public int compareTo(RegenerationBlock other) {
		return Long.compare(this.GrowthTime, other.GrowthTime);
	}
}
