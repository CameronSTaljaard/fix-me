package com.ctaljaar.router.market;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.ctaljaar.router.util.RouterUtil;
import com.ctaljaar.router.Router;
import com.ctaljaar.router.util.MarketUtil;

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
			System.out.println("Market joined with ID: " + marketID);
			// Connection thisMarket = new Connection(marketSocket, marketID);
			BufferedReader marketInput = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));
			PrintWriter outputStream = new PrintWriter(marketSocket.getOutputStream(), true);

			String marketMessage;

			while (true) {
				marketMessage = marketInput.readLine();
				if (marketMessage != null) {
					if (marketMessage.contains("Stock")) {
						thisMarket = new MarketUtil(marketID, marketMessage);
						Router.onlineMarkets.add(thisMarket);
					}

					if (marketMessage.equalsIgnoreCase("exit")) {
						// Removes this broker from the online brokers
						removeMarketFromOnlineList(thisMarket);
						break;
					}
				}
			}

			marketSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	void removeMarketFromOnlineList(MarketUtil thisMarket) {
	int index = Router.onlineMarkets.indexOf(thisMarket);
	Router.onlineMarkets.remove(index);
	System.out.println(Router.onlineMarkets);
	}
}