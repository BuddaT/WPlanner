package net.buddat.wplanner.map;

import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Tile {
	
	/* Object Map:
	 * 
	 *  0 , 1 , 2
	 *  3 , 4 , 5
	 *  6 , 7 , 8
	 */
	
	public static final int NULL_COLOUR = 16777215;
	
	public static final int LEFT_FENCE = 0, TOP_FENCE = 1;
	public static final int OBJECT_COUNT = 9, FENCE_COUNT = 2;
	
	public static final int MAX_LABEL_CHARS = 32;
	
	/*
	 * Total amount of bytes used for each tile.
	 * Used when constructing the byte array for saving.
	 */
	public static final int BYTES_USED = 256;
	
	private short posX, posY;
	
	private byte terrainType, caveTerrainType;
	private byte[] objectTypes, caveObjectTypes;
	private byte[] objectRotate, caveObjectRotate;
	private byte[] fenceTypes, caveFenceTypes;
	private Color overlayColor, caveOverlayColor, labelColor, caveLabelColor;
	private short height, caveHeight;
	private String label = "", caveLabel = "";
	
	public Tile(int x, int y) {
		setX((short) x);
		setY((short) y);
	}
	
	public byte[] getData() {
		int byteCounter = 0;
		ByteBuffer buffer = ByteBuffer.allocate(BYTES_USED);
		
		buffer.putShort(getX()).putShort(getY());
		
		byteCounter += 4;
		
		for (int j = 0; j < 2; j++) {
			boolean caveLayer = j != 0;
			
			buffer.put(getTerrainType(caveLayer));
			byteCounter++;
			
			if ((caveLayer ? caveObjectTypes : objectTypes) != null) {
				for (int i = 0; i < OBJECT_COUNT; i++) {
					buffer.put(getObjectType(caveLayer, i));
					buffer.put(getObjectRotation(caveLayer, i));
					byteCounter += 2;
				}
			} else {
				buffer.put((byte) -1);
				byteCounter++;
			}
			
			if ((caveLayer ? caveFenceTypes : fenceTypes) != null) {
				for (int i = 0; i < FENCE_COUNT; i++) {
					buffer.put(getFenceType(caveLayer, i));
					byteCounter++;
				}
			} else {
				buffer.put((byte) -1);
				byteCounter++;
			}
			
			buffer.putShort(getHeight(caveLayer));
			byteCounter += 2;
			
			if (getOverlayColor(caveLayer) == null)
				buffer.putInt(NULL_COLOUR);
			else
				buffer.putInt(getOverlayColor(caveLayer).getRGB());
			if (getLabelColor(caveLayer) == null)
				buffer.putInt(NULL_COLOUR);
			else
				buffer.putInt(getLabelColor(caveLayer).getRGB());
			
			byteCounter += 8;
	
			byte[] labelBytes = getLabel(caveLayer).getBytes();
			byte labelLength = (byte) labelBytes.length;
			
			buffer.put(labelLength);
			byteCounter++;
			
			if (labelLength > 0) {
				buffer.put(labelBytes);
				byteCounter += labelLength; 
			}
		}
		
		short bytesToSkip = (short) (BYTES_USED - (byteCounter + 2));
		buffer.putShort(bytesToSkip);
		
		buffer.flip();
		
		return buffer.array();
	}

	public short getX() {
		return posX;
	}

	public void setX(short x) {
		this.posX = x;
	}

	public short getY() {
		return posY;
	}

	public void setY(short y) {
		this.posY = y;
	}

	public byte getTerrainType(boolean cave) {
		return cave ? caveTerrainType : terrainType;
	}

	public void setTerrainType(boolean cave, byte terrainType) {
		if (cave)
			this.caveTerrainType = terrainType;
		else
			this.terrainType = terrainType;
	}

	public byte getObjectType(boolean cave, int loc) {
		if (cave)
			return caveObjectTypes == null ? 0 : caveObjectTypes[loc];
		else
			return objectTypes == null ? 0 : objectTypes[loc];
	}

	public void setObjectType(boolean cave, int loc, byte objectType) {
		if (cave) {
			if (caveObjectTypes == null) {
				this.caveObjectTypes = new byte[OBJECT_COUNT];
				this.caveObjectRotate = new byte[OBJECT_COUNT];
			}
			this.caveObjectTypes[loc] = objectType;
		} else {
			if (objectTypes == null) {
				this.objectTypes = new byte[OBJECT_COUNT];
				this.objectRotate = new byte[OBJECT_COUNT];
			}
			this.objectTypes[loc] = objectType;
		}
	}

	public byte getObjectRotation(boolean cave, int loc) {
		if (cave)
			return caveObjectRotate == null ? 0 : caveObjectRotate[loc];
		else
			return objectRotate == null ? 0 : objectRotate[loc];
	}
	
	public void setObjectRotation(boolean cave, int loc, byte objectRotation) {
		if (cave)
			this.caveObjectRotate[loc] = objectRotation;
		else
			this.objectRotate[loc] = objectRotation;
	}

	public byte getFenceType(boolean cave, int loc) {
		if (cave)
			return caveFenceTypes == null ? 0 : caveFenceTypes[loc];
		else
			return fenceTypes == null ? 0 : fenceTypes[loc];
	}

	public void setFenceType(boolean cave, int loc, byte fenceType) {
		if (cave) {
			if (caveFenceTypes == null)
				this.caveFenceTypes = new byte[FENCE_COUNT];
			this.caveFenceTypes[loc] = fenceType;
		} else {
			if (fenceTypes == null)
				this.fenceTypes = new byte[FENCE_COUNT];
			this.fenceTypes[loc] = fenceType;
		}
	}

	public Color getOverlayColor(boolean cave) {
		return cave ? caveOverlayColor : overlayColor;
	}

	public void setOverlayColor(boolean cave, Color overlayColor) {
		if (cave)
			this.caveOverlayColor = overlayColor;
		else
			this.overlayColor = overlayColor;
	}
	
	public void setOverlayColor(boolean cave, int tempColour) {
		this.setOverlayColor(cave, new Color(tempColour, true));
	}

	public short getHeight(boolean cave) {
		return cave ? caveHeight : height;
	}
	
	public void setHeight(boolean cave, short height) {
		if (cave)
			this.caveHeight = height;
		else
			this.height = height;
	}

	public String getLabel(boolean cave) {
		return cave ? caveLabel : label;
	}
	
	public void setLabel(boolean cave, String label) {
		if (label == null)
			label = "";
		
		String oldLabel = label;
		if (label != null)
			if (label.length() > MAX_LABEL_CHARS) {
				label = label.substring(0, MAX_LABEL_CHARS - 1);
				System.out.println("Label too long, resizing to " + MAX_LABEL_CHARS + " chars");
				new IOException("Label too long: " + posX + "," + posY + ":" + oldLabel).printStackTrace();
			}
		
		if (cave)
			this.caveLabel = label;
		else
			this.label = label;
	}
	
	public Color getLabelColor(boolean cave) {
		return cave ? caveLabelColor : labelColor;
	}
	
	public void setLabelColor(boolean cave, Color labelColor) {
		if (cave)
			this.caveLabelColor = labelColor;
		else
			this.labelColor = labelColor;
	}
	
	public void setLabelColor(boolean cave, int tempColor) {
		this.setLabelColor(cave, new Color(tempColor, true));
	}
}
