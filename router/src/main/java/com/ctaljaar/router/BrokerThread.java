package com.ctaljaar.router;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import com.ctaljaar.router.model.RouterUtil;

/*You have to use Thread to make a new Thread each instance of this class 
because Runnable only makes one Thread
*/
public class BrokerThread extends Thread {

	Socket brokerSocket;
	ServerSocket marketServerSocket;

	public BrokerThread(Socket brokerSocket) {
		this.brokerSocket = brokerSocket;
		// this.marketSocket = marketSocket;
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
				brokerMessage = brokerInput.readLine(); // BufferedReader reads the message that was sent by the Broker
				System.out.println("Broker message = " + brokerMessage);

				// if the Broker send 'exit' then the thread will end
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