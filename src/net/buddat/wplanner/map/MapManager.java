package net.buddat.wplanner.map;

import java.util.HashMap;

import net.buddat.wplanner.WPlanner;
import net.buddat.wplanner.util.Constants;
import net.buddat.wplanner.util.Logger;

public class MapManager {

	private WPlanner wplanner;
	private HashMap<String, Map> maps = new HashMap<String, Map>();
	
	public MapManager(WPlanner wplanner) { 
		this.wplanner = wplanner;
	}
	
	public String addMap(String mapName, Map toAdd) {
		int count = 1;
		while (maps.containsKey(mapName)) {
			if (mapName.endsWith("." + (count - 1)))
				mapName = mapName.substring(0, mapName.lastIndexOf(".")) + "." + count;
			else
				mapName += "." + count;
			count++;
			
			
			if (!maps.containsKey(mapName))
				Logger.log("Map name already found, renamed to: " + mapName);
		}
		
		toAdd.setMapName(mapName);
		maps.put(mapName, toAdd);
		
		return mapName;
	}
	
	public void removeMap(String mapName) {
		if (maps.containsKey(mapName))
			maps.remove(mapName);
	}
	
	public Map getMap(String mapName) {
		return maps.get(mapName);
	}
	
	public void saveMap(String mapName) {
		maps.get(mapName).saveMap();
	}
	
	public void saveMap(String mapName, String path) {
		maps.get(mapName).saveMap(path);
	}
	
	public void loadMap(String mapFile) {
		if (mapFile.endsWith(Constants.MAP_FILE_EXT)) {
			Logger.log("Opening map: " + mapFile);
			
			String location;				
			String mapName;
			if (mapFile.contains("/")) {
				location = mapFile.substring(0, mapFile.lastIndexOf("/"));
				mapName = mapFile.substring(mapFile.lastIndexOf("/") + 1, mapFile.lastIndexOf("."));
			} else {
				location = wplanner.getBaseDir();
				mapName = mapFile.substring(0, mapFile.lastIndexOf("."));
			}
			
			WPlanner.getConfig().setDefaultSaveDir(location, true);
			
			Map toLoad = new Map(mapName, 0, 0);
			toLoad.loadMap(mapFile);
			addMap(mapName, toLoad);
		}
	}
}
