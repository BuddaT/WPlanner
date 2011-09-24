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
	private WPlanner wplanner;
	
	public MultiInstanceServer(WPlanner parent) {
		wplanner = parent;
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
				if (newFile != Constants.NO_NEW_FILE_MSG)
					Logger.log("No new map file specified.");
				else {
					wplanner.loadMap(newFile);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
