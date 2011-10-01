package net.buddat.wplanner.gui.action;

import java.util.ArrayList;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Map;

public class BrushTileChange implements UndoableAction {
	
	private ArrayList<TileChange> tilesChanged = new ArrayList<TileChange>();
	
	public BrushTileChange(Map m, int x, int y, int size, byte newTile, boolean caveLayer) {
		int distance = (size - 1) / 2;
		
		int xStart = (x - distance > 0 ? x - distance : 0);
		int xEnd = (x + distance + 1 < m.getMapWidth() ? x + distance + 1 : m.getMapWidth());
		int yStart = (y - distance > 0 ? y - distance : 0);
		int yEnd = (y + distance + 1 < m.getMapHeight() ? y + distance + 1 : m.getMapHeight());
		
		for (int i = xStart; i < xEnd; i++)
			for (int j = yStart; j < yEnd; j++) {
				tilesChanged.add(new TileChange(m.getTile(i, j, true), newTile, caveLayer, false));
			}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public void undo() {
		for (TileChange t : tilesChanged) {
			t.undo();
		}
	}

	@Override
	public void redo() {
		for (TileChange t : tilesChanged) {
			t.redo();
		}
	}

}
