package com.ctaljaar.router;

import com.ctaljaar.router.broker.BrokerListener;
import com.ctaljaar.router.market.MarketListener;

public class Router {

	public static void main(String[] arg) throws Exception {
		Thread brokerListener = new Thread(new BrokerListener());
		brokerListener.start();
		Thread marketListener = new Thread(new MarketListener());
		marketListener.start();
		System.out.println("Router Started");
		while (true) {
		}
	}
}