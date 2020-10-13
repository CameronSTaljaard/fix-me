package com.ctaljaar.market;

import java.io.*;
import java.net.Socket;

public class Market {
	public static void main(String[] args) throws Exception {
		String ip = "localhost";
		int port = 5001;
		String input;
		Socket marketSocket = new Socket(ip, port);
		PrintWriter outputStream = new PrintWriter(marketSocket.getOutputStream(), true);
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			input = inputStream.readLine();
			if (input.equalsIgnoreCase("exit"))
				break;
		}
		marketSocket.close();
	}
}