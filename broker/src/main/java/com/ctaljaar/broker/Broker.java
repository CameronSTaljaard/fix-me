package com.ctaljaar.broker;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.ctaljaar.broker.util.BrokerPrinting;
import com.ctaljaar.broker.util.BrokerUtil;
import com.ctaljaar.broker.model.BrokerStock;

public class Broker {
	public static ArrayList<String> brokerStocks = new ArrayList<>();
	public static HashMap<String, BrokerStock> brokerStocks1 = new HashMap<String, BrokerStock>();

	public static String id;
	public static String ip = "localhost";
	public static int port = 5000;
	public static Socket brokerSocket;

	public static void main(String[] args) throws Exception {
		brokerSocket = new Socket(ip, port);
		String readLine;
		PrintWriter outputStream = new PrintWriter(brokerSocket.getOutputStream(), true);
		BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader routerInput = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));
		readLine = routerInput.readLine();
		if (readLine.equalsIgnoreCase("ID")){
				readLine = routerInput.readLine();
				id = readLine;
				System.out.println("ID: " + id);
		}
		BrokerPrinting.welcomeMessage();
		while (true) {
			readLine = terminalInput.readLine();
			// checks what the broker has input on the terminal
			if (BrokerUtil.validCommand(readLine))
				BrokerUtil.runBrokerCommand(readLine, outputStream, routerInput, terminalInput);
			else
				System.out.println(readLine + " is not a valid command.");
			// if the Broker sends 'exit' then this Socket will close
			if (readLine.equalsIgnoreCase("exit"))
				break;
		}
		brokerSocket.close();
	}
}
