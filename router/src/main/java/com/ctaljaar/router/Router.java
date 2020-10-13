package com.ctaljaar.router;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
public class Router {

	public static ArrayList<String> onlineBrokers = new ArrayList<>();

	public static void main(String[] arg) throws Exception {

		int brokerPort = 5000;
		int marketPort = 5001;

		ServerSocket brokerServerSocket = new ServerSocket(brokerPort);
		// ServerSocket marketServerSocket = new ServerSocket(marketPort);

		System.out.println("connected");
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
	}
}