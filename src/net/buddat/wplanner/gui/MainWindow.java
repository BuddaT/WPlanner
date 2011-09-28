package net.buddat.wplanner.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import net.buddat.wplanner.gui.resources.ResourceManager;
import net.buddat.wplanner.map.Map;
import net.buddat.wplanner.util.Constants;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JToolBar;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import java.awt.Component;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 6408703853685252009L;
	
	private ResourceManager resources;
	
	private JPanel contentPane;
	
	JCheckBoxMenuItem chckbxmntmTerrain;
	JCheckBoxMenuItem chckbxmntmObjects;
	JCheckBoxMenuItem chckbxmntmFences;
	JCheckBoxMenuItem chckbxmntmOverlay;
	JCheckBoxMenuItem chckbxmntmLabels;
	JCheckBoxMenuItem chckbxmntmGrid;
	
	JTabbedPane tabEditor;

	public MainWindow() {
		init();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle(Constants.PROGRAM_NAME + " | v." + Constants.VERSION_MAJOR + "." + Constants.VERSION_MIDI +
				"." + Constants.VERSION_MINOR);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mnFile.add(mntmSaveAs);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmSaveImage = new JMenuItem("Save Image");
		mntmSaveImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSaveImage);
		
		JMenuItem mntmSaveImageAs = new JMenuItem("Save Image As...");
		mnFile.add(mntmSaveImageAs);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		mnEdit.add(mntmRedo);
		
		JSeparator separator_2 = new JSeparator();
		mnEdit.add(separator_2);
		
		JMenuItem mntmResizeMap = new JMenuItem("Resize Map...");
		mntmResizeMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnEdit.add(mntmResizeMap);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmZoomIn = new JMenuItem("Zoom In");
		mntmZoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_MASK));
		mnView.add(mntmZoomIn);
		
		JMenuItem mntmZoomOut = new JMenuItem("Zoom Out");
		mntmZoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK));
		mnView.add(mntmZoomOut);
		
		JSeparator separator_3 = new JSeparator();
		mnView.add(separator_3);
		
		chckbxmntmTerrain = new JCheckBoxMenuItem("Terrain");
		chckbxmntmTerrain.setSelected(true);
		mnView.add(chckbxmntmTerrain);
		
		chckbxmntmObjects = new JCheckBoxMenuItem("Objects");
		chckbxmntmObjects.setSelected(true);
		mnView.add(chckbxmntmObjects);
		
		chckbxmntmFences = new JCheckBoxMenuItem("Fences");
		chckbxmntmFences.setSelected(true);
		mnView.add(chckbxmntmFences);
		
		chckbxmntmOverlay = new JCheckBoxMenuItem("Overlay");
		chckbxmntmOverlay.setSelected(true);
		mnView.add(chckbxmntmOverlay);
		
		chckbxmntmLabels = new JCheckBoxMenuItem("Labels");
		chckbxmntmLabels.setSelected(true);
		mnView.add(chckbxmntmLabels);
		
		JSeparator separator_5 = new JSeparator();
		mnView.add(separator_5);
		
		chckbxmntmGrid = new JCheckBoxMenuItem("Grid");
		chckbxmntmGrid.setSelected(true);
		mnView.add(chckbxmntmGrid);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHelp.add(mntmHelp);
		
		JSeparator separator_4 = new JSeparator();
		mnHelp.add(separator_4);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JToolBar menuTools = new JToolBar();
		panel.add(menuTools);
		
		JButton btnNew = new JButton();
		btnNew.setToolTipText("Create a new map");
		btnNew.setIcon(getResources().getGuiImage(Constants.GUI_NEW));
		btnNew.setBorder(new EmptyBorder(2, 2, 5, 2));
		
		JButton btnOpen = new JButton();
		btnOpen.setToolTipText("Open an existing map");
		btnOpen.setIcon(getResources().getGuiImage(Constants.GUI_OPEN));
		btnOpen.setBorder(new EmptyBorder(2, 2, 5, 2));
		
		JButton btnSave = new JButton();
		btnSave.setToolTipText("Save the current map");
		btnSave.setIcon(getResources().getGuiImage(Constants.GUI_SAVE));
		btnSave.setBorder(new EmptyBorder(2, 2, 5, 2));
		
		JButton btnSaveImage = new JButton();
		btnSaveImage.setToolTipText("Save the current map as an image");
		btnSaveImage.setIcon(getResources().getGuiImage(Constants.GUI_SAVE_IMAGE));
		btnSaveImage.setBorder(new EmptyBorder(2, 2, 5, 2));
		
		menuTools.add(btnNew);
		menuTools.add(btnOpen);
		menuTools.add(btnSave);
		menuTools.add(btnSaveImage);
		
		JToolBar terrainTools = new JToolBar();
		terrainTools.setToolTipText("Terrain Tools");
		panel.add(terrainTools);
		
		JLabel lblTerrain = new JLabel("Terrain:");
		lblTerrain.setBorder(new EmptyBorder(2, 2, 2, 10));
		terrainTools.add(lblTerrain);
		
		JRadioButton rdbtnTerrainPencil = new JRadioButton();
		rdbtnTerrainPencil.setToolTipText("Terrain pencil - changes single tiles");
		rdbtnTerrainPencil.setSelected(true);
		rdbtnTerrainPencil.setIcon(getResources().getGuiImage(Constants.GUI_PENCIL));
		rdbtnTerrainPencil.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnTerrainPencil.setBorderPainted(true);
		terrainTools.add(rdbtnTerrainPencil);
		
		JRadioButton rdbtnTerrainBrush = new JRadioButton();
		rdbtnTerrainBrush.setToolTipText("Terrain brush - changes multiple tiles");
		rdbtnTerrainBrush.setIcon(getResources().getGuiImage(Constants.GUI_BRUSH));
		rdbtnTerrainBrush.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnTerrainBrush);
		
		JRadioButton rdbtnTerrainFill = new JRadioButton();
		rdbtnTerrainFill.setToolTipText("Terrain fill - fills an area");
		rdbtnTerrainFill.setIcon(getResources().getGuiImage(Constants.GUI_FILL));
		rdbtnTerrainFill.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnTerrainFill);
		
		JRadioButton rdbtnTerrainEraser = new JRadioButton();
		rdbtnTerrainEraser.setToolTipText("Terrain eraser - erases a single tile");
		rdbtnTerrainEraser.setIcon(getResources().getGuiImage(Constants.GUI_ERASER));
		rdbtnTerrainEraser.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnTerrainEraser);
		
		JRadioButton rdbtnTerrainPicker = new JRadioButton();
		rdbtnTerrainPicker.setToolTipText("Terrain picker - sets selected terrain to chosen tile");
		rdbtnTerrainPicker.setIcon(getResources().getGuiImage(Constants.GUI_PICKER));
		rdbtnTerrainPicker.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnTerrainPicker);
		
		JLabel lblObjects = new JLabel("Objects:");
		lblObjects.setBorder(new EmptyBorder(2, 10, 2, 10));
		terrainTools.add(lblObjects);
		
		JRadioButton rdbtnObjectsPencil = new JRadioButton();
		rdbtnObjectsPencil.setToolTipText("Objects pencil - changes single objects");
		rdbtnObjectsPencil.setIcon(getResources().getGuiImage(Constants.GUI_PENCIL));
		rdbtnObjectsPencil.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnObjectsPencil);
		
		JRadioButton rdbtnObjectsEraser = new JRadioButton();
		rdbtnObjectsEraser.setToolTipText("Object eraser - erases single objects");
		rdbtnObjectsEraser.setIcon(getResources().getGuiImage(Constants.GUI_ERASER));
		rdbtnObjectsEraser.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnObjectsEraser);
		
		JRadioButton rdbtnObjectsPicker = new JRadioButton();
		rdbtnObjectsPicker.setToolTipText("Object picker - sets selected object to the chosen object");
		rdbtnObjectsPicker.setIcon(getResources().getGuiImage(Constants.GUI_PICKER));
		rdbtnObjectsPicker.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnObjectsPicker);
		
		JLabel lblFences = new JLabel("Fences:");
		lblFences.setBorder(new EmptyBorder(2, 10, 2, 10));
		terrainTools.add(lblFences);
		
		JRadioButton rdbtnFencePencil = new JRadioButton();
		rdbtnFencePencil.setToolTipText("Fence pencil - changes single fences");
		rdbtnFencePencil.setIcon(getResources().getGuiImage(Constants.GUI_PENCIL));
		rdbtnFencePencil.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnFencePencil);

		JRadioButton rdbtnFenceLine = new JRadioButton();
		rdbtnFenceLine.setToolTipText("Fence line - creates a line of the same fence");
		rdbtnFenceLine.setIcon(getResources().getGuiImage(Constants.GUI_LINE));
		rdbtnFenceLine.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnFenceLine);
		
		JRadioButton rdbtnFenceEraser = new JRadioButton();
		rdbtnFenceEraser.setToolTipText("Fence eraser - removes a single fence");
		rdbtnFenceEraser.setIcon(getResources().getGuiImage(Constants.GUI_ERASER));
		rdbtnFenceEraser.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnFenceEraser);
		
		JRadioButton rdbtnFencePicker = new JRadioButton();
		rdbtnFencePicker.setToolTipText("Fence picker - sets the selected fence to the chosen fence");
		rdbtnFencePicker.setIcon(getResources().getGuiImage(Constants.GUI_PICKER));
		rdbtnFencePicker.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnFencePicker);
		
		JLabel lblMisc = new JLabel("Misc:");
		lblMisc.setBorder(new EmptyBorder(2, 10, 2, 10));
		terrainTools.add(lblMisc);
		
		JRadioButton rdbtnLabel = new JRadioButton();
		rdbtnLabel.setToolTipText("Label tool - lets you change the tile label");
		rdbtnLabel.setIcon(getResources().getGuiImage(Constants.GUI_LABEL));
		rdbtnLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnLabel);
		
		JRadioButton rdbtnOverlayPencil = new JRadioButton();
		rdbtnOverlayPencil.setToolTipText("Overlay pencil - highlights a single tile");
		rdbtnOverlayPencil.setIcon(getResources().getGuiImage(Constants.GUI_PENCIL));
		rdbtnOverlayPencil.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnOverlayPencil);
		
		JRadioButton rdbtnOverlayBrush = new JRadioButton();
		rdbtnOverlayBrush.setToolTipText("Overlay brush - highlights multiple tiles");
		rdbtnOverlayBrush.setIcon(getResources().getGuiImage(Constants.GUI_BRUSH));
		rdbtnOverlayBrush.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnOverlayBrush);
		
		JRadioButton rdbtnOverlayEraser = new JRadioButton();
		rdbtnOverlayEraser.setToolTipText("Overlay eraser - removes highlight from single tile");
		rdbtnOverlayEraser.setIcon(getResources().getGuiImage(Constants.GUI_ERASER));
		rdbtnOverlayEraser.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnOverlayEraser);
		
		JRadioButton rdbtnOverlayPicker = new JRadioButton();
		rdbtnOverlayPicker.setToolTipText("Overlay picker - sets overlay colour to the chosen tile's overlay colour");
		rdbtnOverlayPicker.setIcon(getResources().getGuiImage(Constants.GUI_PICKER));
		rdbtnOverlayPicker.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		terrainTools.add(rdbtnOverlayPicker);
		
		JButton btnOverlayColor = new JButton();
		btnOverlayColor.setToolTipText("Overlay colour chooser - sllows you to choose the overlay colour");
		btnOverlayColor.setIcon(getResources().getGuiImage(Constants.GUI_COLOR_CHOOSER));
		btnOverlayColor.setBorder(new EmptyBorder(2, 2, 5, 2));
		terrainTools.add(btnOverlayColor);
		
		JToolBar viewTools = new JToolBar();
		panel.add(viewTools);
		
		JButton btnZoomIn = new JButton();
		btnZoomIn.setToolTipText("Zoom In");
		btnZoomIn.setIcon(getResources().getGuiImage(Constants.GUI_ZOOM_IN));
		btnZoomIn.setBorder(new EmptyBorder(2, 2, 5, 2));
		viewTools.add(btnZoomIn);
		
		JButton btnZoomOut = new JButton();
		btnZoomOut.setToolTipText("Zoom Out");
		btnZoomOut.setIcon(getResources().getGuiImage(Constants.GUI_ZOOM_OUT));
		btnZoomOut.setBorder(new EmptyBorder(2, 2, 5, 2));
		viewTools.add(btnZoomOut);
		
		JButton btnLayerUp = new JButton();
		btnLayerUp.setToolTipText("Layer Up");
		btnLayerUp.setIcon(getResources().getGuiImage(Constants.GUI_LAYER_UP));
		btnLayerUp.setBorder(new EmptyBorder(2, 2, 5, 2));
		viewTools.add(btnLayerUp);
		
		JButton btnLayerDown = new JButton();
		btnLayerDown.setToolTipText("Layer Down");
		btnLayerDown.setIcon(getResources().getGuiImage(Constants.GUI_LAYER_DOWN));
		btnLayerDown.setBorder(new EmptyBorder(2, 2, 5, 2));
		viewTools.add(btnLayerDown);
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JTabbedPane tabSelector = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(tabSelector);
		
		tabEditor = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabEditor);

		setVisible(true);
	}
	
	public void init() {
		resources = new ResourceManager();
	}
	
	public void addMap(Map m) {
		GraphicPanel gfx = new GraphicPanel(this, m);
		JScrollPane graphicScroll = new JScrollPane(gfx, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		graphicScroll.getVerticalScrollBar().setUnitIncrement(graphicScroll.getVerticalScrollBar().getUnitIncrement() * 10);
		
		tabEditor.addTab(m.getMapName(), graphicScroll);
		tabEditor.setTabComponentAt(tabEditor.getTabCount() - 1, new ClosableTab(this, m.getMapName()));
		tabEditor.setSelectedIndex(tabEditor.getTabCount() - 1);
	}
	
	public void removeMap(String mapName) {
		for (int i = 0; i < tabEditor.getTabCount(); i++) {
			if (tabEditor.getTitleAt(i).equals(mapName))
				tabEditor.remove(i);
		}
	}
	
	public ResourceManager getResources() {
		return resources;
	}

	public int getSelectedTerrainType() {
		
		return 0;
	}

	public boolean isCaveEntrance(int tileType) {
		
		return false;
	}
	
}
