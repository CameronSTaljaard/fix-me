package com.ctaljaar.market;

import java.io.*;
import java.net.Socket;
import com.ctaljaar.market.model.MarketObj;
import com.ctaljaar.market.model.MarketUtil;
import java.io.ObjectOutputStream;

public class Market {
	protected static String marketName;
	protected static String marketID;
	private static MarketUtil marketUtil = new MarketUtil();

	public static void main(String[] args) throws Exception {
		String ip = "localhost";
		String readLine;
		int port = 5001;
		Socket marketSocket = new Socket(ip, port);
		PrintWriter outputStream = new PrintWriter(marketSocket.getOutputStream(), true);
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));
		BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));
		marketID = marketUtil.setMarketID(inputStream);

		outputStream.println("Market start");
		//validate there is only 1 market named ...
		System.out.println("What is the market name?");
		readLine = terminalInput.readLine();
		marketName = readLine;
		outputStream.println(readLine);

		MarketObj market = new MarketObj();

		while (true) {
			String routerMessage = inputStream.readLine();
			System.out.println("Broker Message: " + routerMessage);
			if (routerMessage.contains("markets")){
				String[] request = routerMessage.split("\\|");
				outputStream.println(marketID + "*" + "Market: " + marketName + "*" + market.constructStockList() + "*" + request[0]);
				//check for end of list, only sent to last market in list
				if (request.length > 2 && request[2] != null)
					outputStream.println("End of List" + "|" + request[0]);
			}

			if (routerMessage.contains("buy") || routerMessage.contains("sell")){
				String[] request = routerMessage.split("\\|");
				String result = market.validateRequest(request[1], request[2], Integer.parseInt(request[3]), Integer.parseInt(request[5]));
				if(result.equals("Executed"))
					market.processRequest(request[1], request[2], Integer.parseInt(request[3]));
				System.out.println(result);
				String response = marketID + "|" + result + "|" + request[0];
				outputStream.println(response + "|" + marketUtil.calculateChecksum(response));
			}
			if (routerMessage.equalsIgnoreCase("exit"))
				break;
		}
		marketSocket.close();
	}
}