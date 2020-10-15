package com.ctaljaar.router.util;

import java.net.Socket;

public class Connection {
	Socket activeSocket;
	String uniqueID;

	public Connection(Socket activeSocket, String uniqueID) {
		this.activeSocket = activeSocket;
		this.uniqueID = uniqueID;
	}

	@Override
	public String toString() {
		return (uniqueID);
	}
}
