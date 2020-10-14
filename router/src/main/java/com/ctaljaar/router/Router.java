package com.ctaljaar.router;

import java.util.ArrayList;
import com.ctaljaar.router.broker.BrokerListener;
import com.ctaljaar.router.market.MarketListener;
import com.ctaljaar.router.util.Connection;

public class Router {

	public static ArrayList<Connection> onlineBrokers = new ArrayList<>();
	public static ArrayList<Connection> onlineMarkets = new ArrayList<>();

	public static void main(String[] arg) throws Exception {
		Thread brokerListener = new Thread(new BrokerListener());
		brokerListener.start();
		Thread marketListener = new Thread(new MarketListener());
		marketListener.start();

		while (true) {
		}
	}
}