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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import net.buddat.wplanner.gui.resources.ResourceManager;
import net.buddat.wplanner.map.Map;
import net.buddat.wplanner.util.Constants;
import net.buddat.wplanner.util.Logger;

import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JToolBar;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import java.awt.Component;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6408703853685252009L;
	
	private ResourceManager resources;
	
	private JPanel contentPane;
	
	private static final String CMD_NEW = "new", CMD_OPEN = "open", CMD_SAVE = "save", CMD_SAVEAS = "saveas",
			CMD_SAVEIMG = "saveimage", CMD_SAVEIMGAS = "saveimageas", CMD_EXIT = "exit",
			CMD_UNDO = "undo", CMD_REDO = "redo", CMD_RESIZEMAP = "resizemap",
			CMD_ZOOMIN = "zoomin", CMD_ZOOMOUT = "zoomout", 
			CMD_HELP = "help", CMD_ABOUT = "about",
			CMD_TERRAIN_PENCIL = "terrain_pencil", CMD_TERRAIN_BRUSH = "terrain_brush", CMD_TERRAIN_FILL = "terrain_fill", CMD_TERRAIN_ERASER = "terrain_eraser", CMD_TERRAIN_PICKER = "terrain_picker",
			CMD_OBJECTS_PENCIL = "objects_pencil", CMD_OBJECTS_ERASER = "objects_eraser", CMD_OBJECTS_PICKER = "objects_picker",
			CMD_FENCE_PENCIL = "fence_pencil", CMD_FENCE_LINE = "fence_line", CMD_FENCE_ERASER = "fence_eraser", CMD_FENCE_PICKER = "fence_picker",
			CMD_LABEL = "label",
			CMD_OVERLAY_PENCIL = "overlay_pencil", CMD_OVERLAY_BRUSH = "overlay_brush", CMD_OVERLAY_ERASER = "overlay_eraser", CMD_OVERLAY_PICKER = "overlay_picker", CMD_OVERLAY_COLOR = "overlay_color",
			CMD_LAYERUP = "layerup", CMD_LAYERDOWN = "layerdown";
			
	
	JCheckBoxMenuItem chckbxmntmTerrain;
	JCheckBoxMenuItem chckbxmntmObjects;
	JCheckBoxMenuItem chckbxmntmFences;
	JCheckBoxMenuItem chckbxmntmOverlay;
	JCheckBoxMenuItem chckbxmntmLabels;
	JCheckBoxMenuItem chckbxmntmGrid;
	
	JTabbedPane tabEditor;
	ButtonGroup toolGroup;

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
		mntmNew.setActionCommand(CMD_NEW);
		mntmNew.addActionListener(this);
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpen.setActionCommand(CMD_OPEN);
		mntmOpen.addActionListener(this);
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSave.setActionCommand(CMD_SAVE);
		mntmSave.addActionListener(this);
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.setActionCommand(CMD_SAVEAS);
		mntmSaveAs.addActionListener(this);
		mnFile.add(mntmSaveAs);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmSaveImage = new JMenuItem("Save Image");
		mntmSaveImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmSaveImage.setActionCommand(CMD_SAVEIMG);
		mntmSaveImage.addActionListener(this);
		mnFile.add(mntmSaveImage);
		
		JMenuItem mntmSaveImageAs = new JMenuItem("Save Image As...");
		mntmSaveImageAs.setActionCommand(CMD_SAVEIMGAS);
		mntmSaveImageAs.addActionListener(this);
		mnFile.add(mntmSaveImageAs);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmExit.setActionCommand(CMD_EXIT);
		mntmExit.addActionListener(this);
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mntmUndo.setActionCommand(CMD_UNDO);
		mntmUndo.addActionListener(this);
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		mntmRedo.setActionCommand(CMD_REDO);
		mntmRedo.addActionListener(this);
		mnEdit.add(mntmRedo);
		
		JSeparator separator_2 = new JSeparator();
		mnEdit.add(separator_2);
		
		JMenuItem mntmResizeMap = new JMenuItem("Resize Map...");
		mntmResizeMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmResizeMap.setActionCommand(CMD_RESIZEMAP);
		mnEdit.add(mntmResizeMap);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmZoomIn = new JMenuItem("Zoom In");
		mntmZoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_MASK));
		mntmZoomIn.setActionCommand(CMD_ZOOMIN);
		mnView.add(mntmZoomIn);
		
		JMenuItem mntmZoomOut = new JMenuItem("Zoom Out");
		mntmZoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK));
		mntmZoomOut.setActionCommand(CMD_ZOOMOUT);
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
		mntmHelp.setActionCommand(CMD_HELP);
		mnHelp.add(mntmHelp);
		
		JSeparator separator_4 = new JSeparator();
		mnHelp.add(separator_4);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setActionCommand(CMD_ABOUT);
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
		btnNew.setActionCommand(CMD_NEW);
		btnNew.setBorder(new EmptyBorder(2, 2, 5, 2));
		
		JButton btnOpen = new JButton();
		btnOpen.setToolTipText("Open an existing map");
		btnOpen.setIcon(getResources().getGuiImage(Constants.GUI_OPEN));
		btnOpen.setActionCommand(CMD_OPEN);
		btnOpen.setBorder(new EmptyBorder(2, 2, 5, 2));
		
		JButton btnSave = new JButton();
		btnSave.setToolTipText("Save the current map");
		btnSave.setIcon(getResources().getGuiImage(Constants.GUI_SAVE));
		btnSave.setActionCommand(CMD_SAVE);
		btnSave.setBorder(new EmptyBorder(2, 2, 5, 2));
		
		JButton btnSaveImage = new JButton();
		btnSaveImage.setToolTipText("Save the current map as an image");
		btnSaveImage.setIcon(getResources().getGuiImage(Constants.GUI_SAVE_IMAGE));
		btnSaveImage.setActionCommand(CMD_SAVEIMG);
		btnSaveImage.setBorder(new EmptyBorder(2, 2, 5, 2));
		
		menuTools.add(btnNew);
		menuTools.add(btnOpen);
		menuTools.add(btnSave);
		menuTools.add(btnSaveImage);
		
		toolGroup = new ButtonGroup();
		
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
		rdbtnTerrainPencil.setActionCommand(CMD_TERRAIN_PENCIL);
		terrainTools.add(rdbtnTerrainPencil);
		toolGroup.add(rdbtnTerrainPencil);
		
		JRadioButton rdbtnTerrainBrush = new JRadioButton();
		rdbtnTerrainBrush.setToolTipText("Terrain brush - changes multiple tiles");
		rdbtnTerrainBrush.setIcon(getResources().getGuiImage(Constants.GUI_BRUSH));
		rdbtnTerrainBrush.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnTerrainBrush.setActionCommand(CMD_TERRAIN_BRUSH);
		terrainTools.add(rdbtnTerrainBrush);
		toolGroup.add(rdbtnTerrainBrush);
		
		JRadioButton rdbtnTerrainFill = new JRadioButton();
		rdbtnTerrainFill.setToolTipText("Terrain fill - fills an area");
		rdbtnTerrainFill.setIcon(getResources().getGuiImage(Constants.GUI_FILL));
		rdbtnTerrainFill.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnTerrainFill.setActionCommand(CMD_TERRAIN_FILL);
		terrainTools.add(rdbtnTerrainFill);
		toolGroup.add(rdbtnTerrainFill);
		
		JRadioButton rdbtnTerrainEraser = new JRadioButton();
		rdbtnTerrainEraser.setToolTipText("Terrain eraser - erases a single tile");
		rdbtnTerrainEraser.setIcon(getResources().getGuiImage(Constants.GUI_ERASER));
		rdbtnTerrainEraser.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnTerrainEraser.setActionCommand(CMD_TERRAIN_ERASER);
		terrainTools.add(rdbtnTerrainEraser);
		toolGroup.add(rdbtnTerrainEraser);
		
		JRadioButton rdbtnTerrainPicker = new JRadioButton();
		rdbtnTerrainPicker.setToolTipText("Terrain picker - sets selected terrain to chosen tile");
		rdbtnTerrainPicker.setIcon(getResources().getGuiImage(Constants.GUI_PICKER));
		rdbtnTerrainPicker.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnTerrainPicker.setActionCommand(CMD_TERRAIN_PICKER);
		terrainTools.add(rdbtnTerrainPicker);
		toolGroup.add(rdbtnTerrainPicker);
		
		JLabel lblObjects = new JLabel("Objects:");
		lblObjects.setBorder(new EmptyBorder(2, 10, 2, 10));
		terrainTools.add(lblObjects);
		
		JRadioButton rdbtnObjectsPencil = new JRadioButton();
		rdbtnObjectsPencil.setToolTipText("Objects pencil - changes single objects");
		rdbtnObjectsPencil.setIcon(getResources().getGuiImage(Constants.GUI_PENCIL));
		rdbtnObjectsPencil.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnObjectsPencil.setActionCommand(CMD_OBJECTS_PENCIL);
		terrainTools.add(rdbtnObjectsPencil);
		toolGroup.add(rdbtnObjectsPencil);
		
		JRadioButton rdbtnObjectsEraser = new JRadioButton();
		rdbtnObjectsEraser.setToolTipText("Object eraser - erases single objects");
		rdbtnObjectsEraser.setIcon(getResources().getGuiImage(Constants.GUI_ERASER));
		rdbtnObjectsEraser.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnObjectsEraser.setActionCommand(CMD_OBJECTS_ERASER);
		terrainTools.add(rdbtnObjectsEraser);
		toolGroup.add(rdbtnObjectsEraser);
		
		JRadioButton rdbtnObjectsPicker = new JRadioButton();
		rdbtnObjectsPicker.setToolTipText("Object picker - sets selected object to the chosen object");
		rdbtnObjectsPicker.setIcon(getResources().getGuiImage(Constants.GUI_PICKER));
		rdbtnObjectsPicker.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnObjectsPicker.setActionCommand(CMD_OBJECTS_PICKER);
		terrainTools.add(rdbtnObjectsPicker);
		toolGroup.add(rdbtnObjectsPicker);
		
		JLabel lblFences = new JLabel("Fences:");
		lblFences.setBorder(new EmptyBorder(2, 10, 2, 10));
		terrainTools.add(lblFences);
		
		JRadioButton rdbtnFencePencil = new JRadioButton();
		rdbtnFencePencil.setToolTipText("Fence pencil - changes single fences");
		rdbtnFencePencil.setIcon(getResources().getGuiImage(Constants.GUI_PENCIL));
		rdbtnFencePencil.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnFencePencil.setActionCommand(CMD_FENCE_PENCIL);
		terrainTools.add(rdbtnFencePencil);
		toolGroup.add(rdbtnFencePencil);

		JRadioButton rdbtnFenceLine = new JRadioButton();
		rdbtnFenceLine.setToolTipText("Fence line - creates a line of the same fence");
		rdbtnFenceLine.setIcon(getResources().getGuiImage(Constants.GUI_LINE));
		rdbtnFenceLine.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnFenceLine.setActionCommand(CMD_FENCE_LINE);
		terrainTools.add(rdbtnFenceLine);
		toolGroup.add(rdbtnFenceLine);
		
		JRadioButton rdbtnFenceEraser = new JRadioButton();
		rdbtnFenceEraser.setToolTipText("Fence eraser - removes a single fence");
		rdbtnFenceEraser.setIcon(getResources().getGuiImage(Constants.GUI_ERASER));
		rdbtnFenceEraser.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnFenceEraser.setActionCommand(CMD_FENCE_ERASER);
		terrainTools.add(rdbtnFenceEraser);
		toolGroup.add(rdbtnFenceEraser);
		
		JRadioButton rdbtnFencePicker = new JRadioButton();
		rdbtnFencePicker.setToolTipText("Fence picker - sets the selected fence to the chosen fence");
		rdbtnFencePicker.setIcon(getResources().getGuiImage(Constants.GUI_PICKER));
		rdbtnFencePicker.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnFencePicker.setActionCommand(CMD_FENCE_PICKER);
		terrainTools.add(rdbtnFencePicker);
		toolGroup.add(rdbtnFencePicker);
		
		JLabel lblMisc = new JLabel("Misc:");
		lblMisc.setBorder(new EmptyBorder(2, 10, 2, 10));
		terrainTools.add(lblMisc);
		
		JRadioButton rdbtnLabel = new JRadioButton();
		rdbtnLabel.setToolTipText("Label tool - lets you change the tile label");
		rdbtnLabel.setIcon(getResources().getGuiImage(Constants.GUI_LABEL));
		rdbtnLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnLabel.setActionCommand(CMD_LABEL);
		terrainTools.add(rdbtnLabel);
		toolGroup.add(rdbtnLabel);
		
		JRadioButton rdbtnOverlayPencil = new JRadioButton();
		rdbtnOverlayPencil.setToolTipText("Overlay pencil - highlights a single tile");
		rdbtnOverlayPencil.setIcon(getResources().getGuiImage(Constants.GUI_PENCIL));
		rdbtnOverlayPencil.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnOverlayPencil.setActionCommand(CMD_OVERLAY_PENCIL);
		terrainTools.add(rdbtnOverlayPencil);
		toolGroup.add(rdbtnOverlayPencil);
		
		JRadioButton rdbtnOverlayBrush = new JRadioButton();
		rdbtnOverlayBrush.setToolTipText("Overlay brush - highlights multiple tiles");
		rdbtnOverlayBrush.setIcon(getResources().getGuiImage(Constants.GUI_BRUSH));
		rdbtnOverlayBrush.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnOverlayBrush.setActionCommand(CMD_OVERLAY_BRUSH);
		terrainTools.add(rdbtnOverlayBrush);
		toolGroup.add(rdbtnOverlayBrush);
		
		JRadioButton rdbtnOverlayEraser = new JRadioButton();
		rdbtnOverlayEraser.setToolTipText("Overlay eraser - removes highlight from single tile");
		rdbtnOverlayEraser.setIcon(getResources().getGuiImage(Constants.GUI_ERASER));
		rdbtnOverlayEraser.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnOverlayEraser.setActionCommand(CMD_OVERLAY_ERASER);
		terrainTools.add(rdbtnOverlayEraser);
		toolGroup.add(rdbtnOverlayEraser);
		
		JRadioButton rdbtnOverlayPicker = new JRadioButton();
		rdbtnOverlayPicker.setToolTipText("Overlay picker - sets overlay colour to the chosen tile's overlay colour");
		rdbtnOverlayPicker.setIcon(getResources().getGuiImage(Constants.GUI_PICKER));
		rdbtnOverlayPicker.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		rdbtnOverlayPicker.setActionCommand(CMD_OVERLAY_PICKER);
		terrainTools.add(rdbtnOverlayPicker);
		toolGroup.add(rdbtnOverlayPicker);
		
		JButton btnOverlayColor = new JButton();
		btnOverlayColor.setToolTipText("Overlay colour chooser - sllows you to choose the overlay colour");
		btnOverlayColor.setIcon(getResources().getGuiImage(Constants.GUI_COLOR_CHOOSER));
		btnOverlayColor.setBorder(new EmptyBorder(2, 2, 5, 2));
		btnOverlayColor.setActionCommand(CMD_OVERLAY_COLOR);
		terrainTools.add(btnOverlayColor);
		
		JToolBar viewTools = new JToolBar();
		panel.add(viewTools);
		
		JButton btnZoomIn = new JButton();
		btnZoomIn.setToolTipText("Zoom In");
		btnZoomIn.setIcon(getResources().getGuiImage(Constants.GUI_ZOOM_IN));
		btnZoomIn.setBorder(new EmptyBorder(2, 2, 5, 2));
		btnZoomIn.setActionCommand(CMD_ZOOMIN);
		viewTools.add(btnZoomIn);
		
		JButton btnZoomOut = new JButton();
		btnZoomOut.setToolTipText("Zoom Out");
		btnZoomOut.setIcon(getResources().getGuiImage(Constants.GUI_ZOOM_OUT));
		btnZoomOut.setBorder(new EmptyBorder(2, 2, 5, 2));
		btnZoomOut.setActionCommand(CMD_ZOOMOUT);
		viewTools.add(btnZoomOut);
		
		JButton btnLayerUp = new JButton();
		btnLayerUp.setToolTipText("Layer Up");
		btnLayerUp.setIcon(getResources().getGuiImage(Constants.GUI_LAYER_UP));
		btnLayerUp.setBorder(new EmptyBorder(2, 2, 5, 2));
		btnLayerUp.setActionCommand(CMD_LAYERUP);
		viewTools.add(btnLayerUp);
		
		JButton btnLayerDown = new JButton();
		btnLayerDown.setToolTipText("Layer Down");
		btnLayerDown.setIcon(getResources().getGuiImage(Constants.GUI_LAYER_DOWN));
		btnLayerDown.setBorder(new EmptyBorder(2, 2, 5, 2));
		btnLayerDown.setActionCommand(CMD_LAYERDOWN);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCmd = e.getActionCommand();
		GraphicPanel panel = (GraphicPanel) ((JScrollPane) tabEditor.getSelectedComponent()).getViewport().getComponent(0);
		
		Logger.log(actionCmd);
		
		if (actionCmd.equals(CMD_UNDO)) {
			panel.undo();
		} else if (actionCmd.equals(CMD_REDO)) {
			panel.redo();
		}
	}
	
}
