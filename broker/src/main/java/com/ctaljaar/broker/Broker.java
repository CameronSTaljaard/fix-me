package com.ctaljaar.broker;

import java.io.*;
import java.net.Socket;

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
		while (true) {
			readLine = terminalInput.readLine();
			// checks what the broker has input on the terminal
			BrokerUtil.checkBrokerMessage(readLine, outputStream, routerInput);
			// if the Broker sends 'exit' then this Socket will close
			if (readLine.equalsIgnoreCase("exit"))
				break;

		}
		brokerSocket.close();
	}
}
