package com.ctaljaar.router.market;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ctaljaar.router.ServerThread;

public class MarketListener implements Runnable {
	int marketPort = 5001;
	
	@Override
	public void run() {
		try {
			ServerSocket marketServerSocket = new ServerSocket(marketPort);
			while (true) {
				//Makes connection with the new broker socket
				Socket marketSocket = marketServerSocket.accept();
				System.out.println("New Market joined");
				//Creates a new Thread for the Broker
				new ServerThread(marketSocket).start();
				if (marketSocket.isClosed()) {
					marketServerSocket.close();
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
