package Regeneration;

import Entity.Object;

public class RegenereationBlock {
	private int x,y,nextId;
	private Object objectToGrow;
	private long GrowthTime;
	
	public RegenereationBlock(Object objectToGrow,int x,int y,int nextId) {
		this.objectToGrow = objectToGrow;
		this.nextId = nextId;
		this.x = x;
		this.y = y;
		setNextGowthTime();
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public Object getObject() {
		return objectToGrow;
	}
	public long getGrowthTime() {
		return GrowthTime;
	}
	public int getNextId() {
		return nextId;
	}
	public void setNextId(int nextId) {
		this.nextId = nextId;
	}
	public void setNextGowthTime() {
		GrowthTime = RegenereationManager.getNextGowthTime() ;
	}
}
