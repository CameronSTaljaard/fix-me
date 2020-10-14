package com.ctaljaar.broker;

import java.io.*;
import java.net.Socket;

public class Broker {
	public static void main(String[] args) throws Exception {
		String readLine;
		String ip = "localhost";
		int port = 5000;
		Socket brokerSocket = new Socket(ip, port);
		PrintWriter outputStream = new PrintWriter(brokerSocket.getOutputStream(), true);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {;
			readLine = br.readLine();
			outputStream.println(readLine);// Sends the input of the Broker
			// if the Broker sends 'exit' then this Socket will close
			if (readLine.equalsIgnoreCase("exit"))
				break;
		}
		brokerSocket.close();
	}
}
