package net.buddat.wplanner.gui.action;

import java.awt.Color;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Tile;

public class OverlayChange implements UndoableAction {
	
	private Tile tile;
	private boolean caveLayer;
	private Color newColor, oldColor;
	
	public OverlayChange(Tile t, Color c, boolean caveLayer) {
		this.tile = t;
		this.newColor = c;
		this.caveLayer = caveLayer;
		
		oldColor = tile.getOverlayColor(caveLayer);
		
		execute();
	}

	@Override
	public void execute() {
		if (oldColor != newColor)
			tile.setOverlayColor(caveLayer, newColor);
	}

	@Override
	public void undo() {
		if (oldColor != newColor)
			tile.setOverlayColor(caveLayer, oldColor);
	}

	@Override
	public void redo() {
		execute();
	}

}
