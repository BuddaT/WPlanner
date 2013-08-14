package net.buddat.wplanner.gui.action;

import java.awt.Color;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Tile;

public class LabelChange implements UndoableAction {
	
	private Tile t;
	private String oldLabel, newLabel;
	private Color oldColor, newColor;
	private boolean caveLayer;
	
	public LabelChange(Tile t, String newLabel, Color newColor, boolean caveLayer) {
		this.t = t;
		this.newLabel = newLabel;
		this.caveLayer = caveLayer;
		this.newColor = newColor;
		
		this.oldLabel = t.getLabel(caveLayer);
		this.oldColor = t.getLabelColor(caveLayer);
		
		execute();
	}

	@Override
	public void execute() {
		t.setLabelColor(caveLayer, newColor);
		t.setLabel(caveLayer, newLabel);
	}

	@Override
	public void undo() {
		t.setLabelColor(caveLayer, oldColor);
		t.setLabel(caveLayer, oldLabel);
	}

	@Override
	public void redo() {
		execute();
	}

}
