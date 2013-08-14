package net.buddat.wplanner.gui.action;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Tile;

public class ObjectRotationChange implements UndoableAction {
	
	private Tile tile;
	private int object;
	private boolean caveLayer;
	private byte oldObjectRotation;
	private byte newObjectRotation;
	
	public ObjectRotationChange(Tile t, byte newObjectRotation, int object, boolean caveLayer) {
		this.tile = t;
		this.object = object;
		this.caveLayer = caveLayer;
		
		this.oldObjectRotation = tile.getObjectRotation(caveLayer, object);
		this.newObjectRotation = newObjectRotation;
		
		execute();
	}
	
	@Override
	public void execute() {
		tile.setObjectRotation(caveLayer, object, newObjectRotation);
	}

	@Override
	public void undo() {
		tile.setObjectRotation(caveLayer, object, oldObjectRotation);
	}

	@Override
	public void redo() {
		execute();
	}

}
