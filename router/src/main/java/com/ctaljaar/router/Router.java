package com.ctaljaar.router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Router {

	public static void main(String[] arg) throws IOException{
		ServerSocket marketSocket = new ServerSocket(5001);
		Socket clientSocket;

		// int activeConnections = 0;

		System.out.println("Waiting for connection on port 5001");
		while (true) {
			// Code will stop here until a connection is made. Similar to waiting for user input.
			clientSocket = marketSocket.accept();
			System.out.println("Connection made");

			// Just code to test that multiple connections are accepted.
			// activeConnections++;
			// System.out.println("Connections made: " + activeConnections);
		}
	}
}