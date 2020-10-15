package com.ctaljaar.broker;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;

public class Broker {
	public static void main(String[] args) throws Exception {
		String readLine;
		String routerMessage;
		String ip = "localhost";
		int port = 5000;
		Socket brokerSocket = new Socket(ip, port);
		PrintWriter outputStream = new PrintWriter(brokerSocket.getOutputStream(), true);
		BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader routerInput = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));

		while (true) {
			readLine = terminalInput.readLine();
			outputStream.println(readLine);// Sends the input of the Broker

			// if the Broker sends 'list' then Router will send all the online Brokers
			if (readLine.equalsIgnoreCase("list")) {
				System.out.println("-------Connected brokers------");
				System.out.println("");

				// Reads the data from the router
				routerMessage = routerInput.readLine();
				System.out.println("Broker: " + routerMessage);
				System.out.println("");
				System.out.println("-----------End of list---------");
			}
			// outerInput
			// if the Broker sends 'exit' then this Socket will close
			if (readLine.equalsIgnoreCase("exit"))
				break;
		}
		brokerSocket.close();
	}
}
