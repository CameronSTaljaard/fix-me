package com.ctaljaar.router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Router {

	public static void main(String[] arg) throws IOException{
		ServerSocket marketSocket = new ServerSocket(5001);
		Socket clientSocket;

		System.out.println("Waiting for connection on port 5001");
		while (true) {
			clientSocket = marketSocket.accept();
			System.out.println("Connection made");
		}
	}
}