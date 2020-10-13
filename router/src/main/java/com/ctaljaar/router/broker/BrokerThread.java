package com.ctaljaar.router.broker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.ctaljaar.router.util.RouterUtil;
import com.ctaljaar.router.Router;

/*You have to use Thread to make a new Thread each instance of this class 
because Runnable only makes one Thread
*/
public class BrokerThread extends Thread {

	Socket brokerSocket;
	ServerSocket marketServerSocket;

	public BrokerThread(Socket brokerSocket) {
		this.brokerSocket = brokerSocket;
	}

	@Override
	public void run() {
		try {
			String brokerID = RouterUtil.generateID();
			// Adds the New Broker to the List of online Brokers
			System.out.println("Broker joined with ID: " + brokerID);
			Router.onlineBrokers.add(brokerID);
			// Reads the brokers input on the terminal
			BufferedReader brokerInput = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));
			String brokerMessage;
			while (true) {
				brokerMessage = brokerInput.readLine();
				System.out.println("Broker message = " + brokerMessage);
				if (brokerMessage.equalsIgnoreCase("exit")) {
					// Removes this broker from the online brokers
					removeBrokerFromOnlineList(brokerID);
					break;
				}
			}
			brokerSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	void removeBrokerFromOnlineList(String brokerID) {
		int index = Router.onlineBrokers.indexOf(brokerID);
		Router.onlineBrokers.remove(index);
		System.out.println(Router.onlineBrokers);
	}
}