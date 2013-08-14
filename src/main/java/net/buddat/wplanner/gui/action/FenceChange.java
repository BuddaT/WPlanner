package net.buddat.wplanner.gui.action;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Tile;

public class FenceChange implements UndoableAction {
	
	private Tile tile;
	private int fence;
	private boolean caveLayer;
	private byte oldFence;
	private byte newFence;
	
	public FenceChange(Tile t, byte newFence, int fence, boolean caveLayer) {
		this.tile = t;
		this.fence = fence;
		this.caveLayer = caveLayer;
		
		this.oldFence = tile.getFenceType(caveLayer, fence);
		this.newFence = newFence;
		
		execute();
	}

	@Override
	public void execute() {
		tile.setFenceType(caveLayer, fence, newFence);
	}

	@Override
	public void undo() {
		tile.setFenceType(caveLayer, fence, oldFence);
	}

	@Override
	public void redo() {
		execute();
	}

}
