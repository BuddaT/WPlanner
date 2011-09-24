package net.buddat.wplanner.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.buddat.wplanner.util.Constants;

public class Config {

	private ArrayList<String> configContents = new ArrayList<String>();

	private boolean useRegistry;
	private String wurmInstallDir;
	private String objectsDir;
	private String fencesDir;
	private String defaultSaveDir;
	
	public Config (File f) {
		loadConfig(f);
	}
	
	public void loadConfig(File f) {
		if (!f.exists())
			return;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String line;
			while ((line = br.readLine()) != null) {
				configContents.add(line);
				
				if (line.startsWith(Constants.CFG_COMMENT))
					continue;
				if (!line.contains("="))
					continue;
				
				String option = line.split(Constants.CFG_DELIMETER)[1].trim();
				
				if (line.startsWith(Constants.OPT_USE_REGISTRY))
					setUseRegistry(Boolean.parseBoolean(option), false);
				else if (line.startsWith(Constants.OPT_WURM_INSTALL_DIR))
					setWurmInstallDir(option, false);
				else if (line.startsWith(Constants.OPT_OBJECTS_FOLDER))
					setObjectsDir(option, false);
				else if (line.startsWith(Constants.OPT_FENCES_FOLDER))
					setFencesDir(option, false);
				else if (line.startsWith(Constants.OPT_SAVE_DIR))
					setDefaultSaveDir(option, false);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean useRegistry() {
		return useRegistry;
	}

	public void setUseRegistry(boolean useRegistry, boolean update) {
		this.useRegistry = useRegistry;
		
		if (update)
			updateConfig(Constants.OPT_USE_REGISTRY, Boolean.toString(useRegistry));
	}

	public String getWurmInstallDir() {
		return wurmInstallDir;
	}

	public void setWurmInstallDir(String wurmInstallDir, boolean update) {
		this.wurmInstallDir = wurmInstallDir;
		
		if (update)
			updateConfig(Constants.OPT_WURM_INSTALL_DIR, wurmInstallDir);
	}

	public String getObjectsDir() {
		return objectsDir;
	}

	public void setObjectsDir(String objectsDir, boolean update) {
		this.objectsDir = objectsDir;
		
		if (update)
			updateConfig(Constants.OPT_OBJECTS_FOLDER, objectsDir);
	}

	public String getFencesDir() {
		return fencesDir;
	}

	public void setFencesDir(String fencesDir, boolean update) {
		this.fencesDir = fencesDir;
		
		if (update)
			updateConfig(Constants.OPT_FENCES_FOLDER, fencesDir);
	}

	public String getDefaultSaveDir() {
		return defaultSaveDir;
	}

	public void setDefaultSaveDir(String defaultSaveDir, boolean update) {
		this.defaultSaveDir = defaultSaveDir;
		
		if (update)
			updateConfig(Constants.OPT_SAVE_DIR, defaultSaveDir);
	}
	
	public ArrayList<String> getConfigContents() {
		return configContents;
	}

	public void setConfigContents(ArrayList<String> configContents) {
		this.configContents = configContents;
	}
	
	public void updateConfig(String option, String value) {
		for (String s : configContents)
			if (s.startsWith(option))
				configContents.set(configContents.indexOf(s), option + " " + Constants.CFG_DELIMETER + " " + value);
				
		writeConfig(Constants.CONFIG_FILE);
	}
	
	public void writeConfig(String file) {
		File f = new File(file);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			
			for (String s : configContents) {
				bw.write(s);
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
