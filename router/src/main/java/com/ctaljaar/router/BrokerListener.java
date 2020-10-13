package com.ctaljaar.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerListener implements Runnable {
	int brokerPort = 5000;
	
	@Override
	public void run() {
		try {
			ServerSocket brokerServerSocket = new ServerSocket(brokerPort);
			while (true) {
				//Makes connection with the new broker socket
				Socket brokerSocket = brokerServerSocket.accept();
				System.out.println("New Broker joined");
				//Creates a new Thread for the Broker
				new ServerThread(brokerSocket).start();
				if(brokerSocket.isClosed()){
					brokerServerSocket.close();
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
