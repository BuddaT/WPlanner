package net.buddat.wplanner.gui;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;

import net.buddat.wplanner.gui.action.ObjectRotationChange;
import net.buddat.wplanner.gui.action.TileChange;
import net.buddat.wplanner.gui.undo.UndoManager;
import net.buddat.wplanner.map.Map;
import net.buddat.wplanner.map.Tile;

public class GraphicPanel extends JPanel {

	private static final long serialVersionUID = 5938186762187399440L;
	
	public enum EditState {
		TERRAIN_PENCIL, TERRAIN_BRUSH, TERRAIN_ERASER, TERRAIN_FILL, TERRAIN_PICKER,
		OBJECT_PENCIL, OBJECT_ERASER, OBJECT_PICKER,
		FENCE_PENCIL, FENCE_LINE, FENCE_ERASER, FENCE_PICKER,
		OVERLAY_PENCIL, OVERLAY_BRUSH, OVERLAY_ERASER, OVERLAY_FILL, OVERLAY_PICKER
	}
	
	private MainWindow mainWindow;
	private Map map;
	private UndoManager undoManager;
	
	public static final int TILE_MAX_SIZE = 128, TILE_MIN_SIZE = 4, TILE_SIZE_STEP = 4;
	private int tileSize = 48;
	
	private EditState currentState = EditState.TERRAIN_PENCIL;
	
	private boolean caveLayer;
	
	private boolean saveToImage = false;

	public GraphicPanel(MainWindow main, Map m) {
		super();
		
		this.map = m;
		this.mainWindow = main;
		this.undoManager = new UndoManager();
		
		setupMouseDrag();
		revalidateScroll();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		JViewport jv = (JViewport) this.getParent();
		Point jvp = jv.getViewPosition();
		
		int xStart = (int) jvp.getX();
		xStart /= tileSize;
		if (xStart - 1 >= 0)
			xStart -= 1;
		
		int yStart = (int) jvp.getY();
		yStart /= tileSize;
		if (yStart - 1 >= 0)
			yStart -= 1;
		
		int xEnd = (int) (jvp.getX() + jv.getWidth());
		xEnd /= tileSize;
		if (xEnd + 1 <= map.getMapWidth())
			xEnd += 1;
		
		int yEnd = (int) (jvp.getY() + jv.getHeight());
		yEnd /= tileSize;
		if (yEnd + 1 <= map.getMapHeight())
			yEnd += 1;
		
		if (saveToImage) {
			xStart = 0;
			yStart = 0;
			xEnd = map.getMapWidth();
			yEnd = map.getMapHeight();
		}
		
		/*
		 * Terrain and grid.
		 */
		for (int i = xStart; i < xEnd; i++) {
			for (int j = yStart; j < yEnd; j++) {
				Tile t = map.getTile(i, j, false);
				
				if (t != null)
					if (mainWindow.chckbxmntmTerrain.isSelected())
						g.drawImage(mainWindow.getResources().getTerrainImage(t.getTerrainType(caveLayer)), i * tileSize, j * tileSize, tileSize, tileSize, null);
				
				if (mainWindow.chckbxmntmGrid.isSelected())
					g.drawRect(i * tileSize, j * tileSize, tileSize, tileSize);
			}
		}
		
		/*
		 * Objects
		 */
		float objectAreaFloat = tileSize / 3;
		int objectArea = (int) objectAreaFloat;
		
		for (int i = xStart; i < xEnd; i++)
			for (int j = yStart; j < yEnd; j++) 
				if (mainWindow.chckbxmntmObjects.isSelected()) {
					Tile t = map.getTile(i, j, false);
					
					if (t != null)
						for (int i1 = 0; i1 < 3; i1++) 
							for (int j1 = 0; j1 < 3; j1++) {
								int slot = (j1 * 3) + i1;
								if (t.getObjectType(caveLayer, slot) > 0) {
									int objRotation = t.getObjectRotation(caveLayer, slot);
									
									Image objImg;
									if (objRotation < 8 && objRotation >= 0)
										objImg = mainWindow.getResources().getObjectImage(t.getObjectType(caveLayer, slot), objRotation);
									else
										objImg = mainWindow.getResources().getObjectImage(t.getObjectType(caveLayer, slot), 0);
									
									boolean large = mainWindow.getResources().isLargeObject(t.getObjectType(caveLayer, slot));
									int drawSize = (large ? tileSize * 2 : tileSize);
									
									g.drawImage(objImg, (i * tileSize) + ((i1 * objectArea)) - ((drawSize - objectArea) / 2),
											(j * tileSize) + ((j1 * objectArea)) - ((drawSize - objectArea) / 2),
											drawSize, drawSize, null);
								}
							}
				}
		
	}

	public boolean isCaveLayer() {
		return caveLayer;
	}

	public void setCaveLayer(boolean caveLayer) {
		this.caveLayer = caveLayer;
	}
	
	public EditState getEditState() {
		return currentState;
	}
	
	public void setEditState(EditState e) {
		currentState = e;
	}
	
	public UndoManager getUndoManager() {
		return undoManager;
	}
	
	public void revalidateScroll() {
		this.setPreferredSize(new Dimension(map.getMapWidth() * this.tileSize, map.getMapHeight() * this.tileSize));
		this.revalidate();
	}
	
	private void setupMouseDrag() {
		MouseInputAdapter mia = new MouseInputAdapter() {
			int m_XDifference, m_YDifference;
			boolean m2or3_dragging, m1_dragging;
			Container c;

			public void mouseDragged(MouseEvent e) {
				if (m2or3_dragging) {
					c = GraphicPanel.this.getParent();
					if (c instanceof JViewport) {
						JViewport jv = (JViewport) c;
						Point p = jv.getViewPosition();
						int newX = p.x - (e.getX() - m_XDifference);
						int newY = p.y - (e.getY() - m_YDifference);
	
						int maxX = GraphicPanel.this.getWidth() - jv.getWidth();
						int maxY = GraphicPanel.this.getHeight() - jv.getHeight();
						if (newX < 0)
							newX = 0;
						if (newX > maxX)
							newX = maxX;
						if (newY < 0)
							newY = 0;
						if (newY > maxY)
							newY = maxY;
	
						jv.setViewPosition(new Point(newX, newY));
					}
				}
				if (m1_dragging)
					/*if (parent.normalTool.isSelected())
						clickedMouse(e.getPoint())*/;
				
				mouseMoved(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				/*mouseX = e.getX();
				mouseY = e.getY();
				repaint();*/
			}

			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					m_XDifference = e.getX();
					m_YDifference = e.getY();
					m2or3_dragging = true;
				} else if (e.getButton() == MouseEvent.BUTTON1)
					m1_dragging = true;
			}
	
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					m2or3_dragging = false;
				} else if (e.getButton() == MouseEvent.BUTTON1)
					m1_dragging = false;
			}   
			
			public void mouseClicked(MouseEvent e) { 
				if (e.getButton() == MouseEvent.BUTTON1)
					clickedMouse(e.getPoint());
				else if (e.getButton() == MouseEvent.BUTTON3)
					clickedRightMouse(e.getPoint());
			}
		};
		
		addMouseMotionListener(mia);
		addMouseListener(mia);
	}
	
	public void clickedMouse(Point p) {		
		int x = (int) (p.getX() / tileSize);
		int y = (int) (p.getY() / tileSize);
		
		Tile t = map.getTile(x, y, true);
		if (t == null) {
			map.addTile(x, y, new Tile(x, y));
			t = map.getTile(x, y, true);
		}
		
		if (x < map.getMapWidth() && x >= 0 && y >= 0 && y < map.getMapHeight()) {
			switch (currentState) {
				case TERRAIN_PENCIL:
					int tileType = mainWindow.getSelectedTerrainType();
					undoManager.addAction(new TileChange(t, (byte) tileType, caveLayer, mainWindow.isCaveEntrance(tileType)));
					break;
				case TERRAIN_FILL:
					
					break;
				default:
					;
			}
		}
		
		/*if (parent.tabbedEditor.getSelectedIndex() == 0) { //TERRAIN
			if (x < map.getMapWidth() && x >= 0 && y >= 0 && y < map.getMapHeight())
				if (parent.normalTool.isSelected()) {
					if (parent.rbnTerrain.getSelection() != null) {
						String tile = parent.rbnTerrain.getSelection().getActionCommand();
						int tileType = parent.terrainKeyList.indexOf(tile);
						
						t.setTerrainType(caveLayer, (byte) tileType);
							
						if (tile.contains("distance-cave"))
							t.setTerrainType(!caveLayer, (byte) tileType);
					}
				} else if (parent.fillTool.isSelected()) {
					if (parent.rbnTerrain.getSelection() != null) {
						String tile = parent.rbnTerrain.getSelection().getActionCommand();
						int tileType = parent.terrainKeyList.indexOf(tile);
						
						terrainFill(t, t.getTerrainType(caveLayer), (byte) tileType, caveLayer);
					}
				} else if (parent.pickerTool.isSelected()) {
					int tileType = t.getTerrainType(caveLayer);
					String tile = parent.terrainKeyList.get((tileType == -1 ? 0 : tileType));
					
					parent.terrainBtnList.get(tile).doClick();
					parent.normalTool.doClick();
				}
		}*/
		
		/* else if (parent.tabbedEditor.getSelectedIndex() == 1) { //OBJECTS
			if (x < map.getMapWidth() && x >= 0 && y >= 0 && y < map.getMapHeight())
				if (parent.normalTool.isSelected()) {
					if (parent.rbnObjects.getSelection() != null) {
						String object = parent.rbnObjects.getSelection().getActionCommand();
						int objectType = parent.objectKeyList.indexOf(object);
												
						float locX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 3));
						float locY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 3));
						float id = (locY * 3) + locX;
						
						if (id < 9) {
							t.setObjectType(caveLayer, (int) id, (byte) objectType);
							t.setObjectRotation(caveLayer, (int) id, (byte) 0);
						}
					}
				} else if (parent.pickerTool.isSelected()) {
					int locX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 3));
					int locY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 3));
					int id = (locY * 3) + locX;
					
					int objType = t.getObjectType(caveLayer, id);
					String object = parent.objectKeyList.get((objType == -1 ? 0 : objType));
					
					parent.objectBtnList.get(object).doClick();
					parent.normalTool.doClick();
				}
		} else if (parent.tabbedEditor.getSelectedIndex() == 2) { //FENCES
			if (x < map.getMapWidth() && x >= 0 && y >= 0 && y < map.getMapHeight())
				if (parent.normalTool.isSelected()) {
					if (parent.rbnFences.getSelection() != null) {
						String fence = parent.rbnFences.getSelection().getActionCommand();
						int fenceType = parent.fenceKeyList.indexOf(fence);
						int fenceTypeVert = parent.fenceKeyList.indexOf(fence + "_1");
						
						int locX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 4));
						int locY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 4));
						
						Tile rightTile = map.getTile(x + 1, y);
						Tile downTile = map.getTile(x, y + 1);
						
						if (locX == 1 || locX == 2) {
							if (locY == 0)
								t.setFenceType(caveLayer, Tile.TOP_FENCE, (byte) fenceType);
							else if (locY == 3) {
								if (downTile != null)
									downTile.setFenceType(caveLayer, Tile.TOP_FENCE, (byte) fenceType);
								else {
									if (y + 1 < map.getMapHeight()) {
										downTile = new Tile(x, y + 1);
										downTile.setFenceType(caveLayer, Tile.TOP_FENCE, (byte) fenceType);
										map.addTile(x, y + 1, downTile);
									}
								}
							}
						} else if (locX == 0) {
							if (locY == 1 || locY == 2)
								t.setFenceType(caveLayer, Tile.LEFT_FENCE, (byte) fenceTypeVert);
						} else if (locX == 3) {
							if (locY == 1 || locY == 2)
								if (rightTile != null) 
									rightTile.setFenceType(caveLayer, Tile.LEFT_FENCE, (byte) fenceTypeVert);
								else {
									if (x + 1 < map.getMapWidth()) {
										rightTile = new Tile(x + 1, y);
										rightTile.setFenceType(caveLayer, Tile.LEFT_FENCE, (byte) fenceTypeVert);
										map.addTile(x + 1, y, rightTile);
									}
								}
						}					
					}
				} else if (parent.pickerTool.isSelected()) {
					int locX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 4));
					int locY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 4));
					
					Tile rightTile = map.getTile(x + 1, y);
					Tile downTile = map.getTile(x, y + 1);
					
					int fenceType = 0;
					
					if (locX == 1 || locX == 2) {
						if (locY == 0) {
							fenceType = t.getFenceType(caveLayer, Tile.TOP_FENCE);
						} else if (locY == 3) {
							if (downTile != null)
								fenceType = downTile.getFenceType(caveLayer, Tile.TOP_FENCE);
						}
					} else if (locX == 0) {
						if (locY == 1 || locY == 2) {
							fenceType = t.getFenceType(caveLayer, Tile.LEFT_FENCE);
						}
					} else if (locX == 3) {
						if (locY == 1 || locY == 2) {
							if (rightTile != null)
								fenceType = rightTile.getFenceType(caveLayer, Tile.LEFT_FENCE);
						}
					}
					
					String fence = parent.fenceKeyList.get((fenceType == -1 ? 0 : fenceType));
					if (fence.endsWith("_1"))
						fence = fence.substring(0, fence.lastIndexOf("_"));
					
					parent.fenceBtnList.get(fence).doClick();
					parent.normalTool.doClick();
				}
		} else if (parent.tabbedEditor.getSelectedIndex() == 3) { //MISC
			if (x < map.getMapWidth() && x >= 0 && y >= 0 && y < map.getMapHeight())
				if (parent.miscOverlay.isSelected()) {
					if (parent.normalTool.isSelected()) {
						t.setOverlayColor(caveLayer, parent.miscOverlayColor);
					} else if (parent.pickerTool.isSelected()) {
						parent.miscOverlayColor = t.getOverlayColor(caveLayer);
						parent.miscOpacity.setValue(parent.miscOverlayColor.getAlpha());
						parent.updatePreview();
						
						parent.normalTool.doClick();
					}
				} else if (parent.miscText.isSelected()) {
					if (parent.normalTool.isSelected()) {
						t.setLabelColor(caveLayer, parent.miscTextColor);
						t.setLabel(caveLayer, (String) JOptionPane.showInputDialog(parent, "Enter label (Cancel to delete label): ", "Create label", JOptionPane.PLAIN_MESSAGE, null, null, t.getLabel(caveLayer)));
					} else if (parent.pickerTool.isSelected()) {
						parent.miscOverlayColor = t.getLabelColor(caveLayer);
						parent.miscOpacity.setValue(parent.miscOverlayColor.getAlpha());
						parent.updatePreview();
						
						parent.normalTool.doClick();
					}
				}

		}*/
		
		repaint();
	}
	
	public void clickedRightMouse(Point p) {
		int x = (int) (p.getX() / tileSize);
		int y = (int) (p.getY() / tileSize);
		int locX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 3));
		int locY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 3));
		
		switch(currentState) {
			case OBJECT_ERASER:
			case OBJECT_PENCIL:
				if (x < map.getMapWidth() && x >= 0 && y >= 0 && y < map.getMapHeight()) {
					Tile t = map.getTile(x, y, true);
					int loc = (locY * 3) + locX;
					
					if (t != null) {
						if (t.getObjectType(caveLayer, loc) > 0)
							undoManager.addAction(new ObjectRotationChange(t, 
									(byte) (t.getObjectRotation(caveLayer, loc) + 1 > 7 ? 0 : t.getObjectRotation(caveLayer, loc) + 1), loc, caveLayer));
					}
				}
				break;
			default:
				break;
		}
		
		repaint();
	}
	
}
