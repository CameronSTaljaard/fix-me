package com.ctaljaar.router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.ctaljaar.router.model.ConnectionHandler;

public class Router {

	public static void main(String[] arg) throws Exception {

		int port = 5000;
		ArrayList<String> onlineBrokers = new ArrayList<>();
		ServerSocket serverSocket = new ServerSocket(port);

		Socket ss = serverSocket.accept();
		System.out.println("connected");
		DataInputStream dataInputStream = new DataInputStream(ss.getInputStream());
		boolean done = false;
		while (!done) {
			byte messageType = dataInputStream.readByte();// Stream reads the bytes that was sent by the Broker
			String brokerMessage = dataInputStream.readUTF(); // Stream reads the message that was sent by the Broker

			if (messageType == 1) // The first message from a new Broker will go in here
			{
				// Adds the New Broker to the List of online Brokers
				onlineBrokers.add(ConnectionHandler.generateID());
				System.out.println("New Broker joined");
				System.out.println(onlineBrokers);
				System.out.println("Broker message = " + brokerMessage);
			}

			else if (messageType == 2) // The rest of the messages from the Broker will go in here
			{

				System.out.println("Broker message = " + brokerMessage);
				if (brokerMessage.equalsIgnoreCase("exit"))
					break;
			} else {
				done = true;

			}
		}
		ss.close();
		serverSocket.close();

	}
}