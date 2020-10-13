package com.ctaljaar.router.market;

import java.net.ServerSocket;
import java.net.Socket;

import com.ctaljaar.router.model.RouterUtil;

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
			String marketID = RouterUtil.generateID();
			// Adds the New Broker to the List of online Brokers
			System.out.println("Market joined with ID: " + marketID);

			// A new implementation of this needs to made for markets.
			// Router.onlineBrokers.add(brokerID);

			while (true) {
				// Market logic to be added.
				break;
			}
			marketSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}