package com.ctaljaar.router.model;

import java.net.Socket;
import java.util.ArrayList;
import com.ctaljaar.router.util.*;

public class RouterGlobals {
	public static ArrayList<Connection> onlineBrokers = new ArrayList<>();
	public static ArrayList<Connection> onlineMarkets = new ArrayList<>();
	public static ArrayList<MarketUtil> onlineMarketsInfo = new ArrayList<>();
	public static Socket brokerSocket, marketSocket;
}
