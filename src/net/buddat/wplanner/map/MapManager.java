package net.buddat.wplanner.map;

import java.util.HashMap;

import javax.swing.JOptionPane;

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
		
		if (maps.containsKey(mapName)) {
			Map toClose = maps.get(mapName);
			if (toClose.hasChanges()) {
				int response = JOptionPane.showConfirmDialog(null, "Changes have been made to " + mapName + ". Would you like to save?", "Save changes?",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);
				
				if (response == JOptionPane.YES_OPTION) {
					toClose.saveMap();
				} else if (response == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			
			maps.remove(mapName);
			wplanner.getMainWindow().removeMap(mapName);
		}
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
	
	public String loadMap(String mapFile) {
		if (mapFile.endsWith(Constants.MAP_FILE_EXT)) {
			Logger.log("Opening map: " + mapFile);
			
			String location;				
			String mapName;
			if (mapFile.contains("/")) {
				location = mapFile.substring(0, mapFile.lastIndexOf("/"));
				mapName = mapFile.substring(mapFile.lastIndexOf("/") + 1, mapFile.lastIndexOf("."));
			} else {
				location = WPlanner.getBaseDir();
				mapName = mapFile.substring(0, mapFile.lastIndexOf("."));
			}
			
			WPlanner.getConfig().setDefaultSaveDir(location + "/", true);
			
			Map toLoad = new Map(mapName, 0, 0);
			toLoad.loadMap(mapFile);
			
			return addMap(mapName, toLoad);
		}
		
		return null;
	}
}
