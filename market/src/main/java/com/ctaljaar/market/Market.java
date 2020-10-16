package com.ctaljaar.market;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Market {
	public static void main(String[] args) throws Exception {
		String ip = "localhost";
		String readLine;
		int port = 5001;
		// String input,stockName;
		Socket marketSocket = new Socket(ip, port);
		PrintWriter outputStream = new PrintWriter(marketSocket.getOutputStream(), true);
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));
		BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("What is the market type?");
		readLine = terminalInput.readLine();
		outputStream.println("Stock: " + readLine);
		System.out.println("Thank you!");

		while (true) {
			String routerMessage = inputStream.readLine();

			//Get the Fix message from the BrokerThread
			if (routerMessage.equalsIgnoreCase("Query")) {
				ArrayList<String> fixMessage = new ArrayList<>();

				for (int i = 0; i < 4; i++)
					fixMessage.add(inputStream.readLine());
				// The market has the broker message now so do error checking here
				System.out.println("Market has got the query from the broker");

			}
			if (readLine.equalsIgnoreCase("exit"))
				break;
		}
		marketSocket.close();
	}
}