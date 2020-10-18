package com.ctaljaar.router.broker;

import java.io.IOException;
import java.net.ServerSocket;

import com.ctaljaar.router.model.RouterGlobals;

public class BrokerListener implements Runnable {
	int brokerPort = 5000;
	
	@Override
	public void run() {
		try {
			ServerSocket brokerServerSocket = new ServerSocket(brokerPort);
			while (true) {
				//Makes connection with the new broker socket
				RouterGlobals.brokerSocket = brokerServerSocket.accept();
				System.out.println("New Broker joined");
				//Creates a new Thread for the Broker
				new BrokerThread(RouterGlobals.brokerSocket).start();
				if (RouterGlobals.brokerSocket.isClosed()){
					brokerServerSocket.close();
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
