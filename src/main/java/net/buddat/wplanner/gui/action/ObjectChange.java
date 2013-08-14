package net.buddat.wplanner.gui.action;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Tile;

public class ObjectChange implements UndoableAction {

	private Tile tile;
	private int object;
	private boolean caveLayer;
	private byte oldObject;
	private byte newObject;
	
	public ObjectChange(Tile t, byte newObject, int object, boolean caveLayer) {
		this.tile = t;
		this.object = object;
		this.caveLayer = caveLayer;
		
		this.oldObject = tile.getObjectType(caveLayer, object);
		this.newObject = newObject;
		
		execute();
	}
	
	@Override
	public void execute() {
		tile.setObjectType(caveLayer, object, newObject);
	}

	@Override
	public void undo() {
		tile.setObjectType(caveLayer, object, oldObject);
	}

	@Override
	public void redo() {
		execute();
	}

}
