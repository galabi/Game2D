package mapRender;

import java.util.ArrayList;

public class MapObject extends MapEntity{
	
	boolean gate, breakable;
	ArrayList<Integer> itemWhenBroken;
	String objectName = "";
	float renderOffSet;
	
	public MapObject() {
		
	}
	
	public MapObject(int id) {
		super(id);
	}
	
	public boolean isBreakable(){
		return breakable;
	}
	
	public ArrayList<Integer> getItemWhenBroken(){
		return itemWhenBroken;
	}
	
	public boolean isGate() {
	return gate;	
	}
	
	public String getName() {
		return objectName;
	}

	public boolean isTree() {
		return "Tree".equals(objectName);
	}
	
	public float getObjectRenderOffSet() {
		return renderOffSet;
	}

}
