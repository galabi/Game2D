package Regeneration;

import java.util.ArrayList;
import java.util.Random;

import Entity.Object;
import mutiplayer.ServerClientHandler;

public class RegenerationManager {
	
	final private static int maxGrowIdRock = 33,maxGrowIdTree = 1;
	static ArrayList<RegenerationBlock> regenereationList = new ArrayList<>();;
	
	public static void tick() {
		if(regenereationList.isEmpty()) return;
			
		RegenerationBlock temp = regenereationList.get(0);
		if(temp.getGrowthTime() < System.currentTimeMillis()) {
			temp.getObject().setType(temp.getNextId());
			ServerClientHandler.sendDataToServer("update_block " + temp.getY() + " " + temp.getX() + " " + temp.getObject().getId());
			if(temp.getObject().getId() == maxGrowIdRock || temp.getObject().getId() == maxGrowIdTree) {
				regenereationList.remove(0);
			}else {
				temp.setNextId(temp.getNextId()+1);
				temp.setNextGowthTime();
			}
		}
		
	}
	
	//add the object to the growth list;
	public static void insertToGrowthList(Object nextObject,int x,int y) {
		int nextId = 0;
		//tree case
		if(nextObject.getId() == 2) {
			nextId = 1;
		//rock case
		}else if(nextObject.getId() >= 30 && nextObject.getId() <maxGrowIdRock) {
			nextId = nextObject.getId()+1;
			
			//check if the rock is in the list, so it won't add the same block to the list
			for(RegenerationBlock i : regenereationList) {
				if(i.getX() == x && i.getY() == y) {
					i.setNextGowthTime();
					i.setNextId(nextId);
					return;
				}
			}
		}else {
			return;
		}
		regenereationList.add(new RegenerationBlock(nextObject,x,y,nextId));
	}
	
	public static long getNextGowthTime() {
		if(regenereationList.isEmpty()) {
			return System.currentTimeMillis() + new Random().nextInt(30000,50000);
		}
		return regenereationList.get(regenereationList.size()-1).getGrowthTime()+ new Random().nextInt(10000,30000);
	}
	
	public static void resetList() {
		regenereationList.clear();
	}
	
}
