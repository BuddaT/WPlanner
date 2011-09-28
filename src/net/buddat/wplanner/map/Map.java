package net.buddat.wplanner.map;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JOptionPane;

import net.buddat.wplanner.WPlanner;
import net.buddat.wplanner.util.Constants;
import net.buddat.wplanner.util.Logger;

public class Map {

	private String mapName;
	private int mapWidth, mapHeight;
	private boolean changes = false;
	
	private HashMap<Point, Tile> tileMap = new HashMap<Point, Tile>();
		
	public Map () { }
	
	public Map(String name, int x, int y) {
		setMapName(name);
		setMapWidth(x);
		setMapHeight(y);
	}
	
	public void saveMap() {
		saveMap(null);
	}
	
	public void saveMap(String fileName) {		
		if (fileName == null)
			fileName = WPlanner.getConfig().getDefaultSaveDir() + mapName + Constants.MAP_FILE_EXT;
		if (!fileName.endsWith(Constants.MAP_FILE_EXT))
			fileName += Constants.MAP_FILE_EXT;
		
		File file = new File(fileName + Constants.MAP_TEMP_FILE_EXT);
		if (file.exists()) {
			file.delete();
			Logger.log("Deleted old " + Constants.MAP_TEMP_FILE_EXT + " map file: " + fileName  + Constants.MAP_TEMP_FILE_EXT);
		}
		
		ByteBuffer buffer = ByteBuffer.allocate((tileMap.size() * Tile.BYTES_USED) + Constants.MAP_EXTRA_BYTES);
		
		buffer.putInt(mapWidth).putInt(mapHeight);
		
		buffer.putInt(tileMap.size());
		
		for (Point p : tileMap.keySet())
			buffer.put(getTile(p, false).getData());
		
		buffer.flip();
		
		byte[] mapData = buffer.array();
		
		try {
			FileOutputStream fos = new FileOutputStream(fileName + Constants.MAP_TEMP_FILE_EXT);
			
			fos.write(mapData);

			fos.close();
			Logger.log("Saved map to: " + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		compressFile(fileName);
	}
	
	public void loadMap(String fileName) {
		if (!fileName.contains("/"))
			fileName = "/" + fileName;
		
		
		
		decompressFile(fileName);
		try {			
			FileInputStream fis = new FileInputStream(fileName + Constants.MAP_TEMP_FILE_EXT);
			DataInputStream dis = new DataInputStream(fis);
			
			this.setMapWidth(dis.readInt());
			this.setMapHeight(dis.readInt());
			
			int tileCount = dis.readInt();
			int count = 0;
			
			while(count < tileCount) {
				Tile t;
				
				short tileX = dis.readShort();
				short tileY = dis.readShort();
				
				t = new Tile(tileX, tileY);
				
				for (int j = 0; j < 2; j++) {
					boolean caveLayer = j != 0;
					
					t.setTerrainType(caveLayer, dis.readByte());
					
					byte temp = dis.readByte();
					if (temp != -1) {
						t.setObjectType(caveLayer, 0, temp);
						t.setObjectRotation(caveLayer, 0, dis.readByte());
						for (int i = 1; i < Tile.OBJECT_COUNT; i++) {
							t.setObjectType(caveLayer, i, dis.readByte());
							t.setObjectRotation(caveLayer, i, dis.readByte());
						}
					}
					
					temp = dis.readByte();
					if (temp != -1) {
						t.setFenceType(caveLayer, 0, temp);
						t.setFenceType(caveLayer, 1, dis.readByte());
					}
					
					t.setHeight(caveLayer, dis.readShort());
					
					int tempColour = dis.readInt();
					if (tempColour != Tile.NULL_COLOUR)
						t.setOverlayColor(caveLayer, tempColour);
					tempColour = dis.readInt();
					if (tempColour != Tile.NULL_COLOUR)
						t.setLabelColor(caveLayer, tempColour);
					
					byte labelLength = dis.readByte();
					if (labelLength > 0) {
						byte[] label = new byte[labelLength];
						dis.read(label);
						t.setLabel(caveLayer, new String(label));
					}
				}
				
				this.addTile(tileX, tileY, t);
				
				dis.skipBytes(dis.readShort());
				
				count++;
			}
			
			dis.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new File(fileName + Constants.MAP_TEMP_FILE_EXT).delete();
	}
	
	private void compressFile(String fileName) {
		try {
		    GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(fileName));
		    FileInputStream in = new FileInputStream(fileName + Constants.MAP_TEMP_FILE_EXT);

		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
		    in.close();

		    new File(fileName + Constants.MAP_TEMP_FILE_EXT).delete();
		    
		    out.finish();
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void decompressFile(String fileName) {
		try {
		    GZIPInputStream in = new GZIPInputStream(new FileInputStream(fileName));
		    FileOutputStream out = new FileOutputStream(fileName + Constants.MAP_TEMP_FILE_EXT);

		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }

		    in.close();
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addTile(int x, int y, Tile t) {
		addTile(new Point(x, y), t);
	}
	
	public void addTile(Point point, Tile t) {
		if (tileMap.containsKey(point))
			tileMap.remove(point);
		
		tileMap.put(point, t);
	}
	
	public void removeTile(int x, int y) {
		removeTile(new Point(x, y));
	}
	
	public void removeTile(Point p) {
		tileMap.remove(p);
	}

	public Tile getTile(int x, int y, boolean toChange) {
		return getTile(new Point(x, y), toChange);
	}

	public Tile getTile(Point point, boolean toChange) {
		if (toChange)
			changes = true;
		
		if (tileMap.containsKey(point))
			return tileMap.get(point);
		else
			return null;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}

	public void resizeMap(int newWidth, int newHeight, int widthOffset, int heightOffset) {
		setMapWidth(newWidth);
		setMapHeight(newHeight);
		
		HashMap<Point, Tile> newMap = new HashMap<Point, Tile>();
		
		for (Point p : tileMap.keySet()) {
			Tile t = tileMap.get(p);
			short newX = (short) (p.getX() + widthOffset);
			short newY = (short) (p.getY() + heightOffset);
			
			if (newX >= 0 && newX < getMapWidth() && newY >= 0 && newY < getMapHeight()) {
				t.setX(newX);
				t.setY(newY);
				newMap.put(new Point(newX, newY), t);
			}
		}
		
		tileMap = newMap;
	}
	
	public boolean hasChanges() {
		return changes;
	}
}
