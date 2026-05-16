package regeneration;

import MainPackage.TilesManager;
import entity.GameObject;

public class RegenerationBlock implements Comparable<RegenerationBlock> {
	private int x,y,mapI,mapJ;
	private GameObject objectToGrow;
	private long GrowthTime;
	private GrowthType growthType;

	public RegenerationBlock(GameObject objectToGrow,int x,int y,GrowthType growthType) {
		this.objectToGrow = objectToGrow;
		this.growthType = growthType;
		this.x = x;
		this.y = y;
		this.mapI = y/TilesManager.tileSize;
		this.mapJ = x/TilesManager.tileSize;
		setNextGrowthTime();
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
	public GrowthType getGrowthType() {
		return growthType;
	}
	public void setGrowthType(GrowthType growthType) {
		this.growthType = growthType;
	}
	public void setNextGrowthTime() {
		GrowthTime = RegenerationManager.getNextGrowthTime() ;
	}
	
	public boolean isReadyToGrow() {
		return (GrowthTime <= System.currentTimeMillis());
	}

	@Override
	public int compareTo(RegenerationBlock other) {
		return Long.compare(this.GrowthTime, other.GrowthTime);
	}
}
