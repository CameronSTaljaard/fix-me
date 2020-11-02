package com.ctaljaar.router.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import com.ctaljaar.router.util.*;

public class RouterGlobals {
	public static ArrayList<Connection> onlineBrokers = new ArrayList<>();
	public static ArrayList<Connection> onlineMarkets = new ArrayList<>();
	public static HashMap<String, Connection> markets = new HashMap<String, Connection>();
	public static Socket brokerSocket, marketSocket;

	public static Connection getBrokerSocket(String ID){
		for (Connection broker : onlineBrokers){
			if (broker.getID().equals(ID)){
				return broker;
			}
		}
		return null;
	}
}
