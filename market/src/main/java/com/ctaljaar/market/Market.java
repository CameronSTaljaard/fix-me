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
		System.out.println("What is the market name?");
		readLine = terminalInput.readLine();
		marketName = readLine;
		outputStream.println("Market: " + readLine);
		System.out.println("What stock do you have?");
		readLine = terminalInput.readLine();
		outputStream.println("Instrument: "+readLine);

		//marketName = marketUtil.marketStart(outputStream, terminalInput);
		MarketObj market = new MarketObj();

		while (true) {
			String routerMessage = inputStream.readLine();

			if (routerMessage.equalsIgnoreCase("markets")){
				outputStream.println("Market: " + marketName);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(marketSocket.getOutputStream());
				objectOutputStream.writeObject(market.constructStockList());
			}

			if (routerMessage.contains("buy") || routerMessage.contains("sell")){
				String[] request = routerMessage.split("\\|");
				String result = market.validateRequest(request[1], request[2], Integer.parseInt(request[3]), Integer.parseInt(request[5]));
				if(result.equals("Executed"))
					market.processRequest(request[1], request[2], Integer.parseInt(request[3]));
				outputStream.println(marketID + "|" + result + "|" + request[4]);
			}

			// Get the Fix message from the BrokerThread
			if (routerMessage.equalsIgnoreCase("Query")) {
				 // Send Query to the Router to tell it that you have a Query coming through
				outputStream.println("Query");
				//Send fix message back to router
				for (int i = 0; i < 5; i++)
					outputStream.println(inputStream.readLine());
				System.out.println("Market has got the query from the broker");
			}
			if (routerMessage.equalsIgnoreCase("exit"))
				break;
		}
		marketSocket.close();
	}
}