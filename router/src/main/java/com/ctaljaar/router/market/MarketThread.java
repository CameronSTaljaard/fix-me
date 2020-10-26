package com.ctaljaar.router.market;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import com.ctaljaar.router.util.*;
import com.ctaljaar.router.model.RouterGlobals;

/*You have to use Thread to make a new Thread each instance of this class 
because Runnable only makes one Thread
*/
public class MarketThread extends Thread {

	Socket marketSocket;
	ServerSocket marketServerSocket;

	public MarketThread(Socket marketSocket) {
		this.marketSocket = marketSocket;
	}

	@Override
	public void run() {
		try {
			MarketUtil thisMarket = null;
			String marketID = RouterUtil.generateID();
			// System.out.println("Market joined with ID: " + marketID);
			Connection thisMarketID = new Connection(marketSocket, marketID);
			// Adds this market ID to the online Markets
			RouterGlobals.onlineMarkets.add(thisMarketID);
			BufferedReader marketInput = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));
			String marketMessage;

			while (true) {
				marketMessage = marketInput.readLine();
				if (marketMessage != null) {
					if (marketMessage.contains("Market start")) {
						String marketName = marketInput.readLine();
						String stockName = marketInput.readLine();

						// Creates the market info
						thisMarket = new MarketUtil(marketID, marketName, stockName);
						// Adds the instance to the onlineMarketInfo array
						RouterGlobals.onlineMarketsInfo.add(thisMarket);
					} else if (marketMessage.equalsIgnoreCase("Query")) {

						ArrayList<String> fixMessage = new ArrayList<>();

						for (int i = 0; i < 7; i++) {
							// add fix message from market to an arrayList
							String info = marketInput.readLine();
							fixMessage.add(info);
							// System.out.println(info);
						}
						MarketThreadUtil.checkFixMessage(fixMessage, marketID);
						// The market has the broker message now so do error checking here

					} else if (marketMessage.equalsIgnoreCase("exit")) {
						// Removes this broker from the online brokers
						MarketThreadUtil.removeMarketFromOnlineList(thisMarket);
						break;
					}
				}
			}
			marketSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}