package net.buddat.wplanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import net.buddat.wplanner.util.Constants;
import net.buddat.wplanner.util.Logger;

public class MultiInstanceServer extends Thread {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	
	public MultiInstanceServer() {
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(Constants.MULTI_INSTANCE_SERVER_PORT);
			
			while(true) {
				clientSocket = serverSocket.accept();
				Logger.log("Another instance opened.");
				
				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String newFile = br.readLine();
				if (newFile.equals(Constants.NO_NEW_FILE_MSG))
					Logger.log("No new map file specified.");
				else {
					Logger.log("Recieved map to open from another instance.");
					String mapName = WPlanner.getMapManager().loadMap(newFile);
					
					WPlanner.getMainWindow().addMap(WPlanner.getMapManager().getMap(mapName));
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
