package net.buddat.wplanner.gui.action;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Map;
import net.buddat.wplanner.map.Tile;

import java.awt.*;
import java.util.ArrayList;

public class BrushOverlayChange implements UndoableAction {

	private ArrayList<OverlayChange> changeList = new ArrayList<OverlayChange>();
	
	public BrushOverlayChange(Map m, int x, int y, int brushSize, Color c, boolean caveLayer) {
		int distance = (brushSize - 1) / 2;
		
		int xStart = (x - distance > 0 ? x - distance : 0);
		int xEnd = (x + distance + 1 < m.getMapWidth() ? x + distance + 1 : m.getMapWidth());
		int yStart = (y - distance > 0 ? y - distance : 0);
		int yEnd = (y + distance + 1 < m.getMapHeight() ? y + distance + 1 : m.getMapHeight());
		
		for (int i = xStart; i < xEnd; i++)
			for (int j = yStart; j < yEnd; j++) {
				Tile t = m.getTile(i, j, true);
				
				if (t == null) {
					m.addTile(i, j, new Tile(i, j));
					t = m.getTile(i, j, true);
				}
				
				changeList.add(new OverlayChange(m.getTile(i, j, true), c, caveLayer));
			}
	}

	@Override
	public void execute() {

	}

	@Override
	public void undo() {
		for (OverlayChange o : changeList) {
			o.undo();
		}
	}

	@Override
	public void redo() {
		for (OverlayChange o : changeList) {
			o.redo();
		}
	}

}
