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
	
	public static final String OUT_LOG = "console.log";
	public static final String ERR_LOG = "error.log";
	
	public static final String OPT_USE_REGISTRY = "registry.use";
	public static final String OPT_WURM_INSTALL_DIR = "dir.install.wurm";
	public static final String OPT_OBJECTS_FOLDER = "dir.objects";
	public static final String OPT_FENCES_FOLDER = "dir.fences";
	public static final String OPT_SAVE_DIR = "dir.save.default";
	
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
		OPT_SAVE_DIR + " " + CFG_DELIMETER + " " + DEFAULT_SAVE_DIR };
	
}
