package net.buddat.wplanner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

import net.buddat.wplanner.config.Config;
import net.buddat.wplanner.gui.CrashDialog;
import net.buddat.wplanner.gui.LoadingFrame;
import net.buddat.wplanner.gui.MainWindow;
import net.buddat.wplanner.map.MapManager;
import net.buddat.wplanner.util.Constants;
import net.buddat.wplanner.util.Logger;

public class WPlanner {
	
	private static MapManager mapManager;
	private static String baseDir;
	private static Config config;

	private MultiInstanceServer miServer;
	private Socket clientSocket;
	
	private MainWindow mainWindow;
	
	public static void main(String[] args) {
		new WPlanner(args);
	}
	
	public WPlanner(String[] args) {
		LoadingFrame loading = new LoadingFrame();
		
		loading.update("checking instance", 0);
		if (isInstanceRunning()) {
			if (args.length == 0) {
				Logger.err("Another instance is running. Closing this instance.");
				writeToInstance(Constants.NO_NEW_FILE_MSG);
				
				new CrashDialog(Constants.PROGRAM_NAME + " is already running.", null);
			} else {
				Logger.log("Another instance is running. Passing map file to other instance.");
				writeToInstance(args[0]);
			}
			System.exit(0);
		} else {
			miServer = new MultiInstanceServer(this);
			miServer.setName("MultiInstanceServer");
			miServer.start();
		}
		
		loading.update("loading config", 5);
		loadConfig();
		
		loading.update("checking resources", 30);
		initialChecks();
		
		mapManager = new MapManager(this);
		
		if (args.length > 0) {
			loading.update("loading map", 70);
			args[0] = mapManager.loadMap(args[0]);
		}
		
		loading.update("complete", 100);
		
		mainWindow = new MainWindow();
		
		if (args.length > 0)
			mainWindow.addMap(mapManager.getMap(args[0]));
	}

	public void loadConfig() {
		File f = new File(Constants.CONFIG_FILE);
		config = new Config(f);
		
		if (!f.exists()) {
			Logger.log("Config file not found, creating default config.");
			
			ArrayList<String> defaultConfig = new ArrayList<String>();

			for (String s : Constants.DEFAULT_CONFIG)
				defaultConfig.add(s);
			
			config.setConfigContents(defaultConfig);
			config.writeConfig(Constants.CONFIG_FILE);
			
			config.loadConfig(f);
		}
	}
	
	private void initialChecks() {
		try {
			String dir = WPlanner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			if (dir.endsWith(".jar"))
				dir = dir.substring(0, dir.lastIndexOf("/") + 1);
			
			dir = URLDecoder.decode(dir, "UTF-8");
			setBaseDir(dir);
			
			if (config.useRegistry()) {
				if (Preferences.userRoot().nodeExists(Constants.WURM_REGISTRY_NODE)) {
					if (Preferences.userRoot().node(Constants.WURM_REGISTRY_NODE).get("wurm_dir", "").equals("")) {
						Logger.err("Unable to find Wurm Install directory from registry - trying config file.");
						
						/* If either of the next two possibilities work, we won't use the registry next time. */
						config.setUseRegistry(false, true);
						
						File f = new File(config.getWurmInstallDir());
						if (f.exists())
							Logger.log("Wurm folder from config exists, attempting to use.");
						else
							config.setWurmInstallDir(WPlanner.chooseDirectory("Wurm Install", null, true), true);
					} else {
						config.setWurmInstallDir(Preferences.userRoot().node(Constants.WURM_REGISTRY_NODE).get("wurm_dir", ""), true);
					}
				} else {
					/* Wurm registry node doesn't exist. Set registry flag to false and allow the user to choose the install folder. */
					config.setUseRegistry(false, true);
					config.setWurmInstallDir(WPlanner.chooseDirectory("Wurm Install", null, true), true);
				}
			}
			
			/* One final check to make sure we did get the right directory above. */
			File installDir = new File(config.getWurmInstallDir() + Constants.GRAPHICS_PACK_PATH);
			if (!installDir.exists()) {
				Logger.err("Unable to find wurm graphics pack. Please edit config.ini to specify the correct Wurm install folder.");
				Logger.err("File attempted: " + installDir.getAbsolutePath());
				
				/* 
				 * Reset the registry flag to bring up the folder choose box next time the program is run. 
				 * Some users are stupid and won't bother reading the error log to find out what to do.
				 */
				config.setUseRegistry(true, true);
				
				System.exit(1);
			} else
				Logger.log("Graphics pack found: " + installDir.getAbsolutePath());
			
			File objectsDir = new File(config.getObjectsDir());
			if (!objectsDir.exists()) {
				String folder = WPlanner.chooseDirectory("Objects", baseDir, true);
				Logger.log("Using user specified objects directory: " + folder);
				
				config.setObjectsDir(folder, true);
			} else
				Logger.log("Objects directory found: " + objectsDir.getAbsolutePath());
			
			File fencesDir = new File(config.getFencesDir());
			if (!fencesDir.exists()) {
				String folder = WPlanner.chooseDirectory("Fences", baseDir, true);
				Logger.log("Using user specified fences folder: " + folder);
				
				config.setFencesDir(folder, true);
			} else
				Logger.log("Fences directory found: " + fencesDir.getAbsolutePath());
		} catch (BackingStoreException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static String chooseDirectory(String name, String defaultPath, boolean quitOnFail) {
		Logger.err("Unable to find " + name + " directory. Allowing user to choose.");
		
		JFileChooser fc = new JFileChooser();
		
		if (defaultPath != null)
			fc.setCurrentDirectory(new File(defaultPath));
		
		fc.setDialogTitle("Choose " + name + " Directory");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);

		if (fc.showDialog(null, "Accept") == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getAbsolutePath();
		else {
			if (quitOnFail) {
				Logger.err("Unable to start. No " + name + " directory found.");
				System.exit(1);
			}
		}
		
		return null;	
	}
	
	public boolean isInstanceRunning() {
		try {
			clientSocket = new Socket("localhost", Constants.MULTI_INSTANCE_SERVER_PORT);
			return true;
		} catch (Exception e) {
			Logger.log("No other instance is running.");
			return false;
		}
	}
	
	public void writeToInstance(String msg) {
		try {
			PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
			pw.println(msg);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Config getConfig() {
		return config;
	}

	public static String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		WPlanner.baseDir = baseDir;
	}

	public static MapManager getMapManager() {
		return mapManager;
	}
	
	public MainWindow getMainWindow() {
		return mainWindow;
	}

}
