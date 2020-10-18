package com.ctaljaar.broker;

import java.io.*;
import java.net.Socket;

import com.ctaljaar.broker.util.BrokerPrinting;
import com.ctaljaar.broker.util.BrokerUtil;

public class Broker {
	public static void main(String[] args) throws Exception {
		String readLine;
		
		String ip = "localhost";
		int port = 5000;
		Socket brokerSocket = new Socket(ip, port);
		PrintWriter outputStream = new PrintWriter(brokerSocket.getOutputStream(), true);
		BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader routerInput = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));
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
