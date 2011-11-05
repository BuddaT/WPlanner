package net.buddat.wplanner.map;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import net.buddat.wplanner.WPlanner;
import net.buddat.wplanner.util.Constants;
import net.buddat.wplanner.util.Logger;

public class MapManager {

	private HashMap<String, Map> maps = new HashMap<String, Map>();
	private HashMap<String, String> saveDirs = new HashMap<String, String>();
	
	public MapManager(WPlanner wplanner) {
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
	
	public void removeAllMaps() {
		Set<String> keys = maps.keySet();
		for (Object s : keys.toArray())
			removeMap((String) s);
	}
	
	public void removeMap(String mapName) {
		if (maps.containsKey(mapName)) {
			Map toClose = maps.get(mapName);
			if (toClose.hasChanges()) {
				int response = JOptionPane.showConfirmDialog(null, "Changes have been made to " + mapName + ". Would you like to save?", "Save changes?",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);
				
				if (response == JOptionPane.YES_OPTION) {
					saveMap(mapName);
				} else if (response == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			
			maps.remove(mapName);
			WPlanner.getMainWindow().removeMap(mapName);
		}
	}
	
	public Map getMap(String mapName) {
		return maps.get(mapName);
	}
	
	public void openSaveDialog(String mapName) {
		JFileChooser jfc = new JFileChooser(WPlanner.getConfig().getDefaultSaveDir());
		jfc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				
				if (f.getAbsolutePath().endsWith(".wp2"))
					return true;
				
				return false;
			}

			@Override
			public String getDescription() {
				return "WPlanner Map (.wp2)";
			}
		});
		
		int retVal = jfc.showSaveDialog(WPlanner.getMainWindow());
		if (retVal == JFileChooser.APPROVE_OPTION) {
			String path = jfc.getSelectedFile().getAbsolutePath();
			
			saveMap(mapName, path);
		}
	}
	
	public void saveMap(String mapName) {
		if (saveDirs.containsKey(mapName))
			maps.get(mapName).saveMap(saveDirs.get(mapName));
		else {
			openSaveDialog(mapName);
		}
	}
	
	public void saveMap(String mapName, String fileName) {
		if (saveDirs.containsKey(mapName))
			saveDirs.remove(mapName);
		
		saveDirs.put(mapName, (fileName.endsWith(".wp2") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName));
		
		maps.get(mapName).saveMap(fileName);
	}
	
	public void saveMapImage(String mapName, BufferedImage img, boolean saveAs) {
		String filePath;
		
		if (!saveDirs.containsKey(mapName) || saveAs) {
			JFileChooser jfc = new JFileChooser(WPlanner.getConfig().getDefaultSaveDir());
			jfc.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					if (f.isDirectory())
						return true;
					
					if (f.getAbsolutePath().endsWith(".png"))
						return true;
					
					return false;
				}

				@Override
				public String getDescription() {
					return "PNG Image (.png)";
				}
			});
			
			int retVal = jfc.showSaveDialog(WPlanner.getMainWindow());
			if (retVal == JFileChooser.APPROVE_OPTION) {
				filePath = jfc.getSelectedFile().getAbsolutePath();
				if (filePath.endsWith(".png"))
					filePath = filePath.substring(0, filePath.lastIndexOf("."));
				
				saveDirs.put(mapName, filePath);
			} else {
				return;
			}
		} else {
			filePath = saveDirs.get(mapName);
		}
		
		if (!filePath.endsWith(".png"))
			filePath += ".png";
		
		if (filePath != null) {
			try {
				ImageIO.write((RenderedImage) img, "png", new File(filePath));
				Logger.log("Saved " + mapName + " image to: " + filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String loadMap(String mapFile) {
		if (mapFile.endsWith(Constants.MAP_FILE_EXT)) {
			Logger.log("Opening map: " + mapFile);
			
			String location;				
			String mapName;
			
			mapFile = mapFile.replace("\\", "/");
			
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
			
			mapName = addMap(mapName, toLoad);
			
			if (mapFile.endsWith(".wp2"))
				mapFile = mapFile.substring(0, mapFile.lastIndexOf("."));
			saveDirs.put(mapName, mapFile);
			
			return mapName;
		}
		
		return null;
	}
}
