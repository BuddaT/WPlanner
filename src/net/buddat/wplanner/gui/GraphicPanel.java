package net.buddat.wplanner.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;

import net.buddat.wplanner.gui.action.BrushOverlayChange;
import net.buddat.wplanner.gui.action.BrushTileChange;
import net.buddat.wplanner.gui.action.DraggedAction;
import net.buddat.wplanner.gui.action.FenceChange;
import net.buddat.wplanner.gui.action.FenceLine;
import net.buddat.wplanner.gui.action.LabelChange;
import net.buddat.wplanner.gui.action.ObjectChange;
import net.buddat.wplanner.gui.action.ObjectRotationChange;
import net.buddat.wplanner.gui.action.OverlayChange;
import net.buddat.wplanner.gui.action.TileChange;
import net.buddat.wplanner.gui.action.TileFill;
import net.buddat.wplanner.gui.resources.ResourceManager.ImageType;
import net.buddat.wplanner.gui.undo.UndoManager;
import net.buddat.wplanner.map.Map;
import net.buddat.wplanner.map.Tile;
import net.buddat.wplanner.util.Logger;

public class GraphicPanel extends JPanel {

	private static final long serialVersionUID = 5938186762187399440L;
	
	public enum EditState {
		TERRAIN_PENCIL, TERRAIN_BRUSH, TERRAIN_ERASER, TERRAIN_FILL, TERRAIN_PICKER,
		OBJECT_PENCIL, OBJECT_ERASER, OBJECT_PICKER,
		FENCE_PENCIL, FENCE_LINE, FENCE_ERASER, FENCE_PICKER,
		OVERLAY_PENCIL, OVERLAY_BRUSH, OVERLAY_ERASER, OVERLAY_FILL, OVERLAY_PICKER, 
		LABEL
	}
	
	private MainWindow mainWindow;
	private Map map;
	private UndoManager undoManager;
	
	private DraggedAction currentDragAction;
	private FenceLine fenceLineChange;
	
	private int mouseX, mouseY;
	private Color highlightColor = new Color(Color.YELLOW.getRed(), Color.YELLOW.getGreen(), Color.YELLOW.getBlue(), 100);
	
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
		if (xEnd + 1 < map.getMapWidth())
			xEnd += 1;
		else
			xEnd = map.getMapWidth();
		
		int yEnd = (int) (jvp.getY() + jv.getHeight());
		yEnd /= tileSize;
		if (yEnd + 1 < map.getMapHeight())
			yEnd += 1;
		else
			yEnd = map.getMapHeight();
		
		if (saveToImage) {
			xStart = 0;
			yStart = 0;
			xEnd = map.getMapWidth();
			yEnd = map.getMapHeight();
		}
		
		int halfTileSize = tileSize / 2;
		FontMetrics fm = g.getFontMetrics();
		
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
		
		/*
		 * Overlay
		 */
		for (int i = xStart; i < xEnd; i++)
			for (int j = yStart; j < yEnd; j++)
				if (mainWindow.chckbxmntmOverlay.isSelected()) {
					Tile t = map.getTile(i, j, false);
					if (t != null) {
						Color currentColor = t.getOverlayColor(caveLayer);
						
						if (currentColor != null) {
							g.setColor(currentColor);
							g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
						}
					}
				}
		
		/*
		 * Fences
		 */
		for (int i = xStart; i < xEnd; i++)
			for (int j = yStart; j < yEnd; j++) 
				if (mainWindow.chckbxmntmFences.isSelected()) {
					Tile t = map.getTile(i, j, false);
					
					if (t != null) {
						for (int i1 = 0; i1 < 2; i1++) {
							if (t.getFenceType(caveLayer, i1) > 1) {
								Image fenceImg = mainWindow.getResources().getFenceImage(t.getFenceType(caveLayer, i1));
								
								g.drawImage(fenceImg, (i * tileSize) + (i1 == Tile.LEFT_FENCE ? -halfTileSize : 0), 
										(j * tileSize) + (i1 == Tile.TOP_FENCE ? -halfTileSize : 0),
										tileSize, tileSize, null);
							}
						}
					}
				}
		
		/*
		 * Labels
		 */
		for (int i = xStart; i < xEnd; i++)
			for (int j = yStart; j < yEnd; j++)
				if (mainWindow.chckbxmntmLabels.isSelected()) {
					Tile t = map.getTile(i, j, false);
					if (t != null) {
						Color currentColor = t.getLabelColor(caveLayer);
						
						if (currentColor != null && t.getLabel(caveLayer) != null) {
							Rectangle2D rect = fm.getStringBounds(t.getLabel(caveLayer), g);
							int textHeight = (int)(rect.getHeight()); 
							int textWidth  = (int)(rect.getWidth());
							
							g.setColor(currentColor);
							g.drawString(t.getLabel(caveLayer), (i * tileSize) + halfTileSize - (textWidth / 2), (j * tileSize) + halfTileSize + (textHeight / 3));
						}
					}
				}
		
		/*
		 * Highlights
		 */
		if (!saveToImage) {
			int tileX = mouseX / tileSize;
			int tileY = mouseY / tileSize;
			
			if (tileX >= 0 && tileX < map.getMapWidth() && tileY >= 0 && tileY < map.getMapHeight()) {
				int lblY = (int) jvp.getY();
				int lblEndX = (int) (jvp.getX() + jv.getWidth());
				
				String label = "x:" + (tileX + 1) + " y:" + (tileY + 1);
				
				Rectangle2D rect = fm.getStringBounds(label, g);
				int textHeight = (int)(rect.getHeight()); 
				int textWidth  = (int)(rect.getWidth());
				
				g.setColor(highlightColor);
				switch(currentState) {
					case OBJECT_PENCIL:
					case OBJECT_ERASER:
					case OBJECT_PICKER:
						int objX = (mouseX - (tileX * tileSize)) / objectArea;
						int objY = (mouseY - (tileY * tileSize)) / objectArea;
						
						g.fillRect((tileX * tileSize) + (objX * objectArea), (tileY * tileSize) + (objY * objectArea), objectArea, objectArea);
						break;
					case FENCE_PENCIL:
					case FENCE_LINE:
					case FENCE_ERASER:
					case FENCE_PICKER:
						int locX = (int) ((mouseX - (tileX * tileSize)) / (tileSize / 4));
						int locY = (int) ((mouseY - (tileY * tileSize)) / (tileSize / 4));
						
						if (locX == 0 && (locY == 1 || locY == 2))
							g.fillRect((tileX * tileSize) - 3, tileY * tileSize, 6, tileSize);
						else if (locX == 3 && (locY == 1 || locY == 2))
							g.fillRect(((tileX + 1) * tileSize) - 3, tileY * tileSize, 6, tileSize);
						else if (locY == 0 && (locX == 1 || locX == 2))
							g.fillRect(tileX * tileSize, (tileY * tileSize) - 3, tileSize, 6);
						else if (locY == 3 && (locX == 1 || locX == 2))
							g.fillRect(tileX * tileSize, ((tileY + 1) * tileSize) - 3, tileSize, 6);
						
						break;
					case OVERLAY_BRUSH:
						g.setColor(mainWindow.getOverlayColor());
					case TERRAIN_BRUSH:	
						int distance = (mainWindow.getBrushSize() - 1) / 2;
						
						g.fillRect((tileX - distance) * tileSize, (tileY - distance) * tileSize, mainWindow.getBrushSize() * tileSize, mainWindow.getBrushSize() * tileSize);
						break;
					case OVERLAY_PENCIL:
					case OVERLAY_ERASER:
						g.setColor(mainWindow.getOverlayColor());
					default:
						g.fillRect(tileX * tileSize, tileY * tileSize, tileSize, tileSize);
						break;
				}
				
				g.setColor(Color.WHITE);
				g.fillRect(lblEndX - textWidth - 10, lblY, textWidth + 10, textHeight + 5);
				g.setColor(Color.BLACK);
				g.drawString(label, lblEndX - textWidth - 5, lblY + textHeight);
			}
		}
		
		saveToImage = false;
	}

	public boolean isCaveLayer() {
		return caveLayer;
	}

	public void setCaveLayer(boolean caveLayer) {
		this.caveLayer = caveLayer;
		repaint();
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
	
	public void undo() {
		getUndoManager().undo();
		repaint();
	}
	
	public void redo() {
		getUndoManager().redo();
		repaint();
	}
	
	public void zoomIn() {
		if (tileSize + TILE_SIZE_STEP > TILE_MAX_SIZE)
			return;
		
		int oldSize = tileSize;
		Container c = GraphicPanel.this.getParent();
		int newX, newY;
		
		tileSize += TILE_SIZE_STEP;
		
		revalidateScroll();
		
		if (c instanceof JViewport) {
			JViewport jv = (JViewport) c;
			Point pos = jv.getViewPosition();
			
			newX = pos.x + ((pos.x / oldSize) * (TILE_SIZE_STEP + 1));
			newY = pos.y + ((pos.y / oldSize) * (TILE_SIZE_STEP + 1));
			
			jv.setViewPosition(new Point(newX, newY));
		}
		
		repaint();
	}
	
	public void zoomOut() {
		if (tileSize - TILE_SIZE_STEP < TILE_MIN_SIZE)
			return;
		
		int oldSize = tileSize;
		Container c = GraphicPanel.this.getParent();
		int newX, newY;
		
		tileSize -= TILE_SIZE_STEP;
		
		revalidateScroll();
		
		if (c instanceof JViewport) {
			JViewport jv = (JViewport) c;
			Point pos = jv.getViewPosition();
			
			newX = pos.x - ((pos.x / oldSize) * (TILE_SIZE_STEP + 1));
			newY = pos.y - ((pos.y / oldSize) * (TILE_SIZE_STEP + 1));
			
			jv.setViewPosition(new Point(newX, newY));
		}
		
		repaint();
	}

	public void resizeMap() {
		ResizeDialog rd = new ResizeDialog(map.getMapName(), map.getMapWidth(), map.getMapHeight());
		int[] newSize = rd.getAdjustments();
		rd.dispose();
		
		if (newSize == null)
			return;
		
		int northAdj = newSize[0];
		int eastAdj = newSize[1];
		int southAdj = newSize[2];
		int westAdj = newSize[3];
		
		int newWidth = map.getMapWidth() + eastAdj + westAdj;
		int newHeight = map.getMapHeight() + northAdj + southAdj;
		
		map.resizeMap(newWidth, newHeight, westAdj, northAdj);
		
		revalidateScroll();
		repaint();
	}

	public BufferedImage getMapImage() {
		BufferedImage mapImg = new BufferedImage(map.getMapWidth() * tileSize, map.getMapHeight() * tileSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = mapImg.getGraphics();
		saveToImage = true;
		
		paint(g);
		
		return mapImg;
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
					draggedMouse(e.getPoint());
				
				mouseMoved(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				repaint();
			}

			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					m_XDifference = e.getX();
					m_YDifference = e.getY();
					m2or3_dragging = true;
				} else if (e.getButton() == MouseEvent.BUTTON1) {
					m1_dragging = true;
					currentDragAction = new DraggedAction();
				}
			}
	
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					m2or3_dragging = false;
				} else if (e.getButton() == MouseEvent.BUTTON1) {
					m1_dragging = false;
					if (!currentDragAction.isEmpty())
						undoManager.addAction(currentDragAction);
					if (fenceLineChange != null) {
						undoManager.addAction(fenceLineChange);
						fenceLineChange = null;
					}
						
				}
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
	
	public void draggedMouse(Point p) {
		int x = (int) (p.getX() / tileSize);
		int y = (int) (p.getY() / tileSize);
		
		int objLocX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 3));
		int objLocY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 3));
		int objLoc = (objLocY * 3) + objLocX;
		
		int fenceLocX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 4));
		int fenceLocY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 4));
		Tile rightTile = map.getTile(x + 1, y, false);
		Tile downTile = map.getTile(x, y + 1, false);
		
		if (downTile == null)
			if (y + 1 < map.getMapHeight()) {
				downTile = new Tile(x, y + 1);
				map.addTile(x, y + 1, downTile);
			}
		if (rightTile == null) 
			if (x + 1 < map.getMapWidth()) {
				rightTile = new Tile(x + 1, y);
				map.addTile(x + 1, y, rightTile);
			}
		
		int relevantType;
		Color c = mainWindow.getOverlayColor();
		
		Tile t = map.getTile(x, y, true);
		if (t == null) {
			map.addTile(x, y, new Tile(x, y));
			t = map.getTile(x, y, true);
		}
		
		if (x < map.getMapWidth() && x >= 0 && y >= 0 && y < map.getMapHeight()) {
			switch (currentState) {
				case TERRAIN_PENCIL:
					relevantType = mainWindow.getSelectedTerrain();
					if (t.getTerrainType(caveLayer) != relevantType)
						currentDragAction.addAction(new TileChange(t, (byte) relevantType, caveLayer, mainWindow.isCaveEntrance(relevantType)));
					break;
				case TERRAIN_BRUSH:
					relevantType = mainWindow.getSelectedTerrain();
					currentDragAction.addAction(new BrushTileChange(map, x, y, mainWindow.getBrushSize(), (byte) relevantType, caveLayer));
					break;
				case TERRAIN_FILL:
					relevantType = mainWindow.getSelectedTerrain();
					if (currentDragAction.isEmpty())
						if (t.getTerrainType(caveLayer) != relevantType)
							currentDragAction.addAction(new TileFill(map, x, y, (byte) relevantType, caveLayer));
					break;
				case TERRAIN_ERASER:
					relevantType = 0;
					currentDragAction.addAction(new TileChange(t, (byte) relevantType, caveLayer, false));
					break;
				case TERRAIN_PICKER:
					if (mainWindow.getSelectedTerrain() != t.getTerrainType(caveLayer))
						mainWindow.forceSelection(ImageType.TERRAIN, t.getTerrainType(caveLayer));
					break;
				case OBJECT_PENCIL:
					relevantType = mainWindow.getSelectedObject();
					if (t.getObjectType(caveLayer, objLoc) != relevantType)
						currentDragAction.addAction(new ObjectChange(t, (byte) relevantType, objLoc, caveLayer));
					break;
				case OBJECT_ERASER:
					relevantType = 0;
					currentDragAction.addAction(new ObjectChange(t, (byte) relevantType, objLoc, caveLayer));
					break;
				case OBJECT_PICKER:
					if (mainWindow.getSelectedObject() != t.getObjectType(caveLayer, objLoc))
						mainWindow.forceSelection(ImageType.OBJECT, t.getObjectType(caveLayer, objLoc));
					break;
				case FENCE_PENCIL:
				case FENCE_ERASER:
					relevantType = (currentState == EditState.FENCE_ERASER ? 0 : mainWindow.getSelectedFence());

					switch (fenceLocX) {
						case 0:
							if (fenceLocY == 1 || fenceLocY == 2)
								if (t.getFenceType(caveLayer, Tile.LEFT_FENCE) != relevantType + 1)
									currentDragAction.addAction(new FenceChange(t, (byte) (relevantType + 1), Tile.LEFT_FENCE, caveLayer));
							break;
						case 1:
						case 2:
							if (fenceLocY == 0) {
								if (t.getFenceType(caveLayer, Tile.TOP_FENCE) != relevantType)
									currentDragAction.addAction(new FenceChange(t, (byte) relevantType, Tile.TOP_FENCE, caveLayer));
							} else if (fenceLocY == 3) {
								if (downTile != null)
									if (downTile.getFenceType(caveLayer, Tile.TOP_FENCE) != relevantType)
										currentDragAction.addAction(new FenceChange(downTile, (byte) relevantType, Tile.TOP_FENCE, caveLayer));
							}
							break;
						case 3:
							if (fenceLocY == 1 || fenceLocY == 2) {								
								if (rightTile != null)
									if (rightTile.getFenceType(caveLayer, Tile.LEFT_FENCE) != relevantType + 1)
										currentDragAction.addAction(new FenceChange(rightTile, (byte) (relevantType + 1), Tile.LEFT_FENCE, caveLayer));
							}
							break;
					}
					break;
				case FENCE_LINE:
					relevantType = mainWindow.getSelectedFence();
					
					if (fenceLineChange == null) {
						switch (fenceLocX) {
							case 0:
								if (fenceLocY == 1 || fenceLocY == 2)
									fenceLineChange = new FenceLine(map, x, y, (byte) relevantType, Tile.LEFT_FENCE, caveLayer);
								break;
							case 1:
							case 2:
								if (fenceLocY == 0) {
									fenceLineChange = new FenceLine(map, x, y, (byte) relevantType, Tile.TOP_FENCE, caveLayer);
								} else if (fenceLocY == 3) {
									fenceLineChange = new FenceLine(map, x, y + 1, (byte) relevantType, Tile.TOP_FENCE, caveLayer);
								}
								break;
							case 3:
								if (fenceLocY == 1 || fenceLocY == 2) {								
									fenceLineChange = new FenceLine(map, x + 1, y, (byte) relevantType, Tile.LEFT_FENCE, caveLayer);
								}
								break;
						}
					}
					
					if (fenceLineChange != null)
						fenceLineChange.update(x, y);
					break;
				case FENCE_PICKER:
					relevantType = mainWindow.getSelectedFence();
					
					switch (fenceLocX) {
						case 0:
							if (fenceLocY == 1 || fenceLocY == 2)
								relevantType = t.getFenceType(caveLayer, Tile.LEFT_FENCE) - 1;
							break;
						case 1:
						case 2:
							if (fenceLocY == 0) {
								relevantType = t.getFenceType(caveLayer, Tile.TOP_FENCE);
							} else if (fenceLocY == 3) {
								relevantType = downTile.getFenceType(caveLayer, Tile.TOP_FENCE);
							}
							break;
						case 3:
							if (fenceLocY == 1 || fenceLocY == 2) {								
								relevantType = rightTile.getFenceType(caveLayer, Tile.LEFT_FENCE) - 1;
							}
							break;
					}
					
					if (relevantType < 0)
						relevantType = 0;
					
					if (mainWindow.getSelectedFence() != relevantType)
						mainWindow.forceSelection(ImageType.FENCE, relevantType);
					break;
				case OVERLAY_PENCIL:
					if (t.getOverlayColor(caveLayer) != c)
						currentDragAction.addAction(new OverlayChange(t, c, caveLayer));
					break;
				case OVERLAY_BRUSH:
					currentDragAction.addAction(new BrushOverlayChange(map, x, y, mainWindow.getBrushSize(), c, caveLayer));
					break;
				case OVERLAY_ERASER:
					c = null;
					if (t.getOverlayColor(caveLayer) != c)
						currentDragAction.addAction(new OverlayChange(t, c, caveLayer));
					break;
				case OVERLAY_PICKER:
					mainWindow.setOverlayColor(t.getOverlayColor(caveLayer));
					break;
				default:
					break;
			}
		}
		
		repaint();
	}

	public void clickedMouse(Point p) {		
		int x = (int) (p.getX() / tileSize);
		int y = (int) (p.getY() / tileSize);
		
		int objLocX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 3));
		int objLocY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 3));
		int objLoc = (objLocY * 3) + objLocX;
		
		int fenceLocX = (int) ((p.getX() - (x * tileSize)) / (tileSize / 4));
		int fenceLocY = (int) ((p.getY() - (y * tileSize)) / (tileSize / 4));
		Tile rightTile = map.getTile(x + 1, y, false);
		Tile downTile = map.getTile(x, y + 1, false);
		
		if (downTile == null)
			if (y + 1 < map.getMapHeight()) {
				downTile = new Tile(x, y + 1);
				map.addTile(x, y + 1, downTile);
			}
		if (rightTile == null) 
			if (x + 1 < map.getMapWidth()) {
				rightTile = new Tile(x + 1, y);
				map.addTile(x + 1, y, rightTile);
			}
		
		int relevantType;
		Color c = mainWindow.getOverlayColor();
		
		Tile t = map.getTile(x, y, true);
		if (t == null) {
			map.addTile(x, y, new Tile(x, y));
			t = map.getTile(x, y, true);
		}
		
		if (x < map.getMapWidth() && x >= 0 && y >= 0 && y < map.getMapHeight()) {
			switch (currentState) {
				case TERRAIN_PENCIL:
					relevantType = mainWindow.getSelectedTerrain();
					if (t.getTerrainType(caveLayer) != relevantType)
						undoManager.addAction(new TileChange(t, (byte) relevantType, caveLayer, mainWindow.isCaveEntrance(relevantType)));
					break;
				case TERRAIN_BRUSH:
					relevantType = mainWindow.getSelectedTerrain();
					
					undoManager.addAction(new BrushTileChange(map, x, y, mainWindow.getBrushSize(), (byte) relevantType, caveLayer));
					break;
				case TERRAIN_FILL:
					relevantType = mainWindow.getSelectedTerrain();
					if (t.getTerrainType(caveLayer) != relevantType)
						undoManager.addAction(new TileFill(map, x, y, (byte) relevantType, caveLayer));
					break;
				case TERRAIN_ERASER:
					relevantType = 0;
					if (t.getTerrainType(caveLayer) != relevantType)
						undoManager.addAction(new TileChange(t, (byte) relevantType, caveLayer, false));
					break;
				case TERRAIN_PICKER:
					if (mainWindow.getSelectedTerrain() != t.getTerrainType(caveLayer));
						mainWindow.forceSelection(ImageType.TERRAIN, t.getTerrainType(caveLayer));
					break;
				case OBJECT_PENCIL:
					relevantType = mainWindow.getSelectedObject();
					if (t.getObjectType(caveLayer, objLoc) != relevantType)
						undoManager.addAction(new ObjectChange(t, (byte) relevantType, objLoc, caveLayer));
					break;
				case OBJECT_ERASER:
					relevantType = 0;
					undoManager.addAction(new ObjectChange(t, (byte) relevantType, objLoc, caveLayer));
					break;
				case OBJECT_PICKER:
					if (mainWindow.getSelectedObject() != t.getObjectType(caveLayer, objLoc))
						mainWindow.forceSelection(ImageType.OBJECT, t.getObjectType(caveLayer, objLoc));
					break;
				case FENCE_PENCIL:
				case FENCE_LINE:
				case FENCE_ERASER:
					relevantType = (currentState == EditState.FENCE_ERASER ? 0 : mainWindow.getSelectedFence());

					switch (fenceLocX) {
						case 0:
							if (fenceLocY == 1 || fenceLocY == 2)
								if (t.getFenceType(caveLayer, Tile.LEFT_FENCE) != relevantType + 1)
									undoManager.addAction(new FenceChange(t, (byte) (relevantType + 1), Tile.LEFT_FENCE, caveLayer));
							break;
						case 1:
						case 2:
							if (fenceLocY == 0) {
								if (t.getFenceType(caveLayer, Tile.TOP_FENCE) != relevantType)
									undoManager.addAction(new FenceChange(t, (byte) relevantType, Tile.TOP_FENCE, caveLayer));
							} else if (fenceLocY == 3) {
								if (downTile != null)
									if (downTile.getFenceType(caveLayer, Tile.TOP_FENCE) != relevantType)
										undoManager.addAction(new FenceChange(downTile, (byte) relevantType, Tile.TOP_FENCE, caveLayer));
							}
							break;
						case 3:
							if (fenceLocY == 1 || fenceLocY == 2) {								
								if (rightTile != null)
									if (rightTile.getFenceType(caveLayer, Tile.LEFT_FENCE) != relevantType + 1)
										undoManager.addAction(new FenceChange(rightTile, (byte) (relevantType + 1), Tile.LEFT_FENCE, caveLayer));
							}
							break;
					}
					break;
				case FENCE_PICKER:	
					relevantType = mainWindow.getSelectedFence();
					
					switch (fenceLocX) {
						case 0:
							if (fenceLocY == 1 || fenceLocY == 2)
								relevantType = t.getFenceType(caveLayer, Tile.LEFT_FENCE) - 1;
							break;
						case 1:
						case 2:
							if (fenceLocY == 0) {
								relevantType = t.getFenceType(caveLayer, Tile.TOP_FENCE);
							} else if (fenceLocY == 3) {
								relevantType = downTile.getFenceType(caveLayer, Tile.TOP_FENCE);
							}
							break;
						case 3:
							if (fenceLocY == 1 || fenceLocY == 2) {								
								relevantType = rightTile.getFenceType(caveLayer, Tile.LEFT_FENCE) - 1;
							}
							break;
					}
					
					if (mainWindow.getSelectedFence() != relevantType)
						mainWindow.forceSelection(ImageType.FENCE, relevantType);
					break;
				case OVERLAY_PENCIL:
					if (t.getOverlayColor(caveLayer) != c)
						undoManager.addAction(new OverlayChange(t, c, caveLayer));
					break;
				case OVERLAY_BRUSH:
					undoManager.addAction(new BrushOverlayChange(map, x, y, mainWindow.getBrushSize(), c, caveLayer));
					break;
				case OVERLAY_ERASER:
					c = null;
					if (t.getOverlayColor(caveLayer) != c)
						undoManager.addAction(new OverlayChange(t, c, caveLayer));
					break;
				case OVERLAY_PICKER:
					mainWindow.setOverlayColor(t.getOverlayColor(caveLayer));
					break;
				case LABEL:
					LabelDialog d = new LabelDialog();
					undoManager.addAction(new LabelChange(t, d.getLabel(), mainWindow.getLabelColor(), caveLayer));
					d.dispose();
					break;
				default:
					Logger.log("Unhandled State: " + currentState);
					break;
			}
		}
		
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
