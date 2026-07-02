package regeneration;

public class RegenerationBlock implements Comparable<RegenerationBlock> {
	private final int mapI, mapJ;
	private long GrowthTime;
	private GrowthType growthType;

	public RegenerationBlock(int mapI, int mapJ, GrowthType growthType) {
		this.mapI = mapI;
		this.mapJ = mapJ;
		this.growthType = growthType;
		setNextGrowthTime();
	}

	public int getMapI() {
		return mapI;
	}
	public int getMapJ() {
		return mapJ;
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
