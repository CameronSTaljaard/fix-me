package com.ctaljaar.router.broker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.ctaljaar.router.util.*;
import com.ctaljaar.router.model.RouterGlobals;

/*You have to use Thread to make a new Thread each instance of this class 
because Runnable only makes one Thread
*/
public class BrokerThread extends Thread {

	Socket brokerSocket;
	ServerSocket marketServerSocket;
	public static Connection thisBroker;

	public BrokerThread(Socket brokerSocket) {
		this.brokerSocket = brokerSocket;
	}

	@Override
	public void run() {
		try {
			PrintWriter outputStream = new PrintWriter(brokerSocket.getOutputStream(), true);
			String brokerThreadID = RouterUtil.generateID();
			outputStream.println("ID");
			outputStream.println(brokerThreadID);
			thisBroker = new Connection(brokerSocket, brokerThreadID);
			System.out.println("Broker joined with ID: " + brokerThreadID);
			RouterGlobals.onlineBrokers.add(thisBroker);
			BufferedReader brokerInput = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));
			String brokerMessage;

			while (true) {
				brokerMessage = brokerInput.readLine();
				if (brokerMessage != null) {
					// Check what the Broker has sent
					BrokerThreadUtil.checkBrokerMessage(brokerMessage, outputStream, brokerInput,brokerThreadID);
				}
				if (thisBroker == null) {
					break;
				}
			}
			brokerSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}