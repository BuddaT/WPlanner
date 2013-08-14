package net.buddat.wplanner.gui.action;

import java.util.ArrayList;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Map;
import net.buddat.wplanner.map.Tile;

public class FenceLine implements UndoableAction {
	
	private int xStart, yStart;
	private int xEnd, yEnd;
	private int fencePos;
	private byte fenceType;
	private boolean caveLayer;
	private Map map;
	
	private ArrayList<FenceChange> changeList = new ArrayList<FenceChange>();
	
	public FenceLine(Map m, int x, int y, byte fenceType, int fencePos, boolean caveLayer) {
		this.map = m;
		this.xStart = x;
		this.yStart = y;
		this.fenceType = fenceType;
		this.fencePos = fencePos;
		this.caveLayer = caveLayer;
		
		execute();
	}
	
	public void update(int xEnd, int yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
			
		execute();
	}

	@Override
	public void execute() {
		if (!changeList.isEmpty())
			undo();
		
		if (fencePos == Tile.TOP_FENCE) {
			for (int i = (xStart < xEnd ? xStart : xEnd); i <= (xEnd > xStart ? xEnd : xStart); i++) {
				if (map.getTile(i, yStart, false) == null)
					map.addTile(i, yStart, new Tile(i, yStart));
				
				changeList.add(new FenceChange(map.getTile(i, yStart, true), fenceType, fencePos, caveLayer));
			}
		} else if (fencePos == Tile.LEFT_FENCE) {
			for (int i = (yStart < yEnd ? yStart : yEnd); i <= (yEnd > yStart ? yEnd : yStart); i++) {
				if (map.getTile(xStart, i, false) == null)
					map.addTile(xStart, i, new Tile(xStart, i));
				
				changeList.add(new FenceChange(map.getTile(xStart, i, true), (byte) (fenceType + 1), fencePos, caveLayer));
			}
		}
	}

	@Override
	public void undo() {
		for (FenceChange f : changeList) {
			f.undo();
		}
	}

	@Override
	public void redo() {
		for (FenceChange f : changeList) {
			f.redo();
		}
	}


}
