package net.buddat.wplanner.gui.action;

import java.util.ArrayList;

import net.buddat.wplanner.gui.undo.UndoableAction;
import net.buddat.wplanner.map.Map;
import net.buddat.wplanner.map.Tile;

public class TileFill implements UndoableAction {
	
	private Map map;
	
	private ArrayList<TileChange> tilesChanged = new ArrayList<TileChange>();
	
	public TileFill(Map m, int x, int y, byte newTile, boolean caveLayer) {
		this.map = m;
		
		terrainFill(m.getTile(x, y, true), m.getTile(x, y, false).getTerrainType(caveLayer), newTile, caveLayer);
	}
	
	public void terrainFill(Tile t, byte target, byte replacement, boolean cave) {
		ArrayList<Tile> queue = new ArrayList<Tile>();
		
		if (t.getTerrainType(cave) != target)
			return;
		
		queue.add(t);
		
		while (!queue.isEmpty()) {
			Tile t1 = queue.remove(0);
			if (t1.getTerrainType(cave) == target) {
				Tile w = t1, e = t1;
				
				boolean westDone = false;
				while (!westDone) {
					if (w.getX() - 1 < 0) {
						westDone = true;
						continue;
					}
					
					Tile w1 = map.getTile(w.getX() - 1, w.getY(), true);
					if (w1 == null)
						map.addTile(w.getX() - 1, w.getY(), (w1 = new Tile(w.getX() - 1, w.getY())));
					
					if (w1.getTerrainType(cave) != target)
						westDone = true;
					else
						w = w1;
				}
				
				boolean eastDone = false;
				while(!eastDone) {
					if (e.getX() + 1 >= map.getMapWidth()) {
						eastDone = true;
						continue;
					}
					
					Tile e1 = map.getTile(e.getX() + 1, e.getY(), true);
					if (e1 == null)
						map.addTile(e.getX() + 1, e.getY(), (e1 = new Tile(e.getX() + 1, e.getY())));
					
					if (e1.getTerrainType(cave) != target)
						eastDone = true;
					else
						e = e1;
				}
				
				for (int i = w.getX(); i <= e.getX(); i++) {
					Tile fill = map.getTile(i, w.getY(), true);
					if (fill == null)
						map.addTile(i, w.getY(), (fill = new Tile(i, w.getY())));
					
					//fill.setTerrainType(cave, replacement);
					addTileChange(new TileChange(fill, replacement, cave, false));
					
					if (fill.getY() > 0) {
						Tile n = map.getTile(fill.getX(), fill.getY() - 1, true);
						if (n == null)
							map.addTile(fill.getX(), fill.getY() - 1, (n = new Tile(fill.getX(), fill.getY() - 1)));
						
						if (n.getTerrainType(cave) == target)
							queue.add(n);
					}
					
					if (fill.getY() + 1 < map.getMapHeight()) {
						Tile s = map.getTile(fill.getX(), fill.getY() + 1, true);
						if (s == null)
							map.addTile(fill.getX(), fill.getY() + 1, (s = new Tile(fill.getX(), fill.getY() + 1)));
						
						if (s.getTerrainType(cave) == target)
							queue.add(s);
					}				
				}
			}
		}
	}
	
	private void addTileChange(TileChange t) {
		tilesChanged.add(t);
	}

	@Override
	public void execute() {
		
	}

	@Override
	public void undo() {
		for (TileChange t : tilesChanged)
			t.undo();
	}

	@Override
	public void redo() {
		for (TileChange t : tilesChanged)
			t.redo();
	}

}
