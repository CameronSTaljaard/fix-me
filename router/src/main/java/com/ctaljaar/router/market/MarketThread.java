package com.ctaljaar.router.market;

import java.net.ServerSocket;
import java.net.Socket;

import com.ctaljaar.router.util.RouterUtil;
import com.ctaljaar.router.Router;
import com.ctaljaar.router.util.Connection;

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
			System.out.println("Market joined with ID: " + marketID);
			Connection thisMarket = new Connection(marketSocket, marketID);
			Router.onlineMarkets.add(thisMarket);

			while (true) {
				break;
			}
			
			marketSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	void removeMarketFromOnlineList(Connection thisMarket) {
		int index = Router.onlineMarkets.indexOf(thisMarket);
		Router.onlineMarkets.remove(index);
		System.out.println(Router.onlineMarkets);
	}
}