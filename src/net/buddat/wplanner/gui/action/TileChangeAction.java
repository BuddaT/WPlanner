package net.buddat.wplanner.gui.action;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Tile;

public class TileChangeAction implements UndoableAction {
	
	private Tile tile;
	private byte[] oldTerrain = new byte[Tile.LAYER_COUNT];
	private byte[] newTerrain = new byte[Tile.LAYER_COUNT];
	
	public TileChangeAction(Tile t, byte newTile, int layer) {
		this.tile = t;
		
		oldTerrain[Tile.TOP_LAYER] = tile.getTerrainType(false);
		oldTerrain[Tile.CAVE_LAYER] = tile.getTerrainType(true);
		
		System.arraycopy(oldTerrain, 0, newTerrain, 0, Tile.LAYER_COUNT);
		
		newTerrain[layer] = newTile;
		
		execute();
	}

	@Override
	public void execute() {
		for (int i = 0; i < Tile.LAYER_COUNT; i++)
			if (oldTerrain[i] != newTerrain[i])
				tile.setTerrainType(i == Tile.CAVE_LAYER, newTerrain[i]);
	}

	@Override
	public void undo() {
		for (int i = 0; i < Tile.LAYER_COUNT; i++)
			if (oldTerrain[i] != newTerrain[i])
				tile.setTerrainType(i == Tile.CAVE_LAYER, oldTerrain[i]);
	}

	@Override
	public void redo() {
		execute();
	}

}
