package net.buddat.wplanner.map;

import java.util.HashMap;

import net.buddat.wplanner.util.Logger;

public class MapManager {

	private HashMap<String, Map> maps = new HashMap<String, Map>();
	
	public MapManager() { }
	
	public void addMap(String mapName, Map toAdd) {
		int count = 0;
		while (maps.containsKey(mapName)) {
			if (mapName.endsWith("." + (count - 1)))
				mapName = mapName.substring(0, mapName.lastIndexOf(".")) + "." + count;
			else
				mapName += "." + count;
			count++;
			
			
			if (!maps.containsKey(mapName))
				Logger.log("Map name already found, renamed to: " + mapName);
		}
			
		maps.put(mapName, toAdd);
	}
	
	public void removeMap(String mapName) {
		if (maps.containsKey(mapName))
			maps.remove(mapName);
	}
	
	public Map getMap(String mapName) {
		return maps.get(mapName);
	}
}
