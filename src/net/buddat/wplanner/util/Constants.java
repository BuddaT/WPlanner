package net.buddat.wplanner.util;

public class Constants {

	public static final String PROGRAM_NAME = "WPlanner";
	public static final String AUTHOR = "Budda";
	public static final String AUTHOR_WEBSITE = "http://buddat.net";
	
	public static final int VERSION_MAJOR = 3;
	public static final int VERSION_MIDI = 0;
	public static final int VERSION_MINOR = 0;
	
	public static final int MAP_EXTRA_BYTES = 64;
	public static final String MAP_FILE_EXT = ".wp2";
	public static final String MAP_TEMP_FILE_EXT = ".tmp";
	
	public static final int MULTI_INSTANCE_SERVER_PORT = 34732;
	public static final String NO_NEW_FILE_MSG = "NONE";
	
	public static final String WURM_REGISTRY_NODE = "com/wurmonline/client";
	public static final String DEFAULT_WURM_INSTALL_DIR = "C:/Wurm";
	public static final String GRAPHICS_PACK_PATH = "/packs/graphics.jar";
	
	public static final String CONFIG_FILE = "config.ini";
	
	public static final String DEFAULT_DATA_DIR = "data/";
	public static final String DEFAULT_OBJECTS_FOLDER = "objects";
	public static final String DEFAULT_FENCES_FOLDER = "fences";
	
	public static final String DEFAULT_SAVE_DIR = "saved/";
	
	public static final String DEFAULT_SINGLE_INSTANCE = "false";
	
	public static final String DEFAULT_WINDOW_WIDTH = "800";
	public static final String DEFAULT_WINDOW_HEIGHT = "600";
	public static final String DEFAULT_SCROLL_INCREMENT = "10";
	
	public static final String OUT_LOG = "console.log";
	public static final String ERR_LOG = "error.log";
	
	public static final String OPT_USE_REGISTRY = "registry.use";
	public static final String OPT_WURM_INSTALL_DIR = "dir.install.wurm";
	public static final String OPT_OBJECTS_FOLDER = "dir.objects";
	public static final String OPT_FENCES_FOLDER = "dir.fences";
	public static final String OPT_SAVE_DIR = "dir.save.default";
	public static final String OPT_SINGLE_INSTANCE = "instance.single";
	public static final String OPT_WINDOW_WIDTH = "gui.window.width";
	public static final String OPT_WINDOW_HEIGHT = "gui.window.height";
	public static final String OPT_SCROLL_INCREMENT = "gui.scrollbar.increment";
	
	public static final String GUI_PENCIL = "/gui/pencil.png";
	public static final String GUI_BRUSH = "/gui/brush.png";
	public static final String GUI_LINE = "/gui/line.png";
	public static final String GUI_FILL = "/gui/fill.png";
	public static final String GUI_ERASER = "/gui/eraser.png";
	public static final String GUI_PICKER = "/gui/picker.png";
	public static final String GUI_ZOOM_IN = "/gui/zoomin.png";
	public static final String GUI_ZOOM_OUT = "/gui/zoomout.png";
	public static final String GUI_LAYER_UP = "/gui/layerup.png";
	public static final String GUI_LAYER_DOWN = "/gui/layerdown.png";
	public static final String GUI_NEW = "/gui/new.png";
	public static final String GUI_OPEN = "/gui/open.png";
	public static final String GUI_SAVE = "/gui/save.png";
	public static final String GUI_SAVE_IMAGE = "/gui/saveimage.png";
	public static final String GUI_LABEL = "/gui/label.png";
	public static final String GUI_OVERLAY = "/gui/overlay.png";
	public static final String GUI_COLOR_CHOOSER = "/gui/color.png";
	public static final String GUI_LOGO = "/gui/logo.png";
	
	public static final String GUI_HELP = "/gui/wplanner_help.txt";
	
	public static final String CFG_DELIMETER = "=";
	public static final String CFG_COMMENT = "#";
	
	public static String[] DEFAULT_CONFIG = { 
		CFG_COMMENT + " Default config file for " + PROGRAM_NAME + " v." + VERSION_MAJOR + "." + VERSION_MIDI + "." + VERSION_MINOR,
		CFG_COMMENT,
		CFG_COMMENT + " Created by " + AUTHOR + " - " + AUTHOR_WEBSITE,
		"",
		OPT_USE_REGISTRY + " " + CFG_DELIMETER + " true",
		"# If above is true, disregard this line",
		OPT_WURM_INSTALL_DIR + " " + CFG_DELIMETER + " " + DEFAULT_WURM_INSTALL_DIR,
		"",
		OPT_OBJECTS_FOLDER + " " + CFG_DELIMETER + " " + DEFAULT_DATA_DIR + DEFAULT_OBJECTS_FOLDER,
		OPT_FENCES_FOLDER + " " + CFG_DELIMETER + " " + DEFAULT_DATA_DIR + DEFAULT_FENCES_FOLDER,
		"",
		OPT_SAVE_DIR + " " + CFG_DELIMETER + " " + DEFAULT_SAVE_DIR,
		"",
		OPT_SINGLE_INSTANCE + " " + CFG_DELIMETER + " " + DEFAULT_SINGLE_INSTANCE,
		"",
		"#GUI Settings",
		OPT_WINDOW_WIDTH + " " + CFG_DELIMETER + " " + DEFAULT_WINDOW_WIDTH,
		OPT_WINDOW_HEIGHT + " " + CFG_DELIMETER + " " + DEFAULT_WINDOW_HEIGHT,
		OPT_SCROLL_INCREMENT + " " + CFG_DELIMETER + " " + DEFAULT_SCROLL_INCREMENT };
	
}
